package com.example.shopbillinventory

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.example.shopbillinventory.databinding.ActivityMainBinding
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var mainBinding: ActivityMainBinding
    private lateinit var databaseReference: DatabaseReference
    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)
        FirebaseApp.initializeApp(this)
        auth = FirebaseAuth.getInstance()

        // Initialize Firebase database reference
        databaseReference = FirebaseDatabase.getInstance().reference
        databaseReference.child("Owner_register_count").setValue(0)

        mainBinding.btnSignUp.setOnClickListener {
            var longCount: Long = 0


          //  if (mainBinding.rbOwner.isChecked) {
                // Check if 'user_register_count' node exists
                databaseReference.child("Owner_register_count")
                    .addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            if (dataSnapshot.exists()) {

                                var stringCount = dataSnapshot.getValue().toString()
                                longCount = stringCount.toLong() + 1


                                Toast.makeText(
                                    applicationContext,
                                    longCount.toString(),
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                            } else {
                                databaseReference.child("Owner_register_count").setValue(0)
                                longCount = 1

                            }
                            signupWithEmail(longCount)
                        }

                        override fun onCancelled(databaseError: DatabaseError) {
                            // Handle error
                        }
                    })
    //        }
    /*else if (mainBinding.rbEmployee.isChecked) {
                databaseReference.child("Employee_register_count")
                    .addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            if (dataSnapshot.exists()) {
                                // var longCount: Long = 0
                                var stringCount = dataSnapshot.getValue().toString()
                                longCount = stringCount.toLong() + 1

                            } else {
                                databaseReference.child("Employee_register_count")
                                    .setValue(0)
                                longCount = 1
                            }
                            signupWithEmail(longCount)
                        }

                        override fun onCancelled(databaseError: DatabaseError) {
                            // Handle error
                        }
                    })
            }*/


        }
        mainBinding.tvalredyhaveanaccount.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
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

    fun saveUserToDatabase(userid: Long, email: String, password: String, userType: String) {
        val database = FirebaseDatabase.getInstance()
        val usersRef = database.getReference("registered_users").child(userType)

        // Create a HashMap to store user data
        val userData = hashMapOf(
            "userid" to userid,
            "email" to email,
            "password" to password, // Note: It's generally not recommended to store passwords in plaintext
            "userType" to userType
        )

        // Save user data to the database under the user's unique ID
        usersRef.child(userid.toString()).setValue(userData).addOnSuccessListener {
            // Data saved successfully
            Toast.makeText(this, "User Data Saved...!", Toast.LENGTH_SHORT).show()
            if (userType.equals("Owner")) {
                databaseReference.child("Owner_register_count").setValue(userid)
            } else {
                databaseReference.child("Employee_register_count").setValue(userid)
            }

        }.addOnFailureListener { e ->
            // Handle error
        }
    }

    private fun signupWithEmail(longCount: Long) {
        val email = mainBinding.etEmail.text.toString()
        val password = mainBinding.etPassword.text.toString()
        var userType: String = "Owner"
        Toast.makeText(
            this, longCount.toString(), Toast.LENGTH_SHORT
        ).show()
        if (checkAllFields()) {

            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                if (it.isSuccessful) {
                    saveUserToDatabase(longCount, email, password, userType)
                    auth.signOut()
                    Toast.makeText(
                        this, "Account Registered Successfully...!", Toast.LENGTH_SHORT
                    ).show()
                    val intent = Intent(this, LoginActivity::class.java)
                    intent.putExtra("fromRegister", 1)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(
                        this, it.exception.toString(), Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
        }
    }
