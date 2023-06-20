package com.chelz.foodorganizer.screens.settings.presentation

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import com.chelz.foodorganizer.screens.foodList.data.dao.PlacementEntity
import com.chelz.foodorganizer.screens.profile.presentation.domain.ProfileRepository
import com.chelz.foodorganizer.screens.recipes.data.dao.RecipeTypeEntity
import com.chelz.foodorganizer.screens.settings.presentation.navigation.SettingsNavigator
import com.chelz.foodorganizer.utils.notifications.LocalNotificationManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class SettingsViewModel(
	private val navigator: SettingsNavigator,
	private val notificationManager: LocalNotificationManager,
	private val app: Application,
	private val repository: ProfileRepository,
) : AndroidViewModel(app) {

	val allRecipeTypeFlow = MutableStateFlow<List<RecipeTypeEntity>?>(null)
	var allPlacementFlow = MutableStateFlow<List<PlacementEntity>?>(null)

	val preferredTimeFlow = MutableStateFlow<String?>(null)
	val dialogBuyListFlow = MutableStateFlow(false)

	init {
		CoroutineScope(Dispatchers.IO).launch {
			getRecipeTypes()
			getPlacements()
			getPreferredTime()
			getDialogSharedPref()
		}
	}

	companion object {

		const val TIME_PREFERENCE = "TIME_PREFERENCE"
		const val TIME_PREFERENCE_KEY = "TIME_PREFERENCE_KEY"
		const val BUY_DIALOG_PREFERENCE = "BUY_DIALOG_PREFERENCE"
		const val BUY_DIALOG_PREFERENCE_KEY = "BUY_DIALOG_PREFERENCE_KEY"
	}

	fun savePreferredTime() {
		val sharedPreferences = app.applicationContext.getSharedPreferences(TIME_PREFERENCE, Context.MODE_PRIVATE)
		val editor = sharedPreferences.edit()

		preferredTimeFlow.value?.let {
			editor.putString(TIME_PREFERENCE_KEY, it)
		}
		editor.apply()
		CoroutineScope(Dispatchers.IO).launch {
			val items = repository.getFood()
			items.forEach {
				if (it.remindIn != null && it.bestBefore != null) {
					notificationManager.scheduleNotification(
						it.foodId!!,
						it.name,
						it.bestBefore,
						it.remindIn
					)
				}
			}
		}

	}

	private fun getPreferredTime() {
		val value = app.applicationContext.getSharedPreferences(TIME_PREFERENCE, Context.MODE_PRIVATE).getString(TIME_PREFERENCE_KEY, "14:00")
		preferredTimeFlow.value = value

	}

	fun saveDialogSharedPref() {
		val sharedPreferences = app.applicationContext.getSharedPreferences(BUY_DIALOG_PREFERENCE, Context.MODE_PRIVATE)
		val editor = sharedPreferences.edit()

		dialogBuyListFlow.value.let {
			editor.putBoolean(BUY_DIALOG_PREFERENCE_KEY, it)
		}
		editor.apply()
	}

	private fun getDialogSharedPref() {
		val value = app.applicationContext.getSharedPreferences(BUY_DIALOG_PREFERENCE, Context.MODE_PRIVATE).getBoolean(BUY_DIALOG_PREFERENCE_KEY, false)
		dialogBuyListFlow.value = value
	}

	fun addPlacement(placement: String) {
		CoroutineScope(Dispatchers.IO).launch {
			repository.insertPlacement(PlacementEntity(placementName = placement))
			getPlacements()
		}
	}

	fun updatePlacement(id: Int, placement: String) {
		CoroutineScope(Dispatchers.IO).launch {
			repository.updatePlacement(PlacementEntity(id, placement))
			getPlacements()
		}
	}

	fun deletePlacement(placementEntity: PlacementEntity) {
		CoroutineScope(Dispatchers.IO).launch {
			repository.deletePlacement(placementEntity)
			getPlacements()
		}
	}

	private suspend fun getPlacements() {
		allPlacementFlow.value = repository.getPlacements()
	}

	fun addRecipeType(type: String) {
		CoroutineScope(Dispatchers.IO).launch {
			repository.insertRecipeType(RecipeTypeEntity(typeName = type))
			getRecipeTypes()
		}
	}

	fun updateRecipeType(id: Int, type: String) {
		CoroutineScope(Dispatchers.IO).launch {
			repository.updateRecipeType(RecipeTypeEntity(id, type))
			getRecipeTypes()
		}
	}

	fun deleteRecipeType(typeEntity: RecipeTypeEntity) {
		CoroutineScope(Dispatchers.IO).launch {
			repository.deleteRecipeType(typeEntity)
			getRecipeTypes()
		}
	}

	private suspend fun getRecipeTypes() {
		allRecipeTypeFlow.value = repository.getAllRecipeTypes()
	}

}