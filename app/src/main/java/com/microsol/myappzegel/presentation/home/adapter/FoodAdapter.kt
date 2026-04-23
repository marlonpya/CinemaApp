package com.microsol.myappzegel.presentation.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.microsol.myappzegel.R
import com.microsol.myappzegel.data.model.Food
import com.microsol.myappzegel.databinding.ItemFoodBinding

// ─────────────────────────────────────────────────────────────────────────────
// FoodAdapter follows the exact same MVVM + lambda pattern as MovieAdapter.
// Reading both adapters side-by-side shows students the consistent structure
// that Clean Architecture enables.
// ─────────────────────────────────────────────────────────────────────────────
class FoodAdapter(
    private val onFoodClick: (Food) -> Unit   // KOTLIN CONCEPT: lambda parameter
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
            // KOTLIN CONCEPT: Lambda invocation — passes the clicked Food object
            onFoodClick(foods[position])
        }
    }

    override fun getItemCount(): Int = foods.size

    inner class ViewHolder(private val binding: ItemFoodBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(food: Food) {
            // KOTLIN CONCEPT: Destructuring in function body
            // We can destructure a data class anywhere we have an instance.
            val (id, name, description, category, price) = food

            binding.tvFoodName.text = name
            binding.tvFoodCategory.text = category
            // `formattedPrice` is our custom getter — called as a property
            binding.tvFoodPrice.text = food.formattedPrice
            binding.tvFoodRating.text = "★ ${food.rating}"
            binding.tvFoodTime.text = food.preparationTime
            binding.tvFoodAvailability.text = food.availabilityLabel

            val colorRes = getPlaceholderColor(id.value)
            binding.ivFoodImage.setBackgroundColor(
                binding.root.context.getColor(colorRes)
            )
            binding.tvFoodImageInitial.text = name.first().uppercase()
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
        return colors[(id - 1) % colors.size]
    }
}
