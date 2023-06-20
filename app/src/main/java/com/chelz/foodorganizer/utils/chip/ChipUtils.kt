package com.chelz.foodorganizer.utils.chip

import android.annotation.SuppressLint
import android.content.Context
import com.google.android.material.chip.Chip

@SuppressLint("ResourceType")
fun createChip(context: Context, chipName: String): Chip {
	return Chip(context).apply {
		text = chipName
		isCheckable = true
		isCheckedIconVisible = true
	}
}