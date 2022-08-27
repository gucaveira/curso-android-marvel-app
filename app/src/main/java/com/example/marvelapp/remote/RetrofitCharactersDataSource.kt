package com.example.marvelapp.remote

import com.example.core.data.repository.CharactersRemoteDataSource
import com.example.marvelapp.framework.network.MarvelApi
import com.example.marvelapp.framework.network.response.DataWrapperResponse
import javax.inject.Inject

class RetrofitCharactersDataSource @Inject constructor(private val marvelApi: MarvelApi) :
    CharactersRemoteDataSource<DataWrapperResponse> {
    override suspend fun fetchCharecters(queries: Map<String, String>): DataWrapperResponse {
        return marvelApi.getCharacters(queries)
    }
}