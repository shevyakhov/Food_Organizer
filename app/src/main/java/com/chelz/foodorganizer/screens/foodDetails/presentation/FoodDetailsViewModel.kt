package com.chelz.foodorganizer.screens.foodDetails.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chelz.foodorganizer.screens.foodDetails.presentation.navigation.FoodDetailsFragmentNavigator
import com.chelz.foodorganizer.screens.foodList.data.dao.FoodEntity
import com.chelz.foodorganizer.screens.foodList.data.dao.PlacementEntity
import com.chelz.foodorganizer.screens.foodList.data.dao.StatisticsEntity
import com.chelz.foodorganizer.screens.foodList.domain.FoodRepository
import com.chelz.foodorganizer.utils.getFullDateFromString
import com.chelz.foodorganizer.utils.notifications.LocalNotificationManager
import com.chelz.foodorganizer.utils.roundFloatValueString
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date

class FoodDetailsViewModel(
	val foodId: Int,
	val repository: FoodRepository,
	private val navigator: FoodDetailsFragmentNavigator,
	private val notificationManager: LocalNotificationManager,
) : ViewModel() {

	private var _foodFlow = MutableStateFlow<FoodEntity?>(null)
	val foodFlow: StateFlow<FoodEntity?>
		get() = _foodFlow

	var nameFlow = MutableStateFlow<String?>(null)
	var imageFlow = MutableStateFlow<ByteArray?>(null)
	var allPlacementFlow = MutableStateFlow<List<PlacementEntity>?>(null)
	var placementFlow = MutableStateFlow<String?>(null)
	var bestBeforeFlow = MutableStateFlow<String?>(null)
	var bestBeforeFlowFirst = MutableStateFlow<String?>(null)
	var reminderFlow = MutableStateFlow<String?>(null)
	var countFlow = MutableStateFlow<String?>(null)


	init {
		val foodJob = CoroutineScope(viewModelScope.coroutineContext).async {
			_foodFlow.value = repository.getFoodById(foodId)
			placementFlow.value = repository.getPlacementById(_foodFlow.value!!.placement).placementName
		}
		CoroutineScope(viewModelScope.coroutineContext).launch {
			foodJob.await()
			allPlacementFlow.value = repository.getPlacements()
		}
		CoroutineScope(viewModelScope.coroutineContext).launch {
			foodJob.await()
			val triple = getConvertedDate()
			if (triple != null) {
				val dayConverted = triple.first
				val monthConverted = triple.second
				val year = triple.third
				bestBeforeFlowFirst.value = "$dayConverted$monthConverted$year"
				bestBeforeFlow.value = "$dayConverted$monthConverted$year"
			}
		}
		CoroutineScope(viewModelScope.coroutineContext).launch {
			foodJob.await()
			imageFlow.value = _foodFlow.value?.image
			reminderFlow.value = convertRemindersToText(_foodFlow.value?.remindIn)
		}
	}

	private fun convertRemindersToText(remindIn: Int?): String? =
		when (remindIn) {
			2    -> "За 2 дня"
			1    -> "За 1 день"
			else -> null
		}

	private fun getConvertedDate(): Triple<String, String, String>? {
		if (foodFlow.value?.bestBefore != null) {
			val cal = Calendar.getInstance().apply {
				time = Date(foodFlow.value?.bestBefore!!)
			}
			val year: Int = cal.get(Calendar.YEAR)
			val month: Int = cal.get(Calendar.MONTH) + 1
			val day: Int = cal.get(Calendar.DAY_OF_MONTH)
			var monthConverted = "" + month
			if (month < 10) {
				monthConverted = "0$monthConverted"
			}
			var dayConverted = "" + day
			if (day < 10) {
				dayConverted = "0$dayConverted"
			}
			return Triple(dayConverted, monthConverted, year.toString())
		}

		return null
	}

	fun navigateBack() {

		val update = CoroutineScope(Dispatchers.IO).async {
			isChanged()
		}
		CoroutineScope(Dispatchers.IO).launch {
			update.await()
			navigator.navigateBack()
		}
	}

	private suspend fun isChanged(): Boolean = coroutineScope {
		val name = nameFlow.value.orEmpty()
		val placement = placementFlow.value.orEmpty()
		val image = imageFlow.value ?: ByteArray(0)

		if (allPlacementFlow.value?.map { it.placementName }?.contains(placement) == false) {
			repository.insertPlacement(PlacementEntity(placementName = placement))
		}
		val placementId: PlacementEntity = repository.getPlacement(placement)
		val itemCount = countFlow.value.orEmpty()
		val bestBefore = getFullDateFromString(bestBeforeFlow.value.orEmpty())
		val remindIn = getReminder()

		val new = FoodEntity(
			foodId = foodId,
			name = name,
			placement = placementId.placementId!!,
			image = image,
			itemCount = itemCount,
			bestBefore = bestBefore,
			remindIn = remindIn
		)
		if (new != foodFlow.first()) {
			bestBefore?.let {
				if (remindIn != null) {
					notificationManager.scheduleNotification(foodId, name, it, remindIn)
				}
			}
			repository.updateFood(new)
		}
		true
	}

	private fun getReminder(): Int? =
		when (reminderFlow.value) {
			"За 2 дня"  -> 2
			"За 1 день" -> 1
			else        -> null
		}

	fun markAsTrashedFood(trashedKg: Float) {
		CoroutineScope(Dispatchers.IO).launch {
			val cal = Calendar.getInstance()
			val month = cal.get(Calendar.MONTH) + 1
			val year = cal.get(Calendar.YEAR)
			val selectedMonthFormatted = String.format("%02d", month)
			val selectedDate = "$selectedMonthFormatted/$year"

			val date = repository.getByDateStatistics(selectedDate)
			if (date != null) {
				date.trashedNumber += trashedKg
				repository.updateStatistics(date)
			} else {
				repository.insertStatistics(StatisticsEntity(selectedDate, trashedKg, 1f))
			}
			foodFlow.value?.let {
				if (trashedKg == countFlow.value?.toFloat())
					repository.deleteFood(it)
			}
			countFlow.value = roundFloatValueString(countFlow.value?.toFloat()?.minus(trashedKg) ?: 0f)
			notificationManager.cancelScheduledNotification(foodId)
			navigateBack()
		}

	}

	fun markAsConsumedFood(consumedKg: Float) {
		CoroutineScope(Dispatchers.IO).launch {
			val cal = Calendar.getInstance()
			val month = cal.get(Calendar.MONTH) + 1
			val year = cal.get(Calendar.YEAR)
			val selectedMonthFormatted = String.format("%02d", month)
			val selectedDate = "$selectedMonthFormatted/$year"

			val date = repository.getByDateStatistics(selectedDate)
			if (date != null) {
				date.consumedNumber += consumedKg
				repository.updateStatistics(date)
			} else {
				repository.insertStatistics(StatisticsEntity(selectedDate, consumedKg, 0f))
			}
			foodFlow.value?.let {
				if (consumedKg == countFlow.value?.toFloat())
					repository.deleteFood(it)
			}
			countFlow.value = roundFloatValueString(countFlow.value?.toFloat()?.minus(consumedKg) ?: 0f)
			notificationManager.cancelScheduledNotification(foodId)
			navigateBack()

		}
	}

}