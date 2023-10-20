package com.androdu.bosta.features.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.androdu.bosta.data.api.model.User
import com.androdu.bosta.databinding.UserListItemBinding
import com.androdu.bosta.utils.AdapterDiffUtil

class UsersAdapter : RecyclerView.Adapter<UsersAdapter.MyViewHolder>() {
    private var usersList = mutableListOf<User>()

    private var listener: OnItemClickListener? = null

    class MyViewHolder(private val binding: UserListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(user: User, listener: OnItemClickListener?) {
            binding.user = user
            binding.root.setOnClickListener {
                listener?.onClick(user)
            }
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): MyViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = UserListItemBinding.inflate(layoutInflater, parent, false)
                return MyViewHolder(binding)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder.from(parent)
    }

    override fun getItemCount(): Int {
        return usersList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentUser = usersList[position]
        holder.bind(currentUser, listener)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(users: MutableList<User>) {
        val usersDiffUtil = AdapterDiffUtil(usersList, users)
        val usersDiffUtilResult = DiffUtil.calculateDiff(usersDiffUtil)
        usersList = users
        usersDiffUtilResult.dispatchUpdatesTo(this)
        notifyDataSetChanged()
        Log.d("Api", "setData: " + usersDiffUtil.newListSize)
    }

    fun clearData() {
        setData(mutableListOf())
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    fun interface OnItemClickListener {
        fun onClick(user: User)
    }
}