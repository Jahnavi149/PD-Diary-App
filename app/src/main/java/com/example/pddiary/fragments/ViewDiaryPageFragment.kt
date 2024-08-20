package com.example.pddiary.fragments

import android.os.Bundle
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.pddiary.R
import com.example.pddiary.databinding.FragmentViewDiaryPageBinding
import com.example.pddiary.utils.Logger

class ViewDiaryPageFragment : Fragment() {

    private lateinit var binding: FragmentViewDiaryPageBinding
    private lateinit var viewModel: DairyViewModel
    private var startTime: Long = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentViewDiaryPageBinding.inflate(inflater, container, false)
        startTime = SystemClock.elapsedRealtime()
        Logger.logEvent(requireContext(), "Visited View Diary Entries Page")
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity())[DairyViewModel::class.java]

        val savedDates = viewModel.getSavedDiaryDates(requireContext().filesDir)
        binding.savedDatesContainer.removeAllViews()

        for (date in savedDates) {
            val button = LayoutInflater.from(context).inflate(R.layout.item_saved_date_button, binding.savedDatesContainer, false)

            val formattedDate = convertToReadableDate(date)

            button.findViewById<Button>(R.id.savedDateButton).apply {
                text = formattedDate
                setOnClickListener {
                    for (i in 0 until binding.savedDatesContainer.childCount) {
                        val child = binding.savedDatesContainer.getChildAt(i)
                        child.findViewById<Button>(R.id.savedDateButton).isSelected = false
                    }

                    this.isSelected = true
                    viewModel.setSelectedDate(date)

                    val bundle = Bundle().apply {
                        putString("selectedDate", date)
                    }

                    findNavController().navigate(R.id.action_viewDiaryPageFragment_to_dairyFragment, bundle)
                    Logger.logEvent(requireContext(), "Diary date selected: $formattedDate")
                }
            }
            binding.savedDatesContainer.addView(button)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        val timeSpent = SystemClock.elapsedRealtime() - startTime
        Logger.logPageDuration(requireContext(), "View Diary Entries page", timeSpent)
    }

    private fun convertToReadableDate(date: String): String {
        val parts = date.split("-")
        val month = when (parts[0]) {
            "01" -> "January"
            "02" -> "February"
            "03" -> "March"
            "04" -> "April"
            "05" -> "May"
            "06" -> "June"
            "07" -> "July"
            "08" -> "August"
            "09" -> "September"
            "10" -> "October"
            "11" -> "November"
            "12" -> "December"
            else -> "Unknown"
        }
        val day = parts[1]
        val year = parts[2]
        return "$month $day, $year"
    }
}
