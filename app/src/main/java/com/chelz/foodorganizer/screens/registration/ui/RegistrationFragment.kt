package com.chelz.foodorganizer.screens.registration.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.chelz.foodorganizer.R
import com.chelz.foodorganizer.databinding.FragmentRegistrationBinding
import com.chelz.foodorganizer.screens.registration.presentation.RegistrationViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel

class RegistrationFragment : Fragment() {

	private var _binding: FragmentRegistrationBinding? = null
	private val binding get() = _binding!!
	private val viewModel by viewModel<RegistrationViewModel>()

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?,
	): View {
		_binding = FragmentRegistrationBinding.inflate(inflater, container, false)
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		binding.userName.addTextChangedListener(viewModel.textWatcher)
		binding.registerButton.setOnClickListener {
			viewModel.logIn()
		}

		viewModel.userFlow.onEach {
			when (viewModel.isInputCorrect()) {
				true, null -> binding.userNameLayout.error = null
				false      -> binding.userNameLayout.error = getString(R.string.inputIncorrect)
			}
		}.launchIn(lifecycleScope)
	}

	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}
}