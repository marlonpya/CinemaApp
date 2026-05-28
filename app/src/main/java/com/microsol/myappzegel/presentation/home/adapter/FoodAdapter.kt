package com.microsol.myappzegel.presentation.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.microsol.myappzegel.R
import com.microsol.myappzegel.domain.model.Food

// ListAdapter uses DiffUtil to update only the items that actually changed
class FoodAdapter(
    private val onFoodClick: (Food) -> Unit
) : ListAdapter<Food, FoodAdapter.ViewHolder>(FoodDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_food, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val ivFoodImage = view.findViewById<FrameLayout>(R.id.ivFoodImage)
        private val ivFoodImageView = view.findViewById<ImageView>(R.id.ivFoodImageView)
        private val tvFoodImageInitial = view.findViewById<TextView>(R.id.tvFoodImageInitial)
        private val tvFoodName = view.findViewById<TextView>(R.id.tvFoodName)
        private val tvFoodCategory = view.findViewById<TextView>(R.id.tvFoodCategory)
        private val tvFoodRating = view.findViewById<TextView>(R.id.tvFoodRating)
        private val tvFoodTime = view.findViewById<TextView>(R.id.tvFoodTime)
        private val tvFoodPrice = view.findViewById<TextView>(R.id.tvFoodPrice)
        private val tvFoodAvailability = view.findViewById<TextView>(R.id.tvFoodAvailability)

        fun bind(food: Food) {
            tvFoodName.text = food.name
            tvFoodCategory.text = food.category
            tvFoodPrice.text = food.formattedPrice
            tvFoodRating.text = "★ ${"%.1f".format(food.rating)}"
            tvFoodTime.text = food.preparationTime
            tvFoodAvailability.text = food.availabilityLabel

            if (food.imageUrl.isNotBlank()) {
                ivFoodImageView.visibility = View.VISIBLE
                tvFoodImageInitial.visibility = View.GONE
                ivFoodImageView.load(food.imageUrl) {
                    crossfade(true)
                    listener(onError = { _, _ -> showFallback(food) })
                }
            } else {
                showFallback(food)
            }

            itemView.setOnClickListener { onFoodClick(food) }
        }

        private fun showFallback(food: Food) {
            ivFoodImageView.visibility = View.GONE
            tvFoodImageInitial.visibility = View.VISIBLE
            tvFoodImageInitial.text = food.name.first().uppercase()
            ivFoodImage.setBackgroundColor(
                itemView.context.getColor(getPlaceholderColor(food.id))
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
