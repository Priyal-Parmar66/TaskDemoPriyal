package com.example.edittextdemo.view

import EditTextAdapter
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.edittextdemo.R
import com.example.edittextdemo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: EditTextAdapter
    private val itemList = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = EditTextAdapter(itemList) { position -> removeItem(position) }
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        val itemTouchHelper = ItemTouchHelper(ItemMoveCallback(adapter))
        itemTouchHelper.attachToRecyclerView(binding.recyclerView)

        binding.btnAddEditText.setOnClickListener { addNewItem() }
        binding.btnsubmit.setOnClickListener { submitData() }
    }

    private fun submitData() {
        if (adapter.validateFields()) {
            val intent = Intent(this, DisplayListActivity::class.java)
            intent.putStringArrayListExtra(getString(R.string.validated_data), ArrayList(itemList))
            startActivity(intent)
        }
    }

    private fun addNewItem() {
        itemList.add("")
        adapter.notifyItemInserted(itemList.size - 1)
        if (itemList.size >= 2) {
            adapter.notifyItemChanged(itemList.size - 2)
        }
    }

    private fun removeItem(position: Int) {
        if (position in itemList.indices) {
            itemList.removeAt(position)
            adapter.notifyItemRemoved(position)
            adapter.notifyItemRangeChanged(position, itemList.size)
        }
    }
}

class ItemMoveCallback(private val adapter: EditTextAdapter) : ItemTouchHelper.Callback() {
    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
        return makeMovementFlags(dragFlags, 0)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        val fromPosition = viewHolder.bindingAdapterPosition
        val toPosition = target.bindingAdapterPosition
        adapter.moveItem(fromPosition, toPosition)
        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        // implement when needed
    }
}