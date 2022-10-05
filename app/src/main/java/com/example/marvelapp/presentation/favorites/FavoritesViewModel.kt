package com.example.marvelapp.presentation.favorites

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.switchMap
import com.example.core.usecase.GetFavoritesUseCase
import com.example.core.usecase.base.CoroutinesDispatchers
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.catch

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val getFavoritesUseCase: GetFavoritesUseCase,
    coroutinesDispatchers: CoroutinesDispatchers
) : ViewModel() {

    private val action = MutableLiveData<Action>()

    val state: LiveData<UiState> = action.switchMap { action ->
        liveData(coroutinesDispatchers.main()) {
            when (action) {
                is Action.GetAll -> {
                    getFavoritesUseCase().catch {
                        emit(UiState.ShowEmty)
                    }.collect {
                        val items = it.map { character ->
                            FavoriteItem(
                                character.id, character.name, character.imageUrl
                            )
                        }

                        val uiState = if (items.isEmpty()) {
                            UiState.ShowEmty
                        } else UiState.ShowFavorite(items)

                        emit(uiState)
                    }
                }
            }
        }
    }

    fun getAll() {
        action.value = Action.GetAll
    }

    sealed class UiState {
        data class ShowFavorite(val favorites: List<FavoriteItem>) : UiState()
        object ShowEmty : UiState()
    }

    sealed class Action {
        object GetAll : Action()
    }
}