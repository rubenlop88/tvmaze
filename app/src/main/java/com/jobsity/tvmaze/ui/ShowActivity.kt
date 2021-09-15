package com.jobsity.tvmaze.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jobsity.tvmaze.R
import com.jobsity.tvmaze.databinding.ActivityShowBinding
import com.jobsity.tvmaze.model.Episode
import com.jobsity.tvmaze.model.Show
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
            val view = inflater.inflate(android.R.layout.simple_list_item_1, parent, false)
            return EpisodeViewHolder(view)
        }

        override fun onBindViewHolder(holder: EpisodeViewHolder, position: Int) {
            holder.bind(episodes[position])
        }

        override fun getItemCount(): Int {
            return episodes.size
        }
    }

    inner class EpisodeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val text: TextView = itemView.findViewById(android.R.id.text1)
        private var episode: Episode? = null

        init {
            itemView.setOnClickListener {
            }
        }

        fun bind(episode: Episode) {
            this.episode = episode
            this.text.text = episode.name
        }
    }

}