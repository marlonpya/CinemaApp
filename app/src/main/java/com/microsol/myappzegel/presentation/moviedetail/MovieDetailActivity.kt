package com.microsol.myappzegel.presentation.moviedetail

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import coil.load
import com.microsol.myappzegel.data.remote.RetrofitClient
import com.microsol.myappzegel.data.repository.MovieRepositoryImpl
import com.microsol.myappzegel.databinding.ActivityMovieDetailBinding
import com.microsol.myappzegel.domain.usecase.GetMoviesUseCase

class MovieDetailActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_MOVIE_ID = "extra_movie_id"
    }

    private lateinit var binding: ActivityMovieDetailBinding

    private val viewModel: MovieDetailViewModel by viewModels {
        MovieDetailViewModelFactory(
            GetMoviesUseCase(MovieRepositoryImpl(RetrofitClient.tmdbApi))
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener { finish() }

        val movieId = intent.getIntExtra(EXTRA_MOVIE_ID, -1)
        if (movieId != -1) {
            viewModel.loadMovie(movieId)
        }

        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.isLoading.observe(this) { loading ->
            binding.cardContent.visibility = if (loading) View.INVISIBLE else View.VISIBLE
        }

        viewModel.error.observe(this) { errorMsg ->
            if (!errorMsg.isNullOrBlank()) {
                Toast.makeText(this, errorMsg, Toast.LENGTH_LONG).show()
            }
        }

        viewModel.movie.observe(this) { movie ->
            movie ?: return@observe

            binding.tvDetailTitle.text       = movie.title
            binding.tvDetailDescription.text = movie.description
            binding.tvDetailGenre.text       = movie.genre
            binding.tvDetailYear.text        = movie.year.toString()
            binding.tvDetailDuration.text    = movie.duration
            binding.ratingBar.rating         = movie.rating
            binding.tvDetailRatingValue.text = "%.1f / 5.0".format(movie.rating)

            if (movie.director.isNotBlank()) {
                binding.tvDetailDirector.text = "Dir. ${movie.director}"
                binding.tvDetailDirector.visibility = View.VISIBLE
            } else {
                binding.tvDetailDirector.visibility = View.GONE
            }

            if (movie.imageUrl.isNotBlank()) {
                binding.ivDetailPosterImage.visibility = View.VISIBLE
                binding.tvDetailPosterInitial.visibility = View.GONE
                binding.ivDetailPosterImage.load(movie.imageUrl) {
                    crossfade(true)
                    listener(
                        onError = { _, _ ->
                            binding.ivDetailPosterImage.visibility = View.GONE
                            binding.tvDetailPosterInitial.text = movie.title.first().uppercase()
                            binding.tvDetailPosterInitial.visibility = View.VISIBLE
                            binding.ivDetailPoster.setBackgroundColor(getColor(getPlaceholderColor(movie.id.value)))
                        }
                    )
                }
            } else {
                binding.tvDetailPosterInitial.text = movie.title.first().uppercase()
                binding.ivDetailPoster.setBackgroundColor(getColor(getPlaceholderColor(movie.id.value)))
            }
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
        return colors[(id - 1).coerceAtLeast(0) % colors.size]
    }
}
