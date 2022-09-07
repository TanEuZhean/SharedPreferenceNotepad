package com.example.noteclone.fragment


import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.noteclone.MainActivity
import com.example.noteclone.adapter.NoteAdapter
import com.example.noteclone.data.Note
import com.example.noteclone.databinding.FragmentHomeBinding
import com.example.noteclone.viewmodel.NoteViewModel

class HomeFragment : Fragment() {
    lateinit var binding : FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)

        val sharedPreferences: SharedPreferences = activity?.getSharedPreferences("notepad", Context.MODE_PRIVATE)!!

        val viewModel : NoteViewModel by viewModels {
            NoteViewModel.NoteViewModelProvider(sharedPreferences)
        }
        binding.viewModel = viewModel
        viewModel.getNotes()

        val recycler = binding.mainrecyclerview
        recycler.layoutManager = LinearLayoutManager(this.context)
//        recycler.adapter = viewModel.getNotes().value?.let { NoteAdapter(it) }

        val observer : Observer<MutableList<String>> = Observer<MutableList<String>> { newList ->
            if (newList.isNullOrEmpty()) {
                binding.mainrecyclerview.visibility = View.GONE
                binding.isEmpty.visibility = View.VISIBLE
            } else {
                recycler.adapter = NoteAdapter(newList, viewModel, findNavController())
            }
//            recycler.adapter.setModels(newList)
        }
        viewModel.notes.observe(viewLifecycleOwner, observer)

        return binding.root
    }
}
