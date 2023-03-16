package com.chelz.foodorganizer.application.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.chelz.foodorganizer.R
import com.chelz.foodorganizer.application.activity.presentation.MainActivityViewModel
import com.chelz.foodorganizer.utils.modules.GlobalNavigatorName.GLOBAL
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.androidx.AppNavigator
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.qualifier.named

class MainActivity : AppCompatActivity() {

	private val navigatorHolder: NavigatorHolder by inject(named(GLOBAL))
	private val navigator = AppNavigator(this, R.id.global_host)

	private val viewModel by viewModel<MainActivityViewModel>()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)
		navigatorHolder.setNavigator(navigator)
		viewModel.openMainRoot()
	}
	override fun onDestroy() {
		super.onDestroy()
		navigatorHolder.removeNavigator()

	}
}