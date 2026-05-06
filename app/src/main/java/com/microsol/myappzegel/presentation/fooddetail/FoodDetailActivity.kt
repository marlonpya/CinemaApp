package com.microsol.myappzegel.presentation.fooddetail

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import coil.load
import com.microsol.myappzegel.data.remote.RetrofitClient
import com.microsol.myappzegel.data.repository.FoodRepositoryImpl
import com.microsol.myappzegel.databinding.ActivityFoodDetailBinding
import com.microsol.myappzegel.domain.usecase.GetFoodsUseCase

class FoodDetailActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_FOOD_ID = "extra_food_id"
    }

    private lateinit var binding: ActivityFoodDetailBinding

    private val viewModel: FoodDetailViewModel by viewModels {
        FoodDetailViewModelFactory(
            GetFoodsUseCase(FoodRepositoryImpl(RetrofitClient.mealDbApi))
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

        binding.btnAddToOrder.setOnClickListener {
            viewModel.addToOrder()
        }

        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.isLoading.observe(this) { loading ->
            binding.cardFoodContent.visibility = if (loading) View.INVISIBLE else View.VISIBLE
        }

        viewModel.error.observe(this) { errorMsg ->
            if (!errorMsg.isNullOrBlank()) {
                Toast.makeText(this, errorMsg, Toast.LENGTH_LONG).show()
            }
        }

        viewModel.food.observe(this) { food ->
            food ?: return@observe

            binding.tvFoodDetailName.text         = food.name
            binding.tvFoodDetailDescription.text  = food.description
            binding.tvFoodDetailCategory.text     = food.category
            binding.tvFoodDetailPrice.text        = food.formattedPrice
            binding.tvFoodDetailTime.text         = "⏱ ${food.preparationTime}"
            binding.ratingBarFood.rating          = food.rating
            binding.tvFoodDetailRatingValue.text  = "%.1f / 5.0".format(food.rating)
            binding.tvFoodDetailAvailability.text = food.availabilityLabel

            if (food.imageUrl.isNotBlank()) {
                binding.ivFoodDetailImageView.visibility = View.VISIBLE
                binding.tvFoodDetailImageInitial.visibility = View.GONE
                binding.ivFoodDetailImageView.load(food.imageUrl) {
                    crossfade(true)
                    listener(
                        onError = { _, _ ->
                            binding.ivFoodDetailImageView.visibility = View.GONE
                            binding.tvFoodDetailImageInitial.text = food.name.first().uppercase()
                            binding.tvFoodDetailImageInitial.visibility = View.VISIBLE
                            binding.ivFoodDetailImage.setBackgroundColor(getColor(getPlaceholderColor(food.id.value)))
                        }
                    )
                }
            } else {
                binding.tvFoodDetailImageInitial.text = food.name.first().uppercase()
                binding.ivFoodDetailImage.setBackgroundColor(getColor(getPlaceholderColor(food.id.value)))
            }
        }

        viewModel.orderAdded.observe(this) { added ->
            if (added) {
                Toast.makeText(this, "¡Agregado al pedido! 🛒", Toast.LENGTH_SHORT).show()
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
        return colors[(id - 1).coerceAtLeast(0) % colors.size]
    }
}
