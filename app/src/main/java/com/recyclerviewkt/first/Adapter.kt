package com.recyclerviewkt.first

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.recyclerviewkt.R
import kotlinx.android.synthetic.main.item.view.*

class Adapter(private val list: List<Item>, private val listener: OnItemClickListener) :
    RecyclerView.Adapter<Adapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.item,
            parent, false
        )

        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = list[position]



        holder.imgView.setImageResource(currentItem.imageResurce)
        holder.tView1.text = currentItem.text1
        holder.tView2.text = currentItem.text2
    }

    override fun getItemCount() = list.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        val imgView: ImageView = itemView.image_view
        val tView1: TextView = itemView.text_view_1
        val tView2: TextView = itemView.text_view_2

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position: Int = adapterPosition
//            val clickedItem: Item = list[position]
//            clickedItem.text2 = "Clicked"
//            notifyItemChanged(position)
            if (position != RecyclerView.NO_POSITION)
                listener.onItemClick(position)
        }
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }
}