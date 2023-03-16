package com.chelz.foodorganizer.utils.modules

import com.chelz.foodorganizer.application.activity.presentation.navigation.MainActivityNavigator
import com.chelz.foodorganizer.application.activity.presentation.navigation.MainActivityNavigatorDefault
import com.chelz.foodorganizer.application.mainFragment.presentation.navigation.MainFragmentNavigator
import com.chelz.foodorganizer.application.mainFragment.presentation.navigation.MainFragmentNavigatorDefault
import com.chelz.foodorganizer.screens.addFood.presentation.navigation.AddFoodFragmentNavigator
import com.chelz.foodorganizer.screens.addFood.presentation.navigation.AddFoodFragmentNavigatorDefault
import com.chelz.foodorganizer.screens.foodDetails.presentation.navigation.FoodDetailsFragmentNavigator
import com.chelz.foodorganizer.screens.foodDetails.presentation.navigation.FoodDetailsFragmentNavigatorDefault
import com.chelz.foodorganizer.screens.foodList.presentation.navigation.FoodListFragmentNavigator
import com.chelz.foodorganizer.screens.foodList.presentation.navigation.FoodListFragmentNavigatorDefault
import com.chelz.foodorganizer.screens.onboarding.presentation.navigation.OnBoardingFragmentNavigator
import com.chelz.foodorganizer.screens.onboarding.presentation.navigation.OnBoardingFragmentNavigatorDefault
import com.chelz.foodorganizer.screens.registration.presentation.navigation.RegistrationFragmentNavigator
import com.chelz.foodorganizer.screens.registration.presentation.navigation.RegistrationFragmentNavigatorDefault
import com.chelz.foodorganizer.screens.splash.presentation.navigation.SplashFragmentNavigator
import com.chelz.foodorganizer.screens.splash.presentation.navigation.SplashFragmentNavigatorDefault
import org.koin.dsl.module

val NavigatorModule = module {

	factory<MainActivityNavigator> { MainActivityNavigatorDefault(get()) }

	factory<SplashFragmentNavigator> { SplashFragmentNavigatorDefault(get()) }

	factory<OnBoardingFragmentNavigator> { OnBoardingFragmentNavigatorDefault(get()) }

	factory<MainFragmentNavigator> { MainFragmentNavigatorDefault(get()) }

	factory<RegistrationFragmentNavigator> { RegistrationFragmentNavigatorDefault(get()) }

	factory<FoodListFragmentNavigator> { FoodListFragmentNavigatorDefault(get()) }

	factory<FoodDetailsFragmentNavigator> { FoodDetailsFragmentNavigatorDefault(get()) }

	factory<AddFoodFragmentNavigator> { AddFoodFragmentNavigatorDefault(get()) }

}