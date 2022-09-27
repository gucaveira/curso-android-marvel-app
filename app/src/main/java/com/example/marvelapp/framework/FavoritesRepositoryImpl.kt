package com.example.marvelapp.framework

import com.example.core.data.repository.FavoritesLocalDataSource
import com.example.core.data.repository.FavoritesRepository
import com.example.core.domain.model.Character
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class FavoritesRepositoryImpl @Inject constructor(
    private val favoritesLocalDataSource: FavoritesLocalDataSource
) : FavoritesRepository {
    override fun getAll(): Flow<List<Character>> {
        return favoritesLocalDataSource.getAll()
    }

    override suspend fun saveFavorite(character: Character) {
        favoritesLocalDataSource.save(character)
    }

    override suspend fun deleteFavorite(character: Character) {
        favoritesLocalDataSource.delete(character)
    }
}