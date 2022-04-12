package com.udacity.asteroidradar

import java.text.SimpleDateFormat
import java.util.*

object Utils {

    // Utils to get Today's date formatted date and after 7 days date

    fun getTodayFormattedDate() : String{
        val calendar = Calendar.getInstance()
        val currentTime = calendar.time
        val dateFormat = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
        return dateFormat.format(currentTime)
    }

    fun getEndFormattedDate() : String{
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, 7)
        val currentTime = calendar.time
        val dateFormat = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
        return dateFormat.format(currentTime)
    }


}