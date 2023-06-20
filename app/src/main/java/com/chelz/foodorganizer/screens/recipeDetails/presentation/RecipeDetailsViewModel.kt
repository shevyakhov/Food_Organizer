package com.chelz.foodorganizer.screens.recipeDetails.presentation

import androidx.lifecycle.ViewModel
import com.chelz.foodorganizer.screens.foodList.data.dao.FoodEntity
import com.chelz.foodorganizer.screens.foodList.data.dao.FoodRecipeJunctionEntity
import com.chelz.foodorganizer.screens.foodList.data.dao.RecipeWithFood
import com.chelz.foodorganizer.screens.foodList.data.dao.StatisticsEntity
import com.chelz.foodorganizer.screens.foodList.domain.FoodRepository
import com.chelz.foodorganizer.screens.recipeDetails.presentation.navigation.RecipeDetailsNavigator
import com.chelz.foodorganizer.screens.recipes.data.dao.RecipeEntity
import com.chelz.foodorganizer.screens.recipes.data.dao.RecipeProduct
import com.chelz.foodorganizer.screens.recipes.data.dao.RecipeTypeEntity
import com.chelz.foodorganizer.screens.recipes.domain.RecipesRepository
import com.chelz.foodorganizer.utils.getTimeFromString
import com.chelz.foodorganizer.utils.getTimeStringFromLong
import com.chelz.foodorganizer.utils.roundFloatValueString
import com.chelz.foodorganizer.utils.textListeners.StringSimilarity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.withContext
import java.util.Calendar

class RecipeDetailsViewModel(
	private val recipeId: Int,
	private val navigator: RecipeDetailsNavigator,
	private val recipesRepository: RecipesRepository,
	private val foodRepository: FoodRepository,
) :
	ViewModel() {

	private val allFoodFlow = MutableStateFlow<List<FoodEntity>>(emptyList())
	private val recipeWithFood = MutableStateFlow<RecipeWithFood?>(null)
	val recipeFlow = MutableStateFlow<RecipeEntity?>(null)
	val nameFlow = MutableStateFlow<String?>(null)
	val typeFlow = MutableStateFlow<String?>(null)
	private val typeIdFlow = MutableStateFlow<Int?>(null)
	val allTypeFlow = MutableStateFlow<List<RecipeTypeEntity>?>(null)
	val difficultyFlow = MutableStateFlow<Int?>(1)
	val productsFlow = MutableStateFlow<List<RecipeProduct>>(emptyList())
	val timeToCookFlow = MutableStateFlow<String?>(null)
	val imageFlow = MutableStateFlow<ByteArray?>(null)
	val isCookingFlow = MutableStateFlow<Boolean>(false)

	val uiStateFlow = MutableStateFlow<RecipeDetailsUiState?>(RecipeDetailsUiState.Review)

	suspend fun getRecipe() = coroutineScope() {
		recipeFlow.value = recipesRepository.getRecipeById(recipeId)
		allTypeFlow.value = recipesRepository.getAllRecipeTypes()
		allFoodFlow.value = getAllFood()
		scatterFlow()
		removeInvalidValues()
	}

	private suspend fun removeInvalidValues() {
		recipeWithFood.value = getRecipeWithFood().firstOrNull()
		val junctionEntity =
			recipesRepository.getFoodRecipeJunctionEntity(recipeId)
				.map { it.foodId }
				.filter { id -> id !in allFoodFlow.value.map { it.foodId } }
		junctionEntity.forEach {
			deleteJunctionEntity(it)
		}
		val products = productsFlow.value
		val rwf = recipeWithFood.value?.food
		rwf?.forEach { foodEntity ->
			deleteObsoleteValueFromProducts(foodEntity, products)
		}
		recipeWithFood.value = getRecipeWithFood().firstOrNull()
	}

	private suspend fun scatterFlow() = coroutineScope {
		nameFlow.value = recipeFlow.value?.name
		typeIdFlow.value = recipeFlow.value?.typeId
		typeFlow.value = recipesRepository.getRecipeTypeById(typeIdFlow.value ?: return@coroutineScope)?.typeName
		difficultyFlow.value = recipeFlow.value?.difficulty
		productsFlow.value = recipeFlow.value?.products ?: emptyList()
		timeToCookFlow.value = getTimeStringFromLong(recipeFlow.value?.timeToCook ?: 0)
		imageFlow.value = recipeFlow.value?.image

	}

	suspend fun updateRecipe() = coroutineScope {
		val name = nameFlow.value
		val image = imageFlow.value
		val difficulty = difficultyFlow.value

		val allTypes = allTypeFlow.value
		var typeId = allTypes?.firstOrNull { it.typeName == typeFlow.value }?.id
		if (typeId == null) {
			typeId = recipesRepository.insertRecipeType(RecipeTypeEntity(typeName = typeFlow.value.toString().trim()))
		}

		val foodEntity = RecipeEntity(
			recipeId = recipeFlow.value?.recipeId,
			name = name ?: error("имя"),
			typeId = typeId,
			image = image ?: ByteArray(0),
			difficulty = difficulty ?: error("сложность"),
			timeToCook = timeToCookFlow.value?.let { getTimeFromString(it) },
			products = productsFlow.value
		)
		recipesRepository.updateRecipe(foodEntity)

	}

	private suspend fun getAllFood() = foodRepository.getFood()

	suspend fun updateFood() {
		allFoodFlow.value = getAllFood()
	}

	fun navigateBack() {
		navigator.navigateBack()
	}

	suspend fun deleteRecipe() = coroutineScope {
		recipeFlow.value?.let { recipesRepository.deleteRecipe(it) }
		navigateBack()
	}

	suspend fun checkIfFoodReal(product: RecipeProduct): FoodEntity? {
		return withContext(CoroutineScope(Dispatchers.IO).coroutineContext) {

			val similarFoodEntity = findSimilarFood(product.name, allFoodFlow.value)

			if (similarFoodEntity != null) {
				val foodIds = recipeWithFood.value?.food?.map { it.foodId } ?: emptyList()
				if (similarFoodEntity.foodId !in foodIds) {
					addFoodWithRecipe(similarFoodEntity)
					recipeWithFood.value = getRecipeWithFood().firstOrNull()
				}
				similarFoodEntity
			} else
				null
		}

	}

	private fun findSimilarFood(inputString: String, entityList: List<FoodEntity>): FoodEntity? {
		val threshold = 0.8
		for (string in entityList) {
			val similarity = StringSimilarity.jaroWinkler(inputString, string.name)
			if (similarity >= threshold) {
				return string
			}
		}
		return null
	}

	private suspend fun addFoodWithRecipe(foodEntity: FoodEntity) {
		recipesRepository.addFoodWithRecipe(FoodRecipeJunctionEntity(foodEntity.foodId!!, recipeId))
	}

	private suspend fun getRecipeWithFood(): List<RecipeWithFood> {
		return recipesRepository.getRecipeWithFood(recipeId)
	}

	suspend fun updateProductsFlow() {
		val products = productsFlow.value
		val rwf = recipeWithFood.value?.food
		rwf?.forEach { foodEntity ->
			deleteObsoleteValueFromProducts(foodEntity, products)
		}
		recipeWithFood.value = getRecipeWithFood().firstOrNull()
	}

	private suspend fun deleteObsoleteValueFromProducts(foodEntity: FoodEntity, products: List<RecipeProduct>) {
		val similarProduct = findSimilarProduct(foodEntity.name, products)
		if (similarProduct == null) {
			deleteJunctionEntity(foodEntity.foodId!!)
		}
	}

	private fun findSimilarProduct(inputString: String, entityList: List<RecipeProduct>): RecipeProduct? {
		val threshold = 0.8
		for (string in entityList) {
			val similarity = StringSimilarity.jaroWinkler(inputString, string.name)
			if (similarity >= threshold) {
				return string
			}
		}
		return null
	}

	private suspend fun deleteJunctionEntity(foodId: Int) {
		recipesRepository.deleteFoodRecipeJunctionEntity(recipeId, foodId)
	}

	fun navigateToFoodDetails(name: String) {
		val food = findSimilarFood(name, recipeWithFood.value?.food ?: emptyList())
		if (food != null) {
			navigator.navigateToFoodDetails(food.foodId!!)
		}
	}

	suspend fun useRecipe() = coroutineScope {
		productsFlow.value.forEach {
			val entity = findSimilarFood(it.name, allFoodFlow.value)
			val entityCount = entity?.itemCount
			val productCount = it.quantity
			val newValue = entityCount?.toFloat()?.let { newValue ->
				newValue.minus(productCount).takeIf { value -> value >= 0 }
			}
			val newEntity = entity?.copy(
				itemCount = roundFloatValueString(newValue ?: 0f)
			)
			markAsConsumedFood(productCount)
			if (newEntity != null) {
				foodRepository.updateFood(newEntity)
			}
		}
	}

	private suspend fun markAsConsumedFood(consumedKg: Float) = coroutineScope {
		val cal = Calendar.getInstance()
		val month = cal.get(Calendar.MONTH) + 1
		val year = cal.get(Calendar.YEAR)
		val selectedMonthFormatted = String.format("%02d", month)
		val selectedDate = "$selectedMonthFormatted/$year"

		val date = foodRepository.getByDateStatistics(selectedDate)
		if (date != null) {
			date.consumedNumber += consumedKg
			foodRepository.updateStatistics(date)
		} else {
			foodRepository.insertStatistics(StatisticsEntity(selectedDate, consumedKg, 0f))
		}
		recipeWithFood.value = getRecipeWithFood().firstOrNull()
	}

	fun isCooking(productList: List<RecipeProduct>): Boolean {
		val productLink = MutableList<FoodEntity?>(productList.size) { null }
		productList.forEachIndexed { index, recipeProduct ->
			productLink[index] = findSimilarFood(recipeProduct.name, allFoodFlow.value)
		}
		return if (productLink.contains(null)) {
			false
		} else {
			var flag = true
			productLink.forEachIndexed { index, foodEntity ->
				val count = foodEntity?.itemCount?.toDouble() ?: 0.0
				if (productList[index].quantity > count) {
					flag = false
				}
			}
			flag
		}
	}
}
