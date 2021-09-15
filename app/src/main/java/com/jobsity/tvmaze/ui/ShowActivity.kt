package com.jobsity.tvmaze.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.viewModels
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jobsity.tvmaze.R
import com.jobsity.tvmaze.databinding.ActivityShowBinding
import com.jobsity.tvmaze.model.Episode
import com.squareup.picasso.Picasso

class ShowActivity : AppCompatActivity() {

    companion object {
        fun start(context: Context, showId: Long) {
            val intent = Intent(context, ShowActivity::class.java)
            intent.putExtra("show_id", showId)
            context.startActivity(intent)
        }
    }

    private val viewModel: ShowViewModel by viewModels()
    private lateinit var binding: ActivityShowBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityShowBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val showId = intent.getLongExtra("show_id", -1L)

        viewModel.getShow(showId).observe(this, { show ->
            binding.toolbarLayout.title = show.name
            binding.summary.text = Html.fromHtml(show.summary)
            binding.genres.text = show.genres.joinToString(separator = ", ") { it }
            binding.days.text = show.schedule.days.joinToString(separator = ", ") { it }
            binding.time.text = show.schedule.time
            Picasso.get().load(show.image.original)
                .fit().centerCrop()
                .into(binding.imageView)
        })

        viewModel.getEpisodes(showId).observe(this, { episodes ->
            binding.recyclerView.adapter = EpisodesAdapter(episodes)
            binding.recyclerView.layoutManager =
                LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        })

        binding.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    inner class EpisodesAdapter(private val episodes: List<Episode>) :
        RecyclerView.Adapter<EpisodeViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EpisodeViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val view = inflater.inflate(R.layout.episode_item, parent, false)
            return EpisodeViewHolder(view)
        }

        override fun onBindViewHolder(holder: EpisodeViewHolder, position: Int) {
            val episode = episodes[position]
            val showSeason = position == 0 || episodes[position - 1].season != episode.season
            holder.bind(episode, showSeason)
        }

        override fun getItemCount(): Int {
            return episodes.size
        }
    }

    inner class EpisodeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val season: TextView = itemView.findViewById(R.id.season_view)
        private val name: TextView = itemView.findViewById(R.id.name_view)
        private var episode: Episode? = null

        init {
            name.setOnClickListener {
            }
        }

        fun bind(episode: Episode, showSeason: Boolean) {
            this.episode = episode
            this.name.text = episode.name
            if (showSeason) {
                this.season.visibility = View.VISIBLE
                this.season.text = "SEASON ${episode.season}"
            } else {
                this.season.visibility = View.GONE
            }
        }
    }

}