package com.example.marvelapp.presentation.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.domain.model.Comic
import com.example.core.domain.model.Event
import com.example.core.usecase.GetCharacterCategoriesUseCase
import com.example.core.usecase.base.ResultStatus
import com.example.marvelapp.R
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val getCharacterCategoriesUseCase: GetCharacterCategoriesUseCase
) : ViewModel() {

    private val _uiState = MutableLiveData<UiState>()
    val uiState: LiveData<UiState> get() = _uiState

    fun getCharacterCategories(characterId: Int) = viewModelScope.launch {
        getCharacterCategoriesUseCase(
            GetCharacterCategoriesUseCase.GetCategoriesParams(characterId)
        ).watchStatus()
    }

    private fun Flow<ResultStatus<Pair<List<Comic>, List<Event>>>>.watchStatus() =
        viewModelScope.launch {
            collect { status ->
                _uiState.value = when (status) {
                    // Sempre que for uma data Class deve chamar o is. Já para object não é necessário.
                    // Isso pq o object não tem tipo e valor interno.
                    ResultStatus.Loading -> UiState.Loading
                    is ResultStatus.Success -> {
                        val detailParentList = mutableListOf<DetailParentViewEnd>()
                        val comics = status.data.first
                        val events = status.data.second

                        if (comics.isNotEmpty()) {
                            comics.map { DetailChildViewEnd(it.id, it.imageUrl) }
                                .also { detailParentList.add(
                                    DetailParentViewEnd(R.string.details_comics_category, it)) }
                        }

                        if (events.isNotEmpty()) {
                            events.map { DetailChildViewEnd(it.id, it.imageUrl) }
                                .also { detailParentList.add(
                                    DetailParentViewEnd(R.string.details_events_category, it)) }
                        }

                        if (detailParentList.isNotEmpty()) {
                            UiState.Success(detailParentList)
                        } else UiState.Empty

                    }
                    is ResultStatus.Error -> UiState.Error
                }
            }
        }

    sealed class UiState {
        object Loading : UiState()
        data class Success(val detailParentList: List<DetailParentViewEnd>) : UiState()
        object Error : UiState()
        object Empty : UiState()
    }
}