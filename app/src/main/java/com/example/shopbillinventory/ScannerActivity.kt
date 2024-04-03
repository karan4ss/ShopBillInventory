package com.example.shopbillinventory

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import com.budiyev.android.codescanner.ScanMode
import com.example.BillitemDataModel
import com.example.shopbillinventory.Adapters.AdapterItemsofBill
import com.example.shopbillinventory.databinding.ActivityScannerBinding
import java.util.regex.Pattern

class ScannerActivity : AppCompatActivity() {
    private lateinit var scannerBinding: ActivityScannerBinding
    private lateinit var codeScanner: CodeScanner
    private val CAMERA_REQUEST_CODE = 101;
    val myStringList = mutableListOf<String>()
    val billItemListGlobal: MutableList<BillitemDataModel> = mutableListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        scannerBinding = ActivityScannerBinding.inflate(layoutInflater)
        setContentView(scannerBinding.root)
        setUpPermissions()
        codeScanner = CodeScanner(this, scannerBinding.ScannerView)

        // Parameters (default values)
        codeScanner.camera = CodeScanner.CAMERA_BACK // or CAMERA_FRONT or specific camera id
        codeScanner.formats = CodeScanner.ALL_FORMATS // list of type BarcodeFormat,
        // ex. listOf(BarcodeFormat.QR_CODE)
        codeScanner.autoFocusMode = AutoFocusMode.SAFE // or CONTINUOUS
        codeScanner.scanMode = ScanMode.SINGLE // or CONTINUOUS or PREVIEW
        codeScanner.isAutoFocusEnabled = true // Whether to enable auto focus or not
        codeScanner.isFlashEnabled = false // Whether to enable flash or not

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
                            //  val total_amount = 10
                            // Create a BillitemDataModel instance and add it to the list
                            val billItem = BillitemDataModel(
                                name, mrp, weight, et_qty,
                                total_amount.toDouble()
                            )
                            billItemList.add(billItem)
                            billItemListGlobal.addAll(billItemList)
                            billItemList.clear()
                            myStringList.clear()
                        }

                    }
                    setNegativeButton("Cancel") { dialogLayout, which ->

                    }
                    setView(dailog)
                    show()
                }

                scannerBinding.rvItemAddedtoBill.layoutManager =
                    LinearLayoutManager(this)
                scannerBinding.rvItemAddedtoBill.adapter =
                    AdapterItemsofBill(this, billItemListGlobal)


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
    }

    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()
    }

    override fun onPause() {
        codeScanner.releaseResources()
        super.onPause()
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
        }

    }
}
