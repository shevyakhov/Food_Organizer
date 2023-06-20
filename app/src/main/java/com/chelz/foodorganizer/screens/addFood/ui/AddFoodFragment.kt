package com.chelz.foodorganizer.screens.addFood.ui

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
import androidx.exifinterface.media.ExifInterface
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.chelz.foodorganizer.R
import com.chelz.foodorganizer.databinding.FragmentAddFoodBinding
import com.chelz.foodorganizer.screens.addFood.presentation.AddFoodViewModel
import com.chelz.foodorganizer.utils.chip.createChip
import com.chelz.foodorganizer.utils.decodeByteArray
import com.chelz.foodorganizer.utils.flowBinders.bindFlow
import com.chelz.foodorganizer.utils.getOrientation
import com.chelz.foodorganizer.utils.textListeners.bindDateListener
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class AddFoodFragment : Fragment() {

	companion object {

		var PERMISSIONS = arrayOf(
			Manifest.permission.CAMERA,
			Manifest.permission.READ_EXTERNAL_STORAGE,
			Manifest.permission.WRITE_EXTERNAL_STORAGE
		)
		private const val GALLERY_QUERY = "image/*"
		fun newInstance() = AddFoodFragment()
	}

	private var _binding: FragmentAddFoodBinding? = null
	private val binding get() = _binding!!
	private val viewModel by viewModel<AddFoodViewModel>()
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
		_binding = FragmentAddFoodBinding.inflate(inflater, container, false)
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		binding.foodName.bindFlow(lifecycleScope, viewModel.nameFlow)
		binding.quantity.bindFlow(lifecycleScope, viewModel.quantityFlow)

		CoroutineScope(Dispatchers.IO).launch {
			viewModel.init()
		}
		binding.foodIcon.setOnClickListener {
			activity?.let {
				if (hasPermissions(activity as Context, PERMISSIONS)) {
					pickPictureLauncher.launch(GALLERY_QUERY)
				} else {
					permReqLauncher.launch(PERMISSIONS)
				}
			}
		}

		viewModel.everyPlacementFlow.onEach { list ->
			for (i in list) {
				val chip = createChip(requireContext(), i.placementName)
				chip.bindFlow(lifecycleScope, viewModel.placementFlow)
				binding.chipGroupPlacement.addView(chip, 0)
			}
		}.launchIn(lifecycleScope)

		binding.addPlacementChip.setOnClickListener {
			addPlacementChip()
		}

		binding.bestBefore.apply {
			addTextChangedListener(bindDateListener(this))
		}
		binding.addButton.setOnClickListener {
			CoroutineScope(Dispatchers.IO).launch {
				viewModel.bestBeforeFlow.value = binding.bestBefore.text.toString()
				viewModel.addItem()
			}
		}
		binding.reminder1DayBefore.bindFlow(lifecycleScope, viewModel.reminderFlow)
		binding.reminder2DayBefore.bindFlow(lifecycleScope, viewModel.reminderFlow)
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

	private fun hasPermissions(context: Context, permissions: Array<String>): Boolean =
		permissions.all {
			ActivityCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
		}

	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}
}