package com.example.shopbillinventory

import android.app.DatePickerDialog
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.shopbillinventory.databinding.ActivityIncomeBinding
import com.google.firebase.database.*
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class IncomeActivity : AppCompatActivity() {
    private lateinit var incomeBinding: ActivityIncomeBinding
    private lateinit var databaseReference: DatabaseReference

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        incomeBinding = ActivityIncomeBinding.inflate(layoutInflater)
        setContentView(incomeBinding.root)
        databaseReference = FirebaseDatabase.getInstance().reference

        val today = LocalDate.now()
        //val formatter = DateTimeFormatter.ofPattern("d/M/yyyy")
        val formatter = DateTimeFormatter.ofPattern("d-M-yyyy")
        val todayDate = today.format(formatter)
        val currentMonth = SimpleDateFormat("MMMM yyyy", Locale.getDefault()).format(Date())


        val c = Calendar.getInstance()
        val df = SimpleDateFormat("dd-MM-yyyy")
        val formattedDate = df.format(c.time)
        incomeBinding.ivArrowup.setOnClickListener {
            // Increase the date by one day
            c.add(Calendar.DATE, 1)
            val newFormattedDate = df.format(c.time)
            incomeBinding.tvSetdate.text = newFormattedDate
        }
        incomeBinding.ivArrowDown.setOnClickListener {
            // Decrease the date by one day
            c.add(Calendar.DATE, -1)
            val newFormattedDate = df.format(c.time)
            incomeBinding.tvSetdate.text = newFormattedDate
        }



        incomeBinding.tvSelectDate.setOnClickListener {
            // The instance of our calendar.
            val c = Calendar.getInstance()

            // On the next lines, we get our day, month, and year.
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            // On the next line, we create a variable for the date picker dialog.
            val datePickerDialog = DatePickerDialog(
                // On the next line, we pass the context.
                this@IncomeActivity,
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    // On the next line, we set the date to our text view.
                    incomeBinding.tvSetdate.text =
                        formatDate("$dayOfMonth-${month + 1}-$year")
                },
                // On the next line, we pass the year, month, and day for the selected date in our date picker.
                year, month, day
            )
            // At last, we call show to
            // display our date picker dialog.
            datePickerDialog.show()
        }

        incomeBinding.btnShowHistoryinMixOrder.setOnClickListener {
            val selectedDate = incomeBinding.tvSetdate.text.toString()
            val dbReferenceForTodaysBusiness =
                databaseReference.child("Todays_Business_Amount").child(currentMonth)
                    .child(selectedDate)

            dbReferenceForTodaysBusiness.addListenerForSingleValueEvent(object :
                ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val businessAmount = snapshot.getValue(Int::class.java)
                        // Handle the retrieved business amount
                        //  println("Business amount: $businessAmount")
                        incomeBinding.tvSetdate.setText(businessAmount.toString())
                    } else {
                        // Node does not exist for the specified date
                        Toast.makeText(
                            this@IncomeActivity,
                            "No data available for today",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle onCancelled event
                }
            })
        }
    }

    private fun formatDate(inputDate: String): String {
        return try {
            val inputFormat = SimpleDateFormat("d-M-yyyy", Locale.getDefault())
            val date: Date = inputFormat.parse(inputDate)!!

            val outputFormat = SimpleDateFormat("d-M-yyyy", Locale.getDefault())
            outputFormat.format(date)
        } catch (e: ParseException) {
            e.printStackTrace()
            inputDate // Return the original date if there's an error
        }
    }

}