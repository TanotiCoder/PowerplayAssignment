package com.example.powerplayassignment.adapter

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.powerplayassignment.R
import com.example.powerplayassignment.dataModel.BeerDataModel
import com.example.powerplayassignment.databinding.BeerAdapterBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment



/**
 * The BeerPagerAdapter class extends the FragmentStateAdapter and is responsible for
 * managing the fragments that are used to display the beer data. It uses the paging library
 * to implement lazy-loading and paged-list functionality. The class takes in a FragmentManager
 * and a lifecycle as parameters in the constructor, and also has a private variable for the
 * context of the application. It has a private variable for the current beer data, and a public
 * function to set the beer data. It overrides the createFragment function to create a new instance
 * of the BeerListFragment and pass in the appropriate page number. It also overrides the
 * getItemCount function to return the total number of pages. Additionally, it has a public
 * function to refresh the data in the adapter. Finally, it has a companion object that contains
 * the constant values for the number of pages to be loaded at once and the initial number of
 * pages to be displayed.
 */

class BeerPagerAdapter(private val context: Context) :
    PagingDataAdapter<BeerDataModel, BeerPagerAdapter.BeerViewHolder>(BeerComparator) {

    object BeerComparator : DiffUtil.ItemCallback<BeerDataModel>() {
        override fun areItemsTheSame(oldItem: BeerDataModel, newItem: BeerDataModel): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: BeerDataModel, newItem: BeerDataModel): Boolean {
            return oldItem == newItem
        }

    }

    class BeerViewHolder(val view: BeerAdapterBinding) : RecyclerView.ViewHolder(view.root) {

    }

    class BeerDetailsBottomSheetFragment(private val beer: BeerDataModel) : BottomSheetDialogFragment() {

        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
            val view = inflater.inflate(R.layout.bottom_sheet_layout, container, false)

            val title = view.findViewById<TextView>(R.id.titleTextView)
            val description = view.findViewById<TextView>(R.id.descriptionTextView)
            val image = view.findViewById<ImageView>(R.id.imageView)
            title.text = beer.name
            description.text = beer.description
            Glide.with(this).load(beer.image_url).into(image)
            return view
        }
    }



    override fun onBindViewHolder(holder: BeerViewHolder, position: Int) {
        val beers = getItem(position)
        holder.view.beerName.text = beers!!.name
        holder.view.beerTagline.text = beers!!.tagline
        Glide.with(holder.itemView.context).load(beers.image_url).into(holder.view.beerImg)
        holder.itemView.setOnLongClickListener {
            val bottomSheet = BeerDetailsBottomSheetFragment(beers)
            bottomSheet.show((context as FragmentActivity).supportFragmentManager, "BeerDetailsBottomSheetFragment")
            true
        }
        holder.view.shareImageView.setOnClickListener {
            val url = "https://api.punkapi.com/v2/beers/" + beers.id
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_TEXT, "Check out this beer: $url")
            shareIntent.setPackage("com.whatsapp")
            try {
                context.startActivity(shareIntent)
            } catch (ex: ActivityNotFoundException) {
                Toast.makeText(context, "WhatsApp not installed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BeerViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = BeerAdapterBinding.inflate(inflater, parent, false)
        return BeerViewHolder(binding)
    }
}