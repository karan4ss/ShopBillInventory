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
import com.example.shopbillinventory.PlanModel
import com.example.shopbillinventory.R

class AdapterItemsOfPlansInfo(
    private val context: Context,
    private val dataList: List<PlanModel>
) :
    RecyclerView.Adapter<AdapterItemsOfPlansInfo.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_file_plansinfo_data, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.tvOneMonth.setText(dataList.get(position).duration)
        holder.tvOnemontAmount.setText(dataList.get(position).amount)
        holder.tvOneMonthDescription.setText(dataList.get(position).description)

    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvOneMonth: TextView = itemView.findViewById(R.id.tvOneMonth)
        val tvOneMonthDescription: TextView = itemView.findViewById(R.id.tvOneMonthDescription)
        val tvOnemontAmount: TextView = itemView.findViewById(R.id.tvOnemontAmount)


    }
}