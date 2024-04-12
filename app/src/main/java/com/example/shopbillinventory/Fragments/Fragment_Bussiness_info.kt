package com.example.shopbillinventory.Fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.shopbillinventory.R
import com.example.shopbillinventory.databinding.FragmentBussinessInfoBinding
import com.example.shopbillinventory.databinding.FragmentDashboardBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Fragment_Bussiness_info : Fragment() {
    private lateinit var binding: FragmentBussinessInfoBinding
    private var loginEmail: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentBussinessInfoBinding.inflate(inflater, container, false)
        val view = binding.root
        arguments?.let {
            loginEmail = it.getString("login_email")
        }
        val database = FirebaseDatabase.getInstance()
        val businessRef = database.getReference("bussiness_info").child("1")


        businessRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val businessInfo = dataSnapshot.value as HashMap<String, Any>
                    binding.etBussinessName.setText(businessInfo["name"] as String)
                    binding.etBussinessDescription.setText(businessInfo["description"] as String)
                    binding.etBussinessAddress.setText(businessInfo["address"] as String)
                    binding.etMobileNumber.setText(businessInfo["mobile_number"] as String)
                    binding.etfbId.setText(businessInfo["fb_id"] as String)
                    binding.etinstaId.setText(businessInfo["insta_id"] as String)
                    binding.btnSave.visibility = View.GONE
                    binding.btnUpdate.visibility = View.VISIBLE


                } else {
                    binding.btnSave.visibility = View.VISIBLE
                    binding.btnUpdate.visibility = View.GONE

                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.d("MainActivity", "Error getting data", databaseError.toException())
            }
        })

        binding.btnSave.setOnClickListener {
            val bussiName: String = binding.etBussinessName.text.toString()
            val bussiDescription: String = binding.etBussinessDescription.text.toString()
            val bussiAddress: String = binding.etBussinessAddress.text.toString()
            val bussiMobileno: String = binding.etMobileNumber.text.toString()
            val bussiFbId: String = binding.etfbId.text.toString()
            val bussiInsta: String = binding.etinstaId.text.toString()

            if (!bussiName.equals("") && !bussiAddress.equals("") && !bussiDescription.equals("") && !bussiMobileno.equals(
                    ""
                ) &&
                !bussiFbId.equals("") && !bussiInsta.equals("")
            ) {
                val businessData = hashMapOf(
                    "name" to bussiName,
                    "description" to bussiDescription,
                    "address" to bussiAddress,
                    "mobile_number" to bussiMobileno,
                    "fb_id" to bussiFbId,
                    "insta_id" to bussiInsta
                )

                businessRef.setValue(businessData).addOnSuccessListener {
                    Toast.makeText(context, "Data Saved...!", Toast.LENGTH_SHORT).show()
                    binding.btnUpdate.visibility = View.VISIBLE
                    binding.btnSave.visibility = View.GONE

                    val fragmentdashboard = DashboardFragment()
                    val bundle = Bundle()
                    bundle.putString(
                        "login_email",
                        loginEmail
                    ) // Replace "key" with the actual key and "value" with the value you want to pass
                    fragmentdashboard.arguments = bundle
                    val transaction = requireActivity().supportFragmentManager.beginTransaction()
                    transaction.replace(R.id.body_container, fragmentdashboard)
                    transaction.addToBackStack(null)
                    transaction.commit()

                }.addOnFailureListener { e ->
                    Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show()
                }

            } else {
                Toast.makeText(context, "Please fill all data...", Toast.LENGTH_SHORT).show()
            }


        }

        binding.btnUpdate.setOnClickListener {
            val bussiName: String = binding.etBussinessName.text.toString()
            val bussiDescription: String = binding.etBussinessDescription.text.toString()
            val bussiAddress: String = binding.etBussinessAddress.text.toString()
            val bussiMobileno: String = binding.etMobileNumber.text.toString()
            val bussiFbId: String = binding.etfbId.text.toString()
            val bussiInsta: String = binding.etinstaId.text.toString()

            if (!bussiName.equals("") && !bussiAddress.equals("") && !bussiDescription.equals("") && !bussiMobileno.equals(
                    ""
                ) &&
                !bussiFbId.equals("") && !bussiInsta.equals("")
            ) {
                val businessData = hashMapOf(
                    "name" to bussiName,
                    "description" to bussiDescription,
                    "address" to bussiAddress,
                    "mobile_number" to bussiMobileno,
                    "fb_id" to bussiFbId,
                    "insta_id" to bussiInsta
                )

                businessRef.updateChildren(businessData as Map<String, Any>).addOnSuccessListener {
                    Toast.makeText(context, "Data Updated...!", Toast.LENGTH_SHORT).show()
                    binding.btnUpdate.visibility = View.VISIBLE
                    binding.btnSave.visibility = View.GONE

                    val fragmentdashboard = DashboardFragment()
                    val bundle = Bundle()
                    bundle.putString(
                        "login_email",
                        loginEmail
                    ) // Replace "key" with the actual key and "value" with the value you want to pass
                    fragmentdashboard.arguments = bundle
                    val transaction = requireActivity().supportFragmentManager.beginTransaction()
                    transaction.replace(R.id.body_container, fragmentdashboard)
                    transaction.addToBackStack(null)
                    transaction.commit()
                }.addOnFailureListener { e ->
                    Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show()
                }

            } else {
                Toast.makeText(context, "Please fill all data...", Toast.LENGTH_SHORT).show()
            }
        }
        return view
    }


}