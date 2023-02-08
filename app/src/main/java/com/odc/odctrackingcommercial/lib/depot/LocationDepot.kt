package com.odc.odctrackingcommercial.lib.depot

import com.odc.odctrackingcommercial.lib.dao.LocationDao
import com.odc.odctrackingcommercial.models.LocationModel
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class LocationDepot @Inject constructor(private val query: LocationDao) {

    val getAllLocation = query.getAllData()

    fun getSelectedLocations(rowId: Int) = query.getSelectedData(rowId)

    suspend fun addLocation(data: LocationModel) {
        query.add(data)
    }

    suspend fun updateLocation(data: LocationModel) {
        query.update(data)
    }

    suspend fun deleteLocation(data: LocationModel) {
        query.delete(data)
    }

    suspend fun deleteAllLocation() {
        query.deleteAll()
    }

}