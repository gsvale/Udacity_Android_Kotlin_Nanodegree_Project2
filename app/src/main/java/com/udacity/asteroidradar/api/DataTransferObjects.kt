package com.udacity.asteroidradar.api


import com.squareup.moshi.Json
import com.udacity.asteroidradar.database.DatabasePictureOfDay
import com.udacity.asteroidradar.domain.PictureOfDay


// Network Data Objects

data class NetworkPictureOfDay(
    @Json(name = "url")
    val url: String,
    @Json(name = "media_type")
    val mediaType: String,
    @Json(name = "title")
    val title: String
)

fun NetworkPictureOfDay.asDomainModel(): PictureOfDay {
    return PictureOfDay(
        url,
        mediaType,
        title
    )
}

fun NetworkPictureOfDay.asDatabaseModel(): DatabasePictureOfDay {
    return DatabasePictureOfDay(
        url,
        mediaType,
        title
    )
}
