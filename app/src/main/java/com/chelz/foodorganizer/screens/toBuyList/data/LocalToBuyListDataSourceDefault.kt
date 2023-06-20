package com.chelz.foodorganizer.screens.toBuyList.data

import com.chelz.foodorganizer.screens.toBuyList.data.dao.ToBuyListDao
import com.chelz.foodorganizer.screens.toBuyList.data.dao.ToBuyListEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LocalToBuyListDataSourceDefault(private val toBuyListDao: ToBuyListDao) : LocalToBuyListDataSource {

	override suspend fun getAllToBuyLists(): List<ToBuyListEntity> =
		withContext(Dispatchers.IO) {
			toBuyListDao.getAll()
		}

	override suspend fun getToBuyListById(id: Int) =
		withContext(Dispatchers.IO) {
			toBuyListDao.getById(id)
		}

	override suspend fun insertToBuyList(toBuyList: ToBuyListEntity) {
		withContext(Dispatchers.IO) {
			toBuyListDao.insert(toBuyList)
		}
	}

	override suspend fun updateToBuyList(toBuyList: ToBuyListEntity) {
		withContext(Dispatchers.IO) {
			toBuyListDao.update(toBuyList)
		}
	}

	override suspend fun deleteToBuyList(toBuyList: ToBuyListEntity) {
		withContext(Dispatchers.IO) {
			toBuyListDao.delete(toBuyList)
		}
	}

	override suspend fun deleteAll() {
		withContext(Dispatchers.IO) {
			toBuyListDao.deleteAllItems()
		}
	}
}