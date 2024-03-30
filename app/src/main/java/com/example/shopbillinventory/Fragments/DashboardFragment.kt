package com.example.shopbillinventory.Fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.shopbillinventory.BillingDataActivity
import com.example.shopbillinventory.PaymentPlansActivity
import com.example.shopbillinventory.R
import com.example.shopbillinventory.ScannerActivity
import com.example.shopbillinventory.databinding.FragmentDashboardBinding

class DashboardFragment : Fragment() {
    private lateinit var binding: FragmentDashboardBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.includeToolbar.tvToolbarTitle.setText("Counter Billing")
        binding.includeToolbar.ivtoolbarBackicon.visibility = View.GONE
        binding.includeToolbar.ivToolbarPlansIcon.setOnClickListener {
            startActivity(Intent(context, PaymentPlansActivity::class.java))
        }

        binding.cvofBilling.setOnClickListener {
            val intent = Intent(context, ScannerActivity::class.java)
            startActivity(intent)
        }

        return view
    }

}