package com.example.pddiary.adapter

import android.app.AlertDialog
import android.graphics.Color
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.DatePicker
import android.widget.SeekBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pddiary.R
import com.example.pddiary.models.DairyListItem
import com.example.pddiary.models.DairyModel
import com.example.pddiary.models.HeaderModel
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout


class DairyAdapter(private val list: MutableList<DairyListItem>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        val HEADER = 1
        val ROW = 2
        val BUTTON = 3
    }

    class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val time: TextView = itemView.findViewById(R.id.time)
        val on: CheckBox = itemView.findViewById(R.id.on)
        val asleep: CheckBox = itemView.findViewById(R.id.asleep)
        val onWithTroublesome: CheckBox = itemView.findViewById(R.id.on_with_troublesome)
        val onWithoutTroublesome: CheckBox = itemView.findViewById(R.id.on_without_troublesome)
        val off: CheckBox = itemView.findViewById(R.id.off)
        val med1: CheckBox = itemView.findViewById(R.id.med_1_status)
        val med2: CheckBox = itemView.findViewById(R.id.med_2_status)
        val symptomSeekBar: SeekBar = itemView.findViewById(R.id.symptom_rating_seekbar)
//        val measurementSlider: Slider = itemView.findViewById(R.id.measurement_slider)
//        val measurementInput: TextInputEditText = itemView.findViewById(R.id.measurement_input)
//        val measurementInputLayout: TextInputLayout = itemView.findViewById(R.id.measurement_input_layout) // Added this line
    }

    //   Removing the code for prev SUBMIT button at bottom of diary page

//    class ButtonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        val button: Button = itemView.findViewById(R.id.dairy_save_button)
//    }

    override fun getItemViewType(position: Int): Int {
        return when (list[position]) {
            is HeaderModel -> HEADER
            is DairyModel -> ROW
//            is DairyButtonModel -> BUTTON
            else -> throw IllegalArgumentException("unsupported type is sent to dairyAdapter")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            HEADER -> HeaderViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_dairy_headings, parent, false))
            ROW -> ItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_dairy_row, parent, false))
//            BUTTON -> ButtonViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_dairy_save_button, parent, false))
            else -> throw IllegalArgumentException("unsupported type is sent to dairyAdapter")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = list[position]) {
            is DairyModel -> {
                val mHolder = holder as ItemViewHolder

                // Unset listeners to prevent unwanted behavior during binding
                mHolder.asleep.setOnCheckedChangeListener(null)
                mHolder.on.setOnCheckedChangeListener(null)
                mHolder.onWithTroublesome.setOnCheckedChangeListener(null)
                mHolder.onWithoutTroublesome.setOnCheckedChangeListener(null)
                mHolder.off.setOnCheckedChangeListener(null)
                mHolder.med1.setOnCheckedChangeListener(null)
                mHolder.med2.setOnCheckedChangeListener(null)

                // Set checkbox states
                mHolder.time.text = item.time
                mHolder.asleep.isChecked = item.asleep
                mHolder.on.isChecked = item.on
                mHolder.onWithTroublesome.isChecked = item.onWithTroublesome
                mHolder.onWithoutTroublesome.isChecked = item.onWithoutTroublesome
                mHolder.off.isChecked = item.off
                mHolder.med1.isChecked = item.med1
                mHolder.med2.isChecked = item.med2
                mHolder.symptomSeekBar.progress = item.measurement

                // Enable/disable checkboxes based on the "asleep" state
                if (item.asleep) {
                    mHolder.symptomSeekBar.progress = 0
                    mHolder.symptomSeekBar.isEnabled = false
                    mHolder.med1.isEnabled = false
                    mHolder.med2.isEnabled = false
                } else {
                    mHolder.symptomSeekBar.isEnabled = true
                    mHolder.med1.isEnabled = true
                    mHolder.med2.isEnabled = true
                }

                // Set listeners after setting the state to avoid triggering them during binding
                val checkboxListener = View.OnClickListener {
                    item.asleep = mHolder.asleep.isChecked
                    item.on = mHolder.on.isChecked
                    item.onWithTroublesome = mHolder.onWithTroublesome.isChecked
                    item.onWithoutTroublesome = mHolder.onWithoutTroublesome.isChecked
                    item.off = mHolder.off.isChecked

                    // Update the DairyModel with the state of checkboxes
                    item.med1 = mHolder.med1.isChecked
                    item.med2 = mHolder.med2.isChecked

                    if (it == mHolder.asleep) {
                        item.on = false
                        item.onWithTroublesome = false
                        item.onWithoutTroublesome = false
                        item.off = false
                        item.med1 = false
                        item.med2 = false
                        mHolder.med1.isChecked = false
                        mHolder.med2.isChecked = false
                        mHolder.symptomSeekBar.progress = 0
                        mHolder.symptomSeekBar.isEnabled = false
                        mHolder.med1.isEnabled = false
                        mHolder.med2.isEnabled = false
                    } else {
                        mHolder.symptomSeekBar.isEnabled = true
                        mHolder.med1.isEnabled = true
                        mHolder.med2.isEnabled = true
                        if (it == mHolder.on) {
                            item.asleep = false
                            item.onWithTroublesome = false
                            item.onWithoutTroublesome = false
                            item.off = false
                        } else if (it == mHolder.onWithTroublesome) {
                            item.asleep = false
                            item.on = false
                            item.onWithoutTroublesome = false
                            item.off = false
                        } else if (it == mHolder.onWithoutTroublesome) {
                            item.asleep = false
                            item.on = false
                            item.onWithTroublesome = false
                            item.off = false
                        } else if (it == mHolder.off) {
                            item.asleep = false
                            item.on = false
                            item.onWithTroublesome = false
                            item.onWithoutTroublesome = false
                        }
                    }

                    notifyItemChanged(position)
                }

                mHolder.asleep.setOnClickListener(checkboxListener)
                mHolder.on.setOnClickListener(checkboxListener)
                mHolder.onWithTroublesome.setOnClickListener(checkboxListener)
                mHolder.onWithoutTroublesome.setOnClickListener(checkboxListener)
                mHolder.off.setOnClickListener(checkboxListener)
                mHolder.med1.setOnClickListener(checkboxListener)
                mHolder.med2.setOnClickListener(checkboxListener)

                // Ensure listeners correctly set the DairyModel state
                mHolder.med1.setOnCheckedChangeListener { _, isChecked ->
                    item.med1 = isChecked
                    list[position] = item
                }

                mHolder.med2.setOnCheckedChangeListener { _, isChecked ->
                    item.med2 = isChecked
                    list[position] = item
                }

                mHolder.symptomSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                        item.measurement = progress
                        list[position] = item
                    }

                    override fun onStartTrackingTouch(seekBar: SeekBar?) {}
                    override fun onStopTrackingTouch(seekBar: SeekBar?) {}
                })
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun getCurrentList(): List<DairyListItem> {
        return list
    }

    private fun showAlertDialog(view: View, title: String, message: String) {
        AlertDialog.Builder(view.context)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
            .show()
    }
//    private fun showAlertDialog(view: View, title: String, message: String) {
//        val inflater = LayoutInflater.from(view.context)
//        val dialogView = inflater.inflate(R.layout.dialog_alert, null)
//
//        val alertTitle = dialogView.findViewById<TextView>(R.id.alertTitle)
//        val alertMessage = dialogView.findViewById<TextView>(R.id.alertMessage)
//        val alertButton = dialogView.findViewById<Button>(R.id.alertButton)
//
//        alertTitle.text = title
//        alertMessage.text = message
//
//        val dialog = AlertDialog.Builder(view.context)
//            .setView(dialogView)
//            .create()
//
//        alertButton.setOnClickListener {
//            dialog.dismiss()
//        }
//
//        dialog.show()
//    }


}