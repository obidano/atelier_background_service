package com.odc.odctrackingcommercial.vue_models

import android.location.Location
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.odc.odctrackingcommercial.lib.depot.ActivitesDepot
import com.odc.odctrackingcommercial.lib.depot.ArticlesDepot
import com.odc.odctrackingcommercial.lib.depot.DataStoreDepot
import com.odc.odctrackingcommercial.lib.depot.LocationDepot
import com.odc.odctrackingcommercial.lib.utils.*
import com.odc.odctrackingcommercial.models.ActivitesModel
import com.odc.odctrackingcommercial.models.ArticlesModel
import com.odc.odctrackingcommercial.models.LocationModel
import com.odc.odctrackingcommercial.models.UserModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class SharedVueModel @Inject constructor(
    private val articleDepot: ArticlesDepot,
    private val activityDepot: ActivitesDepot,
    private val locationDepot: LocationDepot,
    private val dataStoreDepot: DataStoreDepot,
    val locationH: LocationHelper,
    val socketUtils: SocketUtils,
) : ViewModel() {

    private val _allArticles = MutableStateFlow<List<ArticlesModel>>(emptyList())
    val allArticles: StateFlow<List<ArticlesModel>> = _allArticles

    //    private val _allActivites = MutableStateFlow<List<ActivitesModel>>(emptyList())
    private val _allActivites =
        MutableStateFlow<RequestState<List<ActivitesModel>>>(RequestState.Idle)
    val allActivites: StateFlow<RequestState<List<ActivitesModel>>> = _allActivites

    private val _fitlerActivites =
        MutableStateFlow<RequestState<List<ActivitesModel>>>(RequestState.Idle)
    val fitlerActivites: StateFlow<RequestState<List<ActivitesModel>>> = _fitlerActivites

    private val _selectedActivite = MutableStateFlow<ActivitesModel?>(null)
    val selectedActivite: StateFlow<ActivitesModel?> = _selectedActivite

    val searchAppBarState = mutableStateOf<SearchAppBarState>(SearchAppBarState.Closed)
    val searchText = mutableStateOf("")
    val filterCategorie = mutableStateOf("")

    private val _allLocations =
        MutableStateFlow<RequestState<List<LocationModel>>>(RequestState.Idle)
    val allLocations: StateFlow<RequestState<List<LocationModel>>> = _allLocations

    var identifiant by mutableStateOf<UserModel?>(null)

    init {
        getAllActivites()
        getAllArticles()
        getAllLocations()
        readUSerState()
        readNewMessage()
        readNewnCoordonnees()

    }

    fun readNewMessage() {
        try {
            viewModelScope.launch {

                socketUtils.messageFlow.collect {
                    Log.d("VM", "readNewMessage msg : $it")
                    activityDepot.addActivite(it)
                }
            }
        } catch (e: Exception) {
            _allLocations.value = RequestState.Error(e)
        }


    }

    fun readNewnCoordonnees() {
        //locationH.checkLocationPermission()
        viewModelScope.launch(Dispatchers.IO) {
            locationH.locationFlow.collect { loc ->
                if (identifiant == null) return@collect

                val newLoc = LocationModel(
                    0,
                    loc.longitude, loc.latitude,
                    Constantes.dateFormatter.format(
                        Calendar.getInstance().time
                    ), identifiant!!
                )
                Log.d("TAG", "Receving : $newLoc")
                val a = _allLocations.value
                if (a is RequestState.Success) {

                    if (a.data.isEmpty()) {
                        locationDepot.addLocation(newLoc)
                    } else {

                        val lastData: LocationModel = a.data.first()
                        val locationA = Location("point A").apply {
                            latitude = lastData.latitude
                            longitude = lastData.longitude
                        }
                        val locationB = Location("point B").apply {
                            latitude = newLoc.latitude
                            longitude = newLoc.longitude
                        }

                        val distance = locationA.distanceTo(locationB)
//                        Log.d("TAG", "Curent $newLoc :::: ${a.data.size}")
//                        Log.d("TAG", "Last $lastData")
                        Log.d("TAG", "Distance: $distance")
                        if (distance >= 5) {
                            locationDepot.addLocation(newLoc)
                        }
                    }

                }
            }
        }
    }


    fun getAllArticles() {
        viewModelScope.launch {
            articleDepot.getAllArticle.collect {
                _allArticles.value = it
            }
        }
    }

    fun saveArticle(data: ArticlesModel) {
        viewModelScope.launch {
            articleDepot.addArticle(data)
        }
    }

    fun getAllLocations() {
        _allLocations.value = RequestState.Loading
        try {
            viewModelScope.launch {
                locationDepot.getAllLocation.collect {
                    val d = it.sortedByDescending { it1 -> it1.id }
                    _allLocations.value =
                        RequestState.Success(d)
                }
            }
        } catch (e: Exception) {
            _allLocations.value = RequestState.Error(e)
        }

    }

    fun getAllActivites() {
        _allActivites.value = RequestState.Loading
        try {
            viewModelScope.launch {
                activityDepot.getAllActivites.collect {
                    _allActivites.value = RequestState.Success(it)
                }
            }
        } catch (e: Exception) {
            _allActivites.value = RequestState.Error(e)
        }

    }

    fun getFitlerActivitesByDescription(desc: String) {
        _fitlerActivites.value = RequestState.Loading
        try {
            viewModelScope.launch {
                activityDepot.searchArticle(desc).collect {
                    _fitlerActivites.value = RequestState.Success(it)
                }
            }
        } catch (e: Exception) {
            _fitlerActivites.value = RequestState.Error(e)
        }
        searchAppBarState.value = SearchAppBarState.Triggered
    }

    fun getSelectedActivite(id: Int) {
        viewModelScope.launch {
            activityDepot.getSelectedActivites(id).collect {
                _selectedActivite.value = it
            }
        }
    }

    fun saveActivite(data: ActivitesModel) {
        viewModelScope.launch(Dispatchers.IO) {
            var maxPos: LocationModel? = null
            val allPos = allLocations.value
            if (allPos is RequestState.Success)
                maxPos = allPos.data.maxBy { it.id }

            val newData = data.copy(localisation = maxPos)
            activityDepot.addActivite(newData)
            val d = mapOf(
                Pair("room", "espace_1"),
                Pair("data", Gson().toJson(newData, ActivitesModel::class.java))
            )
            socketUtils.socket?.emit("send_message", Gson().toJson(d))
        }
    }

    fun updateActivite(data: ActivitesModel) {
        viewModelScope.launch(Dispatchers.IO) {
            activityDepot.updateActivite(data)
        }
    }

    /* private val _userState = MutableStateFlow<RequestState<String>>(RequestState.Idle)
     val userState = _userState*/

    fun readUSerState() {
        viewModelScope.launch {
            dataStoreDepot.readUserState.collect {
                if (it.isNotEmpty()) {
                    identifiant = Gson().fromJson(it, UserModel::class.java)
                }
            }
        }
    }


    fun saveUserState(data: UserModel) {
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreDepot.persisteUserState(Gson().toJson(data, UserModel::class.java))
        }
    }

    /*fun readUserState() {
        _userState.value = RequestState.Loading
        try {
            viewModelScope.launch {
                dataStoreDepot.readUserState.collect {
                    _userState.value = RequestState.Success(it)
                }
            }
        } catch (e: Exception) {
            _userState.value = RequestState.Error(e)
        }
    }*/

}