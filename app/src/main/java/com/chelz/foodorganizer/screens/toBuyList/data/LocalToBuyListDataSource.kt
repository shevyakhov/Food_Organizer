package com.chelz.foodorganizer.screens.toBuyList.data

import com.chelz.foodorganizer.screens.toBuyList.data.dao.ToBuyListEntity

interface LocalToBuyListDataSource {

	suspend fun getAllToBuyLists(): List<ToBuyListEntity>

	suspend fun getToBuyListById(id: Int): ToBuyListEntity

	suspend fun insertToBuyList(toBuyList: ToBuyListEntity)

	suspend fun updateToBuyList(toBuyList: ToBuyListEntity)

	suspend fun deleteToBuyList(toBuyList: ToBuyListEntity)

	suspend fun deleteAll()
}