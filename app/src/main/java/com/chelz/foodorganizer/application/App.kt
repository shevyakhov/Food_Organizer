package com.chelz.foodorganizer.application

import android.app.Application
import com.chelz.foodorganizer.utils.modules.AddFoodModule
import com.chelz.foodorganizer.utils.modules.AppModule
import com.chelz.foodorganizer.utils.modules.FoodDetailsModule
import com.chelz.foodorganizer.utils.modules.FoodListModule
import com.chelz.foodorganizer.utils.modules.MainFragmentModule
import com.chelz.foodorganizer.utils.modules.OnBoardingModule
import com.chelz.foodorganizer.utils.modules.NavigatorModule
import com.chelz.foodorganizer.utils.modules.RegistrationModule
import com.chelz.foodorganizer.utils.modules.SplashModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {

	override fun onCreate() {
		super.onCreate()
		INSTANCE = this

		startKoin {
			androidContext(this@App)
			modules(AppModule)
			modules(NavigatorModule)
			modules(SplashModule)
			modules(OnBoardingModule)
			modules(FoodListModule)
			modules(MainFragmentModule)
			modules(RegistrationModule)
			modules(FoodDetailsModule)
			modules(AddFoodModule)
		}
	}

	companion object {

		internal lateinit var INSTANCE: App
			private set
	}
}