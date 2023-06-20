package com.chelz.foodorganizer.screens.recipeDetails.presentation

sealed class RecipeDetailsUiState {
	object Edit : RecipeDetailsUiState()
	object Review : RecipeDetailsUiState()
}