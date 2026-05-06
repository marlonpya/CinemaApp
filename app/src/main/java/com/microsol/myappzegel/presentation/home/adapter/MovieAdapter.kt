package com.microsol.myappzegel.presentation.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.microsol.myappzegel.R
import com.microsol.myappzegel.data.model.Movie
import com.microsol.myappzegel.databinding.ItemMovieBinding

class MovieAdapter(
    private val onMovieClick: (Movie) -> Unit
) : RecyclerView.Adapter<MovieAdapter.ViewHolder>() {

    private var movies: List<Movie> = emptyList()

    fun submitList(newMovies: List<Movie>) {
        movies = newMovies
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemMovieBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(movies[position])
        holder.itemView.setOnClickListener {
            onMovieClick(movies[position])
        }
    }

    override fun getItemCount(): Int = movies.size

    inner class ViewHolder(private val binding: ItemMovieBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(movie: Movie) {
            binding.tvMovieTitle.text    = movie.title
            binding.tvMovieGenre.text    = movie.genre
            binding.tvMovieYear.text     = movie.year.toString()
            binding.tvMovieRating.text   = "★ ${"%.1f".format(movie.rating)}"
            binding.tvMovieDuration.text = movie.duration

            if (movie.imageUrl.isNotBlank()) {
                binding.ivMoviePosterImage.visibility    = View.VISIBLE
                binding.tvMoviePosterInitial.visibility  = View.GONE
                binding.ivMoviePosterImage.load(movie.imageUrl) {
                    crossfade(true)
                    listener(
                        onError = { _, _ -> showFallback(movie) }
                    )
                }
            } else {
                showFallback(movie)
            }
        }

        private fun showFallback(movie: Movie) {
            binding.ivMoviePosterImage.visibility   = View.GONE
            binding.tvMoviePosterInitial.visibility = View.VISIBLE
            binding.tvMoviePosterInitial.text       = movie.title.first().uppercase()
            val colorRes = getPlaceholderColor(movie.id.value)
            binding.ivMoviePoster.setBackgroundColor(binding.root.context.getColor(colorRes))
        }
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
