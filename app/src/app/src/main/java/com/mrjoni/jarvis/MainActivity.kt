package com.mrjoni.jarvis

import android.app.Activity
import android.os.Bundle
import android.graphics.Color
import android.view.Gravity
import android.widget.*

class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val layout = LinearLayout(this)
        layout.orientation = LinearLayout.VERTICAL
        layout.gravity = Gravity.CENTER
        layout.setPadding(30, 30, 30, 30)
        layout.setBackgroundColor(Color.rgb(5, 12, 35))

        val title = TextView(this)
        title.text = "J.A.R.V.I.S"
        title.textSize = 36f
        title.setTextColor(Color.CYAN)
        title.gravity = Gravity.CENTER

        val message = TextView(this)
        message.text = "JARVIS AI READY!"
        message.textSize = 22f
        message.setTextColor(Color.WHITE)
        message.gravity = Gravity.CENTER

        layout.addView(title)
        layout.addView(message)

        setContentView(layout)
    }
}
