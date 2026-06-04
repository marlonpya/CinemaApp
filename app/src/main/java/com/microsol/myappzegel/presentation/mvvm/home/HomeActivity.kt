package com.microsol.myappzegel.presentation.mvvm.home

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.microsol.myappzegel.data.remote.RetrofitClient
import com.microsol.myappzegel.data.repository.FoodRepositoryImpl
import com.microsol.myappzegel.data.repository.MovieRepositoryImpl
import com.microsol.myappzegel.databinding.ActivityMainBinding
import com.microsol.myappzegel.presentation.common.adapter.FoodAdapter
import com.microsol.myappzegel.presentation.common.adapter.MovieAdapter
import com.microsol.myappzegel.presentation.mvvm.fooddetail.FoodDetailActivity
import com.microsol.myappzegel.presentation.mvvm.moviedetail.MovieDetailActivity

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val movieAdapter = MovieAdapter { movie ->
        val intent = Intent(this, MovieDetailActivity::class.java)
        intent.putExtra(MovieDetailActivity.EXTRA_MOVIE_ID, movie.id)
        startActivity(intent)
    }

    private val foodAdapter = FoodAdapter { food ->
        val intent = Intent(this, FoodDetailActivity::class.java)
        intent.putExtra(FoodDetailActivity.EXTRA_FOOD_ID, food.id)
        startActivity(intent)
    }

    private lateinit var viewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerViews()

        // Wire dependencies manually — no DI framework at this stage
        val movieRepository = MovieRepositoryImpl(RetrofitClient.tmdbApi)
        val foodRepository = FoodRepositoryImpl(RetrofitClient.mealDbApi)
        val factory = HomeViewModelFactory(movieRepository, foodRepository)
        viewModel = ViewModelProvider(this, factory)[HomeViewModel::class.java]

        observeViewModel()
    }

    private fun setupRecyclerViews() {
        binding.rvMovies.layoutManager = LinearLayoutManager(
            this, LinearLayoutManager.HORIZONTAL, false
        )
        binding.rvMovies.adapter = movieAdapter

        binding.rvFoods.layoutManager = LinearLayoutManager(this)
        binding.rvFoods.adapter = foodAdapter
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
