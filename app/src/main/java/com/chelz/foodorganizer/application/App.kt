package com.chelz.foodorganizer.application

import android.app.Application
import com.chelz.foodorganizer.utils.modules.AddFoodModule
import com.chelz.foodorganizer.utils.modules.AddRecipeModule
import com.chelz.foodorganizer.utils.modules.AddToBuyListModule
import com.chelz.foodorganizer.utils.modules.AppModule
import com.chelz.foodorganizer.utils.modules.FoodDataModule
import com.chelz.foodorganizer.utils.modules.FoodDetailsModule
import com.chelz.foodorganizer.utils.modules.FoodListModule
import com.chelz.foodorganizer.utils.modules.MainFragmentModule
import com.chelz.foodorganizer.utils.modules.NavigatorModule
import com.chelz.foodorganizer.utils.modules.OnBoardingModule
import com.chelz.foodorganizer.utils.modules.ProfileModule
import com.chelz.foodorganizer.utils.modules.RecipeDetailsModule
import com.chelz.foodorganizer.utils.modules.RecipesModule
import com.chelz.foodorganizer.utils.modules.RegistrationModule
import com.chelz.foodorganizer.utils.modules.SettingsModule
import com.chelz.foodorganizer.utils.modules.SplashModule
import com.chelz.foodorganizer.utils.modules.ToBuyListModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {

	override fun onCreate() {
		super.onCreate()
		INSTANCE = this

		startKoin {
			androidContext(this@App)
			modules(AppModule)
			modules(MainFragmentModule)
			modules(NavigatorModule)

			modules(FoodDataModule)

			modules(SplashModule)
			modules(OnBoardingModule)
			modules(RegistrationModule)

			modules(FoodListModule)
			modules(AddFoodModule)
			modules(FoodDetailsModule)

			modules(RecipesModule)
			modules(AddRecipeModule)
			modules(RecipeDetailsModule)

			modules(ToBuyListModule)
			modules(AddToBuyListModule)

			modules(ProfileModule)
			modules(SettingsModule)
		}
	}

	companion object {

		internal lateinit var INSTANCE: App
			private set
	}
}