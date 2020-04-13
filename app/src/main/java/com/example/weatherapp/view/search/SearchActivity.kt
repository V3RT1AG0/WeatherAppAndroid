package com.example.weatherapp.view.search

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.weatherapp.R
import kotlinx.android.synthetic.main.activity_search.*
import android.app.Activity
import android.content.Intent


class SearchActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        supportActionBar?.elevation = 0F
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        search.setOnClickListener {
            val resultIntent = Intent()
            resultIntent.putExtra("zip", search_bar.text.toString())
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }

        mLocation.setOnClickListener {
            val resultIntent = Intent()
            resultIntent.putExtra("zip", "0")
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }
//        val slide = TransitionInflater.from(this).inflateTransition(R.transition.slide)
//        window.enterTransition = slide
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}