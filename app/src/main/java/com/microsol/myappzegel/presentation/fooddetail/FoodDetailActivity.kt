package com.microsol.myappzegel.presentation.fooddetail

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.microsol.myappzegel.data.repository.FoodRepositoryImpl
import com.microsol.myappzegel.databinding.ActivityFoodDetailBinding
import com.microsol.myappzegel.domain.usecase.GetFoodsUseCase

// ─────────────────────────────────────────────────────────────────────────────
// Food Detail Screen
//
// Shows full details for a selected food item and allows the user to "add to
// order". The button action is handled by the ViewModel (business decision)
// while the UI feedback (Toast) is handled by the Activity (UI concern).
// This separation is a key benefit of MVVM.
// ─────────────────────────────────────────────────────────────────────────────
class FoodDetailActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_FOOD_ID = "extra_food_id"
    }

    private lateinit var binding: ActivityFoodDetailBinding

    private val viewModel: FoodDetailViewModel by viewModels {
        FoodDetailViewModelFactory(
            GetFoodsUseCase(FoodRepositoryImpl())
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFoodDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener { finish() }

        val foodId = intent.getIntExtra(EXTRA_FOOD_ID, -1)
        if (foodId != -1) {
            viewModel.loadFood(foodId)
        }

        // KOTLIN CONCEPT: Lambda in setOnClickListener
        binding.btnAddToOrder.setOnClickListener {
            viewModel.addToOrder()
        }

        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.food.observe(this) { food ->
            food ?: return@observe

            binding.tvFoodDetailName.text = food.name
            binding.tvFoodDetailDescription.text = food.description
            binding.tvFoodDetailCategory.text = food.category
            // KOTLIN CONCEPT: Custom getter called as a property
            binding.tvFoodDetailPrice.text = food.formattedPrice
            binding.tvFoodDetailTime.text = "⏱ ${food.preparationTime}"
            binding.ratingBarFood.rating = food.rating
            binding.tvFoodDetailRatingValue.text = "%.1f / 5.0".format(food.rating)
            binding.tvFoodDetailAvailability.text = food.availabilityLabel

            val colorRes = getPlaceholderColor(food.id.value)
            binding.ivFoodDetailImage.setBackgroundColor(getColor(colorRes))
            binding.tvFoodDetailImageInitial.text = food.name.first().uppercase()
        }

        viewModel.orderAdded.observe(this) { added ->
            if (added) {
                Toast.makeText(
                    this,
                    "¡Agregado al pedido! 🛒",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun getPlaceholderColor(id: Int): Int {
        val colors = listOf(
            com.microsol.myappzegel.R.color.poster_orange,
            com.microsol.myappzegel.R.color.poster_red,
            com.microsol.myappzegel.R.color.poster_teal,
            com.microsol.myappzegel.R.color.poster_blue,
            com.microsol.myappzegel.R.color.poster_purple,
            com.microsol.myappzegel.R.color.poster_green,
            com.microsol.myappzegel.R.color.poster_red,
            com.microsol.myappzegel.R.color.poster_orange
        )
        return colors[(id - 1) % colors.size]
    }
}
