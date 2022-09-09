package com.example.core.usecase

import androidx.paging.PagingConfig
import com.example.core.data.repository.CharactersRepository
import com.exemple.testing.MainCoroutineRule
import com.exemple.testing.model.CharacterFactory
import com.exemple.testing.pagingsource.PagingSourceFactory
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
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

    private lateinit var getCharactersUseCase: GetCharactersUseCase

    private val hero = CharacterFactory().create(CharacterFactory.Hero.ThreeDMan)

    private val fakePagingSource = PagingSourceFactory().create(listOf(hero))

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        getCharactersUseCase = GetCharactersUseCaseImpl(repository)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `should validate flow paging data creation when invoke from use case is called`() =
        runBlockingTest {

            whenever(repository.getCharacters("")).thenReturn(fakePagingSource)

            val result = getCharactersUseCase
                .invoke(GetCharactersUseCase.GetCharactersParams("", PagingConfig(20)))
            verify(repository).getCharacters("")

            assertNotNull(result.first())
        }
}