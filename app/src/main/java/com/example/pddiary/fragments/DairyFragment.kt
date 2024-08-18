package com.example.pddiary.fragments

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pddiary.adapter.DairyAdapter
import com.example.pddiary.databinding.DairyFragmentBinding
import com.example.pddiary.models.DairyModel
import com.example.pddiary.models.HeaderModel
import android.widget.Toast
import com.example.pddiary.R
import java.io.File
import java.io.FileOutputStream
import java.io.FileReader
import java.util.Calendar

class DairyFragment : Fragment() {

    private lateinit var dairyBinding: DairyFragmentBinding
    private val binding: DairyFragmentBinding get() = dairyBinding

    private lateinit var viewModel: DairyViewModel
    private var selectedDate: String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dairyBinding = DairyFragmentBinding.inflate(inflater, container, false)
        return dairyBinding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity())[DairyViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set up the date selector
        binding.tvSelectDiaryDate.setOnClickListener {
            showDatePickerDialog()
        }

        // Access the Button inside the included layout
        val saveButton = view.findViewById<Button>(R.id.dairy_save_button)
        saveButton.setOnClickListener {
            if (selectedDate.isEmpty()) {
                Toast.makeText(requireContext(), "Please select a date first", Toast.LENGTH_SHORT).show()
            } else {
                saveDiaryEntry(selectedDate)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val list = viewModel.getDairyList()
        Log.v("fragmentResumed", list.size.toString() )
        binding.dairyRecyclerview.layoutManager = LinearLayoutManager(context)
        val adapter = DairyAdapter(list)
        binding.dairyRecyclerview.adapter = adapter
    }

    override fun onPause() {
        val adapter = binding.dairyRecyclerview.adapter as DairyAdapter
        val listToSave = adapter.getCurrentList()
        Log.v("fragmentPaused", listToSave.toString() )
//        viewModel.saveDairyList(listToSave)
        super.onPause()
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, selectedYear, selectedMonth, selectedDay ->
                selectedDate = "$selectedYear-${selectedMonth + 1}-$selectedDay"
                loadDiaryEntry(selectedDate)
            },
            year, month, day
        )

        datePickerDialog.show()
    }

    private fun saveDiaryEntry(date: String) {
        val fileName = "$date.csv"
        val file = File(requireContext().filesDir, fileName)
        val data = viewModel.getDiaryData() // Assuming you have a method to get the current data

        FileOutputStream(file).use { output ->
            output.write(data.toByteArray())
        }

        Toast.makeText(requireContext(), "Diary entry saved for $date", Toast.LENGTH_SHORT).show()
    }

    private fun loadDiaryEntry(date: String) {
        val fileName = "$date.csv"
        val file = File(requireContext().filesDir, fileName)

        if (file.exists()) {
            val data = FileReader(file).readText()
            viewModel.setDiaryData(data) // Assuming you have a method to set the data
            updateUI() // Refresh the UI with the loaded data
            Toast.makeText(requireContext(), "Loaded diary entry for $date", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(requireContext(), "No diary entry found for $date", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateUI() {
        val list = viewModel.getDairyList()
        val adapter = DairyAdapter(list)
        binding.dairyRecyclerview.adapter = adapter
    }

}