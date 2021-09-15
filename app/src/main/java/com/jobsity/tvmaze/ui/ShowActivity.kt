package com.jobsity.tvmaze.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import com.jobsity.tvmaze.R
import com.jobsity.tvmaze.databinding.ActivityShowBinding
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
        binding.toolbarLayout.title = ""

        val showId = intent.getLongExtra("show_id", -1L)
        viewModel.getShow(showId).observe(this, { show ->
            binding.toolbarLayout.title = show.name
            Picasso.get().load(show.image.original)
                .fit().centerCrop()
                .into(binding.imageView)
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
}