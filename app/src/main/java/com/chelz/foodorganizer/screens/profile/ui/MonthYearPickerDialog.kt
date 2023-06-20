package com.chelz.foodorganizer.screens.profile.ui

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.NumberPicker
import androidx.fragment.app.DialogFragment
import com.chelz.foodorganizer.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.util.Calendar

class MonthYearPickerDialog : DialogFragment() {

	private var listener: DatePickerDialog.OnDateSetListener? = null
	fun setListener(listener: DatePickerDialog.OnDateSetListener?) {
		this.listener = listener
	}

	override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
		val builder = MaterialAlertDialogBuilder(requireContext())
		val inflater: LayoutInflater = requireActivity().layoutInflater
		val cal = Calendar.getInstance()
		val dialog: View = inflater.inflate(R.layout.date_picker_dialog, null)
		val monthPicker = dialog.findViewById<View>(R.id.picker_month) as NumberPicker
		val yearPicker = dialog.findViewById<View>(R.id.picker_year) as NumberPicker
		monthPicker.minValue = 1
		monthPicker.maxValue = 12
		monthPicker.value = cal[Calendar.MONTH] + 1
		val year = cal[Calendar.YEAR]
		yearPicker.minValue = MIN_YEAR
		yearPicker.maxValue = year
		yearPicker.value = year
		builder.setView(dialog)
			.setPositiveButton(android.R.string.ok) { _, _ -> listener!!.onDateSet(null, yearPicker.value, monthPicker.value, 0) }
			.setNegativeButton(R.string.cancel) { _, _ -> this@MonthYearPickerDialog.dialog?.cancel() }
		return builder.create()
	}

	companion object {

		private const val MIN_YEAR = 2020
	}
}