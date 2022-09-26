package com.example.marvelapp.framework.local

import com.example.core.data.repository.FavoritesLocalDataSource
import com.example.core.domain.model.Character
import com.example.marvelapp.framework.db.dao.FavoriteDao
import com.example.marvelapp.framework.db.entity.FavoriteEntity
import com.example.marvelapp.framework.db.entity.toCharacterModel
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RoomFavoritesDataSource @Inject constructor(
    private val favoriteDao: FavoriteDao
) : FavoritesLocalDataSource {
    override suspend fun getAll(): Flow<List<Character>> {
        return favoriteDao.loadFavorites().map {
            it.toCharacterModel()
        }
    }

    override suspend fun save(character: Character) {
        favoriteDao.insertFavorite(character.toFavoriteEntity())
    }

    override suspend fun delete(character: Character) {
        favoriteDao.deleteFavorite(character.toFavoriteEntity())
    }

    private fun Character.toFavoriteEntity() = FavoriteEntity(id, name, imageUrl)
}