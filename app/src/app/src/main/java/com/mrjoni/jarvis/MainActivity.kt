package com.mrjoni.jarvis

import android.app.Activity
import android.os.Bundle
import android.graphics.Color
import android.view.Gravity
import android.widget.*
import android.speech.tts.TextToSpeech
import java.util.Locale

class MainActivity : Activity(), TextToSpeech.OnInitListener {

    private lateinit var tts: TextToSpeech
    private lateinit var status: TextView
    private lateinit var input: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val layout = LinearLayout(this)
        layout.orientation = LinearLayout.VERTICAL
        layout.gravity = Gravity.CENTER
        layout.setPadding(25, 30, 25, 30)
        layout.setBackgroundColor(Color.rgb(5, 12, 35))

        val title = TextView(this)
        title.text = "J.A.R.V.I.S"
        title.textSize = 36f
        title.setTextColor(Color.CYAN)
        title.gravity = Gravity.CENTER

        status = TextView(this)
        status.text = "YOUR PERSONAL AI"
        status.textSize = 18f
        status.setTextColor(Color.WHITE)
        status.gravity = Gravity.CENTER

        input = EditText(this)
        input.hint = "Ask JARVIS anything..."
        input.setTextColor(Color.WHITE)
        input.setHintTextColor(Color.GRAY)

        val ask = Button(this)
        ask.text = "ASK JARVIS"

        layout.addView(title)
        layout.addView(status)
        layout.addView(input)
        layout.addView(ask)

        setContentView(layout)

        tts = TextToSpeech(this, this)

        ask.setOnClickListener {
            val question = input.text.toString()

            if (question.isNotBlank()) {
                val answer = when {
                    question.contains("नाम") ->
                        "मेरा नाम JARVIS AI है।"

                    question.contains("hello", true) ->
                        "Hello! I am JARVIS."

                    else ->
                        "नमस्ते! मैं JARVIS AI हूँ।"
                }

                status.text = answer
                tts.speak(answer, TextToSpeech.QUEUE_FLUSH, null, "jarvis")
            }
        }
    }

    override fun onInit(code: Int) {
        if (code == TextToSpeech.SUCCESS) {
            tts.language = Locale("hi", "IN")
        }
    }

    override fun onDestroy() {
        if (::tts.isInitialized) {
            tts.stop()
            tts.shutdown()
        }
        super.onDestroy()
    }
}
