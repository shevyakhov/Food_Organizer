package com.chelz.foodorganizer.screens.toBuyList.data.dao

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class PurchaseListTypeConverter {

	@TypeConverter
	fun fromPurchaseList(purchaseList: List<Purchase>?): String? {
		return if (purchaseList == null) {
			null
		} else {
			val gson = Gson()
			val type = object : TypeToken<List<Purchase>>() {}.type
			gson.toJson(purchaseList, type)
		}
	}

	@TypeConverter
	fun toPurchaseList(purchaseListString: String?): List<Purchase>? {
		return if (purchaseListString == null) {
			null
		} else {
			val gson = Gson()
			val type = object : TypeToken<List<Purchase>>() {}.type
			gson.fromJson(purchaseListString, type)
		}
	}
}



