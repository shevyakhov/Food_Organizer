package com.chelz.foodorganizer.screens.foodList.domain

import com.chelz.foodorganizer.screens.foodList.data.dao.StatisticsEntity

interface LocalStatisticsDataSource {

	suspend fun insert(statistics: StatisticsEntity)
	suspend fun update(statistics: StatisticsEntity)
	suspend fun getAll(): List<StatisticsEntity>
	suspend fun getByDate(date: String): StatisticsEntity?
	suspend fun delete(statistics: StatisticsEntity)
}