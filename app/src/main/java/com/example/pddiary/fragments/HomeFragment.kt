package com.example.pddiary.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.pddiary.R
import com.example.pddiary.databinding.HomeFragmentBinding

class HomeFragment : Fragment() {

    private lateinit var homeBinding: HomeFragmentBinding
    private val binding get() = homeBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        homeBinding = HomeFragmentBinding.inflate(inflater, container, false)
        return homeBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Finding the CardView by its ID
        val addNewDiaryCard: CardView = view.findViewById(R.id.add_new_diary_card)

        // A click listener on the CardView
        addNewDiaryCard.setOnClickListener {
            // Navigation to the diary page
            findNavController().navigate(R.id.action_homeFragment_to_dairyFragment)
        }

    }

}