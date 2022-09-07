package com.example.noteclone

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.noteclone.adapter.NoteAdapter
import com.example.noteclone.databinding.ActivityMainBinding
import com.example.noteclone.viewmodel.NoteViewModel
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding;
    private lateinit var viewModel : NoteViewModel;

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        val sharedPreferences: SharedPreferences = this.getSharedPreferences("notepad", Context.MODE_PRIVATE)!!

        val viewModel : NoteViewModel by viewModels {
            NoteViewModel.NoteViewModelProvider(sharedPreferences)
        }
        binding.viewModel = viewModel

        binding.FAB.setOnClickListener {
            lifecycleScope.launch {
                viewModel.addNotes()
            }
        }

        val navHost = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHost.navController
        navController.addOnDestinationChangedListener { _, navDes, _ ->
            if (navDes.displayName == "com.example.noteclone:id/noteFragment") {
                hideBottomNav()
            } else {
                showBottomNav()
            }
        }

        setContentView(binding.root)
    }

    private fun showBottomNav() {
        binding.FAB.show()
        binding.bottomAppBar.visibility = View.VISIBLE
    }

    private fun hideBottomNav() {
        binding.FAB.hide()
        binding.bottomAppBar.visibility = View.GONE
    }

}