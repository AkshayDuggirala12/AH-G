package com.example.hara_gym.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hara_gym.data.model.DietMealDto
import com.example.hara_gym.databinding.ItemMealBinding

class MealAdapter : RecyclerView.Adapter<MealAdapter.MealViewHolder>() {

    private var meals: List<DietMealDto> = emptyList()

    fun submitList(newMeals: List<DietMealDto>?) {
        meals = newMeals ?: emptyList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealViewHolder {
        val binding = ItemMealBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MealViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MealViewHolder, position: Int) {
        holder.bind(meals[position])
    }

    override fun getItemCount(): Int = meals.size

    class MealViewHolder(private val binding: ItemMealBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(meal: DietMealDto) {
            binding.textViewMealName.text = meal.mealName
            binding.textViewMealTime.text = meal.mealTime
            binding.textViewFoods.text = meal.foods
        }
    }
}
