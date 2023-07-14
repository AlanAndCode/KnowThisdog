package com.example.knowthisdog

import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.Manifest
import android.os.Build
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.example.knowthisdog.WholeImageActivity.Companion.PHOTO_URI_KEY
import com.example.knowthisdog.api.ApiServiceInterceptor
import com.example.knowthisdog.auth.LoginActivity
import com.example.knowthisdog.auth.model.User
import com.example.knowthisdog.databinding.ActivityMainBinding
import com.example.knowthisdog.dogList.DogListActivity
import com.example.knowthisdog.settings.SettingsActivity
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
private var isCameraReady = false
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

        requestCameraPermission()
    }

    override fun onDestroy() {
        super.onDestroy()
        if(::cameraExecutor.isInitialized){
            cameraExecutor.shutdown()
        }

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
                        openWholeImageActivity(photoUri.toString())
                    }
                })


    }

    private fun openWholeImageActivity(photoUri: String){
val intent = Intent(this, WholeImageActivity::class.java)
        intent.putExtra(WholeImageActivity.PHOTO_URI_KEY, photoUri)
        startActivity(intent)
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

            cameraProvider.bindToLifecycle(
                this, cameraSelector,
                preview, imageCapture
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