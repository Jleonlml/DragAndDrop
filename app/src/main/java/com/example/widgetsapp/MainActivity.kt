package com.example.widgetsapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.widgetsapp.ui.Fragment1

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction().replace(R.id.nav_container, Fragment1()).commit()
    }
}