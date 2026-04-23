package com.microsol.myappzegel.presentation.moviedetail

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.microsol.myappzegel.data.repository.MovieRepositoryImpl
import com.microsol.myappzegel.databinding.ActivityMovieDetailBinding
import com.microsol.myappzegel.domain.usecase.GetMoviesUseCase

// ─────────────────────────────────────────────────────────────────────────────
// Movie Detail Screen
//
// Receives a movie ID from MainActivity via Intent extras, loads the movie
// from the ViewModel and displays its details.
// ─────────────────────────────────────────────────────────────────────────────
class MovieDetailActivity : AppCompatActivity() {

    companion object {
        // KOTLIN CONCEPT: companion object
        // Constants placed here act like Java's static fields. Using a
        // companion object keeps them associated with the Activity class.
        const val EXTRA_MOVIE_ID = "extra_movie_id"
    }

    private lateinit var binding: ActivityMovieDetailBinding

    private val viewModel: MovieDetailViewModel by viewModels {
        MovieDetailViewModelFactory(
            GetMoviesUseCase(MovieRepositoryImpl())
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Back button on the toolbar
        binding.btnBack.setOnClickListener { finish() }

        // Retrieve the id passed from MainActivity
        val movieId = intent.getIntExtra(EXTRA_MOVIE_ID, -1)
        if (movieId != -1) {
            viewModel.loadMovie(movieId)
        }

        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.movie.observe(this) { movie ->
            movie ?: return@observe   // if null, do nothing

            binding.tvDetailTitle.text = movie.title
            binding.tvDetailDescription.text = movie.description
            binding.tvDetailGenre.text = movie.genre
            binding.tvDetailYear.text = movie.year.toString()
            binding.tvDetailDirector.text = "Dir. ${movie.director}"
            binding.tvDetailDuration.text = movie.duration
            binding.ratingBar.rating = movie.rating
            binding.tvDetailRatingValue.text = "%.1f / 5.0".format(movie.rating)

            // Color placeholder poster
            val colorRes = getPlaceholderColor(movie.id.value)
            binding.ivDetailPoster.setBackgroundColor(getColor(colorRes))
            binding.tvDetailPosterInitial.text = movie.title.first().uppercase()
        }
    }

    private fun getPlaceholderColor(id: Int): Int {
        val colors = listOf(
            com.microsol.myappzegel.R.color.poster_red,
            com.microsol.myappzegel.R.color.poster_blue,
            com.microsol.myappzegel.R.color.poster_green,
            com.microsol.myappzegel.R.color.poster_purple,
            com.microsol.myappzegel.R.color.poster_orange,
            com.microsol.myappzegel.R.color.poster_teal
        )
        return colors[(id - 1) % colors.size]
    }
}
