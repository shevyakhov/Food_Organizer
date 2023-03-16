package com.chelz.foodorganizer.screens.foodList.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.chelz.foodorganizer.screens.foodList.presentation.navigation.FoodListFragmentNavigator

class FoodListViewModel(private val navigator: FoodListFragmentNavigator,val app: Application) : AndroidViewModel(app) {

	fun goToFoodDetailsFragment(id: Int) {
		navigator.goToFoodDetails(id)
	}

	fun goToAddFoodFragment() {
		navigator.goToAddFood()
	}

}