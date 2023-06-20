package com.chelz.foodorganizer.screens.foodList.data.dao

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "PlacementTable", indices = [Index(value = ["placementId", "placementName"], unique = true)])
data class PlacementEntity(
	@PrimaryKey(autoGenerate = true)
	@ColumnInfo(name = "placementId") val placementId: Int? = null,
	@ColumnInfo(name = "placementName") val placementName: String,
)