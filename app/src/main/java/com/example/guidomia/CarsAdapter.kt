package com.example.guidomia

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.example.guidomia.databinding.ItemCarListBinding
import com.example.guidomia.databinding.ItemProConBinding
import com.example.guidomia.databinding.ItemProConTitleBinding

class CarsAdapter(private val context: Context, val list: MutableList<CarDTO>) :
    RecyclerView.Adapter<CarsAdapter.CarsVH>() {

    companion object {
        const val TAG = "CarsAdapter"
    }

    private var selectedPosition = 0 //For expanding and collapsing the list.
    private var filteredList: MutableList<CarDTO> =
        mutableListOf() //Actual list that will be displayed

    class CarsVH(view: View) : RecyclerView.ViewHolder(view) {

        //Declare the binding for item
        val binding: ItemCarListBinding by lazy {
            ItemCarListBinding.bind(view)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarsVH {
        return CarsVH(ItemCarListBinding.inflate(LayoutInflater.from(context), parent, false).root)
    }

    override fun onBindViewHolder(holder: CarsVH, position: Int) {

        //Clear all the views from the pros and cons so that duplication is avoided
        holder.binding.llProsCons.removeAllViews()

        filteredList[position].let { car ->
            //Set the name, price and rating from the model
            holder.binding.tvCarName.text = car.getCarName()
            holder.binding.tvPrice.text = car.getPrice()
            holder.binding.ratingBar.rating = car.rating.toFloat()
            //Based on the make of the car, set the image resource
            when {
                car.make.contains(context.resources.getString(R.string.alpine), true) -> {
                    holder.binding.ivCar.setImageResource(R.drawable.alpine)
                }
                car.make.contains(context.resources.getString(R.string.bmw), true) -> {
                    holder.binding.ivCar.setImageResource(R.drawable.bmw)
                }
                car.make.contains(context.resources.getString(R.string.mercedes), true) -> {
                    holder.binding.ivCar.setImageResource(R.drawable.mercedes)
                }
                car.make.contains(context.resources.getString(R.string.land_rover), true) -> {
                    holder.binding.ivCar.setImageResource(R.drawable.range_rover)
                }
                //Showing the tacoma image by default if none of the conditions meet.
                else -> {
                    holder.binding.ivCar.setImageResource(R.drawable.tacoma)
                }
            }

            //Only load the pros and cons if the car is selected.
            if (car.selected) {
                //Set the visibility to visible
                holder.binding.llProsCons.visibility = View.VISIBLE

                //Only if there are details other than empty string, first add the title for pros
                if (car.prosList.any { it.isNotEmpty() }) {
                    val proTitle = ItemProConTitleBinding.inflate(LayoutInflater.from(context))
                    proTitle.root.text = context.resources.getString(R.string.pros)
                    holder.binding.llProsCons.addView(proTitle.root)
                }

                //Filter all the non empty values
                car.prosList.filter { it.isNotBlank() }.forEach { pro ->
                    //For each pro, inflate the item, set the text and add them to the layout.
                    val pros = ItemProConBinding.inflate(LayoutInflater.from(context))
                    pros.root.text = pro
                    holder.binding.llProsCons.addView(pros.root)
                }

                //Repeat the same procedure for the cons list
                if (car.consList.any { it.isNotEmpty() }) {
                    val consTitle = ItemProConTitleBinding.inflate(LayoutInflater.from(context))
                    consTitle.root.text = context.resources.getString(R.string.cons)
                    holder.binding.llProsCons.addView(consTitle.root)
                }

                car.consList.filter { it.isNotBlank() }.forEach { cons ->
                    val pros = ItemProConBinding.inflate(LayoutInflater.from(context))
                    pros.root.text = cons
                    holder.binding.llProsCons.addView(pros.root)
                }

            } else {
                //Hide the layout if the item is not selected.
                holder.binding.llProsCons.visibility = View.GONE
            }

            holder.itemView.setOnClickListener {
                //If the position is different than already selected item
                if (position != selectedPosition) {
                    //Set the current selection as false, update that item to close it
                    filteredList[selectedPosition].selected = false
                    notifyItemChanged(selectedPosition)
                    //Set the current selection as true, update the item to open it
                    selectedPosition = position
                    car.selected = true
                    notifyItemChanged(position)
                } else {
                    //If the same item is clicked, then revert the selection and update the item
                    car.selected = !car.selected
                    notifyItemChanged(position)
                }

            }
        }
    }

    override fun getItemCount(): Int {
        //Return the Filtered list size, not the original list
        return filteredList.size
    }

    /** 9/7/2022
    * This method will update the current list, set the first item selection as true to open
    * the item by default.
     * @param list -> new list to be displayed.
    **/
    fun updateList(list: List<CarDTO>) {
        this.list.addAll(list)
        this.filteredList.addAll(list)
        this.filteredList[0].selected = true
        notifyItemRangeChanged(0, this.filteredList.size)
    }

    /** 9/7/2022
    * This method will filter the list based on the make and model passed to it
    * @param make -> Make of the car
     * @param model -> Model of the car
    **/
    fun filter(make: String, model: String, callback: (Boolean) -> Unit) {
        //First, clear the list
        this.filteredList.clear()

        //If the selections is default, add all the elements
        if (make == context.resources.getString(R.string.any_make) && model == context.resources.getString(
                R.string.any_model)
        ) {
            this.filteredList.addAll(list)
        }
        //If the make is default and model has value, filter based on model
        else if (make.equals(context.resources.getString(R.string.any_make),
                true) && model != context.resources.getString(R.string.any_model)
        ) {
            this.filteredList.addAll(list.filter { it.model.contains(model, true) })
        }
        //If the model is default and make has value, filter based on make
        else if (model.equals(context.resources.getString(R.string.any_model),
                true) && make != context.resources.getString(R.string.any_make)
        ) {
            this.filteredList.addAll(list.filter { it.make.contains(make, true) })
        }
        //If both make and model are passed, filter the list based on both of them
        else {
            this.filteredList.addAll(list.filter {
                it.make.equals(make, true) && it.model.equals(model,
                    true)
            })
        }
        //Return the result of filter to display the NO DATA message
        callback.invoke(this.filteredList.isEmpty())
        notifyDataSetChanged()
    }
}