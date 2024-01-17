package com.example.locationtracker.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.locationtracker.databinding.AdapterUsersItemBinding
import com.example.locationtracker.listener.UsersItemClickListener
import com.example.locationtracker.model.UserRealmModel


class UsersAdapter(private val context: Context, private val userList: ArrayList<UserRealmModel>, private val listener: UsersItemClickListener) : RecyclerView.Adapter<UsersAdapter.UserViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding = AdapterUsersItemBinding.inflate(LayoutInflater.from(context),parent,false)
        return UserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = userList[position]
        with(holder.binding) {
            tvUserName.text = user.name
            tvUserMail.text = user.mail
        }
        holder.itemView.setOnClickListener {
            listener.userItemClickListener(user)
        }
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    inner class UserViewHolder(val binding: AdapterUsersItemBinding) : RecyclerView.ViewHolder(binding.root)

    // Data class for user information

}