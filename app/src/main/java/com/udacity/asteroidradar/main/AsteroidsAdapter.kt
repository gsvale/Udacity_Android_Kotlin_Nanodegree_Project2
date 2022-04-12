package com.udacity.asteroidradar.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.udacity.asteroidradar.databinding.ListItemAsteroidBinding
import com.udacity.asteroidradar.domain.Asteroid
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

// Adapter used for recyclerview items, receiving a click listener

class AsteroidsAdapter(private val clickListener: AsteroidListener) : ListAdapter<DataItem,
        RecyclerView.ViewHolder>(AsteroidDiffCallback()) {


    private val adapterScope = CoroutineScope(Dispatchers.Default)

    // add/update list of items

    fun addList(list: List<Asteroid>?) {
        adapterScope.launch {
            val items = list!!.map { DataItem.AsteroidItem(it) }
            withContext(Dispatchers.Main) {
                submitList(items)
            }
        }
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ViewHolder -> {
                val asteroidItem = getItem(position) as DataItem.AsteroidItem
                holder.bind(clickListener, asteroidItem.asteroid)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder.from(parent)
    }

    // Binding item to ViewHolder

    class ViewHolder private constructor(private val binding: ListItemAsteroidBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(clickListener: AsteroidListener, item: Asteroid) {
            binding.asteroid = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemAsteroidBinding.inflate(layoutInflater, parent, false)

                return ViewHolder(binding)
            }
        }
    }

}


// DiffCallback to update correctly items in adapter

class AsteroidDiffCallback : DiffUtil.ItemCallback<DataItem>() {
    override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
        return oldItem == newItem
    }
}

// Listener to receive item click event

class AsteroidListener(val clickListener: (asteroid: Asteroid) -> Unit) {
    fun onClick(asteroid: Asteroid) = clickListener(asteroid)
}

sealed class DataItem {
    data class AsteroidItem(val asteroid: Asteroid) : DataItem() {
        override val id = asteroid.id
    }

    abstract val id: Long
}


