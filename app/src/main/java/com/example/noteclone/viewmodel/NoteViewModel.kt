package com.example.noteclone.viewmodel

import android.content.SharedPreferences
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.*
import com.example.noteclone.data.Note
import com.example.noteclone.data.toNote
import java.lang.IllegalArgumentException
import java.time.LocalDate

class NoteViewModel(private val sharedPreferences: SharedPreferences): ViewModel() {
    val notes : MutableLiveData<MutableList<String>> by lazy {
        MutableLiveData<MutableList<String>>()
    }
    val note : MutableLiveData<Note> by lazy {
        MutableLiveData<Note>()
    }

    @JvmName("getNotes1")
    fun getNotes() : MutableLiveData<MutableList<String>> {
        var data = sharedPreferences.getStringSet("note", null)

        notes.value = data?.toMutableList()

        Log.i("notesVal", notes.value.toString())

        return notes
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun addNotes() : MutableList<String> {
        var data = sharedPreferences.getStringSet("note", null)?.toMutableList()
        var arr :  MutableList<String> = mutableListOf()

        if(data.isNullOrEmpty()) {
            val new = Note(
                id = 0,
                title = "New Note",
                content = "",
                time = LocalDate.now().toString(),
                color = "#FFFFFF"
            )

            arr.add(new.toString())
            notes.value = arr

            Log.i("added", notes.value.toString())

            val editor: SharedPreferences.Editor = sharedPreferences.edit()
            editor.putStringSet("note", notes.value?.toMutableSet()).apply()


            Log.i("new", sharedPreferences.getStringSet("note", null).toString())

            return arr
        } else {
            var biggest = 0
            for(note in data) {
                arr.add(note)
                if(toNote(note).id > biggest) {
                    biggest = toNote(note).id
                }
            }

            val new = Note(
                id = biggest+1,
                title = "New Note",
                content = "",
                time = LocalDate.now().toString(),
                color = "#FFFFFF"
            )

            arr.add(new.toString())
            notes.value = arr

            val editor: SharedPreferences.Editor = sharedPreferences.edit()
            editor.putStringSet("note", notes.value?.toMutableSet()).apply()

            Log.i("added", notes.value?.toMutableSet().toString())
            Log.i("new", sharedPreferences.getStringSet("note", null).toString())

            return arr
        }
    }

    fun deleteNotes(id: Int) {
        var data = sharedPreferences.getStringSet("note", null)?.toMutableList()
        var res : MutableList<String> = mutableListOf()

        for(i in data!!) {
            if(toNote(i).id != id) {
                res.add(i)
            }
        }

        notes.value = res

        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putStringSet("note", notes.value?.toMutableSet()).apply()
    }

    fun getNote(id: Int) {
        var data = sharedPreferences.getStringSet("note", null)?.toMutableList()
        lateinit var res : Note

        for(i in data!!) {
            if(toNote(i).id == id) {
                res = toNote(i)
            }
        }

        note.value = res
    }

    fun editNote(title: String, content: String, id: Int) {
        var data = sharedPreferences.getStringSet("note", null)?.toMutableList()
        var res : MutableList<String> = mutableListOf()

        for(i in data!!) {
            if(toNote(i).id == id) {
                var note = toNote(i)
                note.title = title
                note.content = content
                res.add(note.toString())
            } else {
                res.add(i)
            }
        }

        notes.value = res

        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putStringSet("note", notes.value?.toMutableSet()).apply()
    }

    class NoteViewModelProvider(private val sharedPreferences: SharedPreferences): ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if(modelClass.isAssignableFrom(NoteViewModel::class.java)) {
                return NoteViewModel(sharedPreferences) as T
            }
            throw IllegalArgumentException("Invalid viewModel")
        }
    }
}