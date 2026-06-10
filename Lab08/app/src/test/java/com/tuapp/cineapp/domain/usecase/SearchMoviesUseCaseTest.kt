package com.tuapp.cineapp.domain.usecase

import com.tuapp.cineapp.data.repository.FakeMovieRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SearchMoviesUseCaseTest {

    private val fakeRepo = FakeMovieRepository()
    private val useCase = SearchMoviesUseCase(fakeRepo)

    @Test
    fun `retorna lista filtrada cuando query tiene resultados`() = runTest {
        val result = useCase("Dune")
        assertTrue(result.isSuccess)
        assertEquals(1, result.getOrNull()?.size)
        assertEquals("Dune: Parte Dos", result.getOrNull()?.first()?.title)
    }

    @Test
    fun `retorna lista vacia cuando query no tiene resultados`() = runTest {
        val result = useCase("XYZ_NO_EXISTE")
        assertTrue(result.isSuccess)
        assertTrue(result.getOrNull()?.isEmpty() == true)
    }

    @Test
    fun `retorna lista vacia cuando query esta en blanco`() = runTest {
        val result = useCase("   ")
        assertTrue(result.isSuccess)
        assertTrue(result.getOrNull()?.isEmpty() == true)
    }

    @Test
    fun `retorna error cuando el repositorio falla`() = runTest {
        fakeRepo.shouldReturnError = true
        val result = useCase("Dune")
        assertTrue(result.isFailure)
    }
}
