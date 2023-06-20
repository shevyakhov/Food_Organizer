package com.chelz.foodorganizer.utils.modules

import com.chelz.foodorganizer.application.mainFragment.presentation.MainFragmentViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val MainFragmentModule = module {
	viewModel {
		MainFragmentViewModel(
			navigator = get()
		)
	}
}