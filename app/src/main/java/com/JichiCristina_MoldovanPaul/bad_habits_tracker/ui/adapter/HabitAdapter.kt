package com.jichicristina_moldovanpaul.bad_habits_tracker.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.jichicristina_moldovanpaul.bad_habits_tracker.R
import com.jichicristina_moldovanpaul.bad_habits_tracker.data.local.HabitEntity

class HabitAdapter(
    private val onItemClick: (HabitEntity) -> Unit,
    private val onResetClick: (HabitEntity) -> Unit
) : ListAdapter<HabitEntity, HabitAdapter.HabitViewHolder>(HabitDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HabitViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_habit, parent, false)
        return HabitViewHolder(view, onItemClick, onResetClick)
    }

    override fun onBindViewHolder(holder: HabitViewHolder, position: Int) {
        val habit = getItem(position)
        holder.bind(habit)
    }

    class HabitViewHolder(
        itemView: View,
        private val onItemClick: (HabitEntity) -> Unit,
        private val onResetClick: (HabitEntity) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {
        private val tvHabitDetails: TextView = itemView.findViewById(R.id.tvHabitDetails)
        private val btnResetHabit: android.widget.ImageButton = itemView.findViewById(R.id.btnResetHabit)

        fun bind(habit: HabitEntity) {
            val currentTime = System.currentTimeMillis()
            val diffInMillis = currentTime - habit.startDate
            val days = diffInMillis / (1000 * 60 * 60 * 24)
            
            tvHabitDetails.text = "${habit.habitName} — Te-ai lăsat de $days zile!"

            itemView.setOnClickListener {
                onItemClick(habit)
            }
            
            btnResetHabit.setOnClickListener {
                onResetClick(habit)
            }
        }
    }

    class HabitDiffCallback : DiffUtil.ItemCallback<HabitEntity>() {
        override fun areItemsTheSame(oldItem: HabitEntity, newItem: HabitEntity): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: HabitEntity, newItem: HabitEntity): Boolean {
            return oldItem == newItem
        }
    }
}
