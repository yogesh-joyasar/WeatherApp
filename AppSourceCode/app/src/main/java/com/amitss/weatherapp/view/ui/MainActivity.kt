package com.amitss.weatherapp.view.ui

import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.amitss.weatherapp.R
import com.amitss.weatherapp.service.CityResponse
import com.amitss.weatherapp.viewmodel.WeatherAppViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.row_list_item.*
import timber.log.Timber

/**
 * Home screen activity for application.
 */
class MainActivity : BaseActivity() {

    private lateinit var weatherAppViewModel: WeatherAppViewModel
    private val context: Context = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Timber.d(getString(R.string.str_on_create))
        setSupportActionBar(toolbar)

        // View Model
        weatherAppViewModel =
            ViewModelProviders.of(context as FragmentActivity).get(WeatherAppViewModel::class.java)

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        Timber.d(getString(R.string.str_on_create_options_menu))
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_main, menu)
        val menuItem: MenuItem? = menu?.findItem(R.id.app_bar_search)

        return true
    }

    override fun onResume() {
        super.onResume()
        Timber.d(getString(R.string.str_on_resume))
        getSavedCityList()
    }

    /**
     * Returned list of saved last ten city entry from Database.
     */
    private fun getSavedCityList() {
        Timber.d(getString(R.string.str_saved_city_list))

        weatherAppViewModel.fetchDBCityList().observe(context as MainActivity, Observer {
            if (it == null || (it as CityResponse<*>).value is Exception) {
                Timber.d(getString(R.string.str_prev_search_error_title))
                updateSearchListNotFoundError()
            } else {
                Timber.d((it as CityResponse<*>).value.toString())
                updateSearchListView(false)
            }
        })

    }

    /**
     * Update Error screen while not able to find the saved city list
     */
    private fun updateSearchListNotFoundError() {
        updateSearchListView(true)
        ivIcon.setBackgroundResource(R.drawable.ic_info)
        ivIcon.invalidate()
        tvName.text = getString(R.string.str_prev_search_error_title)
        tvMessage.text = getString(R.string.str_prev_search_error_message)
    }

    /**
     * Show/hide error view
     */
    private fun updateSearchListView(isError: Boolean) {
        infoView.visibility = if (isError) {
            View.VISIBLE
        } else {
            View.GONE
        }
        viewCity.visibility = if (isError) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }


}
