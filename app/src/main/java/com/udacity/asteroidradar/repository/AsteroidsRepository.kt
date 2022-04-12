package com.udacity.asteroidradar.repository


import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.Utils
import com.udacity.asteroidradar.api.AsteroidsAPI
import com.udacity.asteroidradar.api.asDatabaseModel
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.database.AsteroidsDatabase
import com.udacity.asteroidradar.database.asDomainModel
import com.udacity.asteroidradar.domain.Asteroid
import com.udacity.asteroidradar.domain.PictureOfDay
import com.udacity.asteroidradar.domain.asDatabaseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.lang.Exception

// Repository which receives the Room database and make API calls and Room queries

class AsteroidsRepository(private val database: AsteroidsDatabase) {

    // Livedata variable for picture of day

    val pictureOfDay: LiveData<PictureOfDay> =
        Transformations.map(database.asteroidsDao.getPictureOfDay()) {
            it?.asDomainModel()
        }

    // Livedata variable for saved in database asteroids

    val savedAsteroids: LiveData<List<Asteroid>> =
        Transformations.map(database.asteroidsDao.getSavedAsteroids()) {
            it?.asDomainModel()
        }

    // Livedata variable for week asteroids

    val weekAsteroids: LiveData<List<Asteroid>> =
        Transformations.map(
            database.asteroidsDao.getWeekAsteroids(
                today = Utils.getTodayFormattedDate(),
                end = Utils.getEndFormattedDate()
            )
        ) {
            it?.asDomainModel()
        }

    // Livedata variable for today asteroids

    val todayAsteroids: LiveData<List<Asteroid>> =
        Transformations.map(database.asteroidsDao.getTodayAsteroids(today = Utils.getTodayFormattedDate())) {
            it?.asDomainModel()
        }


    // Function to get picture of day and update it in database

    suspend fun refreshPictureOfDay() {
        withContext(Dispatchers.IO) {
            try {
                val pictureOfDay = AsteroidsAPI.retrofitService.getPictureOfDay().await()
                database.asteroidsDao.insertPictureOfDay(pictureOfDay.asDatabaseModel())
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }

    // Function to fetch from API week asteroids and update database

    suspend fun refreshWeekAsteroids(loading: MutableLiveData<Int>) {
        withContext(Dispatchers.IO) {
            try {
                val body = AsteroidsAPI.retrofitService.getAsteroids().await()
                val asteroids = parseAsteroidsJsonResult(JSONObject(body))
                database.asteroidsDao.insertAsteroid(*asteroids.asDatabaseModel())
                loading.postValue(View.GONE)
            } catch (e: Exception) {
                loading.postValue(View.GONE)
                e.printStackTrace()
            }

        }
    }

    // Function called from worker to refresh database with week's asteroids

    suspend fun refreshAsteroids() {
        withContext(Dispatchers.IO) {
            try {
                val body = AsteroidsAPI.retrofitService.getAsteroids().await()
                val asteroids = parseAsteroidsJsonResult(JSONObject(body))
                database.asteroidsDao.insertAsteroid(*asteroids.asDatabaseModel())
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }


}