package com.recyclerviewkt.first

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.recyclerviewkt.R
import kotlinx.android.synthetic.main.fragment_first.view.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.random.Random

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment(), Adapter.OnItemClickListener {
    private val list = generateDummyList(123)
    private val adapter = Adapter(list, this)
    private val swipeBackground: ColorDrawable = ColorDrawable(Color.parseColor("#FFBB86FC"))
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_first, container, false)

        view.rv.adapter = adapter
        view.rv.layoutManager = LinearLayoutManager(activity)
//        view.rv.setHasFixedSize(true)

        view.btn_insert.setOnClickListener {
            val index: Int = Random.nextInt(6)
            val newItem = Item(
                R.drawable.ic_pokemon,
                "New item at position $index",
                "Line 2"
            )
            list.add(index, newItem)
            adapter.notifyItemInserted(index)
        }

        view.btn_remove.setOnClickListener {
            val index: Int = Random.nextInt(6)
            list.removeAt(index)
            adapter.notifyItemRemoved(index)
        }
        val touchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                val sourcePos: Int = viewHolder.adapterPosition
                val targetPos: Int = target.adapterPosition
                Collections.swap(list, sourcePos, targetPos)
                adapter.notifyItemMoved(sourcePos, targetPos)
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position: Int = viewHolder.adapterPosition
                val item: Item? = list[position]
                list.removeAt(position)
                adapter.notifyItemRemoved(position)
                Snackbar.make(viewHolder.itemView, "Item: $position removed.", Snackbar.LENGTH_LONG)
                    .setAction("UNDO") {
                        if (item != null) {
                            list.add(position, item)
                            adapter.notifyItemInserted(position)
                        }
                    }.show()
            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                val itemView =viewHolder.itemView
                if (dX>0){
                    swipeBackground.setBounds(itemView.left,itemView.top,dX.toInt(),itemView.bottom)
                }else if (dX<0){
                    swipeBackground.setBounds(itemView.right+dX.toInt(),itemView.top,itemView.right,itemView.bottom)
                }
                swipeBackground.draw(c)
                super.onChildDraw(c,recyclerView,viewHolder,dX, dY,actionState,isCurrentlyActive)
            }
        })
        touchHelper.attachToRecyclerView(view.rv)
        return view
    }

    override fun onItemClick(position: Int) {
        Toast.makeText(activity, "Adapter $position clicked", Toast.LENGTH_SHORT).show()
        val clickedItem: Item = list[position]
        clickedItem.text2 = "Clicked"
        adapter.notifyItemChanged(position)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.button_first).setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }
    }


    private fun generateDummyList(size: Int): ArrayList<Item> {
        val list = ArrayList<Item>()

        for (i in 0 until size) {
            val drawable = when (i % 3) {
                0 -> R.drawable.ic_android
                1 -> R.drawable.ic_adb
                else -> R.drawable.ic_pokemon
            }
            val item = Item(drawable, "Item $i", "Line 2")
            list += item
        }

        return list
    }
}