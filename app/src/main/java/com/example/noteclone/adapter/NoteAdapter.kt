package com.example.noteclone.adapter

import android.content.Context
import android.content.DialogInterface
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemLongClickListener
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.example.noteclone.MainActivity
import com.example.noteclone.R
import com.example.noteclone.data.Note
import com.example.noteclone.data.toNote
import com.example.noteclone.databinding.NoteItemBinding
import com.example.noteclone.fragment.HomeFragmentDirections
import com.example.noteclone.fragment.NoteFragment
import com.example.noteclone.viewmodel.NoteViewModel
import kotlinx.coroutines.coroutineScope

class NoteAdapter(var data: MutableList<String>, var viewModel: NoteViewModel, var navController: NavController) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private lateinit var binding : NoteItemBinding

//    var data : MutableList<Note> = mutableListOf(
//        Note(title="note1",content="note 1",color="#176be5", time="18 August", id=0),
//        Note(title="note2",content="note 2",color="#176be5", time="18 August", id=1)
//    )

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        binding = NoteItemBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return NoteViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        val note : Note = toNote(data[position])
        if(viewHolder is NoteViewHolder) {
            viewHolder.bind(note)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun setModels(newList: MutableList<String>?) {
        if (newList != null) {
            this.data = newList
        }
        notifyDataSetChanged()
    }

    inner class NoteViewHolder(private val binding: NoteItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(note: Note) {
            binding.sideColor.setBackgroundColor(Color.parseColor(note.color))
            binding.noteTitleTv.text = note.title
            binding.noteTimeTv.text = note.time

            binding.noteItem.setOnClickListener{
                val action = HomeFragmentDirections.actionHomeFragmentToNoteFragment(note.id)
                navController.navigate(action)
            }

            binding.noteItem.setOnLongClickListener { view: View  ->
                AlertDialog.Builder(view.context)
                    .setTitle("Delete Note")
                    .setMessage("Are you sure?")
                    .setPositiveButton(
                        "Yes", DialogInterface.OnClickListener() { dialogInterface: DialogInterface?, i: Int ->
                            binding.viewModel = viewModel
                            viewModel.deleteNotes(note.id)
                        }
                    )
                    .setNegativeButton("No", null)
                    .show()

                return@setOnLongClickListener true
            }

        }
    }
}