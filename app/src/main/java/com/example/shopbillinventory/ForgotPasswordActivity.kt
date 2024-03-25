package com.example.shopbillinventory

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.view.View.inflate
import android.widget.Toast
import com.example.shopbillinventory.databinding.ActivityForgotPasswordBinding
import com.example.shopbillinventory.databinding.ActivityForgotPasswordBinding.inflate
import com.example.shopbillinventory.databinding.ActivityLoginBinding
import com.example.shopbillinventory.databinding.ActivityMainBinding
import com.example.shopbillinventory.databinding.ActivityMainBinding.inflate
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth

class ForgotPasswordActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityForgotPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        FirebaseApp.initializeApp(this)
        auth = FirebaseAuth.getInstance()

        binding.btnConfirmForgot.setOnClickListener {
            val email = binding.etforgotpassEmail.text.toString()
            if (checkEmail()) {
                auth.sendPasswordResetEmail(email).addOnCompleteListener {
                    if (it.isSuccessful) {
                        binding.tvgotogmail.visibility = View.VISIBLE
                        Toast.makeText(this, "Email Sent !", Toast.LENGTH_SHORT).show()
                    } else {

                    }
                }
            }

        }

    }

    private fun checkEmail(): Boolean {
        val email = binding.etforgotpassEmail.text.toString()
        if (email == "") {
            binding.etforgotpassEmail.error = "This is required"
            return false
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.etforgotpassEmail.error = "Check Email Format"
            return false
        }
        return true
    }
}