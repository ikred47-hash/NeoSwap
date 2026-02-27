package com.neoswap

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.appcompat.app.AppCompatActivity
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions
import org.opencv.android.OpenCVLoader
import org.opencv.android.Utils
import org.opencv.core.*
import org.opencv.photo.Photo

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
            btnClearSource.visibility = View.VISIBLE
        }
    }

    private val pickTargetMedia = registerForActivityResult(PickVisualMedia()) { uri ->
        if (uri != null) {
            imgTarget.setImageURI(uri)
            btnClearTarget.visibility = View.VISIBLE
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize Official OpenCV
        OpenCVLoader.initDebug()

        imgSource = findViewById(R.id.imgSource)
        imgTarget = findViewById(R.id.imgTarget)
        btnClearSource = findViewById(R.id.btnClearSource)
        btnClearTarget = findViewById(R.id.btnClearTarget)
        progressBar = findViewById(R.id.progressBar)
        btnStop = findViewById(R.id.btnStop)
        btnSwap = findViewById(R.id.btnSwap)

        findViewById<Button>(R.id.btnSource).setOnClickListener {
            pickSourceMedia.launch(PickVisualMediaRequest(PickVisualMedia.ImageOnly))
        }
        findViewById<Button>(R.id.btnTarget).setOnClickListener {
            pickTargetMedia.launch(PickVisualMediaRequest(PickVisualMedia.ImageOnly))
        }

        btnClearSource.setOnClickListener {
            imgSource.setImageDrawable(null)
            btnClearSource.visibility = View.GONE
        }
        btnClearTarget.setOnClickListener {
            imgTarget.setImageDrawable(null)
            btnClearTarget.visibility = View.GONE
        }

        btnSwap.setOnClickListener {
            if (imgSource.drawable == null || imgTarget.drawable == null) {
                Toast.makeText(this, "Select both photos first!", Toast.LENGTH_SHORT).show()
            } else {
                runFaceSwap()
            }
        }

        btnStop.setOnClickListener {
            progressBar.visibility = View.GONE
            btnStop.visibility = View.GONE
        }
    }

    private fun runFaceSwap() {
        progressBar.visibility = View.VISIBLE
        btnStop.visibility = View.VISIBLE

        val srcBmp = (imgSource.drawable as BitmapDrawable).bitmap
        val dstBmp = (imgTarget.drawable as BitmapDrawable).bitmap

        val detector = FaceDetection.getClient(FaceDetectorOptions.Builder()
            .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE).build())

        detector.process(InputImage.fromBitmap(dstBmp, 0)).addOnSuccessListener { faces ->
            if (faces.isEmpty()) {
                progressBar.visibility = View.GONE
                Toast.makeText(this, "No face found in Target!", Toast.LENGTH_SHORT).show()
                return@addOnSuccessListener
            }

            val face = faces[0].boundingBox
            val srcMat = Mat(); val dstMat = Mat()
            Utils.bitmapToMat(srcBmp, srcMat)
            Utils.bitmapToMat(dstBmp, dstMat)

            val center = Point(face.centerX().toDouble(), face.centerY().toDouble())
            val resultMat = Mat()
            
            // Seamless blending logic
            Photo.seamlessClone(srcMat, dstMat, Mat(), center, resultMat, Photo.NORMAL_CLONE)

            val outBmp = Bitmap.createBitmap(resultMat.cols(), resultMat.rows(), Bitmap.Config.ARGB_8888)
            Utils.matToBitmap(resultMat, outBmp)
            
            imgTarget.setImageBitmap(outBmp)
            progressBar.visibility = View.GONE
            btnStop.visibility = View.GONE
            Toast.makeText(this, "Success!", Toast.LENGTH_SHORT).show()
        }
    }
}
