package com.example.shopbillinventory

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.shopbillinventory.Fragments.DashboardFragment
import com.example.shopbillinventory.Fragments.Fragment_BillPaymentHistory
import com.example.shopbillinventory.Fragments.Fragment_Bussiness_info
import com.example.shopbillinventory.databinding.ActivityDashboardBinding
import com.example.shopbillinventory.databinding.FragmentDashboardBinding

class DashboardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDashboardBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportFragmentManager.beginTransaction()
            .replace(R.id.body_container, DashboardFragment())
            .commit()
        true
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
}