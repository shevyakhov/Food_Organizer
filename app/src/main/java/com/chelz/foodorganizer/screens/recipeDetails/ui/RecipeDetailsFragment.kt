package com.chelz.foodorganizer.screens.recipeDetails.ui

import android.Manifest
import android.annotation.SuppressLint
import android.app.TimePickerDialog
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.text.InputType
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast
import androidx.activity.addCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import androidx.core.view.setPadding
import androidx.exifinterface.media.ExifInterface
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import com.bumptech.glide.Glide
import com.chelz.foodorganizer.R
import com.chelz.foodorganizer.databinding.FragmentRecipeDetailsBinding
import com.chelz.foodorganizer.screens.addRecipe.ui.AddRecipeFragment
import com.chelz.foodorganizer.screens.addRecipe.ui.recycler.AddRecipeAdapter
import com.chelz.foodorganizer.screens.addRecipe.ui.recycler.AddRecipeItem
import com.chelz.foodorganizer.screens.recipeDetails.presentation.RecipeDetailsUiState
import com.chelz.foodorganizer.screens.recipeDetails.presentation.RecipeDetailsViewModel
import com.chelz.foodorganizer.screens.recipes.data.dao.RecipeEntity
import com.chelz.foodorganizer.screens.recipes.data.dao.RecipeProduct
import com.chelz.foodorganizer.utils.chip.createChip
import com.chelz.foodorganizer.utils.decodeByteArray
import com.chelz.foodorganizer.utils.flowBinders.bindFlow
import com.chelz.foodorganizer.utils.getOrientation
import com.google.android.material.chip.Chip
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.internal.ViewUtils
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class RecipeDetailsFragment : Fragment() {

	companion object {

		private val PERMISSIONS = arrayOf(
			Manifest.permission.CAMERA,
			Manifest.permission.READ_EXTERNAL_STORAGE,
			Manifest.permission.WRITE_EXTERNAL_STORAGE
		)
		private const val GALLERY_QUERY = "image/*"
		private const val RECIPE_ID = "RECIPE_ID"
		fun newInstance(id: Int) = RecipeDetailsFragment().apply {
			arguments = Bundle().apply {
				putInt(RECIPE_ID, id)
			}
		}
	}

	private val errorHandler = CoroutineExceptionHandler { _, exception ->
		Toast.makeText(requireContext(), "Неверно поле ${exception.localizedMessage}", Toast.LENGTH_SHORT).show()
	}
	private var _binding: FragmentRecipeDetailsBinding? = null
	private val binding get() = _binding!!
	private val viewModel: RecipeDetailsViewModel by viewModel {
		parametersOf(arguments?.getInt(RECIPE_ID))
	}

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?,
	): View {
		_binding = FragmentRecipeDetailsBinding.inflate(inflater, container, false)
		return binding.root
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

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		adapter = AddRecipeAdapter()
		initListeners()
		initObservers()
		setAdapterBinding()
		loadRecipe()
	}

	private fun loadRecipe() {
		CoroutineScope(lifecycleScope.coroutineContext).launch(errorHandler) {
			viewModel.getRecipe()
			viewModel.uiStateFlow.onEach {
				when (it) {
					is RecipeDetailsUiState.Review -> renderReview()

					is RecipeDetailsUiState.Edit   -> renderEdit()

					else                           -> {}
				}
			}.launchIn(lifecycleScope)
		}
	}

	private fun renderReview() {
		binding.editLayout.visibility = View.INVISIBLE
		binding.contentLayout.visibility = View.VISIBLE
		binding.editButton.setOnClickListener {
			setProductsFlow(adapter)
			viewModel.uiStateFlow.value = RecipeDetailsUiState.Edit
			binding.editButton.setImageResource(R.drawable.ic_save)
		}
	}

	private fun renderEdit() {
		binding.editLayout.visibility = View.VISIBLE
		binding.contentLayout.visibility = View.INVISIBLE
		binding.editButton.setOnClickListener {
			setProductsFlow(adapter)
			viewModel.uiStateFlow.value = RecipeDetailsUiState.Review
			binding.editButton.setImageResource(R.drawable.edit_ic)
		}
	}

	private fun setAdapterBinding() {

		(binding.recyclerView.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
		binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
		binding.recyclerView.adapter = adapter

		binding.addIngredients.setOnClickListener {
			adapter.onAddItemClick()
		}
	}

	@SuppressLint("RestrictedApi")
	private fun initListeners() {
		binding.root.viewTreeObserver.addOnGlobalLayoutListener {
			if (_binding != null) {
				val heightDiff = binding.root.rootView.height - binding.root.height
				if (heightDiff > ViewUtils.dpToPx(requireContext(), 200)) {
					binding.saveRecipeButton.visibility = View.GONE
					binding.deleteRecipeButton.visibility = View.GONE
				} else {
					binding.saveRecipeButton.visibility = View.VISIBLE
					binding.deleteRecipeButton.visibility = View.VISIBLE
				}
			}
		}
		binding.recipeIcon.setOnClickListener {
			activity?.let {
				if (hasPermissions(activity as Context, PERMISSIONS)) {
					pickPictureLauncher.launch(GALLERY_QUERY)
				} else {
					permReqLauncher.launch(AddRecipeFragment.PERMISSIONS)
				}
			}
		}
		binding.useRecipeButton.setOnClickListener {
			CoroutineScope(errorHandler).launch {
				viewModel.useRecipe()
				viewModel.updateFood()
				withContext(Dispatchers.Main) {
					setProductBinding(viewModel.productsFlow.value)
				}
			}
		}

		binding.addTypeChip.setOnClickListener {
			addTypeChip()
		}
		binding.saveRecipeButton.setOnClickListener {
			showAlertDialog("Сохранить изменения?") {
				saveRecipe()
			}
		}
		binding.deleteRecipeButton.setOnClickListener {
			showAlertDialog("Удалить рецепт?") {
				CoroutineScope(errorHandler).launch {
					viewModel.deleteRecipe()
				}
			}
		}
		binding.saveRecipeButtonView.setOnClickListener {
			showAlertDialog("Сохранить изменения?") {
				saveRecipe()
			}
		}
		binding.deleteRecipeButtonView.setOnClickListener {
			showAlertDialog("Удалить рецепт?") {
				CoroutineScope(errorHandler).launch {
					viewModel.deleteRecipe()
				}
			}

		}
		requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
			showAlertDialog("Сохранить изменения?") {
				saveRecipe()
			}
		}
		binding.difficultySlider.addOnChangeListener { _, value, _ ->
			viewModel.difficultyFlow.value = value.toInt()
		}
		val timePicker = TimePickerDialog(requireContext(), { _, hourOfDay, minute ->
			val timeString = String.format("%02d:%02d", hourOfDay, minute)
			viewModel.timeToCookFlow.value = timeString
		}, 0, 0, true)

		binding.timeToCook.setOnClickListener {
			timePicker.show()
		}
	}

	private fun saveRecipe() {
		CoroutineScope(errorHandler).launch {
			setProductsFlow(adapter)
			viewModel.updateRecipe()
		}
	}

	private fun initObservers() {
		subscribeOnProducts()
		binding.recipeName.bindFlow(lifecycleScope, viewModel.nameFlow)
		viewModel.recipeFlow.onEach {
			bindData(it)
		}.launchIn(lifecycleScope)

		viewModel.nameFlow.onEach {
			binding.contentRecipeName.text = it
		}.launchIn(lifecycleScope)

		viewModel.imageFlow.onEach {
			Glide
				.with(requireContext())
				.load(it)
				.centerCrop()
				.into(binding.contentImage)
			Glide
				.with(requireContext())
				.load(it)
				.centerCrop()
				.into(binding.recipeIcon)
		}.launchIn(lifecycleScope)


		viewModel.difficultyFlow.onEach {
			binding.difficultySlider.value = it?.toFloat() ?: 1f
			bindDifficulty(it ?: 0)
		}.launchIn(lifecycleScope)

		viewModel.typeFlow.onEach {
			for (i in 0 until binding.chipGroupType.childCount) {
				val chip = binding.chipGroupType.getChildAt(i) as Chip
				if (chip.text == it) {
					chip.isChecked = true
					return@onEach
				}
			}
		}.launchIn(lifecycleScope)
		viewModel.allTypeFlow.onEach { list ->
			for (i in list ?: emptyList()) {
				val chip = createChip(requireContext(), i.typeName)
				if (i.typeName == viewModel.typeFlow.value) {
					chip.isChecked = true
				}
				chip.bindFlow(lifecycleScope, viewModel.typeFlow)
				binding.chipGroupType.addView(chip, 0)
			}
		}.launchIn(lifecycleScope)
		viewModel.timeToCookFlow.onEach {
			binding.contentTimeToCook.text = it
			binding.timeToCook.text = it
		}.launchIn(lifecycleScope)

		viewModel.isCookingFlow.onEach {
			binding.useRecipeButton.isVisible = it
		}.launchIn(lifecycleScope)
	}

	private fun bindData(it: RecipeEntity?) {
		binding.recipeName.setText(it?.name)
		binding.contentRecipeName.text = it?.name
		Glide
			.with(requireContext())
			.load(it?.image)
			.centerCrop()
			.into(binding.contentImage)
		Glide
			.with(requireContext())
			.load(it?.image)
			.centerCrop()
			.into(binding.recipeIcon)

		binding.difficultySlider.value = it?.difficulty?.toFloat() ?: 1f
		bindDifficulty(it?.difficulty ?: 0)
	}

	private fun subscribeOnProducts() {
		viewModel.productsFlow.onEach { productList ->
			setProductBinding(productList)
		}.launchIn(lifecycleScope)
	}

	@SuppressLint("UseCompatLoadingForDrawables")
	private fun setProductBinding(productList: List<RecipeProduct>) {
		bindReviewTable(productList)
		viewModel.isCookingFlow.value = viewModel.isCooking(productList)
		adapter.submitList(productList.map { AddRecipeItem(it.name, it.quantity) })
	}

	private fun bindReviewTable(productList: List<RecipeProduct>) {
		binding.tableLayout.removeAllViews()
		val tableRowMain = TableRow(requireContext()).apply {
			val title = TextView(requireContext()).apply {
				text = getString(R.string.ingredients)
				textSize = 25f
			}
			val quantity = TextView(requireContext()).apply {
				text = getString(R.string.quantity)
				textSize = 25f
			}
			background = requireContext().getDrawable(R.color.grey)
			setPadding(16)
			addView(title)
			addView(quantity)
		}

		binding.tableLayout.addView(tableRowMain)
		for (i in productList) {
			val tableRow = TableRow(requireContext()).apply {
				background = requireContext().getDrawable(R.color.grey_dark)
			}
			val ingredientTextView = TextView(requireContext()).apply {
				text = i.name
				textSize = 20f
				setPadding(10)
			}
			val quantityTextView = TextView(requireContext()).apply {
				textSize = 20f
				setPadding(10)
			}
			val imageView = ImageView(requireContext()).apply {
				CoroutineScope(Dispatchers.Main).launch {
					val foodEntity = withContext(Dispatchers.IO) {
						viewModel.checkIfFoodReal(i)
					}
					val spannableText = "${i.quantity} кг"
					if (foodEntity != null) {
						val color = if (i.quantity > foodEntity.itemCount.toDouble()) {
							ForegroundColorSpan(Color.RED)
						} else {
							ForegroundColorSpan(Color.BLUE)
						}
						quantityTextView.text = SpannableString(spannableText).apply {
							setSpan(color, 0, spannableText.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
						}
						setImageResource(android.R.drawable.btn_star_big_on)
					} else {
						quantityTextView.text = spannableText
						setImageResource(android.R.drawable.btn_star_big_off)
					}
				}
			}
			tableRow.apply {
				addView(ingredientTextView)
				addView(quantityTextView)
				addView(imageView)
				setOnClickListener {
					saveRecipe()
					viewModel.navigateToFoodDetails(i.name)
				}
			}

			binding.tableLayout.addView(tableRow)
		}
	}

	private fun bindDifficulty(diff: Int) {
		with(binding) {
			val diffList = listOf(diff1, diff2, diff3, diff4, diff5)
			for (i in 0 until diff) {
				diffList[i].setBackgroundResource(R.color.red)
			}
			for (i in diff until diffList.size) {
				diffList[i].setBackgroundResource(R.color.light_errorContainer)
			}
		}

	}

	private fun setProductsFlow(adapter: AddRecipeAdapter) {
		val newList = adapter.currentList.map { RecipeProduct(it.name, it.quantity) }
		viewModel.productsFlow.value = newList
		CoroutineScope(Dispatchers.IO).launch {
			viewModel.updateProductsFlow()
		}
	}

	private fun addTypeChip() {
		val builder = MaterialAlertDialogBuilder(requireContext())
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

	private fun showAlertDialog(message: String, onAccept: () -> Unit) {
		val builder = MaterialAlertDialogBuilder(requireContext())
		builder.setTitle("Внимание")
		builder.setMessage(message)

		builder.setPositiveButton(getString(R.string.confirm)) { _, _ ->
			onAccept.invoke()
			viewModel.navigateBack()
		}
		builder.setNegativeButton(getString(R.string.cancel)) { dialog, _ ->
			dialog.cancel()
			viewModel.navigateBack()
		}
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