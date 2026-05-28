package com.microsol.myappzegel.presentation.moviedetail

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.lifecycle.ViewModelProvider
import coil.load
import com.microsol.myappzegel.R
import com.microsol.myappzegel.data.remote.RetrofitClient
import com.microsol.myappzegel.data.repository.MovieRepositoryImpl

class MovieDetailActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_MOVIE_ID = "extra_movie_id"
    }

    private lateinit var ivDetailPoster: FrameLayout
    private lateinit var ivDetailPosterImage: ImageView
    private lateinit var tvDetailPosterInitial: TextView
    private lateinit var btnBack: Button
    private lateinit var cardContent: CardView
    private lateinit var tvDetailTitle: TextView
    private lateinit var tvDetailGenre: TextView
    private lateinit var tvDetailYear: TextView
    private lateinit var tvDetailDuration: TextView
    private lateinit var tvDetailDirector: TextView
    private lateinit var ratingBar: RatingBar
    private lateinit var tvDetailRatingValue: TextView
    private lateinit var tvDetailDescription: TextView

    private lateinit var viewModel: MovieDetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_detail)

        ivDetailPoster = findViewById(R.id.ivDetailPoster)
        ivDetailPosterImage = findViewById(R.id.ivDetailPosterImage)
        tvDetailPosterInitial = findViewById(R.id.tvDetailPosterInitial)
        btnBack = findViewById(R.id.btnBack)
        cardContent = findViewById(R.id.cardContent)
        tvDetailTitle = findViewById(R.id.tvDetailTitle)
        tvDetailGenre = findViewById(R.id.tvDetailGenre)
        tvDetailYear = findViewById(R.id.tvDetailYear)
        tvDetailDuration = findViewById(R.id.tvDetailDuration)
        tvDetailDirector = findViewById(R.id.tvDetailDirector)
        ratingBar = findViewById(R.id.ratingBar)
        tvDetailRatingValue = findViewById(R.id.tvDetailRatingValue)
        tvDetailDescription = findViewById(R.id.tvDetailDescription)

        btnBack.setOnClickListener { finish() }

        val movieRepository = MovieRepositoryImpl(RetrofitClient.tmdbApi)
        val factory = MovieDetailViewModelFactory(movieRepository)
        viewModel = ViewModelProvider(this, factory)[MovieDetailViewModel::class.java]

        val movieId = intent.getIntExtra(EXTRA_MOVIE_ID, -1)
        if (movieId != -1) {
            viewModel.loadMovie(movieId)
        }

        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.isLoading.observe(this) { loading ->
            cardContent.visibility = if (loading) View.INVISIBLE else View.VISIBLE
        }

        viewModel.error.observe(this) { errorMsg ->
            if (!errorMsg.isNullOrBlank()) {
                Toast.makeText(this, errorMsg, Toast.LENGTH_LONG).show()
            }
        }

        viewModel.movie.observe(this) { movie ->
            movie ?: return@observe

            tvDetailTitle.text = movie.title
            tvDetailDescription.text = movie.description
            tvDetailGenre.text = movie.genre
            tvDetailYear.text = movie.year.toString()
            tvDetailDuration.text = movie.duration
            ratingBar.rating = movie.rating
            tvDetailRatingValue.text = "%.1f / 5.0".format(movie.rating)

            if (movie.director.isNotBlank()) {
                tvDetailDirector.text = "Dir. ${movie.director}"
                tvDetailDirector.visibility = View.VISIBLE
            } else {
                tvDetailDirector.visibility = View.GONE
            }

            if (movie.imageUrl.isNotBlank()) {
                ivDetailPosterImage.visibility = View.VISIBLE
                tvDetailPosterInitial.visibility = View.GONE
                ivDetailPosterImage.load(movie.imageUrl) {
                    crossfade(true)
                    listener(
                        onError = { _, _ ->
                            ivDetailPosterImage.visibility = View.GONE
                            tvDetailPosterInitial.text = movie.title.first().uppercase()
                            tvDetailPosterInitial.visibility = View.VISIBLE
                            ivDetailPoster.setBackgroundColor(getColor(getPlaceholderColor(movie.id)))
                        }
                    )
                }
            } else {
                tvDetailPosterInitial.text = movie.title.first().uppercase()
                ivDetailPoster.setBackgroundColor(getColor(getPlaceholderColor(movie.id)))
            }
        }
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
