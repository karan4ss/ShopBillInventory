package com.example.shopbillinventory

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.shopbillinventory.databinding.ActivityPaymentPlansBinding

class PaymentPlansActivity : AppCompatActivity() {
    private lateinit var paymentPlansBinding: ActivityPaymentPlansBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        paymentPlansBinding = ActivityPaymentPlansBinding.inflate(layoutInflater)
        setContentView(paymentPlansBinding.root)

    }
}