package com.example.pddiary.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.pddiary.databinding.ProfileFragmentBinding
import org.json.JSONObject
import java.io.File
import java.io.FileWriter
import java.io.IOException

class ProfileFragment : Fragment() {

    private lateinit var profileBinding: ProfileFragmentBinding
    private val binding: ProfileFragmentBinding get() = profileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        profileBinding = ProfileFragmentBinding.inflate(inflater, container, false)
        return profileBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.saveButton.setOnClickListener {
            saveProfileToJson()
        }
    }

    private fun saveProfileToJson() {
        val patientId = binding.patientIdInput.text.toString()
        val deviceName = binding.patientDeviceInput.text.toString()
        val medicationNumber = binding.patientMedicationNumberInput.text.toString()
        val medicationName = binding.patientMedicationNameInput.text.toString()
        val serverAddress = binding.serverAddressInput.text.toString()

        if (patientId.isEmpty()) {
            Toast.makeText(requireContext(), "Patient ID cannot be empty", Toast.LENGTH_SHORT).show()
            return
        }

        // Create a JSON object with the profile details
        val profileJson = JSONObject().apply {
            put("patient_id", patientId)
            put("device_name", deviceName)
            put("medication_number", medicationNumber)
            put("medication_name", medicationName)
            put("server_address", serverAddress)
        }

        // Define the file name and path
        val fileName = "${patientId}_profile.json"
        val file = File(requireContext().filesDir, fileName)

        // Write the JSON object to the file
        try {
            FileWriter(file).use { it.write(profileJson.toString()) }
            Toast.makeText(requireContext(), "Profile saved successfully", Toast.LENGTH_SHORT).show()
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(requireContext(), "Error saving profile", Toast.LENGTH_SHORT).show()
        }
    }
}
