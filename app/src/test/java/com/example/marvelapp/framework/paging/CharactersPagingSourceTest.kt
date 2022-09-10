package com.example.marvelapp.framework.paging

import androidx.paging.PagingSource
import com.example.core.data.repository.CharactersRemoteDataSource
import com.example.core.domain.model.Character
import com.example.factory.response.DataWrapperResponseFactory
import com.example.marvelapp.framework.network.response.DataWrapperResponse
import com.exemple.testing.MainCoroutineRule
import com.exemple.testing.model.CharacterFactory
import com.exemple.testing.model.CharacterFactory.Hero
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class CharactersPagingSourceTest {

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Mock
    lateinit var remoteDataSource: CharactersRemoteDataSource<DataWrapperResponse>

    private var dataWrapperResponseFactory = DataWrapperResponseFactory()

    private val charactersFactory = CharacterFactory()

    private lateinit var charactersPagingSource: CharactersPagingSource

    @Before
    fun setUp() {
        charactersPagingSource = CharactersPagingSource(remoteDataSource, "")
    }

    @Test
    fun `should return a success load result when load is called`() = runBlockingTest {
        //Arrange
        whenever(remoteDataSource.fetchCharecters(any()))
            .thenReturn(dataWrapperResponseFactory.create())

        //Act
        val result = charactersPagingSource
            .load(
                PagingSource.LoadParams.Refresh(
                    key = null,
                    loadSize = 2,
                    placeholdersEnabled = false
                )
            )

        //Assert
        val expected = listOf(
            charactersFactory.create(Hero.ThreeDMan),
            charactersFactory.create(Hero.ABomb)
        )
        assertEquals(
            PagingSource
                .LoadResult.Page(
                    data = expected,
                    prevKey = null,
                    nextKey = 20
                ),
            result
        )
    }

    @Test
    fun `should return a errror load result when load is called`() = runBlockingTest {
        //arrange
        val exception = RuntimeException()
        whenever(remoteDataSource.fetchCharecters(any())).thenThrow(exception)

        //act
        val result = charactersPagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 2,
                placeholdersEnabled = false
            )
        )

        //assert
        assertEquals(PagingSource.LoadResult.Error<Int, Character>(exception), result)
    }
}