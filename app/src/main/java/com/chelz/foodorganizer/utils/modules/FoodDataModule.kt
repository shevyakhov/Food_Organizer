package com.chelz.foodorganizer.utils.modules

import androidx.room.Room
import com.chelz.foodorganizer.screens.foodList.data.FoodDataBase
import com.chelz.foodorganizer.screens.foodList.data.LocalFoodDataSource
import com.chelz.foodorganizer.screens.foodList.data.LocalFoodDataSourceDefault
import com.chelz.foodorganizer.screens.foodList.data.LocalStatisticsDataSource
import com.chelz.foodorganizer.screens.foodList.data.LocalStatisticsDataSourceDefault
import com.chelz.foodorganizer.screens.foodList.domain.FoodRepository
import com.chelz.foodorganizer.screens.foodList.domain.FoodRepositoryDefault
import com.chelz.foodorganizer.screens.profile.presentation.domain.ProfileRepository
import com.chelz.foodorganizer.screens.profile.presentation.domain.ProfileRepositoryDefault
import com.chelz.foodorganizer.screens.recipes.data.LocalRecipesDataSource
import com.chelz.foodorganizer.screens.recipes.data.LocalRecipesDataSourceDefault
import com.chelz.foodorganizer.screens.recipes.domain.RecipesRepository
import com.chelz.foodorganizer.screens.recipes.domain.RecipesRepositoryDefault
import com.chelz.foodorganizer.screens.toBuyList.data.LocalToBuyListDataSource
import com.chelz.foodorganizer.screens.toBuyList.data.LocalToBuyListDataSourceDefault
import com.chelz.foodorganizer.screens.toBuyList.domain.ToBuyListRepository
import com.chelz.foodorganizer.screens.toBuyList.domain.ToBuyListRepositoryDefault
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val FoodDataModule = module {

	single {
		Room.databaseBuilder(androidContext(), FoodDataBase::class.java, FoodDataBase.FoodDataBaseName)
			.fallbackToDestructiveMigration()
			.build()
	}
	single { get<FoodDataBase>().foodDao() }
	single { get<FoodDataBase>().recipesDao() }
	single { get<FoodDataBase>().toBuyListDao() }
	single { get<FoodDataBase>().statisticsDao() }

	single<LocalFoodDataSource> { LocalFoodDataSourceDefault(get()) }
	single<LocalRecipesDataSource> { LocalRecipesDataSourceDefault(get()) }
	single<LocalToBuyListDataSource> { LocalToBuyListDataSourceDefault(get()) }
	single<LocalStatisticsDataSource> { LocalStatisticsDataSourceDefault(get()) }


	factory<RecipesRepository> {
		RecipesRepositoryDefault(localDataSource = get())
	}

	factory<FoodRepository> {
		FoodRepositoryDefault(localFoodDataSource = get(), localStatisticsDataSource = get())
	}

	factory<ToBuyListRepository> {
		ToBuyListRepositoryDefault(localToBuyListDataSource = get(), localFoodDataSource = get())
	}

	factory<ProfileRepository> {
		ProfileRepositoryDefault(recipesDataSource = get(), foodDataSource = get(), toBuyListDataSource = get(), statisticsDataSource = get())
	}
}