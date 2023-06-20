package com.chelz.foodorganizer.screens.recipes.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.chelz.foodorganizer.screens.recipes.data.dao.RecipeEntity
import com.chelz.foodorganizer.screens.recipes.data.dao.RecipeTypeEntity
import com.chelz.foodorganizer.screens.recipes.domain.RecipesRepository
import com.chelz.foodorganizer.screens.recipes.presentation.navigation.RecipesNavigator
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class RecipesViewModel(
	private val repository: RecipesRepository,
	private val navigator: RecipesNavigator,
	val app: Application,
) :
	AndroidViewModel(app) {

	private val _recipesFlow = MutableStateFlow<List<RecipeEntity>>(emptyList())
	val recipesFlow: StateFlow<List<RecipeEntity>>
		get() = _recipesFlow
	private val _recipeTypesFlow = MutableStateFlow<List<RecipeTypeEntity>>(emptyList())
	val recipeTypesFlow: StateFlow<List<RecipeTypeEntity>>
		get() = _recipeTypesFlow

	suspend fun getAllRecipes(): Flow<List<RecipeEntity>> = coroutineScope {
		val flow = repository.getAllRecipes()
		_recipesFlow.value = flow
		recipesFlow
	}

	suspend fun getAllRecipeTypes(): Flow<List<RecipeTypeEntity>> = coroutineScope {
		val flow = repository.getAllRecipeTypes()
		_recipeTypesFlow.value = flow
		recipeTypesFlow
	}

	fun goToRecipeDetailsFragment(id: Int) {
		navigator.goToRecipeDetails(id)
	}

	fun goToAddRecipeFragment() {
		navigator.goToAddRecipe()
	}
}