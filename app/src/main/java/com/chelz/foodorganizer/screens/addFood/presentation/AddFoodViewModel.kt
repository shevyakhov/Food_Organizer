package com.chelz.foodorganizer.screens.addFood.presentation

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import com.chelz.foodorganizer.screens.addFood.presentation.navigation.AddFoodFragmentNavigator
import com.chelz.foodorganizer.screens.foodList.data.dao.FoodEntity
import com.chelz.foodorganizer.screens.foodList.data.dao.PlacementEntity
import com.chelz.foodorganizer.screens.foodList.domain.FoodRepository
import com.chelz.foodorganizer.utils.notifications.LocalNotificationManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar

class AddFoodViewModel(
	private val navigator: AddFoodFragmentNavigator,
	private val repository: FoodRepository,
	private val notificationManager: LocalNotificationManager,
) : ViewModel() {

	val nameFlow = MutableStateFlow<String?>(null)
	val bestBeforeFlow = MutableStateFlow<String?>(null)
	val quantityFlow = MutableStateFlow<String?>(null)
	val reminderFlow = MutableStateFlow<String?>(null)
	val placementFlow = MutableStateFlow<String?>(null)
	val imageFlow = MutableStateFlow<ByteArray?>(null)

	private val _everyPlacementFlow = MutableStateFlow<List<PlacementEntity>>(emptyList())
	val everyPlacementFlow: StateFlow<List<PlacementEntity>>
		get() = _everyPlacementFlow

	fun navigateBack() {
		navigator.navigateBack()
	}

	suspend fun init() {
		_everyPlacementFlow.value = repository.getPlacements()
	}

	@SuppressLint("SimpleDateFormat")
	private fun getBestBeforeDate(): Long? {
		return try {
			val cal = Calendar.getInstance().apply {
				set(Calendar.HOUR_OF_DAY, 0)
				set(Calendar.MINUTE, 0)
				set(Calendar.SECOND, 0)
				timeInMillis = SimpleDateFormat("dd/MM/yyyy").parse(bestBeforeFlow.value.orEmpty())?.time!!
			}
			cal.time.time
		} catch (e: ParseException) {
			null
		}
	}

	suspend fun addItem() {
		if (!placementFlow.value.isNullOrEmpty()) {
			val placementName = placementFlow.value.toString()
			if (!everyPlacementFlow.value.map { it.placementName }.contains(placementName)) {
				repository.insertPlacement(PlacementEntity(placementName = placementName))
			}
			val bestBefore = getBestBeforeDate()
			val reminder = getReminder()
			val placement = repository.getPlacement(placementFlow.value.toString())
			val foodEntity = FoodEntity(
				name = nameFlow.value ?: error("name"),
				placement = placement.placementId ?: error("placement"),
				image = imageFlow.value ?: ByteArray(0),
				itemCount = quantityFlow.value ?: error("itemCount"),
				bestBefore = bestBefore,
				remindIn = reminder
			)
			val foodId = repository.insertFood(foodEntity).toInt()
			bestBefore?.let {
				if (reminder != null) {
					notificationManager.scheduleNotification(foodId, foodEntity.name, it, reminder)
				}
			}
		}
		navigateBack()
	}

	private fun getReminder(): Int? =
		when (reminderFlow.value) {
			"За 2 дня"  -> 2
			"За 1 день" -> 1

			else        -> null
		}
}
