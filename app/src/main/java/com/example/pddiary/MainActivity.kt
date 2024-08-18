package com.example.pddiary

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.example.pddiary.databinding.MainActivityBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: MainActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainActivityBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val navController = findNavController(R.id.fragment_container)

        setSelectedState(home = true)

        binding.home.setOnClickListener {
            navController.navigate(R.id.action_global_homeFragment)
            setSelectedState(home = true)
        }

        binding.dairy.setOnClickListener {
            navController.navigate(R.id.action_global_dairyFragment)
            setSelectedState(dairy = true)
        }

        binding.profile.setOnClickListener {
            navController.navigate(R.id.action_global_profileFragment)
            setSelectedState(profile = true)
        }
    }

    private fun setSelectedState(home: Boolean = false, dairy: Boolean = false, profile: Boolean = false) {
        binding.home.isSelected = home
        binding.dairy.isSelected = dairy
        binding.profile.isSelected = profile
    }
}