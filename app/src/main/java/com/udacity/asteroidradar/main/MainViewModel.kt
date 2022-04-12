package com.udacity.asteroidradar.main

import android.view.View
import androidx.lifecycle.*
import com.udacity.asteroidradar.domain.PictureOfDay
import com.udacity.asteroidradar.database.AsteroidsDatabase
import com.udacity.asteroidradar.domain.Asteroid
import com.udacity.asteroidradar.repository.AsteroidsRepository
import kotlinx.coroutines.launch

class MainViewModel() : ViewModel() {


    private var _pictureOfDay = MutableLiveData<PictureOfDay>()
    val pictureOfDay: LiveData<PictureOfDay>
        get() = _pictureOfDay

    private var _asteroids = MutableLiveData<List<Asteroid>>()
    val asteroids: LiveData<List<Asteroid>>
        get() = _asteroids

    private var _weekAsteroids = MutableLiveData<List<Asteroid>>()
    val weekAsteroids: LiveData<List<Asteroid>>
        get() = _weekAsteroids

    private var _todayAsteroids = MutableLiveData<List<Asteroid>>()
    val todayAsteroids: LiveData<List<Asteroid>>
        get() = _todayAsteroids

    private var _savedAsteroids = MutableLiveData<List<Asteroid>>()
    val savedAsteroids: LiveData<List<Asteroid>>
        get() = _savedAsteroids

    private var _loading = MutableLiveData<Int>(View.VISIBLE)
    val loading: LiveData<Int>
        get() = _loading

    private lateinit var asteroidsDatabase: AsteroidsDatabase
    private lateinit var asteroidsRepository: AsteroidsRepository


    init {
        _loading.postValue(View.VISIBLE)
    }

    // Create Repository

    fun createRepository(database: AsteroidsDatabase) {
        asteroidsDatabase = database
        asteroidsRepository = AsteroidsRepository(asteroidsDatabase)
    }

    // Call Picture of Day method from repo

    fun callPictureOfDay() {
        _pictureOfDay = asteroidsRepository.pictureOfDay as MutableLiveData<PictureOfDay>

        viewModelScope.launch {
            asteroidsRepository.refreshPictureOfDay()
        }
    }

    // Call GetAsteroids method from repo

    fun callGetAsteroids() {
        _loading.postValue(View.VISIBLE)

        _weekAsteroids = asteroidsRepository.weekAsteroids as MutableLiveData<List<Asteroid>>
        _todayAsteroids = asteroidsRepository.todayAsteroids as MutableLiveData<List<Asteroid>>
        _savedAsteroids = asteroidsRepository.savedAsteroids as MutableLiveData<List<Asteroid>>

        _asteroids = _weekAsteroids

        viewModelScope.launch {
            asteroidsRepository.refreshWeekAsteroids(_loading)
        }
    }

    // Methods to get items corresponding to menu selected option

    fun getWeekAsteroids() {
        callGetAsteroids()
    }

    fun getTodayAsteroids() {
        _asteroids.value = _todayAsteroids.value
    }

    fun getSavedAsteroids() {
        _asteroids.value = _savedAsteroids.value
    }

}