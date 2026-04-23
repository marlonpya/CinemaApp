package com.microsol.myappzegel.data.repository

import com.microsol.myappzegel.data.model.Movie
import com.microsol.myappzegel.data.model.MovieId

// ─────────────────────────────────────────────────────────────────────────────
// KOTLIN CONCEPT: Class implementing an Interface (Inheritance via interface)
// SOLID PRINCIPLE: Single Responsibility (S)
//
// This class has ONE job: supply movie data. It implements MovieRepository
// with hardcoded (fake) data. In a real app this would call a REST API or
// Room database, but the rest of the codebase would not change at all.
// ─────────────────────────────────────────────────────────────────────────────
class MovieRepositoryImpl : MovieRepository {

    // KOTLIN CONCEPT: val with a list initializer
    // `movies` is private and immutable — it is set once and never reassigned.
    private val movies: List<Movie> = buildMovieList()

    override fun getMovies(): List<Movie> = movies

    override fun getMovieById(id: MovieId): Movie? {
        // KOTLIN CONCEPT: Lambda passed to a higher-order function (find)
        // `find { }` iterates and returns the first element matching the
        // predicate lambda, or null if none is found.
        return movies.find { it.id == id }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Hardcoded movie catalogue — 6 classic / popular films
    // ─────────────────────────────────────────────────────────────────────────
    private fun buildMovieList(): List<Movie> = listOf(
        Movie(
            id = MovieId(1),
            title = "Inception",
            description = "Un ladrón que roba secretos corporativos usando tecnología para entrar en los sueños de las personas recibe la tarea de implantar una idea en la mente de un CEO.",
            genre = "Ciencia Ficción",
            year = 2010,
            rating = 4.8f,
            imageUrl = "movie_inception",
            director = "Christopher Nolan",
            duration = "2h 28min"
        ),
        Movie(
            id = MovieId(2),
            title = "The Dark Knight",
            description = "Batman eleva su guerra contra el crimen de Gotham con la ayuda del teniente Jim Gordon y el fiscal Harvey Dent, pero pronto se encuentran perturbados por el caótico Joker.",
            genre = "Acción",
            year = 2008,
            rating = 4.9f,
            imageUrl = "movie_dark_knight",
            director = "Christopher Nolan",
            duration = "2h 32min"
        ),
        Movie(
            id = MovieId(3),
            title = "Interstellar",
            description = "Un equipo de exploradores viaja a través de un agujero de gusano en el espacio en un intento de garantizar la supervivencia de la humanidad.",
            genre = "Aventura",
            year = 2014,
            rating = 4.7f,
            imageUrl = "movie_interstellar",
            director = "Christopher Nolan",
            duration = "2h 49min"
        ),
        Movie(
            id = MovieId(4),
            title = "Parasite",
            description = "Una familia empobrecida conspira para hacerse indispensable para una familia adinerada al infiltrarse en su hogar y emplearse como personal de servicio.",
            genre = "Thriller",
            year = 2019,
            rating = 4.6f,
            imageUrl = "movie_parasite",
            director = "Bong Joon-ho",
            duration = "2h 12min"
        ),
        Movie(
            id = MovieId(5),
            title = "Spider-Man: No Way Home",
            description = "Peter Parker pide ayuda al Doctor Strange para que el mundo olvide que él es Spider-Man, pero el hechizo libera supervillanos de otros universos.",
            genre = "Acción",
            year = 2021,
            rating = 4.5f,
            imageUrl = "movie_spiderman",
            director = "Jon Watts",
            duration = "2h 28min"
        ),
        Movie(
            id = MovieId(6),
            title = "Dune",
            description = "Un joven noble liderado por el destino debe viajar al planeta más peligroso del universo para asegurar el futuro de su familia y su pueblo.",
            genre = "Épica",
            year = 2021,
            rating = 4.4f,
            imageUrl = "movie_dune",
            director = "Denis Villeneuve",
            duration = "2h 35min"
        )
    )
}
