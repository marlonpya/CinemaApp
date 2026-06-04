package com.microsol.myappzegel.presentation.mvvm.fooddetail

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import coil.load
import com.microsol.myappzegel.R
import com.microsol.myappzegel.data.remote.RetrofitClient
import com.microsol.myappzegel.data.repository.FoodRepositoryImpl
import com.microsol.myappzegel.databinding.ActivityFoodDetailBinding

class FoodDetailActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_FOOD_ID = "extra_food_id"
    }

    private lateinit var binding: ActivityFoodDetailBinding
    private lateinit var viewModel: FoodDetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFoodDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener { finish() }
        binding.btnAddToOrder.setOnClickListener { viewModel.addToOrder() }

        val foodRepository = FoodRepositoryImpl(RetrofitClient.mealDbApi)
        val factory = FoodDetailViewModelFactory(foodRepository)
        viewModel = ViewModelProvider(this, factory)[FoodDetailViewModel::class.java]

        val foodId = intent.getIntExtra(EXTRA_FOOD_ID, -1)
        if (foodId != -1) {
            viewModel.loadFood(foodId)
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

            binding.tvFoodDetailName.text = food.name
            binding.tvFoodDetailDescription.text = food.description
            binding.tvFoodDetailCategory.text = food.category
            binding.tvFoodDetailPrice.text = food.formattedPrice
            binding.tvFoodDetailTime.text = "⏱ ${food.preparationTime}"
            binding.ratingBarFood.rating = food.rating
            binding.tvFoodDetailRatingValue.text = "%.1f / 5.0".format(food.rating)
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
                            binding.ivFoodDetailImage.setBackgroundColor(
                                getColor(getPlaceholderColor(food.id))
                            )
                        }
                    )
                }
            } else {
                binding.tvFoodDetailImageInitial.text = food.name.first().uppercase()
                binding.ivFoodDetailImage.setBackgroundColor(getColor(getPlaceholderColor(food.id)))
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
            R.color.poster_orange,
            R.color.poster_red,
            R.color.poster_teal,
            R.color.poster_blue,
            R.color.poster_purple,
            R.color.poster_green,
            R.color.poster_red,
            R.color.poster_orange
        )
        return colors[(id - 1).coerceAtLeast(0) % colors.size]
    }
}
