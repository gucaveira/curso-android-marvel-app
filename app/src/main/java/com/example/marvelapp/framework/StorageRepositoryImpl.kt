package com.example.marvelapp.framework

import com.example.core.data.repository.StorageLocalDatasource
import com.example.core.data.repository.StorageRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class StorageRepositoryImpl @Inject constructor(
    private val storageLocalDatasource: StorageLocalDatasource
) : StorageRepository {
    override val sorting: Flow<String>
        get() = storageLocalDatasource.sorting

    override suspend fun saveSorting(sorting: String) {
        storageLocalDatasource.saveSorting(sorting)
    }
}