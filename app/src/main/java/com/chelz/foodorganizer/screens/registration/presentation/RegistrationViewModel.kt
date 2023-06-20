package com.chelz.foodorganizer.screens.registration.presentation

import android.app.Application
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import androidx.lifecycle.AndroidViewModel
import com.chelz.foodorganizer.screens.registration.presentation.navigation.RegistrationFragmentNavigator
import kotlinx.coroutines.flow.MutableStateFlow

class RegistrationViewModel(private val navigator: RegistrationFragmentNavigator, private val app: Application) : AndroidViewModel(app) {

	val userFlow = MutableStateFlow<String>("")
	private val nameRuleRegex = "[a-zа-я-A-ZА-Я]".toRegex()
	val textWatcher = object : TextWatcher {
		override fun afterTextChanged(s: Editable?) {}

		override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

		override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
			userFlow.value = s.toString()
		}
	}

	fun logIn() {
		if (isInputCorrect() == true) {
			val sharedPreference = app.getSharedPreferences("USER", Context.MODE_PRIVATE)
			val edit = sharedPreference.edit()
			edit.putBoolean("isLogged", true)
			edit.putString("userName", userFlow.value)
			edit.apply()
			sharedPreference.getBoolean("isLogged", false)
			navigator.goToMain()
		}
	}

	fun isInputCorrect(): Boolean? = when {
		userFlow.value.isEmpty()              -> null
		nameRuleRegex.containsMatchIn(userFlow.value) -> true
		else                                  -> false
	}

}