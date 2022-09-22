package com.example.marvelapp.presentation.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.domain.model.Comic
import com.example.core.usecase.GetComicsUseCase
import com.example.core.usecase.base.ResultStatus
import com.example.marvelapp.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val getComicsUseCase: GetComicsUseCase
) : ViewModel() {

    private val _uiState = MutableLiveData<UiState>()
    val uiState: LiveData<UiState> get() = _uiState

    fun geComics(characterId: Int) = viewModelScope.launch {
        getComicsUseCase(GetComicsUseCase.GetComicsParams(characterId)).watchStatus()
    }

    private fun Flow<ResultStatus<List<Comic>>>.watchStatus() = viewModelScope.launch {
        collect { status ->
            _uiState.value = when (status) {
                // Sempre que for uma data Class deve chamar o is. Já para object não é necessário.
                // Isso pq o object não tem tipo e valor interno.
                ResultStatus.Loading -> UiState.Loading
                is ResultStatus.Success -> {
                    val detailChildList = status.data.map { DetailChildViewEnd(it.id, it.imageUrl) }

                    val detailParentList =
                        listOf(
                            DetailParentViewEnd(
                                R.string.details_comics_category,
                                detailChildList
                            )
                        )

                    UiState.Success(detailParentList)
                }
                is ResultStatus.Error -> UiState.Error
            }
        }
    }

    sealed class UiState {
        object Loading : UiState()
        data class Success(val detailParentList: List<DetailParentViewEnd>) : UiState()
        object Error : UiState()
    }
}