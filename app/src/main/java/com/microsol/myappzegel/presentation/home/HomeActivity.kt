package com.microsol.myappzegel.presentation.home

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.microsol.myappzegel.R
import com.microsol.myappzegel.data.remote.RetrofitClient
import com.microsol.myappzegel.data.repository.FoodRepositoryImpl
import com.microsol.myappzegel.data.repository.MovieRepositoryImpl
import com.microsol.myappzegel.presentation.fooddetail.FoodDetailActivity
import com.microsol.myappzegel.presentation.home.adapter.FoodAdapter
import com.microsol.myappzegel.presentation.home.adapter.MovieAdapter
import com.microsol.myappzegel.presentation.moviedetail.MovieDetailActivity

class HomeActivity : AppCompatActivity() {

    private lateinit var rvMovies: RecyclerView
    private lateinit var rvFoods: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var tvError: TextView
    private lateinit var viewModel: HomeViewModel

    private val movieAdapter = MovieAdapter { movie ->
        val intent = Intent(this, MovieDetailActivity::class.java)
        intent.putExtra(MovieDetailActivity.Companion.EXTRA_MOVIE_ID, movie.id)
        startActivity(intent)
    }

    private val foodAdapter = FoodAdapter { food ->
        val intent = Intent(this, FoodDetailActivity::class.java)
        intent.putExtra(FoodDetailActivity.Companion.EXTRA_FOOD_ID, food.id)
        startActivity(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rvMovies = findViewById(R.id.rvMovies)
        rvFoods = findViewById(R.id.rvFoods)
        progressBar = findViewById(R.id.progressBar)
        tvError = findViewById(R.id.tvError)

        setupRecyclerViews()

        // Wire dependencies manually — no DI framework at this stage
        val movieRepository = MovieRepositoryImpl(RetrofitClient.tmdbApi)
        val foodRepository = FoodRepositoryImpl(RetrofitClient.mealDbApi)
        val factory = HomeViewModelFactory(movieRepository, foodRepository)
        viewModel = ViewModelProvider(this, factory)[HomeViewModel::class.java]

        observeViewModel()
    }

    private fun setupRecyclerViews() {
        rvMovies.layoutManager = LinearLayoutManager(
            this, LinearLayoutManager.HORIZONTAL, false
        )
        rvMovies.adapter = movieAdapter

        rvFoods.layoutManager = LinearLayoutManager(this)
        rvFoods.adapter = foodAdapter
    }

    private fun observeViewModel() {
        viewModel.isLoading.observe(this) { loading ->
            progressBar.visibility = if (loading) View.VISIBLE else View.GONE
        }

        viewModel.error.observe(this) { errorMsg ->
            if (!errorMsg.isNullOrBlank()) {
                tvError.text = errorMsg
                tvError.visibility = View.VISIBLE
            } else {
                tvError.visibility = View.GONE
            }
        }

        viewModel.movies.observe(this) { movies ->
            movieAdapter.submitList(movies)
        }

        viewModel.foods.observe(this) { foods ->
            foodAdapter.submitList(foods)
        }
    }
}