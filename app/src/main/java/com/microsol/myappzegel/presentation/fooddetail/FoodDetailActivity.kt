package com.microsol.myappzegel.presentation.fooddetail

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.lifecycle.ViewModelProvider
import coil.load
import com.microsol.myappzegel.R
import com.microsol.myappzegel.data.remote.RetrofitClient
import com.microsol.myappzegel.data.repository.FoodRepositoryImpl

class FoodDetailActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_FOOD_ID = "extra_food_id"
    }

    private lateinit var ivFoodDetailImage: FrameLayout
    private lateinit var ivFoodDetailImageView: ImageView
    private lateinit var tvFoodDetailImageInitial: TextView
    private lateinit var btnBack: Button
    private lateinit var cardFoodContent: CardView
    private lateinit var tvFoodDetailName: TextView
    private lateinit var tvFoodDetailCategory: TextView
    private lateinit var tvFoodDetailAvailability: TextView
    private lateinit var tvFoodDetailPrice: TextView
    private lateinit var tvFoodDetailTime: TextView
    private lateinit var ratingBarFood: RatingBar
    private lateinit var tvFoodDetailRatingValue: TextView
    private lateinit var tvFoodDetailDescription: TextView
    private lateinit var btnAddToOrder: Button

    private lateinit var viewModel: FoodDetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_food_detail)

        ivFoodDetailImage = findViewById(R.id.ivFoodDetailImage)
        ivFoodDetailImageView = findViewById(R.id.ivFoodDetailImageView)
        tvFoodDetailImageInitial = findViewById(R.id.tvFoodDetailImageInitial)
        btnBack = findViewById(R.id.btnBack)
        cardFoodContent = findViewById(R.id.cardFoodContent)
        tvFoodDetailName = findViewById(R.id.tvFoodDetailName)
        tvFoodDetailCategory = findViewById(R.id.tvFoodDetailCategory)
        tvFoodDetailAvailability = findViewById(R.id.tvFoodDetailAvailability)
        tvFoodDetailPrice = findViewById(R.id.tvFoodDetailPrice)
        tvFoodDetailTime = findViewById(R.id.tvFoodDetailTime)
        ratingBarFood = findViewById(R.id.ratingBarFood)
        tvFoodDetailRatingValue = findViewById(R.id.tvFoodDetailRatingValue)
        tvFoodDetailDescription = findViewById(R.id.tvFoodDetailDescription)
        btnAddToOrder = findViewById(R.id.btnAddToOrder)

        btnBack.setOnClickListener { finish() }
        btnAddToOrder.setOnClickListener { viewModel.addToOrder() }

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
            cardFoodContent.visibility = if (loading) View.INVISIBLE else View.VISIBLE
        }

        viewModel.error.observe(this) { errorMsg ->
            if (!errorMsg.isNullOrBlank()) {
                Toast.makeText(this, errorMsg, Toast.LENGTH_LONG).show()
            }
        }

        viewModel.food.observe(this) { food ->
            food ?: return@observe

            tvFoodDetailName.text = food.name
            tvFoodDetailDescription.text = food.description
            tvFoodDetailCategory.text = food.category
            tvFoodDetailPrice.text = food.formattedPrice
            tvFoodDetailTime.text = "⏱ ${food.preparationTime}"
            ratingBarFood.rating = food.rating
            tvFoodDetailRatingValue.text = "%.1f / 5.0".format(food.rating)
            tvFoodDetailAvailability.text = food.availabilityLabel

            if (food.imageUrl.isNotBlank()) {
                ivFoodDetailImageView.visibility = View.VISIBLE
                tvFoodDetailImageInitial.visibility = View.GONE
                ivFoodDetailImageView.load(food.imageUrl) {
                    crossfade(true)
                    listener(
                        onError = { _, _ ->
                            ivFoodDetailImageView.visibility = View.GONE
                            tvFoodDetailImageInitial.text = food.name.first().uppercase()
                            tvFoodDetailImageInitial.visibility = View.VISIBLE
                            ivFoodDetailImage.setBackgroundColor(getColor(getPlaceholderColor(food.id)))
                        }
                    )
                }
            } else {
                tvFoodDetailImageInitial.text = food.name.first().uppercase()
                ivFoodDetailImage.setBackgroundColor(getColor(getPlaceholderColor(food.id)))
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
