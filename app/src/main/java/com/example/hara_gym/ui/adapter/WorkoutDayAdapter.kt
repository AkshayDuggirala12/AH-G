package com.example.hara_gym.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hara_gym.data.model.WorkoutDayDto
import com.example.hara_gym.databinding.ItemWorkoutDayBinding

class WorkoutDayAdapter(private val onDayClick: (WorkoutDayDto) -> Unit) :
    RecyclerView.Adapter<WorkoutDayAdapter.WorkoutDayViewHolder>() {

    private var days: List<WorkoutDayDto> = emptyList()

    fun submitList(newDays: List<WorkoutDayDto>?) {
        days = newDays ?: emptyList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkoutDayViewHolder {
        val binding = ItemWorkoutDayBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WorkoutDayViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WorkoutDayViewHolder, position: Int) {
        holder.bind(days[position])
    }

    override fun getItemCount(): Int = days.size

    inner class WorkoutDayViewHolder(private val binding: ItemWorkoutDayBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(day: WorkoutDayDto) {
            binding.textViewDayName.text = day.dayName
            binding.textViewTitle.text = day.title
            binding.textViewDescription.text = day.description
            binding.textViewExerciseCount.text = "${day.exercises.size} Exercises"
            
            binding.root.setOnClickListener { onDayClick(day) }
        }
    }
}
