package com.example.core.usecase

import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.core.data.repository.CharactersRepository
import com.example.core.data.repository.StorageRepository
import com.exemple.testing.MainCoroutineRule
import com.exemple.testing.model.CharacterFactory
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.MockitoAnnotations

@RunWith(JUnit4::class)
class GetCharactersUseCaseImplTest {

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Mock
    lateinit var repository: CharactersRepository

    @Mock
    lateinit var storeRepository: StorageRepository


    private lateinit var getCharactersUseCase: GetCharactersUseCase

    private val hero = CharacterFactory().create(CharacterFactory.Hero.ThreeDMan)

    private val fakePagingData = PagingData.from(listOf(hero))

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        getCharactersUseCase = GetCharactersUseCaseImpl(repository, storeRepository)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `should validate flow paging data creation when invoke from use case is called`() =
        runTest {

            val pagingConfig = PagingConfig(20)
            val orderBy = "ascending"
            val query = "spider"

            whenever(
                repository.getCachedCharacters(query, orderBy, pagingConfig)
            ).thenReturn(flowOf(fakePagingData))

            whenever(storeRepository.sorting)
                .thenReturn(flowOf(orderBy))

            val result = getCharactersUseCase
                .invoke(GetCharactersUseCase.GetCharactersParams(query, pagingConfig))
            verify(repository).getCachedCharacters(query, orderBy, pagingConfig)

            assertNotNull(result.first())
        }
}