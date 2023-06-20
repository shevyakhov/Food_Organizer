package com.chelz.foodorganizer.utils

import kotlin.math.round

fun roundFloatValueString(floatValue: Float): String {
	return (round(floatValue * 100) / 100).toString()
}