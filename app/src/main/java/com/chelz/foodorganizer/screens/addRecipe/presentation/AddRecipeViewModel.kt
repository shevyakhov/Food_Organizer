package com.chelz.foodorganizer.screens.addRecipe.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chelz.foodorganizer.screens.addRecipe.presentation.navigation.AddRecipeNavigator
import com.chelz.foodorganizer.screens.recipes.data.dao.RecipeEntity
import com.chelz.foodorganizer.screens.recipes.data.dao.RecipeProduct
import com.chelz.foodorganizer.screens.recipes.data.dao.RecipeTypeEntity
import com.chelz.foodorganizer.screens.recipes.domain.RecipesRepository
import com.chelz.foodorganizer.utils.getTimeFromString
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class AddRecipeViewModel(private val navigator: AddRecipeNavigator, private val repository: RecipesRepository) : ViewModel() {

	val nameFlow = MutableStateFlow<String?>(null)
	val typeFlow = MutableStateFlow<String?>(null)
	val difficultyFlow = MutableStateFlow<Int?>(1)
	val productsFlow = MutableStateFlow<List<RecipeProduct>>(emptyList())
	val timeToCookFlow = MutableStateFlow<String?>(null)
	val imageFlow = MutableStateFlow<ByteArray?>(null)
	val allTypeFlow = MutableStateFlow<List<RecipeTypeEntity>?>(null)

	init {
		viewModelScope.launch {
			allTypeFlow.value = repository.getAllRecipeTypes()
		}
	}

	fun addRecipe(recipe: RecipeEntity) {
		viewModelScope.launch {
			repository.insertRecipe(recipe)
		}
	}

	suspend fun addItem() {
		val typeValue = typeFlow.value
		val id: Int = if (allTypeFlow.value?.map { it.typeName }?.contains(typeValue) == false) {
			val type = RecipeTypeEntity(typeName = typeFlow.value ?: error("тип"))
			repository.insertRecipeType(type)
		} else
			allTypeFlow.value?.find { it.typeName == typeValue }?.id ?: error("тип")
		val recipeEntity = RecipeEntity(
			name = nameFlow.value ?: error("имя"),
			typeId = id,
			image = imageFlow.value ?: ByteArray(0),
			difficulty = difficultyFlow.value ?: error("количество"),
			timeToCook = timeToCookFlow.value?.let { getTimeFromString(it) },
			products = productsFlow.value
		)

		Log.e("ITEM", recipeEntity.toString())
		repository.insertRecipe(recipeEntity)
		navigator.navigateBack()
	}
}