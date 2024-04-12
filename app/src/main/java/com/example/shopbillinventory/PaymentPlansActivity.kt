package com.example.shopbillinventory

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.shopbillinventory.databinding.ActivityPaymentPlansBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class PaymentPlansActivity : AppCompatActivity() {
    private val UPI_PAYMENT_REQUEST_CODE = 123 // Define a request code for the UPI payment
    private lateinit var paymentPlansBinding: ActivityPaymentPlansBinding
    lateinit var planamount: String
    lateinit var futureDate: String
    var userDataData = hashMapOf<String, Any>()
    private var loginEmail: String? = null
    lateinit var rootView: View

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        paymentPlansBinding = ActivityPaymentPlansBinding.inflate(layoutInflater)
        setContentView(paymentPlansBinding.root)
        loginEmail = intent?.getStringExtra("login_email")
        val today = LocalDate.now()
        rootView = findViewById(android.R.id.content)
        paymentPlansBinding.cvoneMonth.setOnClickListener {
            planamount = paymentPlansBinding.tvOnemontAmount.text.toString()
            futureDate = today.plusDays(28).toString()
            val parsedFutureDate = LocalDate.parse(futureDate)
            val parsedTodayDate = LocalDate.parse(today.toString())
            val formatter = DateTimeFormatter.ofPattern("d/M/yyyy")
            val futureDateFormatted = parsedFutureDate.format(formatter)
            val todaydate = parsedTodayDate.format(formatter)
            // Toast.makeText(this, futureDateFormatted, Toast.LENGTH_SHORT).show()

            val sharedPreferences = this?.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

// Retrieve the email address from SharedPreferences
            val email = sharedPreferences?.getString("email", "")
            val database = FirebaseDatabase.getInstance()
            val ref = database.getReference("registered_users")
            val query: Query =
                ref.child("Owner").orderByChild("email").equalTo(email)
            query.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        for (snapshot in snapshot.children) {
                            val email = snapshot.child("email").getValue(String::class.java)
                            val password = snapshot.child("password").getValue(String::class.java)
                            val userType = snapshot.child("userType").getValue(String::class.java)
                            val userId = snapshot.child("userid").getValue(Long::class.java)

                            // Do whatever you want with the retrieved data
                            // For example, you can log it
                            println("Email: $email")
                            println("password: $password")
                            println("userType: $userType")
                            println("Userid: $userId")


                            userDataData.apply {
                                put("email", email!!)
                                put("password", password!!)
                                put("userType", userType!!)
                                put("userid", userId!!)
                                put("plan_amount", planamount!!)
                                put("start_date", todaydate!!)
                                put("end_date", futureDateFormatted!!)
                            }
                            openPhonePeWithAmount(planamount)
                        }


                    }

                }

                override fun onCancelled(error: DatabaseError) {

                }

            })


        }

        paymentPlansBinding.cvThreeMonth.setOnClickListener {
            planamount = paymentPlansBinding.tvThreeMonthAmount.text.toString()
            futureDate = today.plusDays(84).toString()
            val parsedFutureDate = LocalDate.parse(futureDate)
            val formatter = DateTimeFormatter.ofPattern("d/M/yyyy")
            val futureDateFormatted = parsedFutureDate.format(formatter)
            openPhonePeWithAmount(planamount)

        }
        paymentPlansBinding.cvOneYear.setOnClickListener {
            planamount = paymentPlansBinding.tvOneYearAmount.text.toString()
            futureDate = today.plusDays(364).toString()
            val parsedFutureDate = LocalDate.parse(futureDate)
            val formatter = DateTimeFormatter.ofPattern("d/M/yyyy")
            val futureDateFormatted = parsedFutureDate.format(formatter)
            openPhonePeWithAmount(planamount)

        }

    }

    /* private fun openPhonePeWithAmount(amount: String) {
         // Construct the URI for the payment deep link
        // val uri = Uri.parse("upi://mandate?mn=Autopay&ver=01&rev=Y&purpose=14&validityend=02042034&QRts=2024-04-04T00:46:35.906802372+05:30&QRexpire=2024-04-04T00:51:35.906802372+05:30&txnType=CREATE&am=7.00&validitystart=04042024&mode=04&pa=TAMASHATVONLINE@ybl&cu=INR&amrule=MAX&mc=7993&qrMedium=00&recur=DAILY&mg=ONLINE&share=Y&block=N&tr=TX3348889977&pn=TamashaTV&orgid=180001&sign=MEUCIGmgX6sbRz2MX+/Khv/mJsX2X4pV87EaqLTGTZ5h5404AiEAijy3AFWryyFC7ssSXvKzbf4O47nS6lMowXeem4QKJps=")
         val uri = Uri.parse("upi://pay?pa=8421888138@ybl&pn=TamashaTV&am=$amount&cu=INR&tn=Purpose%20of%20Payment")


         // Create an Intent with ACTION_VIEW and the constructed URI
         val intent = Intent(Intent.ACTION_VIEW, uri)

         // Verify if there's an app available to handle this intent
         if (intent.resolveActivity(packageManager) != null) {
             // Start the activity to open the UPI app and initiate payment
             startActivity(intent)
         } else {
             // Handle the case where no app is available to handle this intent
         }


     }*/


    private fun openPhonePeWithAmount(amount: String) {
        val uri =
            Uri.parse("upi://pay?pa=8421888138@ybl&pn=TamashaTV&am=$amount&cu=INR&tn=Purpose%20of%20Payment")

        val intent = Intent(Intent.ACTION_VIEW, uri)

        // Start the activity for result to get status after payment
        startActivityForResult(intent, UPI_PAYMENT_REQUEST_CODE)
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
                    showSnackbar(rootView, "Payment Cancel", Snackbar.LENGTH_LONG)
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

}