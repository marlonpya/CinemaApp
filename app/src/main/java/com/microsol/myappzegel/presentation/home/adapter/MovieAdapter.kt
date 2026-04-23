package com.microsol.myappzegel.presentation.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.microsol.myappzegel.R
import com.microsol.myappzegel.data.model.Movie
import com.microsol.myappzegel.databinding.ItemMovieBinding

// ─────────────────────────────────────────────────────────────────────────────
// KOTLIN CONCEPT: Class with a lambda constructor parameter
//
// `onMovieClick: (Movie) -> Unit` is a function type (lambda). The Activity
// passes a lambda when creating the adapter. When the user taps a card, we
// invoke that lambda with the clicked Movie. This keeps the adapter decoupled
// from the Activity: the adapter doesn't know how clicks are handled.
//
// KOTLIN CONCEPT: Inheritance
// MovieAdapter extends RecyclerView.Adapter — we override three required
// functions. The generic type parameter `MovieAdapter.ViewHolder` ties the
// adapter to our custom ViewHolder.
// ─────────────────────────────────────────────────────────────────────────────
class MovieAdapter(
    private val onMovieClick: (Movie) -> Unit  // lambda parameter
) : RecyclerView.Adapter<MovieAdapter.ViewHolder>() {

    // KOTLIN CONCEPT: var with a setter that notifies the RecyclerView
    // When `movies` is set from outside, we notify the adapter so it redraws.
    private var movies: List<Movie> = emptyList()

    fun submitList(newMovies: List<Movie>) {
        movies = newMovies
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // ViewBinding inflates our item_movie.xml layout
        val binding = ItemMovieBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // KOTLIN CONCEPT: Destructuring
        // We destructure the Movie data class into individual variables.
        // The component functions (component1, component2…) are auto-generated
        // by the `data class` keyword in the order of the primary constructor.
        val (id, title, description, genre, year, rating) = movies[position]

        holder.bind(movies[position])

        // KOTLIN CONCEPT: Lambda invocation
        // The full movie object is also passed so the click handler in the
        // Activity has access to all fields (e.g. id for navigation).
        holder.itemView.setOnClickListener {
            onMovieClick(movies[position])
        }
    }

    override fun getItemCount(): Int = movies.size

    // ─────────────────────────────────────────────────────────────────────────
    // KOTLIN CONCEPT: Nested class (inner ViewHolder)
    //
    // ViewHolder is a standard RecyclerView pattern that caches view references
    // so we don't call findViewById() on every scroll event.
    // ─────────────────────────────────────────────────────────────────────────
    inner class ViewHolder(private val binding: ItemMovieBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(movie: Movie) {
            binding.tvMovieTitle.text = movie.title
            binding.tvMovieGenre.text = movie.genre
            binding.tvMovieYear.text = movie.year.toString()
            binding.tvMovieRating.text = "★ ${movie.rating}"
            binding.tvMovieDuration.text = movie.duration

            // Set a color placeholder based on movie id (no network images needed)
            val colorRes = getPlaceholderColor(movie.id.value)
            binding.ivMoviePoster.setBackgroundColor(
                binding.root.context.getColor(colorRes)
            )
            // Display movie initial as a text placeholder
            binding.tvMoviePosterInitial.text = movie.title.first().uppercase()
        }
    }

    // Returns a cycling color from our palette for placeholder backgrounds
    private fun getPlaceholderColor(id: Int): Int {
        val colors = listOf(
            R.color.poster_red,
            R.color.poster_blue,
            R.color.poster_green,
            R.color.poster_purple,
            R.color.poster_orange,
            R.color.poster_teal
        )
        return colors[(id - 1) % colors.size]
    }
}
