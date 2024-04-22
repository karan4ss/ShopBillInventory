package com.example.shopbillinventory

import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import com.budiyev.android.codescanner.ScanMode
import com.dantsu.escposprinter.EscPosPrinter
import com.dantsu.escposprinter.connection.bluetooth.BluetoothPrintersConnections
import com.example.BillitemDataModel
import com.example.shopbillinventory.Adapters.AdapterItemsofBill
import com.example.shopbillinventory.databinding.ActivityScannerBinding
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.regex.Pattern

class ScannerActivity : AppCompatActivity() {
    private lateinit var scannerBinding: ActivityScannerBinding
    private lateinit var codeScanner: CodeScanner
    private val CAMERA_REQUEST_CODE = 101;
    val myStringList = mutableListOf<String>()
    val billItemListGlobal: MutableList<BillitemDataModel> = mutableListOf()
    var global_grand_amount: String = ""
    var billingCount: Long = 0
    private val REQUEST_BLUETOOTH_PERMISSION = 1001
    private val REQUEST_STORAGE_PERMISSION = 1002

    lateinit var today: LocalDate
    private lateinit var databaseReference: DatabaseReference
    var flagforonlysingleclickinprintbtn = false

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        scannerBinding = ActivityScannerBinding.inflate(layoutInflater)
        setContentView(scannerBinding.root)
        // Initialize Firebase database reference
        databaseReference = FirebaseDatabase.getInstance().reference
        setUpPermissions()


        ///////////////////k
        /* val today = LocalDate.now()
         //val formatter = DateTimeFormatter.ofPattern("d/M/yyyy")
         val formatter = DateTimeFormatter.ofPattern("d-M-yyyy")
         val todayDate = today.format(formatter)
         val currentMonth = SimpleDateFormat("MMMM yyyy", Locale.getDefault()).format(Date())

         val dbReferenceForTodaysBusiness =
             databaseReference.child("Todays_Business_Amount").child(currentMonth).child(todayDate)

         dbReferenceForTodaysBusiness.addListenerForSingleValueEvent(object : ValueEventListener {
             override fun onDataChange(snapshot: DataSnapshot) {
                 if (snapshot.exists()) {
                     // Node already exists for today's date in the current month
                     // You can handle this case accordingly
                     Toast.makeText(applicationContext, "Already exists", Toast.LENGTH_SHORT).show()
                 } else {
                     // Node doesn't exist for today's date in the current month
                     // Create the node and set its value
                     dbReferenceForTodaysBusiness.setValue(0).addOnSuccessListener {
                         // Node creation and value setting successful
                     }.addOnFailureListener {
                         // Node creation or value setting failed
                     }
                 }
             }

             override fun onCancelled(error: DatabaseError) {
                 // Handle onCancelled event
             }
         })*/
        val today = LocalDate.now()
        //val formatter = DateTimeFormatter.ofPattern("d/M/yyyy")
        val formatter = DateTimeFormatter.ofPattern("d-M-yyyy")
        val todayDate = today.format(formatter)
        val currentMonth = SimpleDateFormat("MMMM yyyy", Locale.getDefault()).format(Date())

        val dbReferenceForTodaysBusiness =
            databaseReference.child("Todays_Business_Amount").child(currentMonth).child("Cash")
                .child(todayDate)

        val dbReferenceForTodaysBusiness1 =
            databaseReference.child("Todays_Business_Amount").child(currentMonth).child("Online")
                .child(todayDate)

        dbReferenceForTodaysBusiness.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    // Node already exists for today's date in the current month
                    // You can handle this case accordingly
                    Toast.makeText(applicationContext, "Already exists", Toast.LENGTH_SHORT).show()
                } else {
                    // Node doesn't exist for today's date in the current month
                    // Create the node and set its value
                    dbReferenceForTodaysBusiness.setValue(0).addOnSuccessListener {
                        // Node creation and value setting successful
                    }.addOnFailureListener {
                        // Node creation or value setting failed
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle onCancelled event
            }
        })

        dbReferenceForTodaysBusiness1.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    // Node already exists for today's date in the current month
                    // You can handle this case accordingly
                    Toast.makeText(applicationContext, "Already exists", Toast.LENGTH_SHORT).show()
                } else {
                    // Node doesn't exist for today's date in the current month
                    // Create the node and set its value
                    dbReferenceForTodaysBusiness1.setValue(0).addOnSuccessListener {
                        // Node creation and value setting successful
                    }.addOnFailureListener {
                        // Node creation or value setting failed
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle onCancelled event
            }
        })


        //////////////////

        codeScanner = CodeScanner(this, scannerBinding.ScannerView)

        // Parameters (default values)
        codeScanner.camera = CodeScanner.CAMERA_BACK // or CAMERA_FRONT or specific camera id
        codeScanner.formats = CodeScanner.ALL_FORMATS // list of type BarcodeFormat,
        // ex. listOf(BarcodeFormat.QR_CODE)
        codeScanner.autoFocusMode = AutoFocusMode.SAFE // or CONTINUOUS
        codeScanner.scanMode = ScanMode.SINGLE // or CONTINUOUS or PREVIEW
        codeScanner.isAutoFocusEnabled = true // Whether to enable auto focus or not
        codeScanner.isFlashEnabled = false // Whether to enable flash or not


        databaseReference.child("Billing_Count")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        var stringCount = snapshot.getValue().toString()
                        billingCount = stringCount.toLong() + 1

                    } else {
                        databaseReference.child("Billing_Count").setValue(0)
                        billingCount = 0;
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })

        // Callbacks
        codeScanner.decodeCallback = DecodeCallback {
            runOnUiThread {
                var et_qty: String = "0"
                Toast.makeText(this, "Scan result: ${it.text}", Toast.LENGTH_LONG).show()
                val scannedText: String = it.text.toString()
                val billItemList: MutableList<BillitemDataModel> = mutableListOf()
                myStringList.add(scannedText)
                val builder = AlertDialog.Builder(this)
                val inflater = layoutInflater
                val dailog = inflater.inflate(R.layout.qty_edittext_layout_dialog, null)
                val editText = dailog.findViewById<EditText>(R.id.etQty)
                // Create an empty list to store instances of BillitemDataModel

                with(builder) {
                    setTitle("Product Qty")
                    setCancelable(false)
                    setPositiveButton("Enter") { dialogLayout, which ->
                        billItemList.clear()
                        et_qty = editText.text.toString();


                        //val namePattern = Pattern.compile("^[a-zA-Z\\s]+")
                        val mrpPattern = Pattern.compile("mrp\\s*=\\s*(\\d+(?:\\.\\d+)?)")
                        val weightPattern =
                            Pattern.compile("weight\\s*=\\s*(\\d+(?:\\.\\d+)?)\\s*gm")
                        val qtyPattern = Pattern.compile("qty\\s*=\\s*(\\d+)")


                        // Iterate over myStringList to parse each string and create BillitemDataModel instances
                        for (scannedText in myStringList) {
                            // Initialize variables to hold parsed data
                            var name = ""
                            var mrp = 0.0
                            var weight = 0.0
                            var qty = ""

                            // Find name
                            val scannedNewText = scannedText.trim()
                            val lines = scannedNewText.split("\n") // Split the text into lines
                            val trimmedText = lines.first()
                                .trim() // Take the first line and trim any leading or trailing whitespace
                            if (trimmedText.isNotEmpty()) {
                                // Scanned text is not empty
                                // Proceed with further processing
                                name = trimmedText // Example: Assign scanned text to name
                            } else {
                                // Scanned text is empty
                                println("Scanned text is empty")
                            }

                            // Find mrp
                            val mrpMatcher = mrpPattern.matcher(scannedText)
                            if (mrpMatcher.find()) {
                                mrp = mrpMatcher.group(1).toDouble()
                            }

                            // Find weight
                            val weightMatcher = weightPattern.matcher(scannedText)
                            if (weightMatcher.find()) {
                                weight = weightMatcher.group(1).toDouble()
                            }

                            // Find qty
                            val qtyMatcher = qtyPattern.matcher(scannedText)
                            if (qtyMatcher.find()) {
                                et_qty = qtyMatcher.group(1)
                            }
                            //
                            val total_amount = mrp.toDouble() * et_qty.toDouble();
                            global_grand_amount = global_grand_amount + total_amount

                            val billItem = BillitemDataModel(
                                name, mrp, weight, et_qty, total_amount.toDouble()
                            )
                            billItemList.add(billItem)
                            billItemListGlobal.addAll(billItemList)

                            //
                            val reference1 =
                                databaseReference.child("Billings").child(billingCount.toString())
                            databaseReference.child("Billing_Count")
                                .setValue(billingCount.toString())
                            for ((index, item) in billItemListGlobal.withIndex()) {
                                reference1.child("Bill_Items").child(index.toString())
                                    .setValue(item)
                            }
                            // Calculate and save grandTotal
                            val grandTotal =
                                billItemListGlobal.sumByDouble { it.total_mat.toDouble() }
                            reference1.child("grandTotal").setValue(grandTotal)


                            val reference =
                                databaseReference.child("Billings").child(billingCount.toString())
                            reference.addValueEventListener(object : ValueEventListener {
                                override fun onDataChange(dataSnapshot: DataSnapshot) {
                                    val billItems = mutableListOf<BillitemDataModel>()
                                    val billItemsSnapshot = dataSnapshot.child("Bill_Items")

                                    for (childSnapshot in billItemsSnapshot.children) {
                                        val billItem =
                                            childSnapshot.getValue(BillitemDataModel::class.java)
                                        billItem?.let { billItems.add(it) }
                                    }

                                    // Calculate and set grand total
                                    val grandTotal = dataSnapshot.child("grandTotal")
                                        .getValue(Double::class.java)

                                    scannerBinding.tvGrandTotalAmt.setText(grandTotal.toString())
                                    // Update RecyclerView with bill items
                                    scannerBinding.rvItemAddedtoBill.layoutManager =
                                        LinearLayoutManager(this@ScannerActivity)
                                    scannerBinding.rvItemAddedtoBill.adapter = AdapterItemsofBill(
                                        this@ScannerActivity, billItems
                                    )

                                    billItemList.clear()
                                    myStringList.clear()

                                    // Example: textViewGrandTotal.text = grandTotal.toString()
                                }

                                override fun onCancelled(databaseError: DatabaseError) {
                                    // Handle error
                                }
                            })


                        }


                    }
                    setNegativeButton("Cancel") { dialogLayout, which ->

                    }
                    setView(dailog)
                    show()
                }

                //


                //


                //


                //


            }
        }
        codeScanner.errorCallback = ErrorCallback { // or ErrorCallback.SUPPRESS
            runOnUiThread {
                Toast.makeText(
                    this, "Camera initialization error: ${it.message}", Toast.LENGTH_LONG
                ).show()
            }
        }

        scannerBinding.ScannerView.setOnClickListener {
            codeScanner.startPreview()
        }

        scannerBinding.btnPrintBill.setOnClickListener {

            if (flagforonlysingleclickinprintbtn) {
                Toast.makeText(this@ScannerActivity, "Alredy Clicked", Toast.LENGTH_SHORT).show()
            }else{
                val reference = databaseReference.child("Billings").child(billingCount.toString())
                reference.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        val billItems = mutableListOf<BillitemDataModel>()
                        val billItemsSnapshot = dataSnapshot.child("Bill_Items")

                        for (childSnapshot in billItemsSnapshot.children) {
                            val billItem = childSnapshot.getValue(BillitemDataModel::class.java)
                            billItem?.let { billItems.add(it) }
                        }

                        // Calculate and set grand total
                        val grandTotal = dataSnapshot.child("grandTotal").getValue(Double::class.java)

                        scannerBinding.tvGrandTotalAmt.setText(grandTotal.toString())


                        checkStoragePermissions(billItems)


                        //////////////////k
                        val reference1 =
                            databaseReference.child("Confirmed_Billings").child(billingCount.toString())
                        databaseReference.child("Confirmed_Billing_Count")
                            .setValue(billingCount.toString())
                        for ((index, item) in billItemListGlobal.withIndex()) {
                            reference1.child("Confirmed_Bill_Items").child(index.toString())
                                .setValue(item)
                        }
                        // Calculate and save grandTotal
                        val grandTotaltosave =
                            billItemListGlobal.sumByDouble { it.total_mat.toDouble() }
                        reference1.child("Confirmed_grandTotal").setValue(grandTotaltosave)


                        var paymentMode: String

                        if (scannerBinding.rbtnCash.isChecked) {
                            paymentMode = "Cash"

                            dbReferenceForTodaysBusiness.addListenerForSingleValueEvent(object :
                                ValueEventListener {
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    if (snapshot.exists()) {
                                        val businessAmount = snapshot.getValue(Int::class.java)
                                        val todyasIncome: Long
                                        todyasIncome = (businessAmount!! + grandTotaltosave).toLong()
                                        dbReferenceForTodaysBusiness.setValue(todyasIncome)

                                        // Handle the retrieved business amount
                                        Toast.makeText(
                                            applicationContext,
                                            "Business amount: $businessAmount",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        reference1.child("Payment_Mode").setValue(paymentMode)
                                        flagforonlysingleclickinprintbtn = true
                                    } else {
                                        // Node does not exist for the specified date
                                        Toast.makeText(
                                            applicationContext,
                                            "No data available for today",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }

                                override fun onCancelled(error: DatabaseError) {
                                    // Handle onCancelled event
                                }
                            })
                        } else if (scannerBinding.rbtnOnline.isChecked) {
                            paymentMode = "Online"
                            dbReferenceForTodaysBusiness1.addListenerForSingleValueEvent(object :
                                ValueEventListener {
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    if (snapshot.exists()) {
                                        val businessAmount = snapshot.getValue(Int::class.java)
                                        val todyasIncome: Long
                                        todyasIncome = (businessAmount!! + grandTotaltosave).toLong()
                                        dbReferenceForTodaysBusiness1.setValue(todyasIncome)

                                        reference1.child("Payment_Mode").setValue(paymentMode)
                                        flagforonlysingleclickinprintbtn = true
                                        // Handle the retrieved business amount
                                        Toast.makeText(
                                            applicationContext,
                                            "Business amount: $businessAmount",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    } else {
                                        // Node does not exist for the specified date
                                        Toast.makeText(
                                            applicationContext,
                                            "No data available for today",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }

                                override fun onCancelled(error: DatabaseError) {
                                    // Handle onCancelled event
                                }
                            })
                        } else {

                        }


                        //////////////////

                        // Example: textViewGrandTotal.text = grandTotal.toString()
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        // Handle error
                    }
                })
            }



        }
    }

    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()
    }

    override fun onPause() {
        codeScanner.releaseResources()
        super.onPause()
    }

    private fun checkStoragePermissions(billItems: List<BillitemDataModel>) {
        if (ContextCompat.checkSelfPermission(
                this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Permission not granted, request it
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
                REQUEST_STORAGE_PERMISSION
            )
        } else {
            // Storage permission already granted, proceed with your code
            val logo: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.klogo)
            printBill(billItems, "Om Sai Pooja Sahitya", "Shukrawar peth,teli lane, kolhapur", logo)
        }
    }

    private fun setUpPermissions() {
        val permission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
        if (permission != PackageManager.PERMISSION_GRANTED) {
            makeRequest()
        }
    }


    private fun showEditextDialog() {

    }

    private fun makeRequest() {
        ActivityCompat.requestPermissions(
            this, arrayOf(android.Manifest.permission.CAMERA), CAMERA_REQUEST_CODE
        )
    }

    @SuppressLint("MissingSuperCall")
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        when (requestCode) {
            CAMERA_REQUEST_CODE -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "You need the camera permission...!", Toast.LENGTH_SHORT)
                        .show()
                } else {

                }
            }
            REQUEST_BLUETOOTH_PERMISSION -> {
                if (grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                    // Bluetooth permissions granted, proceed to check storage permissions
                    ///  checkStoragePermissions()
                } else {
                    // Bluetooth permissions denied, handle accordingly
                    // For example, show a message to the user explaining why the permissions are necessary
                }
            }
            REQUEST_STORAGE_PERMISSION -> {
                if (grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                    // Storage permission granted, proceed with your code
                    /// printBill()
                } else {
                    // Storage permission denied, handle accordingly
                    // For example, show a message to the user explaining why the permission is necessary
                }
            }
        }

    }

    fun printBill(
        billItems: List<BillitemDataModel>, shopName: String, shopAddress: String, logo: Bitmap
    ) {
        // Initialize Bluetooth printer connection
        val printer = EscPosPrinter(
            BluetoothPrintersConnections.selectFirstPaired(), 203, 48f, 32
        )


        printer.printFormattedText("$shopName\n")
        printer.printFormattedText("$shopAddress\n")
        // printer.printImage(logo) // Assuming you have a method to print images
        printer.printFormattedText("\n") // Add a blank line after the logo

        // Print titles
        printer.printFormattedText(
            "Product Name     Weight     Quantity     Rate     Total Amount\n" + "--------------------------------------------------------------\n"
        )

        // Print bill items
        var grandTotal = 0.0
        for (item in billItems) {
            val productName = item.name.padEnd(15)
            val weight = item.weight.toString().padEnd(10)
            val quantity = item.qty.toString().padEnd(10)
            val rate = item.mrp.toString().padEnd(10)
            val totalAmount = item.total_mat.toString().padEnd(10)

            printer.printFormattedText("$productName$weight$quantity$rate$totalAmount\n")

            // Accumulate total amount
            grandTotal += item.total_mat
        }

        // Print Grand Total
        printer.printFormattedText("--------------------------------------------------------------\n")
        printer.printFormattedText("Grand Total: $grandTotal\n") // Assuming total amount is a double

        // printer.feed(2)
        printer.disconnectPrinter()
    }

}
