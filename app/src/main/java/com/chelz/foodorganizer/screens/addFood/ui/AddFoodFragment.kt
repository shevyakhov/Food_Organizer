package com.chelz.foodorganizer.screens.addFood.ui

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.addCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.view.children
import androidx.fragment.app.Fragment
import com.chelz.foodorganizer.R
import com.chelz.foodorganizer.databinding.FragmentAddFoodBinding
import com.chelz.foodorganizer.screens.addFood.presentation.AddFoodViewModel
import com.chelz.foodorganizer.screens.foodList.ui.recycler.FoodListItem
import com.chelz.foodorganizer.screens.foodList.ui.recycler.ItemCount
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.Date

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
				binding.foodIcon.setImageURI(uri)
				val byteArray = requireContext().contentResolver.openInputStream(uri)?.buffered()?.use { it.readBytes() }
				val bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray?.size ?: 0)
			} else {
				Log.e("sad", "asd")
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

		binding.foodIcon.setOnClickListener {
			activity?.let {
				if (hasPermissions(activity as Context, PERMISSIONS)) {
					pickPictureLauncher.launch(GALLERY_QUERY)
				} else {
					permReqLauncher.launch(PERMISSIONS)
				}
			}
		}
		/*// add chip group dynamically
		item.itemData.projectTags.forEach { tagName ->
			binding.tagChipGroup.addView(createTagChip(context, tagName))
		}*/


		binding.addPlacementChip.setOnClickListener {
			addChip("Добавить расположение", "Расположение", binding.chipGroupPlacement)

		}
		binding.addQuantityPrefixChip.setOnClickListener {
			addChip("Добавить единицу измерения", "Единица измерения", binding.chipGroupQuantityPrefix)
		}
		binding.addButton.setOnClickListener {
			val chipPlacement = binding.chipGroupPlacement.children.toList().filter { (it as Chip).isChecked }
			val chipPrefix = binding.chipGroupQuantityPrefix.children.toList().filter { (it as Chip).isChecked }
			val foodName = binding.foodName.text.toString().ifEmpty { null }
			val chipPlacementText = if (chipPlacement.isNotEmpty()) {
				(chipPlacement.first() as TextView).text
			} else {
				null
			}
			val chipPrefixText = if (chipPrefix.isNotEmpty()) {
				(chipPrefix.first() as TextView).text
			} else {
				null
			}
		}

		binding.bestBefore.apply {
			addTextChangedListener(viewModel.getDateListener(this))
		}
		requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
			viewModel.navigateWithChanges(getItemFromInput())
		}
	}

	private fun getItemFromInput(): FoodListItem {
		return FoodListItem(
			1,
			binding.foodName.text.toString(),
			getPlacement(),
			getCount(),
			getDate(),
			12
		)
	}

	@SuppressLint("SimpleDateFormat")
	private fun getDate(): Date {
		val splitDate = binding.bestBefore.text.toString().split("/")
		return SimpleDateFormat("dd/MM/yyyy").parse(binding.bestBefore.text.toString()) ?: Date()
	}

	private fun getCount(): ItemCount {
		var prefix = ""
		for (i in binding.chipGroupQuantityPrefix.children) {
			if ((i as Chip).isChecked) {
				prefix = i.text.toString()
			}
		}
		return ItemCount(binding.quantity.text.toString(), prefix)
	}

	private fun getPlacement(): String {
		for (i in binding.chipGroupPlacement.children) {
			if ((i as Chip).isChecked) {
				return i.text.toString()
			}
		}
		return ""
	}

	@SuppressLint("ResourceType")
	private fun createTagChip(context: Context, chipName: String): Chip {
		return Chip(context).apply {
			text = chipName
			isCheckable = true
			isCheckedIconVisible = true
		}
	}

	private fun addChip(title: String, hintText: String, chipGroup: ChipGroup) {
		val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
		builder.setTitle(title)
		val input = EditText(requireContext())
		input.hint = hintText
		input.inputType = InputType.TYPE_CLASS_TEXT
		builder.setView(input)

		builder.setPositiveButton(getString(R.string.add)) { _, _ ->
			val chip = createTagChip(requireContext(), input.text.toString())
			chip.setOnClickListener {
				Toast.makeText(requireContext(), input.text.toString(), Toast.LENGTH_SHORT).show()
			}
			chipGroup.addView(chip, 0)
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

