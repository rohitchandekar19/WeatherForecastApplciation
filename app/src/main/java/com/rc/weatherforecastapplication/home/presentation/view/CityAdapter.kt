package com.rc.weatherforecastapplication.home.presentation.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rc.weatherforecastapplication.databinding.CityItemViewBinding
import com.rc.weatherforecastapplication.home.presentation.view.listeners.ClickListener
import com.rc.weatherforecastapplication.home.presentation.viewmodel.WeatherInfo
import com.rc.weatherforecastapplication.home.presentation.viewmodel.WeatherViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CityAdapter(
    private val items: MutableList<WeatherInfo>,
    private val clickListener: ClickListener<WeatherInfo>
) :
    RecyclerView.Adapter<CityAdapter.CityViewHolder>() {

    inner class CityViewHolder(
        private val binding: CityItemViewBinding,
        private val clickListener: ClickListener<WeatherInfo>
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(weatherInfo: WeatherInfo) {
            binding.cityName.text = weatherInfo.city.plus(", ${weatherInfo.country}")
            binding.cardView.setOnClickListener {
                clickListener.onClick(it, weatherInfo)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityViewHolder {
        return CityViewHolder(
            CityItemViewBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), clickListener
        )
    }

    override fun onBindViewHolder(holder: CityViewHolder, position: Int) {
        holder.bind(items[position])
        holder.itemView.tag = items[position]
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun removeAt(position: Int, weatherViewModel: WeatherViewModel) {
        CoroutineScope(Dispatchers.Default).launch {
            val isDeleted = weatherViewModel.deleteItem(items[position].city)
            withContext(Dispatchers.Main) {
                if (isDeleted) {
                    items.removeAt(position)
                    notifyItemRemoved(position)
                }
            }
        }
    }
}