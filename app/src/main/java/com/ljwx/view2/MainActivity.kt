package com.ljwx.view2

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ljwx.view2.R
import com.ljwx.view.SplitTextView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val stv = findViewById<SplitTextView>(R.id.split)
        stv.setTextCenter("中间文字")
        stv.setColorLeft(Color.parseColor("#000000"))

    }
}