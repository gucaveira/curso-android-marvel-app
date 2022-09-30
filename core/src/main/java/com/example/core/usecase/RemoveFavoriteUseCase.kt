package com.example.core.usecase

import com.example.core.data.repository.FavoritesRepository
import com.example.core.domain.model.Character
import com.example.core.usecase.base.CoroutinesDispatchers
import com.example.core.usecase.base.ResultStatus
import com.example.core.usecase.base.UserCase
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

interface RemoveFavoriteUseCase {

    operator fun invoke(params: Params): Flow<ResultStatus<Unit>>

    data class Params(val characterId: Int, val name: String, val imageUrl: String)
}

class RemoveFavoriteUseCaseImpl @Inject constructor(
    private val repository: FavoritesRepository, private val dispatchers: CoroutinesDispatchers
) : UserCase<RemoveFavoriteUseCase.Params, Unit>(), RemoveFavoriteUseCase {

    override suspend fun doWork(params: RemoveFavoriteUseCase.Params): ResultStatus<Unit> {
        return withContext(dispatchers.io()) {
            repository.deleteFavorite(Character(params.characterId, params.name, params.imageUrl))
            ResultStatus.Success(Unit)
        }
    }
}