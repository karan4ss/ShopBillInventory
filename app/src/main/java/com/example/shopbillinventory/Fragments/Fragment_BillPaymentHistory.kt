package com.example.shopbillinventory.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.BillitemDataModel
import com.example.shopbillinventory.Adapters.AdapterGenratedBills
import com.example.shopbillinventory.Adapters.AdapterItemsofBill
import com.example.shopbillinventory.GernratedBillsModel
import com.example.shopbillinventory.databinding.FragmentBillPaymentHistoryBinding
import com.google.firebase.database.*

class Fragment_BillPaymentHistory : Fragment() {
    private lateinit var bindFragment_BillPaymentHistory: FragmentBillPaymentHistoryBinding
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        bindFragment_BillPaymentHistory =
            FragmentBillPaymentHistoryBinding.inflate(inflater, container, false)
        val view = bindFragment_BillPaymentHistory.root


        // val billsRef = databaseReference.child("Confirmed_Billings").child("Confirmed_Bill_Items")

        retrieveData()
        return view
    }

    private fun retrieveData() {
        val billList: MutableList<GernratedBillsModel> = mutableListOf()
        databaseReference = FirebaseDatabase.getInstance().reference
        databaseReference.child("Confirmed_Billings")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        for (itemSnapshot in snapshot.children) {
                            val billing = itemSnapshot.key?.toInt() ?: -1
                            val items = mutableListOf<Int>()
                            val grandTotal =
                                itemSnapshot.child("Confirmed_grandTotal").value?.toString()
                                    ?.toInt()
                                    ?: 0

                            itemSnapshot.child("Confirmed_Bill_Items").children.forEach { itemSnapshot ->
                                val qty = itemSnapshot.child("qty").value?.toString()?.toInt() ?: 0
                                items.add(qty)
                            }

                            val bill =
                                GernratedBillsModel(billing, (items.size).toString(), grandTotal)
                            billList.add(bill)
                        }

                        bindFragment_BillPaymentHistory.rvOfBills.layoutManager =
                            LinearLayoutManager(context)
                        bindFragment_BillPaymentHistory.rvOfBills.adapter =
                            context?.let {
                                AdapterGenratedBills(
                                    it,
                                    billList
                                )
                            }
                    }

                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle error
                }
            })
    }

}