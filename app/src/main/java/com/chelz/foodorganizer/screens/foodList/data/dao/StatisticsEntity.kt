package com.chelz.foodorganizer.screens.foodList.data.dao

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "StatisticsTable")
data class StatisticsEntity(
	@PrimaryKey val date: String,
	@ColumnInfo(name = "consumedNumber") var consumedNumber: Float,
	@ColumnInfo(name = "trashedNumber") var trashedNumber: Float,
)



