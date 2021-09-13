package com.example.androidtestproject

import android.Manifest
import android.content.Context
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.example.androidtestproject.databinding.ActivityLearnCameraBinding
import java.io.File
import java.nio.ByteBuffer
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class LearnCameraActivity : AppCompatActivity() {

    companion object {
        private const val FILE_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
        private const val TAG = "LearnCameraActivity"
    }

    private lateinit var binding: ActivityLearnCameraBinding
    private lateinit var outputDirectory: File
    private lateinit var cameraExecutor: ExecutorService
    private var imageCapture: ImageCapture? = null

    private val cameraPermission = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
        if (it) {
            startCamera()
        } else {
            Toast.makeText(this, "Camera Permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLearnCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)
        cameraPermission.launch(Manifest.permission.CAMERA)
        binding.btnCameraCapture.setOnClickListener {
            takePhoto()
        }

        outputDirectory = getOutputDirectory()
        //is a must to use camera
        cameraExecutor = Executors.newSingleThreadExecutor()
    }


    private fun startCamera() {
        //1. Implement Preview use case
        /* the case is used to let the user preview the photo they will be taking You can implement a
           viewfinder using the CameraX Preview class. */

        //This is used to bind the lifecycle of cameras to the lifecycle owner
        //This eliminates the task of opening and closing the camera since CameraX is lifecycle-aware
        /*val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener(
            {
                //This is used to bind the lifecycle of your camera to the LifecycleOwner within the application's process.
                val cameraProvider = cameraProviderFuture.get()
                val preview = Preview.Builder().build()
                    .also { it.setSurfaceProvider(binding.previewViewFinder.surfaceProvider) }
                // Select back camera as a default
                val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
                try {
                    //make sure nothing is bound to your cameraProvider
                    // Unbind use cases(preview) before rebinding
                    cameraProvider.unbindAll()
                    // Bind use cases(preview) to camera
                    cameraProvider.bindToLifecycle(this, cameraSelector, preview)
                } catch (exc: Exception) {
                    exc.printStackTrace()
                }
            },
            //This returns an Executor that runs on the main thread.
            ContextCompat.getMainExecutor(this)
        )*/


        //2. Implement Capture Photo use case
        /* the case is used to let the user take pictures */
        //it has similar implementation from above
        /*   val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
           cameraProviderFuture.addListener({
               val cameraProvider = cameraProviderFuture.get()
               val preview = Preview.Builder().build().also { it.setSurfaceProvider(binding.previewViewFinder.surfaceProvider) }
               //build image capture use case
               imageCapture = ImageCapture.Builder().build()
               val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
               try {
                   cameraProvider.unbindAll()
                   //the one thing that is extra here from above it the image capture
                   cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture)
               } catch (exc: Exception) {
                   exc.printStackTrace()
               }
           }, ContextCompat.getMainExecutor(this))*/

        //3. Implement ImageAnalysis use case
        /* the case is used to analyze image that is previewed in camera each second or less */
        //we can use the Capture Photo and ImageAnalysis use case with each other at the same time
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder().build().also { it.setSurfaceProvider(binding.previewViewFinder.surfaceProvider) }
            //build image analyzer use case
            val imageAnalyzer = ImageAnalysis.Builder().build().also {
                it.setAnalyzer(ContextCompat.getMainExecutor(this@LearnCameraActivity), LuminosityAnalyzer { luma ->
                    Log.d(TAG, "Average luminosity: $luma")
                })
            }
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageAnalyzer)
            } catch (exc: Exception) {
                exc.printStackTrace()
            }
        }, ContextCompat.getMainExecutor(this))
    }


    //you must use Capture Photo use case for this method to work
    private fun takePhoto() {
        // If the use case is null, exit out of the function
        val imageCapture = this.imageCapture ?: return

        //  create a file to hold the image
        val photoFile = File(
            outputDirectory,
            SimpleDateFormat(FILE_FORMAT, Locale.US).format(System.currentTimeMillis()).plus(".jpg")
        )

        //This object is where you can specify things about how you want your output to be
        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    val savedUri = Uri.fromFile(photoFile)
                    val msg = "Photo capture succeeded: $savedUri"
                    Toast.makeText(this@LearnCameraActivity, msg, Toast.LENGTH_SHORT).show()
                    Log.d(TAG, msg)
                }

                override fun onError(exception: ImageCaptureException) {
                    Log.e(TAG, "Photo capture failed: ${exception.message}", exception)
                }

            })
    }

    //create external directory to store captured images
    private fun getOutputDirectory(): File {
        val mediaDir = externalMediaDirs.firstOrNull()?.let {
            File(it, getString(R.string.app_name)).apply { mkdirs() }
        }
        return if (mediaDir != null && mediaDir.exists())
            mediaDir
        else
            filesDir

    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    private class LuminosityAnalyzer(private val listener: (Double) -> Unit) : ImageAnalysis.Analyzer {
        //didn't what it is doing xD
        private fun ByteBuffer.toByteArray(): ByteArray {
            rewind() // Rewind the buffer to zero
            val data = ByteArray(remaining())
            get(data) // Copy the buffer into a byte array
            return data // Return the byte array
        }

        override fun analyze(image: ImageProxy) {
            //getting Luminosity from image
            val buffer = image.planes[0].buffer
            val data = buffer.toByteArray()
            val pixels = data.map { it.toInt() and 0xFF }
            val luma = pixels.average()
            listener(luma)
            image.close()
        }
    }
}