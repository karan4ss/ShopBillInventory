package com.example.shopbillinventory.Adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.view.menu.MenuView.ItemView
import androidx.recyclerview.widget.RecyclerView
import com.example.BillitemDataModel
import com.example.shopbillinventory.GernratedBillsModel
import com.example.shopbillinventory.R
import com.example.shopbillinventory.ShowPerticularBillActivity

class AdapterPerticularBill(
    private val context: Context,
    private val dataList: List<BillitemDataModel>
) :
    RecyclerView.Adapter<AdapterPerticularBill.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_perticular_bill_data, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val originalText = dataList.get(position).name
        val maxLengthFirstLine = 12
        val firstLine = originalText.take(maxLengthFirstLine)
        val remainingText = originalText.drop(maxLengthFirstLine)


        holder.tvsetProductName.setText(
            firstLine + "\n" + remainingText
        )
        holder.tvsetWeight.setText(dataList.get(position).weight.toString())
        holder.tvsetRate.setText(dataList.get(position).mrp.toString())
        holder.tvsetQty.setText(dataList.get(position).qty)
        holder.tvsetAmount.setText(dataList.get(position).total_mat.toString())

    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvsetProductName: TextView = itemView.findViewById(R.id.tvsetProductName)
        val tvsetWeight: TextView = itemView.findViewById(R.id.tvsetWeight)
        val tvsetRate: TextView = itemView.findViewById(R.id.tvsetRate)
        val tvsetQty: TextView = itemView.findViewById(R.id.tvsetQty)
        val tvsetAmount: TextView = itemView.findViewById(R.id.tvsetAmount)

    }

}