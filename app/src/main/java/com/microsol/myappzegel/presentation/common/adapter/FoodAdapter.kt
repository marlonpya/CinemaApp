package com.microsol.myappzegel.presentation.common.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.microsol.myappzegel.R
import com.microsol.myappzegel.databinding.ItemFoodBinding
import com.microsol.myappzegel.domain.model.Food

// ListAdapter uses DiffUtil to update only the items that actually changed
class FoodAdapter(
    private val onFoodClick: (Food) -> Unit
) : ListAdapter<Food, FoodAdapter.ViewHolder>(FoodDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemFoodBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(
        private val binding: ItemFoodBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(food: Food) {
            binding.tvFoodName.text = food.name
            binding.tvFoodCategory.text = food.category
            binding.tvFoodPrice.text = food.formattedPrice
            binding.tvFoodRating.text = "★ ${"%.1f".format(food.rating)}"
            binding.tvFoodTime.text = food.preparationTime
            binding.tvFoodAvailability.text = food.availabilityLabel

            if (food.imageUrl.isNotBlank()) {
                binding.ivFoodImageView.visibility = View.VISIBLE
                binding.tvFoodImageInitial.visibility = View.GONE
                binding.ivFoodImageView.load(food.imageUrl) {
                    crossfade(true)
                    listener(onError = { _, _ -> showFallback(food) })
                }
            } else {
                showFallback(food)
            }

            binding.root.setOnClickListener { onFoodClick(food) }
        }

        private fun showFallback(food: Food) {
            binding.ivFoodImageView.visibility = View.GONE
            binding.tvFoodImageInitial.visibility = View.VISIBLE
            binding.tvFoodImageInitial.text = food.name.first().uppercase()
            binding.ivFoodImage.setBackgroundColor(
                binding.root.context.getColor(getPlaceholderColor(food.id))
            )
        }
    }

    // DiffUtil.ItemCallback tells ListAdapter how to compare items
    class FoodDiffCallback : DiffUtil.ItemCallback<Food>() {
        override fun areItemsTheSame(oldItem: Food, newItem: Food) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Food, newItem: Food) = oldItem == newItem
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
