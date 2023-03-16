package com.chelz.foodorganizer.screens.registration

import androidx.fragment.app.Fragment
import com.chelz.foodorganizer.screens.registration.ui.RegistrationFragment
import com.chelz.foodorganizer.utils.FragmentIndex

object RegistrationIndex : FragmentIndex {

	override fun createInstance(): Fragment = RegistrationFragment()
}