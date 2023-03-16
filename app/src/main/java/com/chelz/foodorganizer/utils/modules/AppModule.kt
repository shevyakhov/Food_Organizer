package com.chelz.foodorganizer.utils.modules

import com.chelz.foodorganizer.application.activity.presentation.MainActivityViewModel
import com.chelz.foodorganizer.utils.modules.GlobalNavigatorName.GLOBAL
import com.chelz.foodorganizer.utils.modules.MainNavigatorName.MAIN
import com.chelz.foodorganizer.utils.navigators.GlobalNavigator
import com.chelz.foodorganizer.utils.navigators.GlobalNavigatorDefault
import com.chelz.foodorganizer.utils.navigators.MainNavigator
import com.chelz.foodorganizer.utils.navigators.MainNavigatorDefault
import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.Router
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

object GlobalNavigatorName {

	const val GLOBAL = "GLOBAL"
}

object MainNavigatorName {

	const val MAIN = "MAIN"
}

fun buildCicerone(): Cicerone<Router> = Cicerone.create()

val AppModule = module {

	single(named(GLOBAL)) { buildCicerone() }
	single(named(GLOBAL)) { get<Cicerone<Router>>(named(GLOBAL)).router }
	single(named(GLOBAL)) { get<Cicerone<Router>>(named(GLOBAL)).getNavigatorHolder() }
	single<GlobalNavigator> { GlobalNavigatorDefault(get(named(GLOBAL))) }

	single(named(MAIN)) { buildCicerone() }
	single(named(MAIN)) { get<Cicerone<Router>>(named(MAIN)).router }
	single(named(MAIN)) { get<Cicerone<Router>>(named(MAIN)).getNavigatorHolder() }
	single<MainNavigator> { MainNavigatorDefault(get(named(MAIN))) }

	viewModel { MainActivityViewModel(navigator = get()) }
}