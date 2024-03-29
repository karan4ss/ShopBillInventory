package com.example.shopbillinventory

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.shopbillinventory.databinding.ActivityBillingDataBinding

class BillingDataActivity : AppCompatActivity() {
    private lateinit var billingDataBinding: ActivityBillingDataBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        billingDataBinding = ActivityBillingDataBinding.inflate(layoutInflater)
        setContentView(billingDataBinding.root)
        billingDataBinding.btnScanner.setOnClickListener {
            val intent = Intent(this, ScannerActivity::class.java)
            startActivity(intent)
        }


    }
}