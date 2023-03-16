package com.chelz.foodorganizer.screens.onboarding.ui.recycler

import androidx.annotation.RawRes
import androidx.annotation.StringRes
import com.airbnb.lottie.LottieDrawable
import com.chelz.foodorganizer.R

data class SliderItem(
	@RawRes val animationRaw: Int,
	@StringRes val mainText: Int,
	@StringRes val subText: Int,
	val animationMode: Int = LottieDrawable.RESTART
)

val introList = listOf(
	SliderItem(R.raw.food_keep, R.string.introFirstMain, R.string.introFirstSub, LottieDrawable.REVERSE),
	SliderItem(R.raw.recipes, R.string.introSecondMain, R.string.introSecondSub),
	SliderItem(R.raw.food_list, R.string.introThirdMain, R.string.introThirdSub, LottieDrawable.INFINITE)
)