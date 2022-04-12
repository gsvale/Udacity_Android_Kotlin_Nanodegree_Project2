package com.udacity.asteroidradar.main

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.Utils
import com.udacity.asteroidradar.database.getDatabase
import com.udacity.asteroidradar.databinding.FragmentMainBinding
import com.udacity.asteroidradar.domain.Asteroid
import java.text.SimpleDateFormat
import java.util.*

class MainFragment : Fragment() {

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        setHasOptionsMenu(true)

        // Start Room database and create repository in view model, and call API

        val database = getDatabase(requireActivity())
        viewModel.createRepository(database)
        viewModel.callPictureOfDay()
        viewModel.callGetAsteroids()

        // Create Adapter for recyclerview items to be displayed
        // On click item, navigate to detail fragment

        val adapter = AsteroidsAdapter(AsteroidListener { asteroid ->
            this.findNavController().navigate(
                MainFragmentDirections
                    .actionShowDetail(asteroid)
            )
        })
        binding.asteroidRecycler.adapter = adapter

        // Observe variables and update view

        viewModel.pictureOfDay.observe(viewLifecycleOwner, Observer {
            it?.let {
                binding.activityMainImageOfTheDay.contentDescription =
                    getString(R.string.nasa_picture_of_day_content_description_format, it.title)
            }
        })

        viewModel.asteroids.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.addList(it)
            }
        })

        viewModel.weekAsteroids.observe(viewLifecycleOwner, Observer {
        })

        viewModel.todayAsteroids.observe(viewLifecycleOwner, Observer {
        })

        viewModel.savedAsteroids.observe(viewLifecycleOwner, Observer {
        })

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }


    // Set menu filters actions from menu option selected

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.show_week_menu -> viewModel.getWeekAsteroids()
            R.id.show_today_menu -> viewModel.getTodayAsteroids()
            R.id.show_saved_menu -> viewModel.getSavedAsteroids()
            else -> viewModel.getWeekAsteroids()
        }
        return true
    }
}
