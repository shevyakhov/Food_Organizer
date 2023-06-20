package com.chelz.foodorganizer.screens.profile.presentation

import androidx.lifecycle.ViewModel
import com.chelz.foodorganizer.screens.foodList.data.dao.StatisticsEntity
import com.chelz.foodorganizer.screens.profile.presentation.domain.ProfileRepository
import com.chelz.foodorganizer.screens.profile.presentation.navigation.ProfileNavigator
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.Calendar

class ProfileViewModel(private val navigator: ProfileNavigator, private val repository: ProfileRepository) : ViewModel() {

	val dateFlow = MutableStateFlow(initDateFlow())
	val statisticsFlow = MutableStateFlow<StatisticsEntity?>(null)

	private fun initDateFlow(): String {
		val calendar = Calendar.getInstance()
		val currentMonth = calendar.get(Calendar.MONTH) + 1
		val currentYear = calendar.get(Calendar.YEAR)
		val selectedMonthFormatted = String.format("%02d", currentMonth)
		return "$selectedMonthFormatted/$currentYear"
	}

	suspend fun getStatistics() = coroutineScope {
		statisticsFlow.value = repository.getByDate(dateFlow.value)
	}

	fun navigateToSettings() {
		navigator.navigateToSettingsFragment()
	}
}