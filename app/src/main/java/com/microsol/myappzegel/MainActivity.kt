package com.microsol.myappzegel

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.microsol.myappzegel.data.remote.RetrofitClient
import com.microsol.myappzegel.data.repository.FoodRepositoryImpl
import com.microsol.myappzegel.data.repository.MovieRepositoryImpl
import com.microsol.myappzegel.databinding.ActivityMainBinding
import com.microsol.myappzegel.domain.usecase.GetFoodsUseCase
import com.microsol.myappzegel.domain.usecase.GetMoviesUseCase
import com.microsol.myappzegel.presentation.fooddetail.FoodDetailActivity
import com.microsol.myappzegel.presentation.home.HomeViewModelFactory
import com.microsol.myappzegel.presentation.home.HomeViewModel
import com.microsol.myappzegel.presentation.home.adapter.FoodAdapter
import com.microsol.myappzegel.presentation.home.adapter.MovieAdapter
import com.microsol.myappzegel.presentation.moviedetail.MovieDetailActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val viewModel: HomeViewModel by viewModels {
        HomeViewModelFactory(
            getMoviesUseCase = GetMoviesUseCase(MovieRepositoryImpl(RetrofitClient.tmdbApi)),
            getFoodsUseCase  = GetFoodsUseCase(FoodRepositoryImpl(RetrofitClient.mealDbApi))
        )
    }

    private val movieAdapter = MovieAdapter { movie ->
        val intent = Intent(this, MovieDetailActivity::class.java).apply {
            putExtra(MovieDetailActivity.EXTRA_MOVIE_ID, movie.id.value)
        }
        startActivity(intent)
    }

    private val foodAdapter = FoodAdapter { food ->
        val intent = Intent(this, FoodDetailActivity::class.java).apply {
            putExtra(FoodDetailActivity.EXTRA_FOOD_ID, food.id.value)
        }
        startActivity(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerViews()
        observeViewModel()
    }

    private fun setupRecyclerViews() {
        binding.rvMovies.apply {
            layoutManager = LinearLayoutManager(
                this@MainActivity,
                LinearLayoutManager.HORIZONTAL,
                false
            )
            adapter = movieAdapter
        }

        binding.rvFoods.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = foodAdapter
        }
    }

    private fun observeViewModel() {
        viewModel.isLoading.observe(this) { loading ->
            binding.progressBar.visibility = if (loading) View.VISIBLE else View.GONE
        }

        viewModel.error.observe(this) { errorMsg ->
            if (!errorMsg.isNullOrBlank()) {
                binding.tvError.text = errorMsg
                binding.tvError.visibility = View.VISIBLE
            } else {
                binding.tvError.visibility = View.GONE
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
