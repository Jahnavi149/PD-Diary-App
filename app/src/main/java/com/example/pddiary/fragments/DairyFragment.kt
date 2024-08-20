package com.example.pddiary.fragments

import android.app.DatePickerDialog
import android.os.Bundle
import android.os.SystemClock
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
import com.example.pddiary.utils.Logger
import android.widget.Toast
import com.example.pddiary.R
import java.io.File
import java.io.FileOutputStream
import java.io.FileReader
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class DairyFragment : Fragment() {

    private lateinit var dairyBinding: DairyFragmentBinding
    private val binding: DairyFragmentBinding get() = dairyBinding

    private lateinit var viewModel: DairyViewModel
    private var selectedDate: String = ""
    private var startTime: Long = 0
    private var isEditing: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dairyBinding = DairyFragmentBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(requireActivity())[DairyViewModel::class.java]
        startTime = SystemClock.elapsedRealtime()
        Logger.logEvent(requireContext(), "Visited Diary Page")
        return dairyBinding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity())[DairyViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        selectedDate = arguments?.getString("selectedDate", "").orEmpty()

        if (selectedDate.isNotEmpty()) {
            binding.tvSelectedDate.text = selectedDate
            loadDiaryEntry(selectedDate)
        }

        binding.tvSelectDiaryDate.setOnClickListener {
            showDatePickerDialog()
        }

        val saveButton = view.findViewById<Button>(R.id.dairy_save_button)
        saveButton.setOnClickListener {
            if (selectedDate.isEmpty()) {
                Toast.makeText(requireContext(), "Please select a date first", Toast.LENGTH_SHORT).show()
            } else {
                saveDiaryEntry(selectedDate)
                if (isEditing) {
                    Logger.logEvent(requireContext(), "Diary entry edited for $selectedDate")
                } else {
                    Logger.logEvent(requireContext(), "Diary entry created for $selectedDate")
                }
            }
        }

        if (selectedDate.isNotEmpty()) {
            loadDiaryEntry(selectedDate)
            isEditing = true
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadDiaryDataForSelectedDate(requireContext().filesDir)
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
                val selectedCalendar = Calendar.getInstance()
                selectedCalendar.set(selectedYear, selectedMonth, selectedDay)
                val dateFormat = SimpleDateFormat("MM-dd-yyyy", Locale.getDefault())
                selectedDate = dateFormat.format(selectedCalendar.time)
                binding.tvSelectedDate.text = selectedDate
                loadDiaryEntry(selectedDate)
            },
            year, month, day
        )

        datePickerDialog.show()
    }

    private fun saveDiaryEntry(date: String) {
        val fileName = "$date.csv"
        val file = File(requireContext().filesDir, fileName)
        val data = viewModel.getDiaryData()

        FileOutputStream(file).use { output ->
            output.write(data.toByteArray())
        }

        Logger.logEvent(requireContext(), "Diary entry saved or updated for $date")
        Toast.makeText(requireContext(), "Diary entry saved for $date", Toast.LENGTH_SHORT).show()
    }

    private fun loadDiaryEntry(date: String) {
        val fileName = "$date.csv"
        val file = File(requireContext().filesDir, fileName)

        if (file.exists()) {
            val data = FileReader(file).readText()
            viewModel.setDiaryData(data)
            updateUI()
            Toast.makeText(requireContext(), "Loaded diary entry for $date", Toast.LENGTH_SHORT).show()
        } else {
            viewModel.resetToDefault()
            updateUI()
            Toast.makeText(requireContext(), "No diary entry found for $date", Toast.LENGTH_SHORT).show()
        }
        Logger.logEvent(requireContext(), "Diary entry loaded for editing: $date")
    }

    private fun updateUI() {
        val list = viewModel.getDairyList()
        binding.dairyRecyclerview.layoutManager = LinearLayoutManager(context)
        val adapter = DairyAdapter(list)
        binding.dairyRecyclerview.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        val timeSpent = SystemClock.elapsedRealtime() - startTime
        Logger.logPageDuration(requireContext(), "Diary page", timeSpent)
    }
}
