package com.udacity.asteroidradar.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*


// Dao interface with all methods/queries for the Room database created

@Dao
interface AsteroidsDao {

    @Query("select * from databasePictureOfDay")
    fun getPictureOfDay(): LiveData<DatabasePictureOfDay>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPictureOfDay(vararg pictureOfDay: DatabasePictureOfDay)

    @Query("select * from databaseAsteroid ORDER BY closeApproachDate ASC")
    fun getSavedAsteroids(): LiveData<List<DatabaseAsteroid>>

    @Query("select * from databaseAsteroid WHERE closeApproachDate BETWEEN :today and :end ORDER BY closeApproachDate ASC")
    fun getWeekAsteroids(today: String?, end: String?): LiveData<List<DatabaseAsteroid>>

    @Query("select * from databaseAsteroid WHERE closeApproachDate LIKE '%' || :today || '%' ORDER BY closeApproachDate ASC")
    fun getTodayAsteroids(today: String?): LiveData<List<DatabaseAsteroid>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAsteroid(vararg asteroid: DatabaseAsteroid)

}

// Set database tables, version, etc..

@Database(
    entities = [DatabasePictureOfDay::class, DatabaseAsteroid::class],
    version = 1,
    exportSchema = false
)
abstract class AsteroidsDatabase : RoomDatabase() {
    abstract val asteroidsDao: AsteroidsDao
}

private lateinit var INSTANCE: AsteroidsDatabase

fun getDatabase(context: Context): AsteroidsDatabase {
    synchronized(AsteroidsDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(
                context.applicationContext,
                AsteroidsDatabase::class.java,
                "asteroids"
            )
                .build()
        }
    }
    return INSTANCE
}