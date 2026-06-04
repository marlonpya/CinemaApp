package com.microsol.myappzegel.presentation.common.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.microsol.myappzegel.R
import com.microsol.myappzegel.databinding.ItemMovieBinding
import com.microsol.myappzegel.domain.model.Movie

// ListAdapter uses DiffUtil to calculate the minimal set of changes needed
// when the list is updated — more efficient than notifyDataSetChanged()
class MovieAdapter(
    private val onMovieClick: (Movie) -> Unit
) : ListAdapter<Movie, MovieAdapter.ViewHolder>(MovieDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(
        private val binding: ItemMovieBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(movie: Movie) {
            binding.tvMovieTitle.text = movie.title
            binding.tvMovieGenre.text = movie.genre
            binding.tvMovieYear.text = movie.year.toString()
            binding.tvMovieRating.text = "★ ${"%.1f".format(movie.rating)}"
            binding.tvMovieDuration.text = movie.duration

            if (movie.imageUrl.isNotBlank()) {
                binding.ivMoviePosterImage.visibility = View.VISIBLE
                binding.tvMoviePosterInitial.visibility = View.GONE
                binding.ivMoviePosterImage.load(movie.imageUrl) {
                    crossfade(true)
                    listener(onError = { _, _ -> showFallback(movie) })
                }
            } else {
                showFallback(movie)
            }

            binding.root.setOnClickListener { onMovieClick(movie) }
        }

        private fun showFallback(movie: Movie) {
            binding.ivMoviePosterImage.visibility = View.GONE
            binding.tvMoviePosterInitial.visibility = View.VISIBLE
            binding.tvMoviePosterInitial.text = movie.title.first().uppercase()
            binding.ivMoviePoster.setBackgroundColor(
                binding.root.context.getColor(getPlaceholderColor(movie.id))
            )
        }
    }

    // DiffUtil.ItemCallback tells ListAdapter how to compare items
    class MovieDiffCallback : DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Movie, newItem: Movie) = oldItem == newItem
    }

    private fun getPlaceholderColor(id: Int): Int {
        val colors = listOf(
            R.color.poster_red,
            R.color.poster_blue,
            R.color.poster_green,
            R.color.poster_purple,
            R.color.poster_orange,
            R.color.poster_teal
        )
        return colors[(id - 1).coerceAtLeast(0) % colors.size]
    }
}
