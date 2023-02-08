package com.odc.odctrackingcommercial.lib.dao

import androidx.room.*
import com.odc.odctrackingcommercial.models.ActivitesModel
import com.odc.odctrackingcommercial.lib.utils.Constantes
import kotlinx.coroutines.flow.Flow

@Dao
interface ActivitesDao {
    @Query("select * from ${Constantes.TB_ACTIVITES} order by id desc")
    fun getAllData(): Flow<List<ActivitesModel>>

    @Query("select * from ${Constantes.TB_ACTIVITES} where id=:rowID")
    fun getSelectedData(rowID: Int): Flow<ActivitesModel>

    @Query("select * from ${Constantes.TB_ACTIVITES} where category=:category")
    fun filterByCategorie(category: String): Flow<List<ActivitesModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun add(data: ActivitesModel)

    @Update
    suspend fun update(data: ActivitesModel)

    @Delete
    suspend fun delete(data: ActivitesModel)

    @Query("Delete from ${Constantes.TB_ACTIVITES}")
    suspend fun deleteAll()

    @Query("Select * from ${Constantes.TB_ACTIVITES} where description like :search")
    fun search(search: String): Flow<List<ActivitesModel>>

}