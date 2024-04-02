package com.example.shopbillinventory.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.appcompat.view.menu.MenuView.ItemView
import androidx.recyclerview.widget.RecyclerView
import com.example.BillitemDataModel
import com.example.shopbillinventory.R

class AdapterItemsofBill(
    private val context: Context,
    private val dataList: List<BillitemDataModel>
) :
    RecyclerView.Adapter<AdapterItemsofBill.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_file_scanned_data, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val printableData = StringBuilder()
        // for (product in dataList) {
        // printableData.append("\n")
        printableData.append("Product Name: ${dataList.get(position).name}\n")
        printableData.append("MRP: ${dataList.get(position).mrp}\n")
        printableData.append("Weight: ${dataList.get(position).weight}\n")
        printableData.append("Qty: ${dataList.get(position).qty}\n")
        printableData.append("Total_amt: ${dataList.get(position).total_mat}\n")
        printableData.append("\n")

        holder.tvid.setText("1")
        holder.tvproductName.setText(dataList.get(position).name)
        val weighIntValue = dataList.get(position).weight.toInt()
        holder.tvprdWeight.setText(weighIntValue.toString() + "gm")
        holder.tvprdMrp.setText(dataList.get(position).mrp.toString())
       // holder.tvprdQty.setText(dataList.get(position).qty.toString())
        holder.tvprdQty.setText("1")
        holder.tvTotalAmt.setText(dataList.get(position).total_mat.toString())
        // }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvid: TextView = itemView.findViewById(R.id.tvId)
        val tvproductName: TextView = itemView.findViewById(R.id.tvproductName)
        val tvprdWeight: TextView = itemView.findViewById(R.id.tvprdWeight)
        val tvprdQty: TextView = itemView.findViewById(R.id.tvprdQty)
        val tvprdMrp: TextView = itemView.findViewById(R.id.tvprdMrp)
        val tvTotalAmt: TextView = itemView.findViewById(R.id.tvTotalAmt)

    }

    fun formatPrintableData(products: List<BillitemDataModel>): String {
        val printableData = StringBuilder()
        for (product in products) {
            printableData.append("Product Name: ${product.name}\n")
            printableData.append("MRP: ${product.mrp}\n")
            printableData.append("Weight: ${product.weight}\n")
            printableData.append("\n")
        }
        return printableData.toString()
    }
}