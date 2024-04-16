package com.example.shopbillinventory

import android.app.Dialog
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.BillitemDataModel
import com.example.shopbillinventory.Adapters.AdapterGenratedBills
import com.example.shopbillinventory.Adapters.AdapterPerticularBill
import com.example.shopbillinventory.databinding.ActivityShowPerticularBillBinding
import com.google.firebase.database.*
import java.io.ByteArrayOutputStream

class ShowPerticularBillActivity : AppCompatActivity() {
    private lateinit var perticularBillBinding: ActivityShowPerticularBillBinding
    private lateinit var database: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference
    lateinit var dialog: Dialog
    var grand_totoal: String = ""
    lateinit var btnShare: Button
    lateinit var btnCancel: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        perticularBillBinding = ActivityShowPerticularBillBinding.inflate(layoutInflater)

        setContentView(perticularBillBinding.root)
        val bill_id = intent?.getStringExtra("billid")
        grand_totoal = intent?.getStringExtra("grand_totoal").toString()
        database = FirebaseDatabase.getInstance()
        databaseReference = database.reference
        if (bill_id != null || bill_id != "") {
            fetchDataFromFirebase(bill_id.toString())
        }
        perticularBillBinding.btnOpenAndSharetoWhatsapp.setOnClickListener {

            dialog = Dialog(this)
            dialog.setContentView(R.layout.confirm_billshare_dialog_)
            dialog.window?.setLayout(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            val drawable = ContextCompat.getDrawable(this, R.drawable.custome_dialog_bg)
            dialog.window?.setBackgroundDrawable(drawable)
            dialog.setCancelable(false)
            btnShare = dialog.findViewById(R.id.btnShare)
            btnCancel = dialog.findViewById(R.id.btncancel)
            dialog.show()

            btnCancel.setOnClickListener {
                dialog.dismiss()
            }
            btnShare.setOnClickListener {

                //  val phoneNumber = dialog.findViewById<EditText>(R.id.etMobileNumber).text.toString()
                // if (phoneNumber.isNotEmpty()) {
                // openWhatsApp(phoneNumber)
                shareAsImage()
                //   } else {

                //   Toast.makeText(this, "Please enter a WhatsApp number", Toast.LENGTH_SHORT)
                //    .show()
                // }
            }


            // val phoneNumber = perticularBillBinding.tvmobilenumber.text.toString().trim()

        }
    }

    private fun fetchDataFromFirebase(billId: String) {
        val query: Query = databaseReference.child("Confirmed_Billings").child(billId)
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // Check if data exists for the given bill_id
                if (snapshot.exists()) {
                    // Retrieve "Confirmed_Bill_Items" node
                    val confirmedBillItemsNode = snapshot.child("Confirmed_Bill_Items")

                    // Create a list to store items
                    val itemList = mutableListOf<BillitemDataModel>()

                    // Iterate through the children of the "Confirmed_Bill_Items" node
                    confirmedBillItemsNode.children.forEach { itemSnapshot ->
                        // Parse each item and add it to the list
                        val mrp =
                            itemSnapshot.child("mrp").value?.toString()?.toDoubleOrNull() ?: 0.0
                        val name = itemSnapshot.child("name").value?.toString() ?: ""
                        val qty = itemSnapshot.child("qty").value?.toString()?.toIntOrNull() ?: 0
                        val weight =
                            itemSnapshot.child("weight").value?.toString()?.toDoubleOrNull() ?: 0.0
                        val total_amt =
                            itemSnapshot.child("total_mat").value?.toString()?.toDoubleOrNull()
                                ?: 0.0

                        // Create Item object and add it to the list
                        val item = BillitemDataModel(name, mrp, weight, qty.toString(), total_amt)
                        itemList.add(item)
                    }
                    perticularBillBinding.rvofPerticularBill.layoutManager =
                        LinearLayoutManager(this@ShowPerticularBillActivity)
                    perticularBillBinding.rvofPerticularBill.adapter =
                        this@ShowPerticularBillActivity?.let {
                            AdapterPerticularBill(
                                it,
                                itemList
                            )
                        }
                    perticularBillBinding.tvGrandTotatal.setText("Grand Total : " + grand_totoal)
                    perticularBillBinding.tvBillNo.setText("Bill No : " + billId)

                    // Do something with the list of items
                    // For example, display it in a RecyclerView or process it further
                } else {
                    // Handle case where data for the given bill_id doesn't exist
                    Log.d(TAG, "Data for bill $billId does not exist.")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle any errors
                Log.e(TAG, "Error fetching data: ${error.message}")
            }
        })
    }

    private fun shareBitmap(bitmap: Bitmap, phoneNumber: String) {
        val uri = getImageUri(bitmap)
        if (uri != null) {
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "image/*"
            intent.putExtra(Intent.EXTRA_STREAM, uri)

            // Set the WhatsApp number in the message body
            intent.putExtra("jid", phoneNumber + "@s.whatsapp.net")
            intent.putExtra(Intent.EXTRA_TEXT, "Thanks For Visiting...!")

            // Set the package to WhatsApp
            intent.setPackage("com.whatsapp")

            // Verify that the intent resolves to an activity
            if (intent.resolveActivity(packageManager) != null) {
                startActivity(intent)
            } else {
                Toast.makeText(this, "WhatsApp is not installed", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Failed to capture screenshot", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getImageUri(bitmap: Bitmap): Uri? {
        val bytes = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(
            contentResolver,
            bitmap,
            "CardView Image",
            null
        )
        return Uri.parse(path)
    }

    private fun shareAsImage() {
        val bitmap = getBitmapFromView(perticularBillBinding.cvofperticularBill)

        val phoneNumber = dialog.findViewById<EditText>(R.id.etMobileNumber).text.toString()
        if (phoneNumber.isNotEmpty()) {
            // openWhatsApp(phoneNumber)
            if (bitmap != null) {
                shareBitmap(bitmap, phoneNumber)
            } else {
                Toast.makeText(this, "Failed to capture screenshot", Toast.LENGTH_SHORT).show()
            }
        } else {

            Toast.makeText(this, "Please enter a WhatsApp number", Toast.LENGTH_SHORT)
                .show()
        }


    }

    private fun getBitmapFromView(view: CardView): Bitmap? {
        view.isDrawingCacheEnabled = true
        val bitmap = Bitmap.createBitmap(view.drawingCache)
        view.isDrawingCacheEnabled = false
        return bitmap
    }
}