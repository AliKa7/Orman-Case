package com.example.ormancase4

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class InstructionItemAdapter(private val instItemList:List<InstructionItem>) :
    RecyclerView.Adapter<InstructionItemAdapter.InstViewHolder>() {
    class InstViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val numberText: TextView = itemView.findViewById(R.id.adviceNumber)
        val descriptionText: TextView = itemView.findViewById(R.id.adviceDescription)
        val image: ImageView = itemView.findViewById(R.id.adviceImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InstViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.instruction_item, parent, false)
        return InstViewHolder(view)

    }

    override fun getItemCount(): Int {
        return instItemList.size
    }

    override fun onBindViewHolder(holder: InstViewHolder, position: Int) {
        val instItem = instItemList[position]
        holder.numberText.text = instItem.elemNumber
        holder.descriptionText.text = instItem.elemDescription
        holder.image.setImageResource(instItem.elemImage)
    }

}