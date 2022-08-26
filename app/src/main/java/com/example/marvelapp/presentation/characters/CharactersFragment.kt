package com.example.marvelapp.presentation.characters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.core.domain.model.Character
import com.example.marvelapp.databinding.FragmentCharactersBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CharactersFragment : Fragment() {

    private var _binding: FragmentCharactersBinding? = null
    private val binding: FragmentCharactersBinding get() = _binding!!

    private val chractersAdapter = CharactersAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentCharactersBinding.inflate(inflater, container, false).apply {
        _binding = this
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initCharactersAdapter()

        chractersAdapter.submitList(
            listOf(
                Character(
                    "3D-Man",
                    "https://i.annihil.us/u/prod/marvel/i/mg/c/e0/535fecbbb9784.jbg"
                ),
                Character(
                    "3D-Man",
                    "https://i.annihil.us/u/prod/marvel/i/mg/c/e0/535fecbbb9784.jbg"
                ),
                Character(
                    "3D-Man",
                    "https://i.annihil.us/u/prod/marvel/i/mg/c/e0/535fecbbb9784.jbg"
                ),
                Character(
                    "3D-Man",
                    "https://i.annihil.us/u/prod/marvel/i/mg/c/e0/535fecbbb9784.jbg"
                )
            )
        )

    }

    private fun initCharactersAdapter() {
        binding.recyclerCharacters.run {
            // quando os item do lista tiverem o mesmo tamanho em dimensões,
            // usar essa função para otimizar a listagem.
            setHasFixedSize(true)
            adapter = chractersAdapter
        }
    }
}