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
        holder.tvBillid.setText("Bill No : " + dataList.get(position).billingId.toString())
        holder.tvnoofItems.setText(dataList.get(position).items + " " + "Items")
        holder.tvgrandTotal.setText("Grand_Total :" + dataList.get(position).payment_mode.toString())

        holder.llofBillItems.setOnClickListener {
            val intent = Intent(context, ShowPerticularBillActivity::class.java)
            intent.putExtra("billid", dataList.get(position).billingId.toString())
            intent.putExtra("grand_totoal", dataList.get(position).grandTotal.toString())
            intent.putExtra("payment_mode", dataList.get(position).payment_mode.toString())
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvBillid: TextView = itemView.findViewById(R.id.tvBillId)
        val tvnoofItems: TextView = itemView.findViewById(R.id.tvnoofItems)
        val tvgrandTotal: TextView = itemView.findViewById(R.id.tvgrandTotal)
        val llofBillItems: LinearLayout = itemView.findViewById(R.id.llofBillItems)

    }

}