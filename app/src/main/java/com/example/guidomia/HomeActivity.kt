package com.example.guidomia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.guidomia.databinding.ActivityHomeBinding
import com.example.guidomia.viewModel.CarViewModel

class HomeActivity : AppCompatActivity() {

    companion object{
        const val TAG = "HomeActivity"
    }
    private val binding: ActivityHomeBinding by lazy {
        ActivityHomeBinding.inflate(layoutInflater)
    }

    private lateinit var viewModel: CarViewModel //To perform operations and get data
    private lateinit var adapter: CarsAdapter //To display the list

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initData()
        initUI()

        loadScreen()
    }

    /** 9/6/2022
     * This method will initialize the view models, helpers and other dependencies as required
     *
     **/
    private fun initData() {

        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        )[CarViewModel::class.java]

        adapter = CarsAdapter(this, mutableListOf())
    }

    /** 9/6/2022
     * This method will initialize the static UI elements like toolbar, recycler view layouts etc.
     * and set the click listeners
     **/
    private fun initUI() {

        window.statusBarColor = ContextCompat.getColor(this, R.color.orange)

        binding.rvCars.layoutManager = LinearLayoutManager(this,
            RecyclerView.VERTICAL, false)
        binding.rvCars.adapter = adapter

        viewModel.carsLiveData.observe(this) { carsList ->
            adapter.updateList(carsList)
            setFilters()
        }

    }

    /** 9/6/2022
    * This method will initiate the loading of screen data.
    *
    **/
    private fun loadScreen() {

        viewModel.loadData()

    }

    //Used to display the NO DATA message after filtering if applicable.
    private val emptyList = { isEmpty: Boolean ->
        if(isEmpty){
            binding.tvNoData.visibility = View.VISIBLE
        } else {
            binding.tvNoData.visibility = View.GONE
        }
    }

    private fun setFilters(){
        val makeSpinnerItems = mutableListOf<String>(resources.getString(R.string.any_make))
        makeSpinnerItems.addAll(viewModel.getCarMakeList())
        binding.makeSpinner.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, makeSpinnerItems)

        val modelSpinnerItems = mutableListOf<String>(resources.getString(R.string.any_model))
        modelSpinnerItems.addAll(viewModel.getCarModelList())
        binding.modelSpinner.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, modelSpinnerItems)

        binding.modelSpinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                adapter.filter(makeSpinnerItems[binding.makeSpinner.selectedItemPosition],modelSpinnerItems[position], emptyList)
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }

        binding.makeSpinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                adapter.filter(makeSpinnerItems[position], modelSpinnerItems[binding.modelSpinner.selectedItemPosition], emptyList)
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }

    }
}