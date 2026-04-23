package com.microsol.myappzegel

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
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

// ─────────────────────────────────────────────────────────────────────────────
// MainActivity = Home Screen
//
// MVVM ROLE: This is the VIEW. Its only jobs are:
//   1. Observe LiveData from the ViewModel
//   2. Update the UI when data changes
//   3. Forward user events (clicks) to the ViewModel or navigate
//
// The Activity does NOT fetch data, filter lists, or contain business logic.
// ─────────────────────────────────────────────────────────────────────────────
class MainActivity : AppCompatActivity() {

    // ViewBinding — type-safe reference to every view in activity_main.xml
    private lateinit var binding: ActivityMainBinding

    // ─────────────────────────────────────────────────────────────────────────
    // Manual Dependency Injection (DI)
    //
    // We build the dependency graph here at the entry point (Activity).
    // In production apps, a DI framework like Hilt does this automatically.
    // Doing it manually helps students see exactly which classes depend on which.
    // ─────────────────────────────────────────────────────────────────────────
    private val viewModel: HomeViewModel by viewModels {
        HomeViewModelFactory(
            getMoviesUseCase = GetMoviesUseCase(MovieRepositoryImpl()),
            getFoodsUseCase = GetFoodsUseCase(FoodRepositoryImpl())
        )
    }

    // KOTLIN CONCEPT: Lambda stored as val
    // The adapters receive these lambdas as their click handlers.
    private val movieAdapter = MovieAdapter { movie ->
        // Navigate to Movie Detail, passing the movie id as an Intent extra
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
        // Horizontal RecyclerView for movies
        binding.rvMovies.apply {
            layoutManager = LinearLayoutManager(
                this@MainActivity,
                LinearLayoutManager.HORIZONTAL,
                false
            )
            adapter = movieAdapter
        }

        // Vertical RecyclerView for foods
        binding.rvFoods.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = foodAdapter
        }
    }

    private fun observeViewModel() {
        // KOTLIN CONCEPT: Lambda passed to observe()
        // The lambda `{ movies -> ... }` is called every time LiveData emits a
        // new value. The Activity simply passes data to the adapter.
        viewModel.movies.observe(this) { movies ->
            movieAdapter.submitList(movies)
        }

        viewModel.foods.observe(this) { foods ->
            foodAdapter.submitList(foods)
        }
    }
}
