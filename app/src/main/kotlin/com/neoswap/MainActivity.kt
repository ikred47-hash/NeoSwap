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
    private lateinit var progressBar: ProgressBar
    private lateinit var btnSwap: Button

    private val pickSource = registerForActivityResult(PickVisualMedia()) { uri ->
        if (uri != null) imgSource.setImageURI(uri)
    }

    private val pickTarget = registerForActivityResult(PickVisualMedia()) { uri ->
        if (uri != null) imgTarget.setImageURI(uri)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        OpenCVLoader.initDebug()

        imgSource = findViewById(R.id.imgSource)
        imgTarget = findViewById(R.id.imgTarget)
        progressBar = findViewById(R.id.progressBar)
        btnSwap = findViewById(R.id.btnSwap)

        findViewById<Button>(R.id.btnSource).setOnClickListener {
            pickSource.launch(PickVisualMediaRequest(PickVisualMedia.ImageOnly))
        }
        findViewById<Button>(R.id.btnTarget).setOnClickListener {
            pickTarget.launch(PickVisualMediaRequest(PickVisualMedia.ImageOnly))
        }

        btnSwap.setOnClickListener {
            if (imgSource.drawable == null || imgTarget.drawable == null) {
                Toast.makeText(this, "Select images", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            runSwap()
        }
    }

    private fun runSwap() {
        progressBar.visibility = View.VISIBLE
        val srcBmp = (imgSource.drawable as BitmapDrawable).bitmap
        val dstBmp = (imgTarget.drawable as BitmapDrawable).bitmap

        val detector = FaceDetection.getClient(FaceDetectorOptions.Builder()
            .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE).build())

        detector.process(InputImage.fromBitmap(dstBmp, 0)).addOnSuccessListener { faces ->
            if (faces.isEmpty()) {
                progressBar.visibility = View.GONE
                return@addOnSuccessListener
            }

            val face = faces[0].boundingBox
            val srcMat = Mat(); val dstMat = Mat()
            Utils.bitmapToMat(srcBmp, srcMat)
            Utils.bitmapToMat(dstBmp, dstMat)

            val center = Point(face.centerX().toDouble(), face.centerY().toDouble())
            val resultMat = Mat()
            
            Photo.seamlessClone(srcMat, dstMat, Mat(), center, resultMat, Photo.NORMAL_CLONE)

            val outBmp = Bitmap.createBitmap(resultMat.cols(), resultMat.rows(), Bitmap.Config.ARGB_8888)
            Utils.matToBitmap(resultMat, outBmp)
            
            imgTarget.setImageBitmap(outBmp)
            progressBar.visibility = View.GONE
        }
    }
}
