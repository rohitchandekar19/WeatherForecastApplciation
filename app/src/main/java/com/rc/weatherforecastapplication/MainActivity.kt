package com.rc.weatherforecastapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.rc.weatherforecastapplication.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var mainBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)
        title = getString(R.string.title_activity_city)
    }

}