package com.jobsity.tvmaze.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jobsity.tvmaze.R
import com.jobsity.tvmaze.api.Show

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        val adapter = ShowsAdapter()
        recyclerView.adapter = adapter

        val model: MainViewModel by viewModels()
        model.shows.observe(this, { result ->

            val progressBar = findViewById<ProgressBar>(R.id.progress_bar)
            progressBar.visibility = View.GONE

            when (result) {
                is Result.Success -> {
                    recyclerView.visibility = View.VISIBLE
                    adapter.setShows(result.data)
                }
                is Result.Error -> {
                    Toast.makeText(this, "Error Loading Shows", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        })
    }

    class ShowsAdapter : RecyclerView.Adapter<ShowViewHolder>() {

        private val shows: MutableList<Show> = mutableListOf()

        fun setShows(shows: List<Show>) {
            this.shows.addAll(shows)
            notifyItemRangeInserted(0, shows.size - 1)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShowViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val view = inflater.inflate(android.R.layout.simple_list_item_1, parent, false)
            return ShowViewHolder(view)
        }

        override fun onBindViewHolder(holder: ShowViewHolder, position: Int) {
            holder.bind(shows[position])
        }

        override fun getItemCount(): Int {
            return shows.size
        }
    }

    class ShowViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textView: TextView = itemView.findViewById(android.R.id.text1)

        fun bind(show: Show) {
            textView.text = show.name
        }
    }
}