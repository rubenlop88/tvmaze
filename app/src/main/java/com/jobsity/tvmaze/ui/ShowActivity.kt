package com.jobsity.tvmaze.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.customview.getCustomView
import com.jobsity.tvmaze.R
import com.jobsity.tvmaze.databinding.ShowActivityBinding
import com.jobsity.tvmaze.model.Episode
import com.squareup.picasso.Picasso

class ShowActivity : AppCompatActivity() {

    companion object {
        fun start(context: Context, showId: Long, showName: String) {
            val intent = Intent(context, ShowActivity::class.java)
            intent.putExtra("show_id", showId)
            intent.putExtra("show_name", showName)
            context.startActivity(intent)
        }
    }

    private val viewModel: ShowViewModel by viewModels()
    private lateinit var binding: ShowActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ShowActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val showId = intent.getLongExtra("show_id", -1L)
        val showName = intent.getStringExtra("show_name")
        binding.toolbarLayout.title = showName


        viewModel.getShow(showId).observe(this, { show ->
            binding.summary.text = Html.fromHtml(show.summary)
            binding.genres.text = show.genres.joinToString(separator = ", ") { it }
            binding.days.text = show.schedule.days.joinToString(separator = ", ") { it }
            binding.time.text = show.schedule.time
            Picasso.get().load(show.image.original).fit().centerCrop().into(binding.imageView)
        })

        viewModel.getEpisodes(showId).observe(this, { episodes ->
            binding.recyclerView.adapter = EpisodesAdapter(episodes)
            binding.recyclerView.layoutManager =
                LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        })
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
                val dialog = MaterialDialog(this@ShowActivity)
                    .customView(R.layout.episode_dialog, scrollable = true)
                val customView = dialog.getCustomView()

                val nameView = customView.findViewById<TextView>(R.id.name_view)
                nameView.text = episode?.name

                val seasonView = customView.findViewById<TextView>(R.id.season_view)
                seasonView.text = "Season ${episode?.season} Episode ${episode?.number}"

                val summaryView = customView.findViewById<TextView>(R.id.summary_view)
                summaryView.text = Html.fromHtml(episode?.summary)

                val imageView = customView.findViewById<ImageView>(R.id.image_view)
                episode?.image?.original?.let {
                    Picasso.get().load(it).fit().centerCrop().into(imageView)
                }

                dialog.show()
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