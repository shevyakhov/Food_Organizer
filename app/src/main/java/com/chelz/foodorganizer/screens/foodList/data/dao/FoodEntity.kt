package com.chelz.foodorganizer.screens.foodList.data.dao

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "FoodTable",
		foreignKeys = [ForeignKey(entity = PlacementEntity::class,
								  parentColumns = ["placementId"],
								  childColumns = ["placement"],
								  onDelete = ForeignKey.CASCADE)])
data class FoodEntity(
	@PrimaryKey(autoGenerate = true) val foodId: Int? = null,
	@ColumnInfo(name = "name") val name: String,
	@ColumnInfo(name = "placement") val placement: Int,
	@ColumnInfo(typeAffinity = ColumnInfo.BLOB) val image: ByteArray,
	@ColumnInfo(name = "itemCount") val itemCount: String,
	@ColumnInfo(name = "bestBefore") val bestBefore: Long? = null,
	@ColumnInfo(name = "remindIn") val remindIn: Int? = null,

	) {

	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (javaClass != other?.javaClass) return false

		other as FoodEntity

		if (foodId != other.foodId) return false
		if (name != other.name) return false
		if (placement != other.placement) return false
		if (!image.contentEquals(other.image)) return false
		if (itemCount != other.itemCount) return false
		if (bestBefore != other.bestBefore) return false
		if (remindIn != other.remindIn) return false

		return true
	}

	override fun hashCode(): Int {
		var result = foodId ?: 0
		result = 31 * result + name.hashCode()
		result = 31 * result + placement
		result = 31 * result + image.contentHashCode()
		result = 31 * result + itemCount.hashCode()
		result = 31 * result + (bestBefore?.hashCode() ?: 0)
		result = 31 * result + (remindIn ?: 0)
		return result
	}
}



