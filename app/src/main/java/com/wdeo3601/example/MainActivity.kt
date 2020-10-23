package com.wdeo3601.example

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<MaterialButton>(R.id.tv_multi_conditions).setOnClickListener {
            startActivity(Intent(this, MultiConditionsActivity::class.java))
        }
        findViewById<MaterialButton>(R.id.tv_single_condition).setOnClickListener {
            startActivity(Intent(this, SingleConditionActivity::class.java))
        }
    }
}