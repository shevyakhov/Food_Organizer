package com.chelz.foodorganizer.screens.foodList.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface StatisticsDao {

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	fun insert(statistics: StatisticsEntity): Long

	@Update
	fun update(statistics: StatisticsEntity)

	@Query("SELECT * FROM StatisticsTable")
	fun getAll(): List<StatisticsEntity>

	@Query("SELECT * FROM StatisticsTable WHERE date = :date")
	fun getByDate(date: String): StatisticsEntity?

	@Delete
	fun delete(statistics: StatisticsEntity)
}