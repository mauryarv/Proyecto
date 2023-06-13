package com.example.proyecto.views

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.proyecto.databinding.ActivityHomeBinding
import com.example.proyecto.extensions.Extensions.toast
import com.example.proyecto.utils.FirebaseUtils.firebaseAuth

@SuppressLint("StaticFieldLeak")
private lateinit var binding: ActivityHomeBinding
class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
// sign out a user

        binding.btnSignOut.setOnClickListener {
            firebaseAuth.signOut()
            startActivity(Intent(this, CreateAccountActivity::class.java))
            toast("signed out")
            finish()
        }
    }
}