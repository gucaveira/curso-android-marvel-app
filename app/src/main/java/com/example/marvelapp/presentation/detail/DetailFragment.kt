package com.example.marvelapp.presentation.detail

import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.marvelapp.databinding.FragmentDetailBinding
import com.example.marvelapp.framework.imageLoader.ImageLoader
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DetailFragment : Fragment() {

    private val viewModel: DetailViewModel by viewModels()

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var imageLoader: ImageLoader

    private val args by navArgs<DetailFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentDetailBinding.inflate(inflater, container, false).apply {
        _binding = this
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val detailViewArg = args.detailViewArg
        binding.imageCharacter.run {
            transitionName = detailViewArg.name
            imageLoader.load(this, detailViewArg.imageUrl)
        }

        setSharedElementTransitionOnEnter()

        viewModel.uiState.observe(viewLifecycleOwner) { uiState ->
            binding.flipperDetail.displayedChild = when (uiState) {
                DetailViewModel.UiState.Loading -> {
                    setShimmerVisibility(true)
                    FLIPPER_CHILD_POSITION_LOADING
                }
                is DetailViewModel.UiState.Success -> {
                    setRecyclerView(uiState)
                    setShimmerVisibility(false)
                    FLIPPER_CHILD_POSITION_DETAIL
                }
                is DetailViewModel.UiState.Error -> {
                    binding.includeErrorView.buttonRetry.setOnClickListener {
                        viewModel.getCharacterCategories(detailViewArg.characterId)
                    }
                    setShimmerVisibility(false)
                    FLIPPER_CHILD_POSITION_ERROR
                }
                DetailViewModel.UiState.Empty -> FLIPPER_CHILD_POSITION_EMPTY
            }

        }

        viewModel.getCharacterCategories(detailViewArg.characterId)
    }

    private fun setRecyclerView(uiState: DetailViewModel.UiState.Success) {
        binding.RecyclerParentDetail.run {
            setHasFixedSize(true)
            adapter = DetailParentAdapter(uiState.detailParentList, imageLoader)
        }
    }

    private fun setShimmerVisibility(visibility: Boolean) {
        binding.includeLoadingState.shimmerData.run {
            isVisible = visibility
            if (visibility) {
                startShimmer()
            } else stopShimmer()
        }
    }

    // Define a animação de transição como "move"
    private fun setSharedElementTransitionOnEnter() {
        TransitionInflater.from(requireContext()).inflateTransition(android.R.transition.move)
            .apply {
                sharedElementEnterTransition = this
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val FLIPPER_CHILD_POSITION_LOADING = 0
        private const val FLIPPER_CHILD_POSITION_DETAIL = 1
        private const val FLIPPER_CHILD_POSITION_ERROR = 2
        private const val FLIPPER_CHILD_POSITION_EMPTY = 3
    }
}