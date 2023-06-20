package com.chelz.foodorganizer.utils

import android.annotation.SuppressLint
import android.icu.text.SimpleDateFormat
import java.text.ParseException
import java.util.Calendar
import java.util.Locale

@SuppressLint("SimpleDateFormat")
fun getFullDateFromString(time:String): Long? {
	return try {
		val cal = Calendar.getInstance().apply {
			set(Calendar.HOUR_OF_DAY, 0)
			set(Calendar.MINUTE, 0)
			set(Calendar.SECOND, 0)
			timeInMillis = java.text.SimpleDateFormat("dd/MM/yyyy").parse(time)?.time!!
		}
		cal.time.time
	} catch (e: ParseException) {
		null
	}
}
@SuppressLint("SimpleDateFormat")
fun getTimeStringFromLong(time: Long): String? {
	val calendar = Calendar.getInstance().apply {
		timeInMillis = time
	}
	val formatter = SimpleDateFormat("HH:mm")
	return formatter.format(calendar.time)
}

@SuppressLint("SimpleDateFormat")
fun getTimeFromString(time: String): Long? {
	val df = SimpleDateFormat("HH:mm")
	return try {
		df.parse(time).time
	} catch (e: Exception) {
		null
	}
}