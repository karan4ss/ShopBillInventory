package com.example.shopbillinventory.Fragments

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.example.shopbillinventory.*
import com.example.shopbillinventory.R
import com.example.shopbillinventory.databinding.FragmentDashboardBinding
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.HashMap

class DashboardFragment : Fragment() {
    private lateinit var binding: FragmentDashboardBinding

    private var loginEmail: String? = null
    lateinit var dialog: Dialog
    lateinit var btnCancelRecharge: Button
    lateinit var btnGotoRecharge: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDashboardBinding.inflate(inflater, container, false)
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
                    binding.tvBussinessName.setText(businessInfo["name"] as String)
                    binding.tvBussinessDescription.setText(businessInfo["description"] as String)
                    binding.tvBussinessAddress.setText(businessInfo["address"] as String)
                    binding.tvmobilenumber.setText(businessInfo["mobile_number"] as String)
                    binding.tvfacebookid.setText(businessInfo["fb_id"] as String)
                    binding.tvInstaId.setText(businessInfo["insta_id"] as String)


                } else {


                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.d("MainActivity", "Error getting data", databaseError.toException())
            }
        })


        binding.includeToolbar.tvToolbarTitle.setText("Counter Billing")
        binding.includeToolbar.ivtoolbarBackicon.visibility = View.GONE
        binding.includeToolbar.ivLogout.visibility = View.GONE
        binding.includeToolbar.ivToolbarPlansIcon.setOnClickListener {
            //startActivity(Intent(context, PaymentPlansActivity::class.java))
            val intent = Intent(context, PaymentPlansActivity::class.java)
            intent.putExtra("login_email", loginEmail)
            startActivity(intent)
        }

        binding.cvofBilling.setOnClickListener {
            val sharedPreferences =
                requireContext()?.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

// Retrieve the email address from SharedPreferences
            val email = sharedPreferences?.getString("email", "")

            // retrieveData()
            if(email!=null){
                retrieveDataByEmail(email)
            }

        }
        binding.cvIncome.setOnClickListener {
            val intent = Intent(context, IncomeActivity::class.java)
            startActivity(intent)
        }

        return view
    }

    private fun retrieveData() {
        val database = FirebaseDatabase.getInstance()
        val userRef = database.getReference("plan_validity")


        userRef.addListenerForSingleValueEvent(object : ValueEventListener {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val userData = dataSnapshot.value as HashMap<String, Any>
                    // Process the retrieved data here
                    val email = userData["email"].toString()
                    val endDate = userData["end_date"].toString()
                    val password = userData["password"].toString()
                    val planAmount = userData["plan_amount"].toString()
                    val startDate = userData["start_date"].toString()
                    val userType = userData["userType"].toString()

                    val today = LocalDate.now()
                    val parsedTodayDate = LocalDate.parse(today.toString())
                    val formatter = DateTimeFormatter.ofPattern("d/M/yyyy")
                    val todaydate = parsedTodayDate.format(formatter)

                    val currentDate =
                        SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(todaydate)
                    val endingDate =
                        SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(endDate)
                    val comparisonResult = currentDate.compareTo(endingDate)

                    if (comparisonResult < 0) {
                        // today date is below ending date
                        val intent = Intent(context, ScannerActivity::class.java)
                        startActivity(intent)
                    } else if (comparisonResult == 0) {
                        Toast.makeText(
                            context,
                            "your plan are expired , recharge and gets ",
                            Toast.LENGTH_SHORT
                        ).show()
                        dialog = Dialog(requireContext())
                        dialog.setContentView(R.layout.confirm_dialog_planexpired)
                        dialog.window?.setLayout(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                        )
                        val drawable = ContextCompat.getDrawable(
                            requireContext(),
                            R.drawable.custome_dialog_bg
                        )
                        dialog.window?.setBackgroundDrawable(drawable)
                        dialog.setCancelable(false)
                        btnCancelRecharge = dialog.findViewById(R.id.btncancelRecharge)
                        btnGotoRecharge = dialog.findViewById(R.id.btnGotoRecharge)
                        dialog.show()

                        btnGotoRecharge.setOnClickListener {
                            val intent = Intent(context, PaymentPlansActivity::class.java)
                            startActivity(intent)

                        }
                        btnCancelRecharge.setOnClickListener {
                            dialog.dismiss()
                        }

                    }
                } else {
                    // Data does not exist
                    Toast.makeText(
                        context,
                        "Please Recharge First...!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle errors
            }
        })
    }


    fun retrieveDataByEmail(email: String) {
        val options = FirebaseOptions.Builder()
            .setApplicationId("com.example.shopbillinventory")
            .setApiKey("AIzaSyAB53hxifXMHW8Aq0Hhh-26E_FT86eRF7Q")
            .setDatabaseUrl("https://shopadminpanel-default-rtdb.firebaseio.com")
            // Add any other necessary configurations
            .build()


        val existingApp =
            FirebaseApp.getApps(requireContext()).find { it.name == "Shopbillinventory" }

        if (existingApp == null) {
            // FirebaseApp with the name "Shopbillinventory" doesn't exist, so initialize it
            FirebaseApp.initializeApp(requireContext(), options, "Shopbillinventory")
        }
        val database = FirebaseDatabase.getInstance(FirebaseApp.getInstance("Shopbillinventory"))
        val reference: DatabaseReference = database.reference
        val query: Query = reference.child("plan_validity").orderByChild("email").equalTo(email)

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (snapshot in dataSnapshot.children) {

                        val getemail = snapshot.child("email").getValue(String::class.java)
                        val encodeEmailBack =encodeEmailAsFirebaseKeyBack(getemail.toString()!!)
                        val end_date = snapshot.child("end_date").getValue(String::class.java)
                        val planAmount = snapshot.child("planAmount").getValue(String::class.java)
                        val start_date = snapshot.child("start_date").getValue(String::class.java)
                        val validDays = snapshot.child("validdays").getValue(String::class.java)


                        val today = LocalDate.now()
                        val parsedTodayDate = LocalDate.parse(today.toString())
                        val formatter = DateTimeFormatter.ofPattern("d/M/yyyy")
                        val todaydate = parsedTodayDate.format(formatter)

                        val currentDate =
                            SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(todaydate)
                        val endingDate =
                            SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(end_date)
                        val comparisonResult = currentDate.compareTo(endingDate)

                        if (comparisonResult < 0) {
                            // today date is below ending date
                            val intent = Intent(context, ScannerActivity::class.java)
                            startActivity(intent)
                        } else if (comparisonResult == 0) {
                            Toast.makeText(
                                context,
                                "your plan are expired , recharge and gets ",
                                Toast.LENGTH_SHORT
                            ).show()
                            dialog = Dialog(requireContext())
                            dialog.setContentView(R.layout.confirm_dialog_planexpired)
                            dialog.window?.setLayout(
                                ViewGroup.LayoutParams.WRAP_CONTENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT
                            )
                            val drawable = ContextCompat.getDrawable(
                                requireContext(),
                                R.drawable.custome_dialog_bg
                            )
                            dialog.window?.setBackgroundDrawable(drawable)
                            dialog.setCancelable(false)
                            btnCancelRecharge = dialog.findViewById(R.id.btncancelRecharge)
                            btnGotoRecharge = dialog.findViewById(R.id.btnGotoRecharge)
                            dialog.show()

                            btnGotoRecharge.setOnClickListener {
                                val intent = Intent(context, PaymentPlansActivity::class.java)
                                startActivity(intent)

                            }
                            btnCancelRecharge.setOnClickListener {
                                dialog.dismiss()
                            }

                        } else {
                            // Data does not exist
                            Toast.makeText(
                                context,
                                "Please Recharge First...!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }


                    }
                } else {
                    println("No data found for the provided email.")
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                println("Data retrieval cancelled. Error: ${databaseError.message}")
            }
        })
    }
    fun encodeEmailAsFirebaseKeyBack(email: String): String {
        return email.replace(",", ".")
    }
}