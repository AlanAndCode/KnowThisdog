package com.example.knowthisdog

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.knowthisdog.api.ApiServiceInterceptor
import com.example.knowthisdog.auth.LoginActivity
import com.example.knowthisdog.auth.model.User
import com.example.knowthisdog.databinding.ActivityMainBinding
import com.example.knowthisdog.settings.SettingsActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
val binding = ActivityMainBinding.inflate(layoutInflater)
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
    }

    private fun openListActivity() {
        startActivity(Intent(this, SettingsActivity::class.java))
    }

    private fun openSettingsActivity() {
        startActivity(Intent(this, SettingsActivity::class.java))
    }

    private fun openLoginActivity() {
        startActivity(Intent(this,LoginActivity::class.java))
        finish()
    }
}