package com.example.proyecto.views

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.proyecto.databinding.ActivityCreateAccountBinding
import com.example.proyecto.extensions.Extensions.toast
import com.example.proyecto.utils.FirebaseUtils.firebaseAuth
import com.example.proyecto.utils.FirebaseUtils.firebaseUser

@SuppressLint("StaticFieldLeak")
private lateinit var binding: ActivityCreateAccountBinding

class CreateAccountActivity : AppCompatActivity() {
    private lateinit var userEmail: String
    private lateinit var userPassword: String
    private lateinit var createAccountInputsArray: Array<EditText>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateAccountBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        createAccountInputsArray = arrayOf(binding.etEmail, binding.etPassword, binding.etConfirmPassword)
        binding.btnCreateAccount.setOnClickListener {
            signIn()
        }

        binding.btnSignIn2.setOnClickListener {
            startActivity(Intent(this, SignInActivity::class.java))
            toast("please sign into your account")
            finish()
        }
    }

    /* check if there's a signed-in user*/


    private fun notEmpty(): Boolean = binding.etEmail.text.toString().trim().isNotEmpty() &&
            binding.etPassword.text.toString().trim().isNotEmpty() &&
            binding.etConfirmPassword.text.toString().trim().isNotEmpty()

    private fun identicalPassword(): Boolean {
        var identical = false
        if (notEmpty() &&
            binding.etPassword.text.toString().trim() == binding.etConfirmPassword.text.toString().trim()
        ) {
            identical = true
        } else if (!notEmpty()) {
            createAccountInputsArray.forEach { input ->
                if (input.text.toString().trim().isEmpty()) {
                    input.error = "${input.hint} is required"
                }
            }
        } else {
            toast("passwords are not matching !")
        }
        return identical
    }

    private fun signIn() {
        if (identicalPassword()) {
            // identicalPassword() returns true only  when inputs are not empty and passwords are identical
            userEmail = binding.etEmail.text.toString().trim()
            userPassword = binding.etPassword.text.toString().trim()

            /*create a user*/
            firebaseAuth.createUserWithEmailAndPassword(userEmail, userPassword)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        toast("created account successfully !")
                        sendEmailVerification()
                        startActivity(Intent(this, HomeActivity::class.java))
                        finish()
                    } else {
                        toast("failed to Authenticate !")
                    }
                }
        }
    }

    /* send verification email to the new user. This will only
    *  work if the firebase user is not null.
    */

    private fun sendEmailVerification() {
        firebaseUser?.let {
            it.sendEmailVerification().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    toast("email sent to $userEmail")
                }
            }
        }
    }
}