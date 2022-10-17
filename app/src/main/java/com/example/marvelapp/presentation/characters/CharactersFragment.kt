package com.example.marvelapp.presentation.characters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import com.example.marvelapp.R
import com.example.marvelapp.databinding.FragmentCharactersBinding
import com.example.marvelapp.framework.imageLoader.ImageLoader
import com.example.marvelapp.presentation.characters.adapter.CharactersAdapter
import com.example.marvelapp.presentation.characters.adapter.CharactersLoadMoreStateAdapter
import com.example.marvelapp.presentation.characters.adapter.CharactersRefreshStateAdapter
import com.example.marvelapp.presentation.detail.DetailViewArg
import com.example.marvelapp.presentation.sort.SortFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CharactersFragment : Fragment(), SearchView.OnQueryTextListener,
    MenuItem.OnActionExpandListener {

    private var _binding: FragmentCharactersBinding? = null
    private val binding: FragmentCharactersBinding get() = _binding!!

    private val viewModel: CharactersViewModel by viewModels()

    private lateinit var searchView: SearchView

    @Inject
    lateinit var imageLoader: ImageLoader

    private val headerAdapter: CharactersRefreshStateAdapter by lazy {
        CharactersRefreshStateAdapter(
            charactersAdapter::retry
        )
    }

    private val charactersAdapter: CharactersAdapter by lazy {
        CharactersAdapter(imageLoader) { character, view ->
            val extras = FragmentNavigatorExtras(view to character.name)

            val directions = CharactersFragmentDirections.actionCharactersFragmentToDetailFragment(
                character.name, DetailViewArg(character.id, character.name, character.imageUrl)
            )

            findNavController().navigate(directions, extras)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ) = FragmentCharactersBinding.inflate(inflater, container, false).apply {
        _binding = this
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initCharactersAdapter()
        observeInitialLoadState()
        observeSortingData()

        viewModel.state.observe(viewLifecycleOwner) { uiState ->
            when (uiState) {
                is CharactersViewModel.UiState.SearchResult -> {
                    lifecycleScope.launch {
                        charactersAdapter.submitData(uiState.data)
                    }
                }
            }
        }
        viewModel.searchCharacters()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    private fun initCharactersAdapter() {
        binding.recyclerCharacters.run {
            postponeEnterTransition()

            // quando os item do lista tiverem o mesmo tamanho em dimensões,
            // usar essa função para otimizar a listagem.
            setHasFixedSize(true)

            //para voltar sempre para posicao inicial
            //scrollToPosition(0)
            adapter = charactersAdapter.withLoadStateHeaderAndFooter(
                header = headerAdapter,
                footer = CharactersLoadMoreStateAdapter(charactersAdapter::retry)
            )

            viewTreeObserver.addOnPreDrawListener {
                startPostponedEnterTransition()
                true
            }
        }
    }

    private fun observeInitialLoadState() {
        lifecycleScope.launch {
            charactersAdapter.loadStateFlow.collect { loadState ->

                headerAdapter.loadState = loadState.mediator?.refresh?.takeIf {
                    it is LoadState.Error && charactersAdapter.itemCount > 0
                } ?: loadState.prepend


                binding.flipperCharacters.displayedChild = when {

                    loadState.mediator?.refresh is LoadState.Loading -> {
                        setShimmerVisibility(true)
                        FLIPPER_CHILD_LOADING
                    }

                    loadState.mediator?.refresh is LoadState.Error && charactersAdapter.itemCount == 0 -> {
                        setShimmerVisibility(false)
                        binding.includeViewCharactersErrorState.buttonRetry.setOnClickListener {
                            charactersAdapter.retry()
                        }
                        FLIPPER_CHILD_ERROR
                    }

                    loadState.source.refresh is LoadState.NotLoading
                            || loadState.mediator?.refresh is LoadState.NotLoading -> {
                        setShimmerVisibility(false)
                        FLIPPER_CHILD_CHARACTERS
                    }
                    else -> {
                        setShimmerVisibility(false)
                        FLIPPER_CHILD_CHARACTERS
                    }
                }
            }
        }
    }

    private fun setShimmerVisibility(visibility: Boolean) {
        binding.includeViewCharactersLoadingState.shimmerCharacters.run {
            isVisible = visibility
            if (visibility) {
                startShimmer()
            } else stopShimmer()
        }
    }

    private fun observeSortingData() {
        val navBackStackEntry = findNavController().getBackStackEntry(R.id.charactersFragment)
        val observer = LifecycleEventObserver { _, event ->
            val isSortingApplied = navBackStackEntry.savedStateHandle
                .contains(SortFragment.SORTING_APPLIED_BACK_STACK_KEY)

            if (event == Lifecycle.Event.ON_RESUME && isSortingApplied) {
                viewModel.applySort()
                navBackStackEntry.savedStateHandle
                    .remove<Boolean>(SortFragment.SORTING_APPLIED_BACK_STACK_KEY)
            }
        }

        navBackStackEntry.lifecycle.addObserver(observer)

        viewLifecycleOwner.lifecycle.addObserver(LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_DESTROY) {
                navBackStackEntry.lifecycle.removeObserver(observer)
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menuSort -> {
                findNavController().navigate(R.id.action_charactersFragment_to_sortFragment)
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.characters_menu_items, menu)

        val searchItem = menu.findItem(R.id.menu_search)
        searchView = searchItem.actionView as SearchView

        searchItem.setOnActionExpandListener(this)

        if (viewModel.currentSearchQuery.isNotEmpty()) {
            searchItem.expandActionView()
            searchView.setQuery(viewModel.currentSearchQuery, false)
        }

        searchView.run {
            //Habilita o botao de pesquisa dentro do searchView
            isSubmitButtonEnabled = true

            setOnQueryTextListener(this@CharactersFragment)
        }
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return query?.let {
            viewModel.currentSearchQuery = it
            viewModel.searchCharacters()
            true
        } ?: false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        return true
    }

    override fun onMenuItemActionExpand(item: MenuItem): Boolean {
        return true
    }

    override fun onMenuItemActionCollapse(item: MenuItem): Boolean {
        viewModel.closeSearch()
        viewModel.searchCharacters()
        return true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        searchView.setOnQueryTextListener(null)
    }

    companion object {
        private const val FLIPPER_CHILD_LOADING = 0
        private const val FLIPPER_CHILD_CHARACTERS = 1
        private const val FLIPPER_CHILD_ERROR = 2
    }
}