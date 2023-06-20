package com.chelz.foodorganizer.utils.modules

import com.chelz.foodorganizer.screens.addToBuyList.presentation.AddToBuyListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val AddToBuyListModule = module {
	viewModel {
		AddToBuyListViewModel(
			navigator = get(),
			repository = get(),
		)
	}
}