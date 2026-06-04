package com.microsol.myappzegel.presentation.mvp.home

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.microsol.myappzegel.data.remote.RetrofitClient
import com.microsol.myappzegel.data.repository.FoodRepositoryImpl
import com.microsol.myappzegel.data.repository.MovieRepositoryImpl
import com.microsol.myappzegel.databinding.ActivityMainBinding
import com.microsol.myappzegel.domain.model.Food
import com.microsol.myappzegel.domain.model.Movie
import com.microsol.myappzegel.presentation.common.adapter.FoodAdapter
import com.microsol.myappzegel.presentation.common.adapter.MovieAdapter
import com.microsol.myappzegel.presentation.mvp.fooddetail.FoodDetailMvpActivity
import com.microsol.myappzegel.presentation.mvp.moviedetail.MovieDetailMvpActivity

// Activity is the View in MVP — it only renders data and delegates decisions to the Presenter
class HomeMvpActivity : AppCompatActivity(), HomeContract.View {

    private lateinit var binding: ActivityMainBinding

    private val movieAdapter = MovieAdapter { movie ->
        presenter.onMovieClicked(movie.id)
    }

    private val foodAdapter = FoodAdapter { food ->
        presenter.onFoodClicked(food.id)
    }

    private val presenter = HomePresenter(
        MovieRepositoryImpl(RetrofitClient.tmdbApi),
        FoodRepositoryImpl(RetrofitClient.mealDbApi)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerViews()
        presenter.attachView(this)
        presenter.loadData()
    }

    private fun setupRecyclerViews() {
        binding.rvMovies.layoutManager = LinearLayoutManager(
            this, LinearLayoutManager.HORIZONTAL, false
        )
        binding.rvMovies.adapter = movieAdapter

        binding.rvFoods.layoutManager = LinearLayoutManager(this)
        binding.rvFoods.adapter = foodAdapter
    }

    // --- HomeContract.View implementation ---

    override fun showMovies(movies: List<Movie>) {
        movieAdapter.submitList(movies)
    }

    override fun showFoods(foods: List<Food>) {
        foodAdapter.submitList(foods)
    }

    override fun showLoading() {
        binding.progressBar.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        binding.progressBar.visibility = View.GONE
    }

    override fun showError(message: String) {
        binding.tvError.text = message
        binding.tvError.visibility = View.VISIBLE
    }

    override fun navigateToMovieDetail(id: Int) {
        val intent = Intent(this, MovieDetailMvpActivity::class.java)
        intent.putExtra(MovieDetailMvpActivity.EXTRA_MOVIE_ID, id)
        startActivity(intent)
    }

    override fun navigateToFoodDetail(id: Int) {
        val intent = Intent(this, FoodDetailMvpActivity::class.java)
        intent.putExtra(FoodDetailMvpActivity.EXTRA_FOOD_ID, id)
        startActivity(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDestroy()
    }
}
