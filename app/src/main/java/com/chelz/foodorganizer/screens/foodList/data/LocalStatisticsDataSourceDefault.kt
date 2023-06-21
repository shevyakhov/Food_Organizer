package com.chelz.foodorganizer.screens.foodList.data

import com.chelz.foodorganizer.screens.foodList.data.dao.StatisticsDao
import com.chelz.foodorganizer.screens.foodList.data.dao.StatisticsEntity
import com.chelz.foodorganizer.screens.foodList.domain.LocalStatisticsDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LocalStatisticsDataSourceDefault(private val statisticsDao: StatisticsDao) : LocalStatisticsDataSource {

	override suspend fun insert(statistics: StatisticsEntity) {
		withContext(Dispatchers.IO) {
			statisticsDao.insert(statistics)
		}
	}

	override suspend fun update(statistics: StatisticsEntity) {
		withContext(Dispatchers.IO) {
			statisticsDao.update(statistics)
		}
	}

	override suspend fun getAll(): List<StatisticsEntity> =
		withContext(Dispatchers.IO) {
			statisticsDao.getAll()
		}

	override suspend fun getByDate(date: String): StatisticsEntity? =
		withContext(Dispatchers.IO) {
			statisticsDao.getByDate(date)
		}

	override suspend fun delete(statistics: StatisticsEntity) {
		withContext(Dispatchers.IO) {
			statisticsDao.delete(statistics)
		}
	}
}