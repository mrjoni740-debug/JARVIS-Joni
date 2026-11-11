package com.mrjoni.jarvis

import android.app.Activity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.speech.RecognitionListener
import android.content.Intent
import android.widget.*
import android.graphics.Color
import java.util.Locale

class MainActivity : Activity(), TextToSpeech.OnInitListener {

    private lateinit var tts: TextToSpeech
    private lateinit var status: TextView
    private lateinit var input: EditText
    private var speech: SpeechRecognizer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        tts = TextToSpeech(this, this)

        val layout = LinearLayout(this)
        layout.orientation = LinearLayout.VERTICAL
        layout.gravity = android.view.Gravity.CENTER
        layout.setPadding(25, 30, 25, 30)
        layout.setBackgroundColor(Color.rgb(5, 12, 35))

        val title = TextView(this)
        title.text = "J.A.R.V.I.S"
        title.textSize = 36f
        title.setTextColor(Color.CYAN)
        title.gravity = 17

        status = TextView(this)
        status.text = "YOUR PERSONAL AI"
        status.textSize = 18f
        status.setTextColor(Color.WHITE)
        status.gravity = 17

        input = EditText(this)
        input.hint = "Ask JARVIS anything..."
        input.setTextColor(Color.WHITE)
        input.setHintTextColor(Color.GRAY)

        val ask = Button(this)
        ask.text = "ASK JARVIS"

        val mic = Button(this)
        mic.text = "🎤 SPEAK"

        layout.addView(title)
        layout.addView(status)
        layout.addView(input)
        layout.addView(ask)
        layout.addView(mic)

        setContentView(layout)

        ask.setOnClickListener {
            reply(input.text.toString())
        }

        mic.setOnClickListener {
            listen()
        }
    }

    private fun reply(text: String) {
        if (text.isBlank()) return

        val answer = when {
            text.contains("hello", true) ->
                "Hello! I am JARVIS. How can I help you?"

            text.contains("नाम") ->
                "मेरा नाम JARVIS AI है।"

            text.contains("समय") ->
                "अभी समय ${java.text.SimpleDateFormat("hh:mm a", Locale.getDefault()).format(java.util.Date())} है।"

            else ->
                "आपका सवाल मिला। मैं JARVIS AI हूँ।"
        }

        status.text = answer
        tts.speak(answer, TextToSpeech.QUEUE_FLUSH, null, "jarvis")
    }

    private fun listen() {
        if (!SpeechRecognizer.isRecognitionAvailable(this)) {
            status.text = "Speech recognition unavailable"
            return
        }

        if (android.os.Build.VERSION.SDK_INT >= 23 &&
            checkSelfPermission(android.Manifest.permission.RECORD_AUDIO)
            != android.content.pm.PackageManager.PERMISSION_GRANTED) {
            requestPermissions(
                arrayOf(android.Manifest.permission.RECORD_AUDIO), 101
            )
            return
        }

        speech = SpeechRecognizer.createSpeechRecognizer(this)

        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        )
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "hi-IN")

        speech?.setRecognitionListener(object : RecognitionListener {
            override fun onReadyForSpeech(params: Bundle?) {
                status.text = "Listening..."
            }

            override fun onResults(results: Bundle?) {
                val words = results?.getStringArrayList(
                    SpeechRecognizer.RESULTS_RECOGNITION
                )
                input.setText(words?.firstOrNull() ?: "")
                reply(input.text.toString())
            }

            override fun onError(error: Int) {
                status.text = "Please try again"
            }

            override fun onBeginningOfSpeech() {}
            override fun onRmsChanged(rmsdB: Float) {}
            override fun onBufferReceived(buffer: ByteArray?) {}
            override fun onEndOfSpeech() {}
            override fun onPartialResults(partialResults: Bundle?) {}
            override fun onEvent(eventType: Int, params: Bundle?) {}
        })

        speech?.startListening(intent)
    }

    override fun onInit(statusCode: Int) {
        if (statusCode == TextToSpeech.SUCCESS) {
            tts.language = Locale("hi", "IN")
        }
    }

    override fun onDestroy() {
        speech?.destroy()
        tts.shutdown()
        super.onDestroy()
    }
}
