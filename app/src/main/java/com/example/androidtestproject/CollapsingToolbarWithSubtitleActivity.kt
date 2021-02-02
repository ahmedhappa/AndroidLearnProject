package com.example.androidtestproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class CollapsingToolbarWithSubtitleActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_collapsing_toolbar_with_subtitle)
        supportActionBar?.hide()
    }
}