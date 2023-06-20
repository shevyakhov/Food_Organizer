package com.chelz.foodorganizer.screens.foodList.data.dao

import androidx.room.Embedded
import androidx.room.Relation

class PlacementWithFood {

	@Embedded
	var placement: PlacementEntity? = null

	@Relation(parentColumn = "placementId", entityColumn = "placement")
	var foods: List<FoodEntity>? = null
}