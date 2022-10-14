package com.example.marvelapp.framework.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.core.data.StorageConstants.MARVEL_DATA_STORE_NAME
import com.example.core.data.StorageConstants.SORT_ORDER_BY_KEY
import com.example.core.data.repository.StorageLocalDatasource
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DataStorePreferencesDataSource @Inject constructor(
    @ApplicationContext private val context: Context
) : StorageLocalDatasource {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
        name = MARVEL_DATA_STORE_NAME
    )

    private val sortingKey = stringPreferencesKey(SORT_ORDER_BY_KEY)

    override val sorting: Flow<String>
        get() = context.dataStore.data.map { marvelStore ->
            marvelStore[sortingKey].orEmpty()
        }

    override suspend fun saveSorting(sorting: String) {
        context.dataStore.edit { marvelStore ->
            marvelStore[sortingKey] = sorting
        }
    }
}