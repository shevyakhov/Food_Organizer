package com.chelz.foodorganizer.screens.settings.ui

import android.app.TimePickerDialog
import android.os.Build
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.chelz.foodorganizer.R
import com.chelz.foodorganizer.databinding.FragmentSettingsBinding
import com.chelz.foodorganizer.screens.foodList.data.dao.PlacementEntity
import com.chelz.foodorganizer.screens.recipes.data.dao.RecipeTypeEntity
import com.chelz.foodorganizer.screens.settings.presentation.SettingsViewModel
import com.chelz.foodorganizer.utils.chip.createChip
import com.google.android.material.chip.Chip
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment : Fragment() {

	companion object {

		fun newInstance() = SettingsFragment()
	}

	private var _binding: FragmentSettingsBinding? = null
	private val binding get() = _binding!!

	private val viewModel by viewModel<SettingsViewModel>()
	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?,
	): View {
		_binding = FragmentSettingsBinding.inflate(inflater, container, false)
		return binding.root
	}

	@RequiresApi(Build.VERSION_CODES.TIRAMISU)
	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		val timePicker = TimePickerDialog(requireContext(), { _, hourOfDay, minute ->
			val timeString = String.format("%02d:%02d", hourOfDay, minute)
			binding.timePicker.text = timeString
			viewModel.preferredTimeFlow.value = timeString
			viewModel.savePreferredTime()
		}, 0, 0, true)

		binding.timePicker.setOnClickListener {
			timePicker.show()
		}
		viewModel.dialogBuyListFlow.onEach { binding.toBuyListCheck.isChecked = it }.launchIn(lifecycleScope)
		binding.toBuyListCheck.setOnCheckedChangeListener { _, b ->
			viewModel.dialogBuyListFlow.value = b
			viewModel.saveDialogSharedPref()
		}
		viewModel.preferredTimeFlow.onEach {
			when (it) {
				null -> binding.timePicker.text = ""
				else -> binding.timePicker.text = it
			}
		}.launchIn(lifecycleScope)

		viewModel.allPlacementFlow.onEach { list ->
			binding.chipGroupPlacement.removeAllViews()
			binding.chipGroupPlacement.addView(binding.addPlacementChip)
			for (i in list ?: emptyList()) {
				val chip = createChip(requireContext(), i.placementName).apply {
					isCheckable = false
					setEditPlacementDialogListener(i)
				}
				binding.chipGroupPlacement.addView(chip, 0)
			}
		}.launchIn(lifecycleScope)
		viewModel.allRecipeTypeFlow.onEach { list ->
			binding.chipGroupRecipeType.removeAllViews()
			binding.chipGroupRecipeType.addView(binding.addRecipeTypeChip)
			for (i in list ?: emptyList()) {
				val chip = createChip(requireContext(), i.typeName).apply {
					isCheckable = false
					setEditRecipeTypeDialogListener(i)
				}
				binding.chipGroupRecipeType.addView(chip, 0)
			}
		}.launchIn(lifecycleScope)
		binding.addPlacementChip.setOnClickListener {
			addPlacementChip()
		}
		binding.addRecipeTypeChip.setOnClickListener {
			addTypeChip()
		}
	}

	private fun addPlacementChip() {
		val builder = MaterialAlertDialogBuilder(requireContext())
		builder.setTitle("Добавить расположение")
		val input = EditText(requireContext())
		input.hint = "Расположение"
		input.inputType = InputType.TYPE_CLASS_TEXT
		builder.setView(input)

		builder.setPositiveButton(getString(R.string.add)) { _, _ ->
			viewModel.addPlacement(input.text.toString())
		}
		builder.setNegativeButton(getString(R.string.cancel)) { dialog, _ -> dialog.cancel() }
		builder.show()
	}

	private fun addTypeChip() {
		val builder = MaterialAlertDialogBuilder(requireContext())
		builder.setTitle("Добавить тип рецепта")
		val input = EditText(requireContext())
		input.hint = "Тип"
		input.inputType = InputType.TYPE_CLASS_TEXT
		builder.setView(input)
		builder.setPositiveButton(getString(R.string.add)) { _, _ ->
			viewModel.addRecipeType(input.text.toString())
		}
		builder.setNegativeButton(getString(R.string.cancel)) { dialog, _ -> dialog.cancel() }
		builder.show()
	}

	private fun Chip.setEditPlacementDialogListener(placementEntity: PlacementEntity) {
		setOnClickListener {
			val input = EditText(requireContext()).apply {
				this.hint = "Название"
				this.text = text
			}
			MaterialAlertDialogBuilder(requireContext()).apply {
				setTitle("Редактирование")
				setView(input)
				setPositiveButton("Сохранить") { _, _ ->
					viewModel.updatePlacement(placementEntity.placementId!!, input.text.toString())
				}
				setNegativeButton("Удалить") { _, _ ->
					MaterialAlertDialogBuilder(requireContext()).apply {
						setTitle("Вы уверены?")
						setMessage("Все связанные продукты также будут удалены")
						setPositiveButton("ДА") { _, _ ->
							viewModel.deletePlacement(placementEntity)
						}
						setNeutralButton("Отмена") { _, _ -> }
						show()
					}
				}
				setNeutralButton("Назад") { _, _ -> }
				show()
			}
		}
	}

	private fun Chip.setEditRecipeTypeDialogListener(typeEntity: RecipeTypeEntity) {
		setOnClickListener {
			val input = EditText(requireContext()).apply {
				this.hint = "Название"
				this.text = text
			}
			MaterialAlertDialogBuilder(requireContext()).apply {
				setTitle("Редактирование")
				setView(input)
				setPositiveButton("Сохранить") { _, _ ->
					viewModel.updateRecipeType(typeEntity.id!!, input.text.toString())
				}
				setNegativeButton("Удалить") { _, _ ->
					MaterialAlertDialogBuilder(requireContext()).apply {
						setTitle("Вы уверены?")
						setMessage("Все связанные рецепты также будут удалены")
						setPositiveButton("ДА") { _, _ ->
							viewModel.deleteRecipeType(typeEntity)
						}
						setNeutralButton("Отмена") { _, _ -> }
						show()
					}
				}
				setNeutralButton("Назад") { _, _ ->

				}
				show()
			}
		}
	}

	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}
}