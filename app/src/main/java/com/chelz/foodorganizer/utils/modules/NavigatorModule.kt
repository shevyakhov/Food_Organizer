package com.chelz.foodorganizer.utils.modules

import com.chelz.foodorganizer.application.activity.presentation.navigation.MainActivityNavigator
import com.chelz.foodorganizer.application.activity.presentation.navigation.MainActivityNavigatorDefault
import com.chelz.foodorganizer.application.mainFragment.presentation.navigation.MainFragmentNavigator
import com.chelz.foodorganizer.application.mainFragment.presentation.navigation.MainFragmentNavigatorDefault
import com.chelz.foodorganizer.screens.addFood.presentation.navigation.AddFoodFragmentNavigator
import com.chelz.foodorganizer.screens.addFood.presentation.navigation.AddFoodFragmentNavigatorDefault
import com.chelz.foodorganizer.screens.addRecipe.presentation.navigation.AddRecipeNavigator
import com.chelz.foodorganizer.screens.addRecipe.presentation.navigation.AddRecipeNavigatorDefault
import com.chelz.foodorganizer.screens.addToBuyList.presentation.navigation.AddToBuyListNavigator
import com.chelz.foodorganizer.screens.addToBuyList.presentation.navigation.AddToBuyListNavigatorDefault
import com.chelz.foodorganizer.screens.foodDetails.presentation.navigation.FoodDetailsFragmentNavigator
import com.chelz.foodorganizer.screens.foodDetails.presentation.navigation.FoodDetailsFragmentNavigatorDefault
import com.chelz.foodorganizer.screens.foodList.presentation.navigation.FoodListFragmentNavigator
import com.chelz.foodorganizer.screens.foodList.presentation.navigation.FoodListFragmentNavigatorDefault
import com.chelz.foodorganizer.screens.onboarding.presentation.navigation.OnBoardingFragmentNavigator
import com.chelz.foodorganizer.screens.onboarding.presentation.navigation.OnBoardingFragmentNavigatorDefault
import com.chelz.foodorganizer.screens.profile.presentation.navigation.ProfileNavigator
import com.chelz.foodorganizer.screens.profile.presentation.navigation.ProfileNavigatorDefault
import com.chelz.foodorganizer.screens.recipeDetails.presentation.navigation.RecipeDetailsNavigator
import com.chelz.foodorganizer.screens.recipeDetails.presentation.navigation.RecipeDetailsNavigatorDefault
import com.chelz.foodorganizer.screens.recipes.presentation.navigation.RecipesNavigator
import com.chelz.foodorganizer.screens.recipes.presentation.navigation.RecipesNavigatorDefault
import com.chelz.foodorganizer.screens.registration.presentation.navigation.RegistrationFragmentNavigator
import com.chelz.foodorganizer.screens.registration.presentation.navigation.RegistrationFragmentNavigatorDefault
import com.chelz.foodorganizer.screens.settings.presentation.navigation.SettingsNavigator
import com.chelz.foodorganizer.screens.settings.presentation.navigation.SettingsNavigatorDefault
import com.chelz.foodorganizer.screens.splash.presentation.navigation.SplashFragmentNavigator
import com.chelz.foodorganizer.screens.splash.presentation.navigation.SplashFragmentNavigatorDefault
import com.chelz.foodorganizer.screens.toBuyList.presentation.navigation.ToBuyListNavigator
import com.chelz.foodorganizer.screens.toBuyList.presentation.navigation.ToBuyListNavigatorDefault
import org.koin.dsl.module

val NavigatorModule = module {
	factory<MainActivityNavigator> { MainActivityNavigatorDefault(get()) }
	factory<MainFragmentNavigator> { MainFragmentNavigatorDefault(get()) }

	factory<SplashFragmentNavigator> { SplashFragmentNavigatorDefault(get()) }
	factory<OnBoardingFragmentNavigator> { OnBoardingFragmentNavigatorDefault(get()) }
	factory<RegistrationFragmentNavigator> { RegistrationFragmentNavigatorDefault(get()) }

	factory<FoodListFragmentNavigator> { FoodListFragmentNavigatorDefault(get()) }
	factory<AddFoodFragmentNavigator> { AddFoodFragmentNavigatorDefault(get()) }
	factory<FoodDetailsFragmentNavigator> { FoodDetailsFragmentNavigatorDefault(get()) }

	factory<RecipesNavigator> { RecipesNavigatorDefault(get()) }
	factory<AddRecipeNavigator> { AddRecipeNavigatorDefault(get()) }
	factory<RecipeDetailsNavigator> { RecipeDetailsNavigatorDefault(get()) }

	factory<ToBuyListNavigator> { ToBuyListNavigatorDefault(get()) }
	factory<AddToBuyListNavigator> { AddToBuyListNavigatorDefault(get()) }

	factory<ProfileNavigator> { ProfileNavigatorDefault(get()) }
	factory<SettingsNavigator> { SettingsNavigatorDefault(get()) }
}