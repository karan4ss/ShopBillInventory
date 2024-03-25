package com.example.shopbillinventory

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.example.shopbillinventory.databinding.ActivityMainBinding
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var mainBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)
        FirebaseApp.initializeApp(this)
        auth = FirebaseAuth.getInstance()
        mainBinding.btnSignUp.setOnClickListener {
            val email = mainBinding.etEmail.text.toString()
            val password = mainBinding.etPassword.text.toString()
            if (checkAllFields()) {
                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                    if (it.isSuccessful) {
                        auth.signOut()
                        Toast.makeText(
                            this,
                            "Account Registered Successfully...!",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(
                            this,
                            it.exception.toString(),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }

        }

    }

    private fun checkAllFields(): Boolean {
        if (mainBinding.etEmail.text.toString() == "") {
            mainBinding.tilofemail.error = "This is Required field"
            return false
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(mainBinding.etEmail.text).matches()) {
            mainBinding.tilofemail.error = "Invalid Email"
            return false
        }
        if (mainBinding.etPassword.text.toString().length < 6) {
            mainBinding.tilofPassword.error = "At least 6 characters password"
            return false
        }
        if (mainBinding.etPassword.text.toString() == "") {
            mainBinding.tilofemail.error = "This is Required field"
            return false
        }
        if (!mainBinding.etPassword.text.toString()
                .equals(mainBinding.etConfirmPaassword.text.toString())
        ) {
            mainBinding.tilofPassword.error = "Password do not match!"
            return false
        }
        return true
    }
}