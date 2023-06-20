package com.chelz.foodorganizer.screens.foodList.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.chelz.foodorganizer.screens.foodList.data.dao.FoodEntity
import com.chelz.foodorganizer.screens.foodList.data.dao.PlacementEntity
import com.chelz.foodorganizer.screens.foodList.data.dao.PlacementWithFood
import com.chelz.foodorganizer.screens.foodList.domain.FoodRepository
import com.chelz.foodorganizer.screens.foodList.presentation.navigation.FoodListFragmentNavigator
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class FoodListViewModel(
	private val navigator: FoodListFragmentNavigator,
	private val repository: FoodRepository,
	val app: Application,
) : AndroidViewModel(app) {

	companion object {

		const val ALL_FOOD_TEXT = "Все"
	}

	private val _placementFlow = MutableStateFlow<List<PlacementEntity>>(emptyList())
	val placementFlow: MutableStateFlow<List<PlacementEntity>>
		get() = _placementFlow
	private val _foodListFlow = MutableStateFlow<List<FoodEntity>>(emptyList())
	val foodListFlow: StateFlow<List<FoodEntity>>
		get() = _foodListFlow

	private val placementWithFood = MutableStateFlow<List<PlacementWithFood>>(emptyList())

	suspend fun getFoodListFlow() = coroutineScope {
		val flow = repository.getPlacementWithFood()
		_placementFlow.value = flow.map { it.placement ?: error("unexpected") }
		_foodListFlow.value = flow.flatMap { it.foods ?: emptyList() }.sortedBy { it.bestBefore }
		placementWithFood.value = flow
	}

	fun goToFoodDetailsFragment(id: Int) {
		navigator.goToFoodDetails(id)
	}

	fun goToAddFoodFragment() {
		navigator.goToAddFood()
	}

	suspend fun getPlacement(placementId: Int) = coroutineScope {
		repository.getPlacementById(placementId)
	}

	fun filterByTabText(text: String): List<FoodEntity> =
		when (text) {
			ALL_FOOD_TEXT -> foodListFlow.value
			else          -> placementWithFood.value.first { it.placement?.placementName == text }.foods ?: emptyList()
		}
}