package com.chelz.foodorganizer.screens.foodDetails.ui

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.exifinterface.media.ExifInterface
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.chelz.foodorganizer.R
import com.chelz.foodorganizer.databinding.FragmentFoodDetailsBinding
import com.chelz.foodorganizer.screens.addFood.ui.AddFoodFragment
import com.chelz.foodorganizer.screens.foodDetails.presentation.FoodDetailsViewModel
import com.chelz.foodorganizer.utils.chip.createChip
import com.chelz.foodorganizer.utils.decodeByteArray
import com.chelz.foodorganizer.utils.flowBinders.bindFlow
import com.chelz.foodorganizer.utils.getOrientation
import com.chelz.foodorganizer.utils.textListeners.bindDateListener
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.slider.Slider
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class FoodDetailsFragment : Fragment() {

	companion object {

		var PERMISSIONS = arrayOf(
			Manifest.permission.CAMERA,
			Manifest.permission.READ_EXTERNAL_STORAGE,
			Manifest.permission.WRITE_EXTERNAL_STORAGE
		)
		private const val GALLERY_QUERY = "image/*"

		private const val FOOD_ID = "FOOD_ID"
		fun newInstance(id: Int) = FoodDetailsFragment().apply {
			arguments = Bundle().apply {
				putInt(FOOD_ID, id)
			}
		}
	}

	private var _binding: FragmentFoodDetailsBinding? = null
	private val binding get() = _binding!!
	private val viewModel: FoodDetailsViewModel by viewModel {
		parametersOf(arguments?.getInt(FOOD_ID))
	}
	private val permReqLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
		val granted = permissions.entries.all { it.value }
		if (granted) {
			pickPictureLauncher.launch(GALLERY_QUERY)
		}
	}
	private val pickPictureLauncher: ActivityResultLauncher<String> =
		registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
			if (uri != null) {
				Glide
					.with(requireContext())
					.load(uri)
					.centerCrop()
					.into(binding.foodIcon)
				val stream = requireContext().contentResolver.openInputStream(uri)
				val byteArray = stream?.use { it.readBytes() }
				val exif = stream?.let {
					ExifInterface(it)
				}
				val orientation = getOrientation(exif)
				val resizedImageByteArray = orientation?.let { decodeByteArray(byteArray, it) }
				viewModel.imageFlow.value = resizedImageByteArray
			}
		}

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?,
	): View {
		_binding = FragmentFoodDetailsBinding.inflate(inflater, container, false)
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		initListeners()
		initObservers()
	}

	private fun initListeners() {
		binding.addPlacementChip.setOnClickListener {
			addPlacementChip()
		}

		binding.trashedButton.setOnClickListener {
			showFoodDeleteAlertDialog(viewModel::markAsTrashedFood)
		}
		binding.recycledButton.setOnClickListener {
			showFoodDeleteAlertDialog(viewModel::markAsConsumedFood)
		}
		binding.saveButton.setOnClickListener {
			viewModel.bestBeforeFlow.value = binding.bestBefore.text.toString()
			viewModel.navigateBack()
		}
		binding.foodIcon.setOnClickListener {
			activity?.let {
				if (hasPermissions(activity as Context, AddFoodFragment.PERMISSIONS)) {
					pickPictureLauncher.launch(GALLERY_QUERY)
				} else {
					permReqLauncher.launch(PERMISSIONS)
				}
			}
		}
	}

	private fun initObservers() {
		binding.foodName.bindFlow(lifecycleScope, viewModel.nameFlow)
		binding.quantity.bindFlow(lifecycleScope, viewModel.countFlow)
		binding.reminder1DayBefore.bindFlow(lifecycleScope, viewModel.reminderFlow)
		binding.reminder2DayBefore.bindFlow(lifecycleScope, viewModel.reminderFlow)

		viewModel.bestBeforeFlowFirst.onEach {
			viewModel.bestBeforeFlow.value = it
			binding.bestBefore.setText(it)
		}.launchIn(lifecycleScope)

		viewModel.reminderFlow.onEach {
			when (it) {
				binding.reminder1DayBefore.text.toString() -> binding.reminder1DayBefore.isChecked = true
				binding.reminder2DayBefore.text.toString() -> binding.reminder2DayBefore.isChecked = true

				else                                       -> {}
			}
		}.launchIn(lifecycleScope)


		binding.bestBefore.apply {
			addTextChangedListener(bindDateListener(this))
		}

		viewModel.allPlacementFlow.onEach { list ->
			for (i in list ?: emptyList()) {
				val chip = createChip(requireContext(), i.placementName)
				if (i.placementName == viewModel.placementFlow.value) {
					chip.isChecked = true
				}
				chip.bindFlow(lifecycleScope, viewModel.placementFlow)
				binding.chipGroupPlacement.addView(chip, 0)
			}
		}.launchIn(lifecycleScope)

		viewModel.foodFlow.onEach {
			binding.foodName.setText(it?.name)
			Glide
				.with(requireContext())
				.load(it?.image)
				.centerCrop()
				.into(binding.foodIcon)
			binding.quantity.setText(it?.itemCount)
		}.launchIn(lifecycleScope)
	}

	private fun addPlacementChip() {
		val builder = MaterialAlertDialogBuilder(requireContext())
		builder.setTitle("Добавить расположение")
		val input = EditText(requireContext())
		input.hint = "Расположение"
		input.inputType = InputType.TYPE_CLASS_TEXT
		builder.setView(input)

		builder.setPositiveButton(getString(R.string.add)) { _, _ ->
			val chip = createChip(requireContext(), input.text.toString())
			binding.chipGroupPlacement.addView(chip, 0)
			chip.bindFlow(lifecycleScope, viewModel.placementFlow)
			chip.isChecked = true
		}
		builder.setNegativeButton(getString(R.string.cancel)) { dialog, _ -> dialog.cancel() }
		builder.show()
	}

	private fun showFoodDeleteAlertDialog(onDelete: (kg: Float) -> Unit) {
		val builder = MaterialAlertDialogBuilder(requireContext())
		if (viewModel.countFlow.value?.toFloat()?.equals(0f) == false) {
			builder.setMessage("Сколько кг продукта было использовано")
			val slider = Slider(requireContext()).apply {
				valueFrom = 0f
				valueTo = viewModel.countFlow.value?.toFloat() ?: 0f
			}
			builder.setView(slider)
			builder.setPositiveButton(getString(R.string.confirm)) { _, _ ->
				onDelete.invoke(slider.value)
			}
		} else {
			builder.setMessage("Данное действие удалит продукт без возможности восстановления")
			builder.setPositiveButton(getString(R.string.confirm)) { _, _ ->
				onDelete.invoke(0f)
			}
		}
		builder.setTitle("Внимание")
		builder.setNegativeButton(getString(R.string.cancel)) { dialog, _ -> dialog.cancel() }
		builder.show()
	}

	private fun hasPermissions(context: Context, permissions: Array<String>): Boolean =
		permissions.all {
			ActivityCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
		}

	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}
}