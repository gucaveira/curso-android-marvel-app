package com.example.core.usecase

import com.example.core.data.mapper.SortingMapper
import com.example.core.data.repository.StorageRepository
import com.example.core.usecase.SaveCharactersSortingUseCase.Params
import com.example.core.usecase.base.CoroutinesDispatchers
import com.example.core.usecase.base.ResultStatus
import com.example.core.usecase.base.UserCase
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

interface SaveCharactersSortingUseCase {

    operator fun invoke(params: Params): Flow<ResultStatus<Unit>>

    data class Params(val sortingPair: Pair<String, String>)
}

class SaveCharactersSortingUseCaseImpl @Inject constructor(
    private val repository: StorageRepository,
    private val sortingMapper: SortingMapper,
    private val dispatchers: CoroutinesDispatchers
) : UserCase<Params, Unit>(), SaveCharactersSortingUseCase {

    override suspend fun doWork(params: Params): ResultStatus<Unit> {
        return withContext(dispatchers.io()) {
            repository.saveSorting(sortingMapper.mapFromPair(params.sortingPair))
            ResultStatus.Success(Unit)
        }
    }
}