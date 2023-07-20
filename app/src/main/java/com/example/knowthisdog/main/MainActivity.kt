package com.example.knowthisdog.main

import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.Manifest
import android.graphics.BitmapFactory
import android.os.Build
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.example.knowthisdog.LABEL_PATH
import com.example.knowthisdog.MODEL_PATH
import com.example.knowthisdog.R
import com.example.knowthisdog.api.ApiResponseStatus
import com.example.knowthisdog.api.ApiServiceInterceptor
import com.example.knowthisdog.api.dogdetail.DogDetailActivity
import com.example.knowthisdog.api.dogdetail.DogDetailActivity.Companion.DOG_KEY
import com.example.knowthisdog.auth.LoginActivity
import com.example.knowthisdog.auth.model.Dog
import com.example.knowthisdog.auth.model.User
import com.example.knowthisdog.databinding.ActivityMainBinding
import com.example.knowthisdog.dogList.DogListActivity
import com.example.knowthisdog.machinelearning.Classifier
import com.example.knowthisdog.settings.SettingsActivity
import org.tensorflow.lite.support.common.FileUtil
import java.io.File
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {

        private   val requestPermissionLauncher =
            registerForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { isGranted: Boolean ->
                if (isGranted) {
                    setupCamera()

                } else {
                    Toast.makeText(this, getString(R.string.camera_denied), Toast.LENGTH_LONG).show()
                }
            }

private lateinit var binding: ActivityMainBinding
private lateinit var imageCapture: ImageCapture
private lateinit var cameraExecutor: ExecutorService
private lateinit var classifier: Classifier
private var isCameraReady = false
    private val viewModel: MainViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)



        val user = User.getLoggedInUser(this)
        if (user == null){
            openLoginActivity()
            return
        }else {
            ApiServiceInterceptor.setSessionToken(user.authenticationtoken)
        }

        binding.settingsFab.setOnClickListener {
            openSettingsActivity()
        }


        binding.dogListFab.setOnClickListener {
            openListActivity()
        }

        binding.takePhotoFab.setOnClickListener {
            if(isCameraReady){
                takePhoto()
            }

        }
       viewModel.status.observe(this){
        status ->
        when (status) {
        is ApiResponseStatus.Error -> {
            binding.loadingWheel.visibility = View.GONE
            Toast.makeText(this, status.messageId, Toast.LENGTH_SHORT).show()

        }
        is ApiResponseStatus.Loading -> binding.loadingWheel.visibility = View.VISIBLE
        is ApiResponseStatus.Success -> binding.loadingWheel.visibility = View.GONE
    }
}
        viewModel.dog.observe(this){
            dog ->
            if (dog != null) {
                openDogDetailActivity(dog)
            }
        }
        requestCameraPermission()
    }

    private fun openDogDetailActivity(dog: Dog) {
        val intent = Intent(this, DogDetailActivity::class.java)
        intent.putExtra(DOG_KEY, dog)
        startActivity(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        if(::cameraExecutor.isInitialized){
            cameraExecutor.shutdown()
        }

    }

    override fun onStart() {
        super.onStart()
         classifier = Classifier(
            FileUtil.loadMappedFile(this@MainActivity, MODEL_PATH),
            FileUtil.loadLabels(this@MainActivity, LABEL_PATH)
        )
    }
    private fun setupCamera() {
        binding.cameraPreview.post {


            imageCapture = ImageCapture.Builder()
                .setTargetRotation(binding.cameraPreview.display.rotation)
                .build()
            cameraExecutor = Executors.newSingleThreadExecutor()
            startCamera()
            isCameraReady = true
        }
    }
   private fun requestCameraPermission() {
       if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
           when {
               ContextCompat.checkSelfPermission(
                   this, Manifest.permission.CAMERA
               ) == PackageManager.PERMISSION_GRANTED -> {

                   setupCamera()
               }
               shouldShowRequestPermissionRationale(Manifest.permission.CAMERA) -> {

                   AlertDialog.Builder(this)
                       .setTitle("Permission camera")
                       .setMessage("Accept to use camera")
                       .setPositiveButton(android.R.string.ok) { _, _ ->
                           requestPermissionLauncher.launch(
                               Manifest.permission.CAMERA
                           )

                       }.setNegativeButton(android.R.string.cancel) { _, _ ->
                       }.show()
               }   else -> {
                   requestPermissionLauncher.launch(
                       Manifest.permission.CAMERA
                   )
               }
           }
       } else{
// Open camera
           setupCamera()
       }

    }

    private fun takePhoto(){

            val outputFileOptions = ImageCapture.OutputFileOptions.Builder(getOutputPhotoFile()).build()
            imageCapture.takePicture(outputFileOptions, cameraExecutor,
                object : ImageCapture.OnImageSavedCallback {
                    override fun onError(error: ImageCaptureException)
                    {
                        Toast.makeText(this@MainActivity,
                            "Error taking photo ${error.message}",
                            Toast.LENGTH_SHORT).show()
                    }
                    override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                        // insert your code here.
                       val photoUri = outputFileResults.savedUri

val bitmap = BitmapFactory.decodeFile(photoUri?.path)
                       val dogRecognition = classifier.recognizeImage(bitmap).first()
                        viewModel.getDogByMlId(dogRecognition.id)

                    }
                })


    }


    private fun getOutputPhotoFile(): File {
        val mediaDir = externalMediaDirs.firstOrNull()?.let{
            File(it, resources.getString(R.string.app_name) + ".jpg").apply { mkdirs() }
        }
        return if (mediaDir != null && mediaDir.exists()){
            mediaDir
        } else {
            filesDir
        }
    }


    private fun startCamera(){
        val cameraProviderFuture =
            ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({

            val cameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder().build()
            preview.setSurfaceProvider(binding.cameraPreview.surfaceProvider)

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            val imageAnalysis = ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()
imageAnalysis.setAnalyzer(cameraExecutor) { imageProxy ->
    val rotationDegrees = imageProxy.imageInfo.rotationDegrees

    imageProxy.close()

}
            cameraProvider.bindToLifecycle(
                this, cameraSelector,
                preview, imageCapture, imageAnalysis
            )
        }, ContextCompat.getMainExecutor(this))
    }


    private fun openListActivity() {
        startActivity(Intent(this, DogListActivity::class.java))
    }

    private fun openSettingsActivity() {
        startActivity(Intent(this, SettingsActivity::class.java))
    }

    private fun openLoginActivity() {
        startActivity(Intent(this,LoginActivity::class.java))
        finish()
    }
}