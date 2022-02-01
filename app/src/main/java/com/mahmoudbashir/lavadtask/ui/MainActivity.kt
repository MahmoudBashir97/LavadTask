package com.mahmoudbashir.lavadtask.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mahmoudbashir.lavadtask.R
import com.mahmoudbashir.lavadtask.viewModel.NewsViewModel
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {

    val viewModel  by inject<NewsViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }
}