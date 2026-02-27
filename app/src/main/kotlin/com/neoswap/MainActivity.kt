package com.neoswap

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var imgSource: ImageView
    private lateinit var imgTarget: ImageView
    private lateinit var btnClearSource: Button
    private lateinit var btnClearTarget: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var btnStop: Button
    private lateinit var btnSwap: Button

    private val pickSourceMedia = registerForActivityResult(PickVisualMedia()) { uri ->
        if (uri != null) {
            imgSource.setImageURI(uri)
            btnClearSource.visibility = View.VISIBLE // Show 'X' when photo is added
        }
    }

    private val pickTargetMedia = registerForActivityResult(PickVisualMedia()) { uri ->
        if (uri != null) {
            imgTarget.setImageURI(uri)
            btnClearTarget.visibility = View.VISIBLE // Show 'X' when photo is added
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize all the new UI elements
        imgSource = findViewById(R.id.imgSource)
        imgTarget = findViewById(R.id.imgTarget)
        btnClearSource = findViewById(R.id.btnClearSource)
        btnClearTarget = findViewById(R.id.btnClearTarget)
        progressBar = findViewById(R.id.progressBar)
        btnStop = findViewById(R.id.btnStop)
        btnSwap = findViewById(R.id.btnSwap)

        // Select Photo Buttons
        findViewById<Button>(R.id.btnSource).setOnClickListener {
            pickSourceMedia.launch(PickVisualMediaRequest(PickVisualMedia.ImageOnly))
        }
        findViewById<Button>(R.id.btnTarget).setOnClickListener {
            pickTargetMedia.launch(PickVisualMediaRequest(PickVisualMedia.ImageOnly))
        }

        // 'X' Clear Buttons - Resets only one photo
        btnClearSource.setOnClickListener {
            imgSource.setImageDrawable(null)
            btnClearSource.visibility = View.GONE
        }
        btnClearTarget.setOnClickListener {
            imgTarget.setImageDrawable(null)
            btnClearTarget.visibility = View.GONE
        }

        // Full Screen Toggle - Tap the image to zoom in/out
        imgSource.setOnClickListener { toggleZoom(imgSource) }
        imgTarget.setOnClickListener { toggleZoom(imgTarget) }

        // Swap Action - Shows progress and stop button
        btnSwap.setOnClickListener {
            progressBar.visibility = View.VISIBLE
            btnStop.visibility = View.VISIBLE
            Toast.makeText(this, "Processing...", Toast.LENGTH_SHORT).show()
        }

        // Stop Action - Cancels the visual progress
        btnStop.setOnClickListener {
            progressBar.visibility = View.GONE
            btnStop.visibility = View.GONE
        }
    }

    private fun toggleZoom(view: ImageView) {
        if (view.scaleType == ImageView.ScaleType.CENTER_CROP) {
            view.scaleType = ImageView.ScaleType.FIT_CENTER
        } else {
            view.scaleType = ImageView.ScaleType.CENTER_CROP
        }
    }
}
