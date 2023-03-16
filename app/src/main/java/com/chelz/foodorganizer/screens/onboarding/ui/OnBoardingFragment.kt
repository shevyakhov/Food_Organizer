package com.chelz.foodorganizer.screens.onboarding.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.chelz.foodorganizer.R
import com.chelz.foodorganizer.databinding.FragmentOnBoardingBinding
import com.chelz.foodorganizer.screens.onboarding.presentation.OnBoardingViewModel
import com.chelz.foodorganizer.screens.onboarding.ui.recycler.SliderAdapter
import com.chelz.foodorganizer.screens.onboarding.ui.recycler.introList
import org.koin.androidx.viewmodel.ext.android.viewModel

class OnBoardingFragment : Fragment() {

	companion object {

		fun newInstance() = OnBoardingFragment()
	}

	private var _binding: FragmentOnBoardingBinding? = null
	private val binding get() = _binding!!
	private val sliderAdapter = SliderAdapter()
	private val viewModel by viewModel<OnBoardingViewModel>()

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		_binding = FragmentOnBoardingBinding.inflate(inflater, container, false)
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		initBinding()
	}

	private fun initBinding() {
		binding.viewPager.adapter = sliderAdapter
		sliderAdapter.initList(introList)

		val indicator = binding.indicator
		indicator.setViewPager(binding.viewPager)
		binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
			override fun onPageSelected(position: Int) {
				super.onPageSelected(position)
				if (position == sliderAdapter.itemCount - 1) {
					binding.botButton.text = getString(R.string.start)
				} else
					binding.botButton.text = getString(R.string.next)
			}
		})
		binding.skipBtn.setOnClickListener {
			viewModel.navigateToRegistration()
		}
		binding.botButton.setOnClickListener {
			if (binding.viewPager.currentItem == sliderAdapter.itemCount - 1) {
				viewModel.navigateToRegistration()
			} else
				binding.viewPager.currentItem++
		}
	}

	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}

}