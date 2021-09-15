package com.jobsity.tvmaze.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jobsity.tvmaze.R
import com.jobsity.tvmaze.api.Show
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        val adapter = ShowsAdapter()
        recyclerView.adapter = adapter

        val viewModel: MainViewModel by viewModels()
        val progressBar = findViewById<ProgressBar>(R.id.progress_bar)
        lifecycleScope.launch {
            viewModel.pagedShows.observe(this@MainActivity, {
                progressBar.visibility = View.GONE
                recyclerView.visibility = View.VISIBLE
                adapter.submitData(lifecycle, it)
            })
        }
    }

    class ShowsAdapter : PagingDataAdapter<Show, ShowViewHolder>(ShowComparator) {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShowViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val view = inflater.inflate(android.R.layout.simple_list_item_2, parent, false)
            return ShowViewHolder(view)
        }

        override fun onBindViewHolder(holder: ShowViewHolder, position: Int) {
            holder.bind(getItem(position))
        }
    }

    class ShowViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val text1: TextView = itemView.findViewById(android.R.id.text1)
        private val text2: TextView = itemView.findViewById(android.R.id.text2)

        fun bind(show: Show?) {
            text1.text = show?.name
            text2.text = show?.id.toString()
        }
    }

    object ShowComparator : DiffUtil.ItemCallback<Show>() {
        override fun areItemsTheSame(oldItem: Show, newItem: Show): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Show, newItem: Show): Boolean {
            return oldItem == newItem
        }
    }
}