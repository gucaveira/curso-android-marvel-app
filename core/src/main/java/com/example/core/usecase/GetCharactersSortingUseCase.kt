package com.example.core.usecase

import com.example.core.data.StorageConstants
import com.example.core.data.repository.StorageRepository
import com.example.core.usecase.base.CoroutinesDispatchers
import com.example.core.usecase.base.FlowUseCase
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

interface GetCharactersSortingUseCase {
    suspend operator fun invoke(params: Unit = Unit): Flow<Pair<String, String>>
}

class GetCharactersSortingUseCaseImpl @Inject constructor(
    private val storageRepository: StorageRepository, private val dispatchers: CoroutinesDispatchers
) : FlowUseCase<Unit, Pair<String, String>>(), GetCharactersSortingUseCase {

    override suspend fun createFlowObservable(params: Unit): Flow<Pair<String, String>> {
        return withContext(dispatchers.io()) {
            storageRepository.sorting.map { sorting ->

                when (sorting) {
                    StorageConstants.ORDER_BY_NAME_ASCENDING -> "name" to "ascending"
                    StorageConstants.ORDER_BY_NAME_DESCENDING -> "name" to "descending"
                    StorageConstants.ORDER_BY_MODIFIED_ASCENDING -> "modified" to "ascending"
                    StorageConstants.ORDER_BY_MODIFIED_DESCENDING -> "modified" to "descending"
                    else -> "name" to "ascending"
                }
            }
        }
    }
}