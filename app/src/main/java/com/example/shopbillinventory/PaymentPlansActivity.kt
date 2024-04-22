package com.example.shopbillinventory

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shopbillinventory.Adapters.AdapterItemsOfPlansInfo
import com.example.shopbillinventory.Adapters.AdapterItemsofBill
import com.example.shopbillinventory.databinding.ActivityPaymentPlansBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.database.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class PaymentPlansActivity : AppCompatActivity() {
    private val UPI_PAYMENT_REQUEST_CODE = 123 // Define a request code for the UPI payment
    private lateinit var paymentPlansBinding: ActivityPaymentPlansBinding
    var planamount: Int = 0
    lateinit var futureDate: String
    var userDataData = hashMapOf<String, Any>()
    private var loginEmail: String? = null
    lateinit var rootView: View
    lateinit var today: LocalDate

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        paymentPlansBinding = ActivityPaymentPlansBinding.inflate(layoutInflater)
        setContentView(paymentPlansBinding.root)
        loginEmail = intent?.getStringExtra("login_email")
        today = LocalDate.now()
        rootView = findViewById(android.R.id.content)

        val options = FirebaseOptions.Builder()
            .setApplicationId("com.example.shopbillinventory")
            .setApiKey("AIzaSyAB53hxifXMHW8Aq0Hhh-26E_FT86eRF7Q")
            .setDatabaseUrl("https://shopadminpanel-default-rtdb.firebaseio.com")
            // Add any other necessary configurations
            .build()

        //FirebaseApp.initializeApp(this, options, "Shopbillinventory")
        val existingApp = FirebaseApp.getApps(this).find { it.name == "Shopbillinventory" }

        if (existingApp == null) {
            // FirebaseApp with the name "Shopbillinventory" doesn't exist, so initialize it
            FirebaseApp.initializeApp(this, options, "Shopbillinventory")
        }
        val database = FirebaseDatabase.getInstance(FirebaseApp.getInstance("Shopbillinventory"))
        val planRef = database.getReference("plan_info")

        val planList = mutableListOf<PlanModel>()

        planRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (postSnapshot in snapshot.children) {
                    val id = postSnapshot.key ?: ""
                    val duration =
                        postSnapshot.child("plan-duration").getValue(String::class.java) ?: ""
                    val amount = postSnapshot.child("plan_amt").getValue(String::class.java) ?: ""
                    val description =
                        postSnapshot.child("plan_desc").getValue(String::class.java) ?: ""
                    val plan = PlanModel(id, duration, amount, description)
                    planList.add(plan)
                }
                // Call a function to display the list (step 5)
                paymentPlansBinding.rvPlansInfo.layoutManager =
                    LinearLayoutManager(this@PaymentPlansActivity)
                paymentPlansBinding.rvPlansInfo.adapter = AdapterItemsOfPlansInfo(
                    this@PaymentPlansActivity, planList
                )
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle database error
            }
        })


    }

    private fun openPhonePeWithAmount(amount: Int) {
        val packageName = "com.phonepe.app" // Package name of PhonePe app

        // Check if PhonePe app is installed
        val packageManager = applicationContext.packageManager
        val isPhonePeInstalled = try {
            packageManager.getPackageInfo(packageName, 0)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }

        if (isPhonePeInstalled) {
            // Construct intent
            val uri =
                Uri.parse("upi://pay?pa=9373351996@axl&pn=KSOFTTech&am=$amount&cu=INR&tn=Purpose%20of%20Payment")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            intent.setPackage(packageName)

            // Check if there's an activity available to handle the intent
            if (intent.resolveActivity(packageManager) != null) {
                // Start the activity for result to get status after payment
                startActivityForResult(intent, UPI_PAYMENT_REQUEST_CODE)
            } else {
                Toast.makeText(applicationContext, "Unable to open PhonePe app", Toast.LENGTH_SHORT)
                    .show()
            }
        } else {
            // PhonePe app is not installed, handle accordingly
            Toast.makeText(applicationContext, "PhonePe app is not installed", Toast.LENGTH_SHORT)
                .show()
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == UPI_PAYMENT_REQUEST_CODE) {
            when (resultCode) {
                RESULT_OK -> {
                    // Payment successful, handle accordingly
                    // You may need to parse the data intent to get additional information
                    // For example, data?.getStringExtra("response") may contain UPI transaction details
                    // Toast.makeText(this, "success", Toast.LENGTH_SHORT).show()
                    val database = FirebaseDatabase.getInstance()
                    val ref = database.getReference("plan_validity")
                    ref.setValue(userDataData).addOnSuccessListener {

                        showSnackbar(rootView, "Login Successfully...!", Snackbar.LENGTH_LONG)
                    }.addOnFailureListener { e ->
                        Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show()
                    }


                }
                RESULT_CANCELED -> {
                    // Payment cancelled by user
                    // Toast.makeText(this, "payment canceled", Toast.LENGTH_SHORT).show()
//                    showSnackbar(rootView, "Payment Cancel", Snackbar.LENGTH_LONG)
//                    val database = FirebaseDatabase.getInstance()
//                    val ref = database.getReference("plan_validity")
//                    ref.setValue(userDataData).addOnSuccessListener {
//
//                        showSnackbar(rootView, "Login Successfully...!", Snackbar.LENGTH_LONG)
//                    }.addOnFailureListener { e ->
//                        Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show()
//                    }
                }
                else -> {
                    // Payment failed
                    //Toast.makeText(this, "payment failed", Toast.LENGTH_SHORT).show()
                    showSnackbar(rootView, "Payment Failed", Snackbar.LENGTH_LONG)
                }
            }
        }
    }

//    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + "com.phonepe.app"));
//    startActivity(intent);

    private fun showSnackbar(view: View, message: String, duration: Int) {
        val snackbar = Snackbar.make(view, message, duration)
        val snackbarView = snackbar.view
        snackbarView.setBackgroundColor(Color.DKGRAY) // Set background color
        val textView =
            snackbarView.findViewById(com.google.android.material.R.id.snackbar_text) as TextView
        textView.setTextColor(Color.WHITE) // Set text color
        snackbar.show()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getUserdata(futureDatePassed: String, planAmountPassed: Int) {

        val parsedFutureDate = LocalDate.parse(futureDatePassed)
        val parsedTodayDate = LocalDate.parse(today.toString())
        val formatter = DateTimeFormatter.ofPattern("d/M/yyyy")
        val futureDateFormatted = parsedFutureDate.format(formatter)
        val todaydate = parsedTodayDate.format(formatter)
        val sharedPreferences = this?.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

// Retrieve the email address from SharedPreferences
        val email = sharedPreferences?.getString("email", "")
        val database = FirebaseDatabase.getInstance()
        val ref = database.getReference("registered_users")
        val query: Query = ref.child(
            "\n" +
                    "plan_validity"
        ).orderByChild("email").equalTo(email)
        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (snapshot in snapshot.children) {
                        val email = snapshot.child("email").getValue(String::class.java)
                        val encodedEmail = encodeEmailAsFirebaseKeyBack(email!!)
                        val end_date = snapshot.child("end_date").getValue(String::class.java)
                        val planAmount = snapshot.child("userType").getValue(String::class.java)
                        val start_date = snapshot.child("start_date").getValue(String::class.java)
                        val validdays = snapshot.child("validdays").getValue(String::class.java)


                        userDataData.apply {
                            put("email", encodedEmail!!)
                            put("end_date", end_date!!)
                            put("planAmount", planAmount!!)
                            put("start_date", start_date!!)
                            put("validdays", validdays!!)
                        }
                        //openPhonePeWithAmount(planAmountPassed)



                    }


                }

            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

    }

    fun encodeEmailAsFirebaseKeyBack(email: String): String {
        return email.replace(",", ".")
    }

}