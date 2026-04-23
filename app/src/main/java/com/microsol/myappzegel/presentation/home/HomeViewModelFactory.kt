package com.microsol.myappzegel.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.microsol.myappzegel.domain.usecase.GetFoodsUseCase
import com.microsol.myappzegel.domain.usecase.GetMoviesUseCase

// ─────────────────────────────────────────────────────────────────────────────
// ViewModelFactory
//
// Android's ViewModelProvider needs a factory to create ViewModels that have
// constructor parameters. Without a factory, only no-arg ViewModels can be
// created automatically. This factory bridges that gap.
//
// KOTLIN CONCEPT: Inheritance
// ViewModelProvider.Factory is an interface we must implement.
// `@Suppress` silences the unchecked cast warning — it is safe here because
// we check the class type before casting.
// ─────────────────────────────────────────────────────────────────────────────
class HomeViewModelFactory(
    private val getMoviesUseCase: GetMoviesUseCase,
    private val getFoodsUseCase: GetFoodsUseCase
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(getMoviesUseCase, getFoodsUseCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel: ${modelClass.name}")
    }
}
