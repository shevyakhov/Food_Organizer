package com.chelz.foodorganizer.screens.splash.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.chelz.foodorganizer.databinding.FragmentSplashBinding
import com.chelz.foodorganizer.screens.splash.presentation.SplashViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SplashFragment : Fragment() {

	private var _binding: FragmentSplashBinding? = null
	private val binding get() = _binding!!
	private val viewModel by viewModel<SplashViewModel>()

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		_binding = FragmentSplashBinding.inflate(inflater, container, false)
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		viewModel.init()
	}

	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}
}