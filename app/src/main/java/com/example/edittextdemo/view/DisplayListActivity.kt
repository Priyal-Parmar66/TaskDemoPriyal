package com.example.edittextdemo.view

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.edittextdemo.R
import com.example.edittextdemo.databinding.ActivityDisplayBinding
import com.example.edittextdemo.viewmodel.DisplayViewModel

class DisplayListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDisplayBinding
    private val viewModel: DisplayViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDisplayBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adapter = DisplayListAdapter(emptyList())
        binding.recyclerViewValidated.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewValidated.adapter = adapter

        intent.getStringArrayListExtra(getString(R.string.validated_data))?.let { validatedData ->
            viewModel.setValidatedData(validatedData)
        }

        viewModel.validatedList.observe(this, Observer { list ->
            adapter.updateData(list)
        })
    }
}
