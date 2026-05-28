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
import com.microsol.myappzegel.domain.model.Movie

// ListAdapter uses DiffUtil to calculate the minimal set of changes needed
// when the list is updated — more efficient than notifyDataSetChanged()
class MovieAdapter(
    private val onMovieClick: (Movie) -> Unit
) : ListAdapter<Movie, MovieAdapter.ViewHolder>(MovieDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_movie, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val ivMoviePoster = view.findViewById<FrameLayout>(R.id.ivMoviePoster)
        private val ivMoviePosterImage = view.findViewById<ImageView>(R.id.ivMoviePosterImage)
        private val tvMoviePosterInitial = view.findViewById<TextView>(R.id.tvMoviePosterInitial)
        private val tvMovieTitle = view.findViewById<TextView>(R.id.tvMovieTitle)
        private val tvMovieGenre = view.findViewById<TextView>(R.id.tvMovieGenre)
        private val tvMovieRating = view.findViewById<TextView>(R.id.tvMovieRating)
        private val tvMovieYear = view.findViewById<TextView>(R.id.tvMovieYear)
        private val tvMovieDuration = view.findViewById<TextView>(R.id.tvMovieDuration)

        fun bind(movie: Movie) {
            tvMovieTitle.text = movie.title
            tvMovieGenre.text = movie.genre
            tvMovieYear.text = movie.year.toString()
            tvMovieRating.text = "★ ${"%.1f".format(movie.rating)}"
            tvMovieDuration.text = movie.duration

            if (movie.imageUrl.isNotBlank()) {
                ivMoviePosterImage.visibility = View.VISIBLE
                tvMoviePosterInitial.visibility = View.GONE
                ivMoviePosterImage.load(movie.imageUrl) {
                    crossfade(true)
                    listener(onError = { _, _ -> showFallback(movie) })
                }
            } else {
                showFallback(movie)
            }

            itemView.setOnClickListener { onMovieClick(movie) }
        }

        private fun showFallback(movie: Movie) {
            ivMoviePosterImage.visibility = View.GONE
            tvMoviePosterInitial.visibility = View.VISIBLE
            tvMoviePosterInitial.text = movie.title.first().uppercase()
            ivMoviePoster.setBackgroundColor(
                itemView.context.getColor(getPlaceholderColor(movie.id))
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
