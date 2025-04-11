package com.example.multidisplayapplication

import androidx.appcompat.app.AppCompatActivity
import android.content.Context
import android.hardware.display.DisplayManager
import android.os.Build
import android.os.Bundle
import android.view.Display
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi

class MainActivity : AppCompatActivity() {

    private var presentation: SecondDisplayPresentation? = null

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (display?.displayId != Display.DEFAULT_DISPLAY) {
            finish()
            return
        }

        setContentView(R.layout.activity_main)

        val launchButton = findViewById<Button>(R.id.launch_button)
        val sendButton = findViewById<Button>(R.id.send_button)
        val inputField = findViewById<EditText>(R.id.message_input)

        launchButton.setOnClickListener {
            showPresentation()
        }

        sendButton.setOnClickListener {

            val message = inputField.text.toString()
            if (presentation != null) {
                if (message.isEmpty()) {
                    Toast.makeText(this, "Please enter a message", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                presentation?.addMessage(message)
                inputField.text.clear()
            } else {
                Toast.makeText(this, "Presentation not started", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showPresentation() {
        val displayManager = getSystemService(Context.DISPLAY_SERVICE) as DisplayManager
        val displays = displayManager.displays

        val secondDisplay = displays.firstOrNull { it.displayId != Display.DEFAULT_DISPLAY }

        if (secondDisplay == null) {
            Toast.makeText(this, "No secondary display found", Toast.LENGTH_SHORT).show()
            return
        }

        presentation?.dismiss()
        presentation = SecondDisplayPresentation(this, secondDisplay)
        presentation?.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        presentation?.dismiss()
    }
}