package com.odc.odctrackingcommercial.lib.dao

import androidx.room.*
import com.odc.odctrackingcommercial.models.ArticlesModel
import com.odc.odctrackingcommercial.lib.utils.Constantes
import kotlinx.coroutines.flow.Flow

@Dao
interface ArticlesDao {
    @Query("select * from ${Constantes.TB_ARTICLES} order by id desc")
    fun getAllData(): Flow<List<ArticlesModel>>

    @Query("select * from ${Constantes.TB_ARTICLES} where id=:rowID")
    fun getSelectedData(rowID: Int): Flow<ArticlesModel>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun add(data: ArticlesModel)

    @Update
    suspend fun update(data: ArticlesModel)

    @Delete
    suspend fun delete(data: ArticlesModel)

    @Query("Delete from ${Constantes.TB_ARTICLES}")
    suspend fun deleteAll()

    @Query("Select * from ${Constantes.TB_ARTICLES} where nom like :search")
    fun search(search: String): Flow<List<ArticlesModel>>

}