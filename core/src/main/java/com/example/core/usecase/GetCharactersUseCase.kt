package com.example.core.usecase

import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.core.data.repository.CharactersRepository
import com.example.core.data.repository.StorageRepository
import com.example.core.domain.model.Character
import com.example.core.usecase.GetCharactersUseCase.GetCharactersParams
import com.example.core.usecase.base.PagingUseCase
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

interface GetCharactersUseCase {
    operator fun invoke(params: GetCharactersParams): Flow<PagingData<Character>>

    data class GetCharactersParams(val query: String, val pagingConfig: PagingConfig)
}

class GetCharactersUseCaseImpl @Inject constructor(
    private val charactersRepository: CharactersRepository,
    private val storeRepository: StorageRepository
) :
    PagingUseCase<GetCharactersParams, Character>(), GetCharactersUseCase {

    override fun createFlowObservable(params: GetCharactersParams): Flow<PagingData<Character>> {
        //Basicamente o runBloking vai tratar uma funcao assincrona como sincrona
        // vai bloquear a continuação do codigo até a funcao assincrona terminar
        val orderBy = runBlocking { storeRepository.sorting.first() }
        return charactersRepository.getCachedCharacters(params.query, orderBy, params.pagingConfig)
    }

}