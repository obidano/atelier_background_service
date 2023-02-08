package com.odc.odctrackingcommercial.lib.dao

import androidx.room.*
import com.odc.odctrackingcommercial.lib.utils.Constantes
import com.odc.odctrackingcommercial.models.LocationModel
import kotlinx.coroutines.flow.Flow

@Dao
interface LocationDao {
    @Query("select * from ${Constantes.TB_LOCATIONS} order by id desc")
    fun getAllData(): Flow<List<LocationModel>>

    @Query("select * from ${Constantes.TB_LOCATIONS} where id=:rowID")
    fun getSelectedData(rowID: Int): Flow<LocationModel>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun add(data: LocationModel)

    @Update
    suspend fun update(data: LocationModel)

    @Delete
    suspend fun delete(data: LocationModel)

    @Query("Delete from ${Constantes.TB_LOCATIONS}")
    suspend fun deleteAll()


}