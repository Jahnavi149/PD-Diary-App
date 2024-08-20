package com.example.pddiary.fragments

import android.os.Bundle
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.pddiary.R
import com.example.pddiary.databinding.HomeFragmentBinding
import com.example.pddiary.utils.Logger

class HomeFragment : Fragment() {

    private lateinit var homeBinding: HomeFragmentBinding
    private val binding get() = homeBinding
    private lateinit var viewModel: DairyViewModel
    private var startTime: Long = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        homeBinding = HomeFragmentBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(requireActivity())[DairyViewModel::class.java]
        startTime = SystemClock.elapsedRealtime()
        Logger.logEvent(requireContext(), "Visited Homepage")
        return homeBinding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        val timeSpent = SystemClock.elapsedRealtime() - startTime
        Logger.logPageDuration(requireContext(), "Homepage", timeSpent)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val addNewDiaryCard: CardView = view.findViewById(R.id.add_new_diary_card)
        val viewPreviousDiaryCard: CardView = view.findViewById(R.id.view_update_diary_card)

        addNewDiaryCard.setOnClickListener {
            viewModel.clearSelectedDate()
            findNavController().navigate(R.id.action_homeFragment_to_dairyFragment)
            Logger.logEvent(requireContext(), "Add New Diary card clicked")
        }

        viewPreviousDiaryCard.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_viewDiaryPageFragment)
            Logger.logEvent(requireContext(), "View/Update Diary card clicked")
        }
    }
}
