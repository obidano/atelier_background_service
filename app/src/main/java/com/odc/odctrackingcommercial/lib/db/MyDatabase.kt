package com.odc.odctrackingcommercial.lib.db

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.odc.odctrackingcommercial.lib.dao.ActivitesDao
import com.odc.odctrackingcommercial.lib.dao.ArticlesDao
import com.odc.odctrackingcommercial.lib.dao.LocationDao
import com.odc.odctrackingcommercial.lib.dao.TodoDao
import com.odc.odctrackingcommercial.models.ActivitesModel
import com.odc.odctrackingcommercial.models.ArticlesModel
import com.odc.odctrackingcommercial.models.LocationModel
import com.odc.odctrackingcommercial.models.TodoModel

@Database(
    entities = [TodoModel::class, ArticlesModel::class, ActivitesModel::class, LocationModel::class],
    version = 1,
   /* autoMigrations = [
        AutoMigration(from = 1, to = 2),
        AutoMigration(from = 2, to = 3),
        AutoMigration(from = 3, to = 4),
    ],*/
    exportSchema = true
)
@TypeConverters(RoomConverters::class)
abstract class MyDatabase : RoomDatabase() {

    abstract fun todoQueries(): TodoDao
    abstract fun articlesQueries(): ArticlesDao
    abstract fun activitesQueries(): ActivitesDao
    abstract fun locationQueries(): LocationDao

}