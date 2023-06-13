package com.example.proyecto.views

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.proyecto.databinding.ActivitySignInBinding
import com.example.proyecto.extensions.Extensions.toast
import com.example.proyecto.utils.FirebaseUtils.firebaseAuth

@SuppressLint("StaticFieldLeak")
private lateinit var binding: ActivitySignInBinding
class SignInActivity : AppCompatActivity() {
    private lateinit var signInEmail: String
    private lateinit var signInPassword: String
    private lateinit var signInInputsArray: Array<EditText>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        signInInputsArray = arrayOf(binding.etSignInEmail, binding.etSignInPassword)
        binding.btnCreateAccount2.setOnClickListener {
            val intento12 = Intent(this, CreateAccountActivity::class.java)
            startActivity(intento12)
        }

        binding.btnSignIn.setOnClickListener {
            signInUser()
        }
    }

    private fun notEmpty(): Boolean = signInEmail.isNotEmpty() && signInPassword.isNotEmpty()

    private fun signInUser() {
        signInEmail = binding.etSignInEmail.text.toString().trim()
        signInPassword = binding.etSignInPassword.text.toString().trim()

        if (notEmpty()) {
            firebaseAuth.signInWithEmailAndPassword(signInEmail, signInPassword)
                .addOnCompleteListener { signIn ->
                    if (signIn.isSuccessful) {
                        startActivity(Intent(this, HomeActivity::class.java))
                        toast("signed in successfully")
                        finish()
                    } else {
                        toast("sign in failed")
                    }
                }
        } else {
            signInInputsArray.forEach { input ->
                if (input.text.toString().trim().isEmpty()) {
                    input.error = "${input.hint} is required"
                }
            }
        }
    }
}