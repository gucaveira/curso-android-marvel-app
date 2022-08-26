package com.example.core.data.repository

interface CharactersRemoteDataSource<T> {

    suspend fun fetchCharecters(queries: Map<String, String>): T
}