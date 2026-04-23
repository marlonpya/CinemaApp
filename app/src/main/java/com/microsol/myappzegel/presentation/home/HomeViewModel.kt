package com.microsol.myappzegel.presentation.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.microsol.myappzegel.data.model.Food
import com.microsol.myappzegel.data.model.Movie
import com.microsol.myappzegel.domain.usecase.GetFoodsUseCase
import com.microsol.myappzegel.domain.usecase.GetMoviesUseCase

// ─────────────────────────────────────────────────────────────────────────────
// WHY MVVM?
//
// Model-View-ViewModel separates concerns:
//   • View  (Activity/Fragment) only observes data and reacts to it
//   • ViewModel  holds UI state, survives configuration changes (rotation)
//   • Model  (data + domain layers) provides the data
//
// This avoids putting business logic inside Activities, which makes the code
// harder to test and maintain.
//
// KOTLIN CONCEPT: Class with constructor parameters (manual DI)
// The use cases are passed in — this is manual dependency injection.
// A real project might use Hilt or Koin; here we keep it simple.
// ─────────────────────────────────────────────────────────────────────────────
class HomeViewModel(
    private val getMoviesUseCase: GetMoviesUseCase,
    private val getFoodsUseCase: GetFoodsUseCase
) : ViewModel() {

    // ─────────────────────────────────────────────────────────────────────────
    // KOTLIN CONCEPT: val with backing MutableLiveData
    //
    // _movies is private MutableLiveData — only the ViewModel can write to it.
    // movies is the public LiveData that the Activity observes (read-only).
    // This pattern is called "encapsulated LiveData" and prevents external
    // classes from pushing values into the ViewModel's state.
    // ─────────────────────────────────────────────────────────────────────────
    private val _movies = MutableLiveData<List<Movie>>()
    val movies: LiveData<List<Movie>> = _movies

    private val _foods = MutableLiveData<List<Food>>()
    val foods: LiveData<List<Food>> = _foods

    // Called once when the ViewModel is created
    init {
        loadData()
    }

    private fun loadData() {
        // The operator fun invoke() pattern lets us call use cases like functions
        _movies.value = getMoviesUseCase()
        _foods.value = getFoodsUseCase()
    }
}
