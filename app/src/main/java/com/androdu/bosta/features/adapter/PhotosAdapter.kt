package com.androdu.bosta.features.adapter

import android.annotation.SuppressLint
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.androdu.bosta.data.api.model.Photo
import com.androdu.bosta.databinding.PhotoListItemBinding
import com.androdu.bosta.utils.AdapterDiffUtil

@SuppressLint("NotifyDataSetChanged")
class PhotosAdapter : RecyclerView.Adapter<PhotosAdapter.MyViewHolder>(), Filterable {
    private var originalPhotosList = mutableListOf<Photo>()
    private var photosList = mutableListOf<Photo>()

    private var listener: OnItemClickListener? = null

    class MyViewHolder(private val binding: PhotoListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(photo: Photo, listener: OnItemClickListener?) {
            binding.photo = photo
            binding.root.setOnClickListener {
                listener?.onClick(photo)
            }
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): MyViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = PhotoListItemBinding.inflate(layoutInflater, parent, false)
                return MyViewHolder(binding)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder.from(parent)
    }

    override fun getItemCount(): Int {
        return photosList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentPhoto = photosList[position]
        holder.bind(currentPhoto, listener)
    }

    fun setData(photos: MutableList<Photo>) {
        // Copy the original list of photos
        originalPhotosList.clear()
        originalPhotosList.addAll(photos)
        // Apply the DiffUtil to update the photosList
        val photoDiffUtil = AdapterDiffUtil(photosList, photos)
        val photoDiffUtilResult = DiffUtil.calculateDiff(photoDiffUtil)
        photosList = photos
        photoDiffUtilResult.dispatchUpdatesTo(this)
    }

    fun clearData() {
        setData(mutableListOf())
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filteredList = mutableListOf<Photo>()
                if (constraint.isNullOrEmpty()) {
                    // If the query is empty, return the original list
                    filteredList.addAll(originalPhotosList)
                } else {
                    // Otherwise, filter the list by matching the photo title with the query
                    val filterPattern = constraint.toString().lowercase().trim()
                    originalPhotosList.forEach {
                        if (it.title.contains(filterPattern))
                            filteredList.add(it)
                    }
                    println(originalPhotosList.size)
                    println(filteredList.size)
                }
                val filterResults = FilterResults()
                filterResults.values = filteredList
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                // Update the photosList with the filtered results and notify the adapter
                photosList.clear()
                photosList.addAll(results?.values as MutableList<Photo>)
                notifyDataSetChanged()
            }
        }
    }

    fun interface OnItemClickListener {
        fun onClick(photo: Photo)
    }
}