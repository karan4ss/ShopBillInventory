package com.example.shopbillinventory

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.example.shopbillinventory.databinding.ActivityLoginBinding
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        FirebaseApp.initializeApp(this)
        auth = FirebaseAuth.getInstance()
        binding.btnSignIn.setOnClickListener {
            val email = binding.etloginEmail.text.toString()
            val password = binding.etPasswordLogin.text.toString()
            if (checkAllFields()) {
                auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                    if (it.isSuccessful) {

                        Toast.makeText(this, "Login Successfully...!", Toast.LENGTH_SHORT).show()
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
}