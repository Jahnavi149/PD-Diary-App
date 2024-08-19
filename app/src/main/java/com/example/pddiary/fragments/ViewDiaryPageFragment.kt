package com.example.pddiary.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.pddiary.R
import com.example.pddiary.databinding.FragmentViewDiaryPageBinding

class ViewDiaryPageFragment : Fragment() {

    private lateinit var binding: FragmentViewDiaryPageBinding
    private lateinit var viewModel: DairyViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentViewDiaryPageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity())[DairyViewModel::class.java]

        // Fetch and display the saved dates
        val savedDates = viewModel.getSavedDiaryDates(requireContext().filesDir)
        binding.savedDatesContainer.removeAllViews()

        for (date in savedDates) {
            val button = LayoutInflater.from(context).inflate(R.layout.item_saved_date_button, binding.savedDatesContainer, false)

            // Convert the date to the readable format
            val formattedDate = convertToReadableDate(date)

            button.findViewById<Button>(R.id.savedDateButton).text = formattedDate
            button.setOnClickListener {
                // Set the selected date in the ViewModel
                viewModel.setSelectedDate(date)

                // Create a bundle and add the date to it
                val bundle = Bundle().apply {
                    putString("selectedDate", date)
                }

                findNavController().navigate(R.id.action_viewDiaryPageFragment_to_dairyFragment, bundle)
            }
            binding.savedDatesContainer.addView(button)
        }
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
