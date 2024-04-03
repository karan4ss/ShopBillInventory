package com.example.shopbillinventory

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.shopbillinventory.databinding.ActivityPaymentPlansBinding

class PaymentPlansActivity : AppCompatActivity() {
    private val UPI_PAYMENT_REQUEST_CODE = 123 // Define a request code for the UPI payment
    private lateinit var paymentPlansBinding: ActivityPaymentPlansBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        paymentPlansBinding = ActivityPaymentPlansBinding.inflate(layoutInflater)
        setContentView(paymentPlansBinding.root)

        paymentPlansBinding.cvoneMonth.setOnClickListener {
            var tvmonthAmount = paymentPlansBinding.tvOnemontAmount.text.toString()
            openPhonePeWithAmount(tvmonthAmount)

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
                    Toast.makeText(this, "success", Toast.LENGTH_SHORT).show()
                }
                RESULT_CANCELED -> {
                    // Payment cancelled by user
                    Toast.makeText(this, "payment canceled", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    // Payment failed
                    Toast.makeText(this, "payment failed", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

//    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + "com.phonepe.app"));
//    startActivity(intent);

}