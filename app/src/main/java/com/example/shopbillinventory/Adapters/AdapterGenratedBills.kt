package com.example.shopbillinventory.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.view.menu.MenuView.ItemView
import androidx.recyclerview.widget.RecyclerView
import com.example.BillitemDataModel
import com.example.shopbillinventory.GernratedBillsModel
import com.example.shopbillinventory.R

class AdapterGenratedBills(
    private val context: Context,
    private val dataList: List<GernratedBillsModel>
) :
    RecyclerView.Adapter<AdapterGenratedBills.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_file_genratedbill_data, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.tvBillid.setText(dataList.get(position).billingId.toString())
        holder.tvnoofItems.setText(dataList.get(position).items +" " + "Items")
        holder.tvgrandTotal.setText(dataList.get(position).grandTotal.toString())
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvBillid: TextView = itemView.findViewById(R.id.tvBillId)
        val tvnoofItems: TextView = itemView.findViewById(R.id.tvnoofItems)
        val tvgrandTotal: TextView = itemView.findViewById(R.id.tvgrandTotal)

    }

}