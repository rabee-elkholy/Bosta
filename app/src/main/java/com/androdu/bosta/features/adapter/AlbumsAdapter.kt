package com.androdu.bosta.features.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.androdu.bosta.data.api.model.Album
import com.androdu.bosta.data.api.model.User
import com.androdu.bosta.databinding.AlbumListItemBinding
import com.androdu.bosta.utils.AdapterDiffUtil

class AlbumsAdapter : RecyclerView.Adapter<AlbumsAdapter.MyViewHolder>() {
    private var albumsList = mutableListOf<Album>()

    private var listener: OnItemClickListener? = null

    class MyViewHolder(private val binding: AlbumListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(album: Album, listener: OnItemClickListener?) {
            binding.album = album
            binding.root.setOnClickListener {
                listener?.onClick(album)
            }
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): MyViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = AlbumListItemBinding.inflate(layoutInflater, parent, false)
                return MyViewHolder(binding)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder.from(parent)
    }

    override fun getItemCount(): Int {
        return albumsList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentAlbum = albumsList[position]
        holder.bind(currentAlbum, listener)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(albums: MutableList<Album>) {
        val albumsDiffUtil = AdapterDiffUtil(albumsList, albums)
        val albumsDiffUtilResult = DiffUtil.calculateDiff(albumsDiffUtil)
        albumsList = albums
        albumsDiffUtilResult.dispatchUpdatesTo(this)
        notifyDataSetChanged()
        Log.d("Api", "setData: " + albumsDiffUtil.newListSize)
    }

    fun clearData() {
        setData(mutableListOf())
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    fun interface OnItemClickListener {
        fun onClick(album: Album)
    }
}