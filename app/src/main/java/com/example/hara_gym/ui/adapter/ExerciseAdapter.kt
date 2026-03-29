package com.example.hara_gym.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hara_gym.R
import com.example.hara_gym.data.model.ExerciseProgressStatusDto
import com.example.hara_gym.databinding.ItemExerciseBinding

class ExerciseAdapter(private val onToggle: (ExerciseProgressStatusDto) -> Unit) :
    RecyclerView.Adapter<ExerciseAdapter.ExerciseViewHolder>() {

    private var exercises: List<ExerciseProgressStatusDto> = emptyList()

    fun submitList(newExercises: List<ExerciseProgressStatusDto>?) {
        exercises = newExercises ?: emptyList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseViewHolder {
        val binding = ItemExerciseBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ExerciseViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ExerciseViewHolder, position: Int) {
        holder.bind(exercises[position])
    }

    override fun getItemCount(): Int = exercises.size

    inner class ExerciseViewHolder(private val binding: ItemExerciseBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(exercise: ExerciseProgressStatusDto) {
            binding.textViewExerciseName.text = exercise.exerciseName
            
            val iconRes = if (exercise.isCompleted) {
                R.drawable.ic_check_circle
            } else {
                R.drawable.ic_circle_outline
            }
            binding.imageViewStatus.setImageResource(iconRes)
            
            binding.root.setOnClickListener { onToggle(exercise) }
        }
    }
}
