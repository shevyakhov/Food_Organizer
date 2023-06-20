package com.chelz.foodorganizer.screens.addRecipe.ui

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.exifinterface.media.ExifInterface
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import com.bumptech.glide.Glide
import com.chelz.foodorganizer.R
import com.chelz.foodorganizer.databinding.FragmentAddRecipeBinding
import com.chelz.foodorganizer.screens.addRecipe.presentation.AddRecipeViewModel
import com.chelz.foodorganizer.screens.addRecipe.ui.recycler.AddRecipeAdapter
import com.chelz.foodorganizer.screens.recipes.data.dao.RecipeProduct
import com.chelz.foodorganizer.utils.chip.createChip
import com.chelz.foodorganizer.utils.decodeByteArray
import com.chelz.foodorganizer.utils.flowBinders.bindFlow
import com.chelz.foodorganizer.utils.getOrientation
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.internal.ViewUtils.dpToPx
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class AddRecipeFragment : Fragment() {
	companion object {

		var PERMISSIONS = arrayOf(
			Manifest.permission.CAMERA,
			Manifest.permission.READ_EXTERNAL_STORAGE,
			Manifest.permission.WRITE_EXTERNAL_STORAGE
		)
		private const val GALLERY_QUERY = "image/*"
		fun newInstance() = AddRecipeFragment()
	}

	private val errorHandler = CoroutineExceptionHandler { _, exception ->
		Toast.makeText(requireContext(), "Неверно поле ${exception.localizedMessage}", Toast.LENGTH_SHORT).show()
	}
	private var _binding: FragmentAddRecipeBinding? = null
	private val binding get() = _binding!!
	private val viewModel: AddRecipeViewModel by viewModel()
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
					.into(binding.recipeIcon)
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
	private lateinit var adapter: AddRecipeAdapter

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?,
	): View {
		_binding = FragmentAddRecipeBinding.inflate(inflater, container, false)
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		adapter = AddRecipeAdapter()
		setAdapterBinding()
		initListeners()

		viewModel.allTypeFlow.onEach { list ->
			for (i in list ?: emptyList()) {
				val chip = createChip(requireContext(), i.typeName)
				chip.bindFlow(lifecycleScope, viewModel.typeFlow)
				binding.chipGroupType.addView(chip, 0)
			}
		}.launchIn(lifecycleScope)
		binding.recipeName.bindFlow(lifecycleScope, viewModel.nameFlow)
	}

	private fun setAdapterBinding() {

		(binding.recyclerView.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
		binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
		binding.recyclerView.adapter = adapter
		adapter.submitList(listOf())

		binding.addIngredients.setOnClickListener {
			adapter.onAddItemClick()
		}
	}

	@SuppressLint("RestrictedApi")
	private fun initListeners() {
		binding.root.viewTreeObserver.addOnGlobalLayoutListener {
			if (_binding != null) {
				val heightDiff = binding.root.rootView.height - binding.root.height
				if (heightDiff > dpToPx(requireContext(), 200)) {
					binding.saveRecipeButton.visibility = View.GONE
				} else {
					binding.saveRecipeButton.visibility = View.VISIBLE
				}
			}
		}
		binding.recipeIcon.setOnClickListener {
			activity?.let {
				if (hasPermissions(activity as Context, PERMISSIONS)) {
					pickPictureLauncher.launch(GALLERY_QUERY)
				} else {
					permReqLauncher.launch(PERMISSIONS)
				}
			}
		}
		binding.addTypeChip.setOnClickListener {
			addTypeChip()
		}
		binding.saveRecipeButton.setOnClickListener {
			viewLifecycleOwner.lifecycleScope.launch(errorHandler) {
				viewModel.productsFlow.value = getProducts(adapter)
				viewModel.addItem()
			}
		}
		binding.difficultySlider.addOnChangeListener { _, value, _ ->
			viewModel.difficultyFlow.value = value.toInt()
		}
		val timePicker = TimePickerDialog(requireContext(), { _, hourOfDay, minute ->
			val timeString = String.format("%02d:%02d", hourOfDay, minute)
			binding.timeToCook.text = timeString
			viewModel.timeToCookFlow.value = timeString
		}, 0, 0, true)

		binding.timeToCook.setOnClickListener {
			timePicker.show()
		}
	}

	private fun getProducts(adapter: AddRecipeAdapter): List<RecipeProduct> =
		adapter.currentList.map { RecipeProduct(it.name, it.quantity) }

	private fun addTypeChip() {
		val builder= MaterialAlertDialogBuilder(requireContext())
		builder.setTitle("Добавить тип рецепта")
		val input = EditText(requireContext())
		input.hint = "Тип"
		input.inputType = InputType.TYPE_CLASS_TEXT
		builder.setView(input)

		builder.setPositiveButton(getString(R.string.add)) { _, _ ->
			val chip = createChip(requireContext(), input.text.toString())
			chip.bindFlow(lifecycleScope, viewModel.typeFlow)
			binding.chipGroupType.addView(chip, 0)
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