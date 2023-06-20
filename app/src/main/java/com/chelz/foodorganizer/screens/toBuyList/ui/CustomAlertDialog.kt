package com.chelz.foodorganizer.screens.toBuyList.ui

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.lifecycle.LifecycleCoroutineScope
import com.chelz.foodorganizer.R
import com.chelz.foodorganizer.screens.foodList.data.dao.FoodEntity
import com.chelz.foodorganizer.screens.foodList.data.dao.PlacementEntity
import com.chelz.foodorganizer.screens.toBuyList.presentation.ToBuyListViewModel
import com.chelz.foodorganizer.utils.flowBinders.bindFlow
import com.chelz.foodorganizer.utils.textListeners.StringSimilarity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@SuppressLint("SetTextI18n")
class CustomAlertDialog(context: Context, private val scope: LifecycleCoroutineScope, private val item: String, private val viewModel: ToBuyListViewModel) {

	private val nameFlow = MutableStateFlow<String?>("")
	private val quantityFlow = MutableStateFlow<String?>("")
	private val foodFlow = MutableStateFlow<FoodEntity?>(null)
	private val allFoodFlow = MutableStateFlow<List<FoodEntity>?>(null)
	private val placementFlow = MutableStateFlow<List<PlacementEntity>?>(null)
	private val chosenPlacementFlow = MutableStateFlow<String?>(null)
	private val alertDialog: AlertDialog

	private val errorHandler = CoroutineExceptionHandler { _, throwable ->
		Toast.makeText(context, "Неверный ввод ${throwable.message}", Toast.LENGTH_SHORT).show()
	}

	init {
		CoroutineScope(Dispatchers.IO + errorHandler).launch {
			placementFlow.value = viewModel.getAllPlacements()
			allFoodFlow.value = viewModel.getAllFood()
		}

		val builder = MaterialAlertDialogBuilder(context)
		val inflater = LayoutInflater.from(context)
		val dialogView = inflater.inflate(R.layout.alert_dialog_food_layout, null)
		builder.setView(dialogView)
		alertDialog = builder.create()

		val name = dialogView.findViewById<TextInputEditText>(R.id.foodName).apply {
			bindFlow(scope, nameFlow)
			setText(item)
		}

		val editQuantity = dialogView.findViewById<TextInputEditText>(R.id.quantity).apply {
			bindFlow(scope, quantityFlow)
		}

		val savedQuantity = dialogView.findViewById<TextView>(R.id.savedQuantity)
		val spinner = dialogView.findViewById<Spinner>(R.id.spinner)
		val searchButton = dialogView.findViewById<Button>(R.id.searchButton)
		val cancelButton = dialogView.findViewById<Button>(R.id.cancelButton)
		val addButton = dialogView.findViewById<Button>(R.id.addButton)
		val checkBox = dialogView.findViewById<CheckBox>(R.id.checkbox)
		val spinnerArrayAdapter: ArrayAdapter<String> = ArrayAdapter<String>(
			context, android.R.layout.simple_spinner_dropdown_item, placementFlow.value?.map { it.placementName } ?: emptyList())
		spinner.adapter = spinnerArrayAdapter
		spinner.setSelection(0)
		spinner.onItemSelectedListener = object : OnItemSelectedListener {
			override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
				chosenPlacementFlow.value = parent.getItemAtPosition(position) as String
			}

			override fun onNothingSelected(p0: AdapterView<*>?) {}
		}
		addButton.setOnClickListener {
			if (isInputCorrect()) {
				if (foodFlow.value != null) {
					CoroutineScope(Dispatchers.IO + errorHandler).launch {
						val foodEntity = foodFlow.value as FoodEntity
						viewModel.addFoodEntity(
							foodEntity.copy(
								name = nameFlow.value.toString(),
								placement = placementFlow.value?.first { place -> place.placementName == chosenPlacementFlow.value }?.placementId
									?: error("Расположение"),
								itemCount = (foodEntity.itemCount.toFloat() + quantityFlow.value.toString().toFloat()).toString()
							)
						)
					}
				} else {
					CoroutineScope(Dispatchers.IO + errorHandler).launch {
						viewModel.addFoodEntity(
							FoodEntity(
								name = nameFlow.value.toString(),
								placement = placementFlow.value?.first { place -> place.placementName == chosenPlacementFlow.value }?.placementId
									?: error("Расположение"),
								itemCount = (quantityFlow.value.orEmpty().toFloat()).toString(),
								image = ByteArray(0)
							)
						)
					}
				}
				alertDialog.dismiss()
				Toast.makeText(context, context.getString(R.string.added), Toast.LENGTH_SHORT).show()
			} else
				Toast.makeText(context, "Ввод неверный", Toast.LENGTH_SHORT).show()
		}
		searchButton.setOnClickListener {
			if (foodFlow.value == null) {
				foodFlow.value = findSimilarFood(nameFlow.value.orEmpty(), allFoodFlow.value ?: error("atut empty"))
			} else
				foodFlow.value = null
		}

		cancelButton.setOnClickListener {
			alertDialog.dismiss()
		}

		foodFlow.onEach { food ->
			if (food == null) {
				searchButton.text = context.getString(R.string.searchRealFood)
				savedQuantity.isVisible = false
				checkBox.isChecked = false
			} else {
				searchButton.text = context.getString(R.string.clear)
				savedQuantity.isVisible = true
				checkBox.isChecked = true
				placementFlow.value?.map { it.placementId }?.indexOf(food.placement)?.let {
					spinner.setSelection(it)
				}
				name.setText(food.name)
				savedQuantity.text = "${food.itemCount.toFloat()} имеется"
			}
		}.launchIn(scope)
	}

	private fun isInputCorrect(): Boolean =
		nameFlow.value?.isNotEmpty() ?: error("имя") &&
			chosenPlacementFlow.value?.isNotEmpty() ?: error("Расположение") &&
			quantityFlow.value?.isNotEmpty() ?: error("Количество")

	fun findSimilarFood(inputString: String, entityList: List<FoodEntity>): FoodEntity? {
		val threshold = 0.8
		for (string in entityList) {
			val similarity = StringSimilarity.jaroWinkler(inputString, string.name)
			if (similarity >= threshold) {
				return string
			}
		}
		return null
	}

	fun show() {
		alertDialog.show()
	}
}