package com.chelz.foodorganizer.screens.addToBuyList.presentation

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chelz.foodorganizer.screens.addToBuyList.presentation.navigation.AddToBuyListNavigator
import com.chelz.foodorganizer.screens.toBuyList.data.dao.Purchase
import com.chelz.foodorganizer.screens.toBuyList.data.dao.ToBuyListEntity
import com.chelz.foodorganizer.screens.toBuyList.domain.ToBuyListRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.text.ParseException

class AddToBuyListViewModel(private val navigator: AddToBuyListNavigator, private val repository: ToBuyListRepository) : ViewModel() {

	val nameFlow = MutableStateFlow<String?>("")
	val dateFlow = MutableStateFlow<String?>(null)
	val productsFlow = MutableStateFlow<List<String>>(emptyList())

	fun addList() = viewModelScope.launch {

		val name = nameFlow.value.orEmpty()
		val date = getTime()
		val items = productsFlow.value.map { Purchase(it) }

		val list = ToBuyListEntity(
			name = name,
			date = date,
			items = items
		)
		repository.insertToBuyList(list)
		navigateBack()
	}

	@SuppressLint("SimpleDateFormat")
	private fun getTime(): Long? {
		return try {
			java.text.SimpleDateFormat("dd/MM/yyyy").parse(dateFlow.value.orEmpty())?.time
		} catch (e: ParseException) {
			null
		}
	}

	private fun navigateBack() {
		navigator.navigateBack()
	}
}