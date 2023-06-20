package com.chelz.foodorganizer.screens.toBuyList.presentation

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.chelz.foodorganizer.screens.foodList.data.dao.FoodEntity
import com.chelz.foodorganizer.screens.settings.presentation.SettingsViewModel
import com.chelz.foodorganizer.screens.toBuyList.data.dao.ToBuyListEntity
import com.chelz.foodorganizer.screens.toBuyList.domain.ToBuyListRepository
import com.chelz.foodorganizer.screens.toBuyList.presentation.navigation.ToBuyListNavigator
import com.chelz.foodorganizer.utils.textListeners.StringSimilarity
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ToBuyListViewModel(val navigator: ToBuyListNavigator, private val repository: ToBuyListRepository, val app: Application) :
	AndroidViewModel(app) {

	private val _toBuyListFlow = MutableStateFlow<List<ToBuyListEntity>>(emptyList())
	val toBuyListFlow: StateFlow<List<ToBuyListEntity>>
		get() = _toBuyListFlow
	private val _addDialogFlow = MutableStateFlow(false)
	val addDialogFlow: StateFlow<Boolean>
		get() = _addDialogFlow

	init {
		getDialogSharedPref()
	}

	suspend fun getToBuyList() = coroutineScope {
		val flow = repository.getAllToBuyLists()
		_toBuyListFlow.value = flow
		toBuyListFlow
	}

	fun navigateToAddToBuyList() {
		navigator.navigateToToBuyListFragment()
	}

	private fun addItemToBuyList(item: ToBuyListEntity) {
		viewModelScope.launch {
			repository.insertToBuyList(item)
		}
	}

	suspend fun removeItemFromBuyList(id: Int) = coroutineScope {
		val item = getItemById(id)
		val list = _toBuyListFlow.value.toMutableList()
		list.remove(item)
		_toBuyListFlow.value = list
		repository.deleteToBuyList(item)
	}

	fun getItemById(id: Int) = _toBuyListFlow.value.first { it.id == id }

	suspend fun notifyItemChecked(itemId: Int, productName: String, isChecked: Boolean) {
		val items = _toBuyListFlow.value.first { it.id == itemId }.items
		items.first { it.name == productName }.isTicked = isChecked
		_toBuyListFlow.value.first { it.id == itemId }.items = items
		repository.updateToBuyList(_toBuyListFlow.value.first { it.id == itemId })
	}

	fun addItem(position: Int, item: ToBuyListEntity) {
		val newList = _toBuyListFlow.value.toMutableList().apply {
			add(position, item)
		}
		_toBuyListFlow.value = newList
		addItemToBuyList(item)
	}


	suspend fun getAllFood() = repository.getAllFood()
	suspend fun addFoodEntity(foodEntity: FoodEntity) = repository.insertFood(foodEntity)
	suspend fun getAllPlacements() = repository.getAllPlacements()

	private fun getDialogSharedPref() {
		val value = app.applicationContext.getSharedPreferences(SettingsViewModel.BUY_DIALOG_PREFERENCE, Context.MODE_PRIVATE)
			.getBoolean(SettingsViewModel.BUY_DIALOG_PREFERENCE_KEY, true)
		_addDialogFlow.value = value
	}
}
