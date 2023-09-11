package com.example.knowthisdog.main

import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.Manifest
import android.os.Build
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.core.content.ContextCompat
import coil.annotation.ExperimentalCoilApi
import com.example.knowthisdog.LABEL_PATH
import com.example.knowthisdog.MODEL_PATH
import com.example.knowthisdog.R
import com.example.knowthisdog.api.ApiResponseStatus
import com.example.knowthisdog.api.ApiServiceInterceptor
import com.example.knowthisdog.api.dogdetail.DogDetailComposeActivity
import com.example.knowthisdog.auth.LoginActivity
import com.example.knowthisdog.auth.model.Dog
import com.example.knowthisdog.auth.model.User
import com.example.knowthisdog.databinding.ActivityMainBinding
import com.example.knowthisdog.dogList.DogListActivity
import com.example.knowthisdog.machinelearning.Classifier
import com.example.knowthisdog.machinelearning.DogRecognition
import com.example.knowthisdog.settings.SettingsActivity
import org.tensorflow.lite.support.common.FileUtil
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
@ExperimentalMaterialApi
@ExperimentalFoundationApi
@ExperimentalCoilApi
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


        viewModel.dogRecognition.observe(this){
            enabledTakePhotoButton(it)
        }
        requestCameraPermission()
    }


    private fun openDogDetailActivity(dog: Dog) {
        val intent = Intent(this, DogDetailComposeActivity::class.java)
        intent.putExtra(DogDetailComposeActivity.DOG_KEY, dog)
        intent.putExtra(DogDetailComposeActivity.IS_RECOGNITION_KEY, true)
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
         viewModel.setupClassifier(
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
                 viewModel.recognizeImage(imageProxy)

   // imageProxy.close()

}
            cameraProvider.bindToLifecycle(
                this, cameraSelector,
                preview, imageCapture, imageAnalysis
            )
        }, ContextCompat.getMainExecutor(this))
    }

    private fun enabledTakePhotoButton(dogRecognition: DogRecognition) {
        binding.takePhotoFab.isEnabled
       if (dogRecognition.confidence > 70.0){
           binding.takePhotoFab.alpha = 1f
           binding.takePhotoFab.setOnClickListener {
               viewModel.getDogByMlId(dogRecognition.id)
           }
       } else {
           binding.takePhotoFab.alpha = 0.2f
           binding.takePhotoFab.setOnClickListener(null)
       }
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