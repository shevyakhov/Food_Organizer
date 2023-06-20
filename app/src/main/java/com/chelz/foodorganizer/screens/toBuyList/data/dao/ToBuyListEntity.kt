package com.chelz.foodorganizer.screens.toBuyList.data.dao

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters

@Entity(tableName = "ToBuyList")
@TypeConverters(PurchaseListTypeConverter::class)
data class ToBuyListEntity(
	@PrimaryKey(autoGenerate = true) val id: Int = 0,
	@ColumnInfo(name = "name") val name: String,
	@ColumnInfo(name = "date") val date: Long?,
	@ColumnInfo(name = "items") var items: List<Purchase>,
)

data class Purchase(var name: String, var isTicked: Boolean = false)

