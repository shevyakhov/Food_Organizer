package com.chelz.foodorganizer.utils.modules

import com.chelz.foodorganizer.screens.toBuyList.presentation.ToBuyListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val ToBuyListModule = module {
	viewModel {
		ToBuyListViewModel(
			navigator = get(),
			repository = get(),
			app = get()
		)
	}
}