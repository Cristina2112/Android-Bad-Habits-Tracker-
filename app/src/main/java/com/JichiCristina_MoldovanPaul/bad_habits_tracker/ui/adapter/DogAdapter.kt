package com.jichicristina_moldovanpaul.bad_habits_tracker.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.jichicristina_moldovanpaul.bad_habits_tracker.R
import com.jichicristina_moldovanpaul.bad_habits_tracker.network.models.DogResponseItem

class DogAdapter : ListAdapter<DogResponseItem, DogAdapter.DogViewHolder>(DogDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DogViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_dog, parent, false)
        return DogViewHolder(view)
    }

    override fun onBindViewHolder(holder: DogViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class DogViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val ivDog: ImageView = itemView.findViewById(R.id.ivDog)

        fun bind(dog: DogResponseItem) {
            Glide.with(itemView.context)
                .load(dog.url)
                .transition(DrawableTransitionOptions.withCrossFade())
                .centerCrop()
                .into(ivDog)
        }
    }

    class DogDiffCallback : DiffUtil.ItemCallback<DogResponseItem>() {
        override fun areItemsTheSame(oldItem: DogResponseItem, newItem: DogResponseItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: DogResponseItem, newItem: DogResponseItem): Boolean {
            return oldItem == newItem
        }
    }
}
