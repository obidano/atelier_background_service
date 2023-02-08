package com.odc.odctrackingcommercial.lib.depot

import com.odc.odctrackingcommercial.lib.dao.ActivitesDao
import com.odc.odctrackingcommercial.models.ActivitesModel
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

//@ViewModelScoped
class ActivitesDepot @Inject constructor(private val query: ActivitesDao) {

    val getAllActivites = query.getAllData()
    fun filterActivitesByCat(cat:String) = query.filterByCategorie(cat)

    fun getSelectedActivites(rowId: Int) = query.getSelectedData(rowId)

    suspend fun addActivite(data: ActivitesModel) {
        query.add(data)
    }

    suspend fun updateActivite(data: ActivitesModel) {
        query.update(data)
    }

    suspend fun deleteActivite(data: ActivitesModel) {
        query.delete(data)
    }

    suspend fun deleteAllActivite() {
        query.deleteAll()
    }

    fun searchArticle(search: String) = query.search(search)
}