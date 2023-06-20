package com.chelz.foodorganizer.application.mainFragment

sealed class MainScreenValue {

	object FoodList : MainScreenValue()
	object Recipes : MainScreenValue()
	object ToBuyList : MainScreenValue()
	object Profile : MainScreenValue()
}