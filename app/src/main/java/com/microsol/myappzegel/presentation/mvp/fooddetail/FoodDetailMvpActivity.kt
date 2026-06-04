package com.microsol.myappzegel.presentation.mvp.fooddetail

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import coil.load
import com.microsol.myappzegel.R
import com.microsol.myappzegel.data.remote.RetrofitClient
import com.microsol.myappzegel.data.repository.FoodRepositoryImpl
import com.microsol.myappzegel.databinding.ActivityFoodDetailBinding
import com.microsol.myappzegel.domain.model.Food

class FoodDetailMvpActivity : AppCompatActivity(), FoodDetailContract.View {

    companion object {
        const val EXTRA_FOOD_ID = "extra_food_id"
    }

    private lateinit var binding: ActivityFoodDetailBinding

    private val presenter = FoodDetailPresenter(
        FoodRepositoryImpl(RetrofitClient.mealDbApi)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFoodDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener { presenter.onBackClicked() }
        binding.btnAddToOrder.setOnClickListener { presenter.onAddToOrderClicked() }

        presenter.attachView(this)

        val foodId = intent.getIntExtra(EXTRA_FOOD_ID, -1)
        if (foodId != -1) {
            presenter.loadFood(foodId)
        }
    }

    // --- FoodDetailContract.View implementation ---

    override fun showFood(food: Food) {
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

    override fun showLoading() {
        binding.cardFoodContent.visibility = View.INVISIBLE
    }

    override fun hideLoading() {
        binding.cardFoodContent.visibility = View.VISIBLE
    }

    override fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    override fun showOrderConfirmation() {
        Toast.makeText(this, "¡Agregado al pedido! 🛒", Toast.LENGTH_SHORT).show()
    }

    override fun close() {
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDestroy()
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
