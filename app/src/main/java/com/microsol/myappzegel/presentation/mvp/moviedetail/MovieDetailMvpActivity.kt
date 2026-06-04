package com.microsol.myappzegel.presentation.mvp.moviedetail

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import coil.load
import com.microsol.myappzegel.R
import com.microsol.myappzegel.data.remote.RetrofitClient
import com.microsol.myappzegel.data.repository.MovieRepositoryImpl
import com.microsol.myappzegel.databinding.ActivityMovieDetailBinding
import com.microsol.myappzegel.domain.model.Movie

class MovieDetailMvpActivity : AppCompatActivity(), MovieDetailContract.View {

    companion object {
        const val EXTRA_MOVIE_ID = "extra_movie_id"
    }

    private lateinit var binding: ActivityMovieDetailBinding

    private val presenter = MovieDetailPresenter(
        MovieRepositoryImpl(RetrofitClient.tmdbApi)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener { presenter.onBackClicked() }

        presenter.attachView(this)

        val movieId = intent.getIntExtra(EXTRA_MOVIE_ID, -1)
        if (movieId != -1) {
            presenter.loadMovie(movieId)
        }
    }

    // --- MovieDetailContract.View implementation ---

    override fun showMovie(movie: Movie) {
        binding.tvDetailTitle.text = movie.title
        binding.tvDetailDescription.text = movie.description
        binding.tvDetailGenre.text = movie.genre
        binding.tvDetailYear.text = movie.year.toString()
        binding.tvDetailDuration.text = movie.duration
        binding.ratingBar.rating = movie.rating
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
                        binding.ivDetailPoster.setBackgroundColor(
                            getColor(getPlaceholderColor(movie.id))
                        )
                    }
                )
            }
        } else {
            binding.tvDetailPosterInitial.text = movie.title.first().uppercase()
            binding.ivDetailPoster.setBackgroundColor(getColor(getPlaceholderColor(movie.id)))
        }
    }

    override fun showLoading() {
        binding.cardContent.visibility = View.INVISIBLE
    }

    override fun hideLoading() {
        binding.cardContent.visibility = View.VISIBLE
    }

    override fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    override fun close() {
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDestroy()
    }

    private fun getPlaceholderColor(id: Int): Int {
        val colors = listOf(
            R.color.poster_red,
            R.color.poster_blue,
            R.color.poster_green,
            R.color.poster_purple,
            R.color.poster_orange,
            R.color.poster_teal
        )
        return colors[(id - 1).coerceAtLeast(0) % colors.size]
    }
}
