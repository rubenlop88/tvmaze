package com.jobsity.tvmaze.ui

import android.os.Bundle
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.lifecycleScope
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jobsity.tvmaze.model.Show
import kotlinx.coroutines.launch
import com.jobsity.tvmaze.R
import com.squareup.picasso.Picasso

class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()
    private val adapter = ShowsAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        val columns = resources.getInteger(R.integer.columns)
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.layoutManager = GridLayoutManager(this, columns)
        recyclerView.adapter = adapter
        observeShows(null)
    }

    private fun observeShows(query: String?) {
        lifecycleScope.launch {
            viewModel.getShows(query).observe(this@MainActivity, {
                adapter.submitData(lifecycle, it)
            })
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        val menuItem = menu.findItem(R.id.action_search)
        val searchView = menuItem.actionView as SearchView
        searchView.setOnQueryTextListener(SearchListener())
        return true
    }

    inner class SearchListener : SearchView.OnQueryTextListener {

        override fun onQueryTextSubmit(query: String): Boolean {
            return onQueryTextChange(query)
        }

        override fun onQueryTextChange(query: String?): Boolean {
            observeShows(query)
            return true
        }
    }

    inner class ShowsAdapter : PagingDataAdapter<Show, ShowViewHolder>(ShowComparator) {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShowViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val view = inflater.inflate(R.layout.show_item, parent, false)
            return ShowViewHolder(view)
        }

        override fun onBindViewHolder(holder: ShowViewHolder, position: Int) {
            holder.bind(getItem(position))
        }
    }

    inner class ShowViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val image: ImageView = itemView.findViewById(R.id.image_view)
        private val text: TextView = itemView.findViewById(R.id.text_view)
        private var show: Show? = null

        init {
            itemView.setOnClickListener {
                show?.let {
                    ShowActivity.start(this@MainActivity, it.id)
                }
            }
        }

        fun bind(show: Show?) {
            this.show = show
            this.text.text = show?.name
            this.show?.image?.medium?.let {
                Picasso.get().load(it).into(image)
            }
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