package com.example.shopbillinventory

import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.shopbillinventory.Fragments.DashboardFragment
import com.example.shopbillinventory.Fragments.Fragment_BillPaymentHistory
import com.example.shopbillinventory.Fragments.Fragment_Bussiness_info
import com.example.shopbillinventory.databinding.ActivityDashboardBinding
import com.example.shopbillinventory.databinding.FragmentDashboardBinding
import com.google.android.material.snackbar.Snackbar

class DashboardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDashboardBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val includedLayout = findViewById<View>(R.id.includeToolbar)
        val rootView: View = findViewById(android.R.id.content)


        val isRegisteredUser = intent?.getIntExtra("fromRegister", 0)
        val login_email = intent?.getStringExtra("login_email")
        if (isRegisteredUser == 1) {
            val businessInfoFragment = Fragment_Bussiness_info().apply {
                arguments = Bundle().apply {
                    putString("login_email", login_email)
                }
            }
            supportFragmentManager.beginTransaction()
                .replace(R.id.body_container, businessInfoFragment)
                .commit()
            showSnackbar(rootView, "Login Successfully...!", Snackbar.LENGTH_LONG)
            true
        } else {
            val dashboardFragment = DashboardFragment().apply {
                arguments = Bundle().apply {
                    putString("login_email", login_email)
                }
            }
            supportFragmentManager.beginTransaction()
                .replace(R.id.body_container, dashboardFragment)
                .commit()
            val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
            val loggedInBefore = sharedPreferences.getBoolean("loggedInBefore", false)
            if (loggedInBefore){

            }else{
                showSnackbar(rootView, "Login Successfully...!", Snackbar.LENGTH_LONG)
            }


            true
        }


        binding.bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->

            when (menuItem.itemId) {
                R.id.bottom_nav_dashboard -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.body_container, DashboardFragment())
                        .commit()

                    true


                }
                R.id.bottom_nav_bill_history -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.body_container, Fragment_BillPaymentHistory())
                        .commit()

                    true
                }
                R.id.bottom_nav_bussprofile -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.body_container, Fragment_Bussiness_info())
                        .commit()

                    true
                }
                else -> false
            }


        }
    }

    private fun showSnackbar(view: View, message: String, duration: Int) {
        val snackbar = Snackbar.make(view, message, duration)
        val snackbarView = snackbar.view
        snackbarView.setBackgroundColor(Color.DKGRAY) // Set background color
        val textView =
            snackbarView.findViewById(com.google.android.material.R.id.snackbar_text) as TextView
        textView.setTextColor(Color.WHITE) // Set text color
        snackbar.show()
    }
}