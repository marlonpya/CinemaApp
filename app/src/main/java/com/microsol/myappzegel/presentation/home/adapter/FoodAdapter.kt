package com.microsol.myappzegel.presentation.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.microsol.myappzegel.R
import com.microsol.myappzegel.data.model.Food
import com.microsol.myappzegel.databinding.ItemFoodBinding

class FoodAdapter(
    private val onFoodClick: (Food) -> Unit
) : RecyclerView.Adapter<FoodAdapter.ViewHolder>() {

    private var foods: List<Food> = emptyList()

    fun submitList(newFoods: List<Food>) {
        foods = newFoods
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemFoodBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(foods[position])
        holder.itemView.setOnClickListener {
            onFoodClick(foods[position])
        }
    }

    override fun getItemCount(): Int = foods.size

    inner class ViewHolder(private val binding: ItemFoodBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(food: Food) {
            binding.tvFoodName.text         = food.name
            binding.tvFoodCategory.text     = food.category
            binding.tvFoodPrice.text        = food.formattedPrice
            binding.tvFoodRating.text       = "★ ${"%.1f".format(food.rating)}"
            binding.tvFoodTime.text         = food.preparationTime
            binding.tvFoodAvailability.text = food.availabilityLabel

            if (food.imageUrl.isNotBlank()) {
                binding.ivFoodImageView.visibility    = View.VISIBLE
                binding.tvFoodImageInitial.visibility = View.GONE
                binding.ivFoodImageView.load(food.imageUrl) {
                    crossfade(true)
                    listener(
                        onError = { _, _ -> showFallback(food) }
                    )
                }
            } else {
                showFallback(food)
            }
        }

        private fun showFallback(food: Food) {
            binding.ivFoodImageView.visibility    = View.GONE
            binding.tvFoodImageInitial.visibility = View.VISIBLE
            binding.tvFoodImageInitial.text       = food.name.first().uppercase()
            val colorRes = getPlaceholderColor(food.id.value)
            binding.ivFoodImage.setBackgroundColor(binding.root.context.getColor(colorRes))
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
