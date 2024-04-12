package com.example.shopbillinventory

import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.example.shopbillinventory.databinding.ActivityLoginBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val isRegisteredUser = intent?.getIntExtra("fromRegister", 0)

        FirebaseApp.initializeApp(this)
        auth = FirebaseAuth.getInstance()
        binding.btnSignIn.setOnClickListener {
            val email = binding.etloginEmail.text.toString()
            val password = binding.etPasswordLogin.text.toString()
            if (checkAllFields()) {
                auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                    if (it.isSuccessful) {
                        //Toast.makeText(this, "Login Successfully...!", Toast.LENGTH_SHORT).show()

                        val sharedPreferences =
                            this?.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

// Use the SharedPreferences.Editor to add the email address to SharedPreferences
                        val editor = sharedPreferences?.edit()
                        editor?.putString(
                            "email",
                            binding.etloginEmail.text.toString()
                        )
                        editor?.putBoolean("loggedInBefore", true)


// Commit or apply the changes
                        editor?.apply()

                        val intent = Intent(this, DashboardActivity::class.java)
                        intent.putExtra("fromRegister", isRegisteredUser)
                        intent.putExtra("login_email", binding.etloginEmail.text.toString())
                        startActivity(intent)
                    } else {
                        Toast.makeText(this, "Login Failed...!", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
        binding.tvRegisterNow.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        binding.tvforgotPassord.setOnClickListener {
            val intent = Intent(this, ForgotPasswordActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    fun checkAllFields(): Boolean {
        if (binding.etloginEmail.text.toString() == "") {
            binding.tilOfemaillogin.error = "This is Required field"
            return false
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(binding.etloginEmail.text).matches()) {
            binding.tilOfemaillogin.error = "Invalid Email"
            return false
        }
        if (binding.etPasswordLogin.text.toString().length < 6) {
            binding.tilOfemaillogin.error = "At least 6 characters password"
            return false
        }
        if (binding.etPasswordLogin.text.toString() == "") {
            binding.tinlofPasswordlogin.error = "This is Required field"
            return false
        }
        return true
    }

    /* this code is for remove from sharedpefrnece
    val sharedPreferences = context?.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

    // Use the SharedPreferences.Editor to remove the email address from SharedPreferences
    val editor = sharedPreferences?.edit()
    editor?.remove("email")

// Commit or apply the changes
    editor?.apply()*/
}