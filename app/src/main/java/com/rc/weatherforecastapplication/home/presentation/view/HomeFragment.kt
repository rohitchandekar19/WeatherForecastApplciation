package com.rc.weatherforecastapplication.home.presentation.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.maps.model.LatLng
import com.rc.weatherforecastapplication.MapsActivity
import com.rc.weatherforecastapplication.R
import com.rc.weatherforecastapplication.databinding.FragmentHomeBinding
import com.rc.weatherforecastapplication.home.presentation.view.listeners.ClickListener
import com.rc.weatherforecastapplication.home.presentation.view.listeners.SwipeToDeleteCallback
import com.rc.weatherforecastapplication.home.presentation.viewmodel.WeatherInfo
import com.rc.weatherforecastapplication.home.presentation.viewmodel.WeatherViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment(), ClickListener<WeatherInfo> {

    private lateinit var binding: FragmentHomeBinding
    private val weatherViewModel: WeatherViewModel by activityViewModels()

    private val resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { activityResult ->
            if (activityResult.resultCode == Activity.RESULT_OK) {
                val intent = activityResult.data
                intent?.extras?.let { bundle ->
                    val latLong = bundle.getParcelable<LatLng>(LOCATION_LAT_LONG)
                    if (latLong != null) {
                        weatherViewModel.getWeatherConditionsOfLocation("", latLong)
                    } else {
                        Toast.makeText(
                            context,
                            "Not getting the location co-ordinates",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.help) {
            findNavController().navigate(R.id.action_homeFragment_to_helpFragment)
        }
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        activity?.title = "Select/ Add Bookmark City"
        setOnClickListener()
        observeLocationWeather()
        handleSwipe()
    }

    private fun handleSwipe() {
        val swipeHandler = object : SwipeToDeleteCallback(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val adapter = binding.cityRecyclerView.adapter as CityAdapter
                adapter.removeAt(viewHolder.adapterPosition, weatherViewModel)
            }
        }

        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(binding.cityRecyclerView)
    }


    override fun onClick(view: View, item: WeatherInfo) {
        weatherViewModel.resetLiveData()
        findNavController().navigate(
            R.id.action_homeFragment_to_cityWeatherFragment, bundleOf(
                CITY_WEATHER to item
            )
        )
    }

    private fun observeLocationWeather() {
        weatherViewModel.allCityWeatherLiveData.observe(viewLifecycleOwner, {
            if (it.isNotEmpty()) {
                setAdapterView(it)
                binding.bookmarkedErrorTextView.visibility = View.GONE
            } else {
                binding.bookmarkedErrorTextView.visibility = View.VISIBLE
            }
        })
    }

    private fun setAdapterView(cityList: MutableList<WeatherInfo>) {
        val recyclerView = binding.cityRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = CityAdapter(cityList, this)
    }

    private fun setOnClickListener() {
        binding.addBookmark.setOnClickListener {
            resultLauncher.launch(Intent(context, MapsActivity::class.java))
        }
    }

    companion object {
        private const val CITY_WEATHER = "city_weather"
        private const val LOCATION_LAT_LONG = "location_lat_long"
    }

}