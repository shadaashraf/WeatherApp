import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.forcastingapp.R
import com.example.forcastingapp.database.LocalRepository
import com.example.forcastingapp.map.viewModel.MapViewModel
import com.example.forcastingapp.network.WeatherRemoteDataSourceImpl
import com.example.forcastingapp.map.viewModel.MapViewModelFactory
import com.example.forcastingapp.network.State
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.osmdroid.api.IGeoPoint
import org.osmdroid.api.IMapController
import org.osmdroid.config.Configuration
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import java.net.URL

class MapFragment : Fragment() {
    private lateinit var mapViewModel: MapViewModel
    private lateinit var mapView: MapView
    private lateinit var cityEditText: EditText
    private lateinit var btnSaveLocation: Button
    private lateinit var btnSearch: Button

    private var selectedLocation: GeoPoint? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_map, container, false)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize repositories and ViewModel
        val localRepo = LocalRepository.getInstance(requireContext())
        val remoteRepo = WeatherRemoteDataSourceImpl.getInstance() // Ensure this is properly initialized
        mapViewModel = ViewModelProvider(this, MapViewModelFactory(localRepo, remoteRepo)).get(MapViewModel::class.java)

        // Initialize OSMDroid
        Configuration.getInstance().load(requireContext(), requireContext().getSharedPreferences("osm_prefs", 0))

        // Find views by ID
        cityEditText = view.findViewById(R.id.searchEditText)
        btnSaveLocation = view.findViewById(R.id.Savebtn)
        btnSearch = view.findViewById(R.id.searchButton)
        mapView = view.findViewById(R.id.mapView)

        setupObservers()

        // Configure map view
        mapView.setMultiTouchControls(true)
        val mapController: IMapController = mapView.controller
        mapController.setZoom(10.0)
        mapController.setCenter(GeoPoint(38.9072, -77.0369)) // Default to Washington DC

        // Set a touch listener for the MapView to select location
        mapView.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                val geoPoint = mapView.projection.fromPixels(event.x.toInt(), event.y.toInt())
                addMarker(geoPoint)
                selectedLocation = geoPoint as GeoPoint?
                Toast.makeText(requireContext(), "Location selected: $geoPoint", Toast.LENGTH_SHORT).show()
            }
            true
        }

        // Search for city and mark it on map
        btnSearch.setOnClickListener {
            val cityName = cityEditText.text.toString().trim()
            if (cityName.isNotEmpty()) {
                viewLifecycleOwner.lifecycleScope.launch {
                    val geoPoint = getCoordinatesFromCity(cityName)
                    if (geoPoint != null) {
                        selectedLocation = geoPoint
                        addMarker(geoPoint)
                        mapView.controller.setCenter(geoPoint)
                        Toast.makeText(requireContext(), "Location found: $cityName", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(requireContext(), "City not found", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(requireContext(), "Please enter a city name", Toast.LENGTH_SHORT).show()
            }
        }

        // Save the selected location's weather data
        btnSaveLocation.setOnClickListener {
            selectedLocation?.let { geoPoint ->
                mapViewModel.fetchWeatherData(geoPoint.latitude, geoPoint.longitude, "f8b536d2b58104f68af8c41bfd2cad06")
                Toast.makeText(requireContext(), "Weather data saved for location: $geoPoint", Toast.LENGTH_SHORT).show()
            } ?: run {
                Toast.makeText(requireContext(), "Please select a location on the map or search for a city", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            mapViewModel.weatherState.collect { state ->
                when (state) {
                    is State.Loading -> {
                        // Show loading if necessary
                    }
                    is State.WeatherSuccess -> {
                        mapViewModel.saveWeatherDataToDatabase()
                        Toast.makeText(requireContext(), "Location weather saved", Toast.LENGTH_SHORT).show()
                    }
                    is State.Error -> {
                        Toast.makeText(requireContext(), "Error fetching data: ${state.message}", Toast.LENGTH_SHORT).show()
                    }
                    is State.CurrWeatherSuccess -> {}
                }
            }
        }
    }

    private suspend fun getCoordinatesFromCity(cityName: String): GeoPoint? {
        return withContext(Dispatchers.IO) {
            try {
                val url = "https://nominatim.openstreetmap.org/search?city=$cityName&format=json&limit=1"
                val response = URL(url).readText()
                val jsonArray = JSONArray(response)
                if (jsonArray.length() > 0) {
                    val jsonObject = jsonArray.getJSONObject(0)
                    val lat = jsonObject.getDouble("lat")
                    val lon = jsonObject.getDouble("lon")
                    GeoPoint(lat, lon)
                } else {
                    null
                }
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }

    private fun addMarker(geoPoint: IGeoPoint) {
        mapView.overlays.clear()
        val marker = Marker(mapView)
        marker.position = geoPoint as GeoPoint?
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        marker.title = "Selected Location"
        mapView.overlays.add(marker)
        mapView.controller.setCenter(geoPoint)
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mapView.overlays.clear()
        mapView.onDetach()
    }
}
