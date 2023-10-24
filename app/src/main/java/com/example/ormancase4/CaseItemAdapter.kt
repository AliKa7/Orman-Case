package com.example.ormancase4

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

public class CaseItemAdapter(private val caseItemsList : List<CaseItem>) :
    RecyclerView.Adapter<CaseItemAdapter.CaseItemViewHolder>() {
    class CaseItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemImage : ImageView = itemView.findViewById(R.id.itemImage)
        val itemName : TextView = itemView.findViewById(R.id.itemText)
        val itemDescription : TextView = itemView.findViewById(R.id.itemDescription)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CaseItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.case_item, parent, false)
        return CaseItemViewHolder(view)

    }

    override fun getItemCount(): Int {
        return caseItemsList.size
    }

    override fun onBindViewHolder(holder: CaseItemViewHolder, position: Int) {
        val caseItem = caseItemsList[position]
        holder.itemImage.setImageResource(caseItem.elemImage)
        holder.itemName.text = caseItem.elemName
        holder.itemDescription.text = caseItem.elemDescription

    }
}