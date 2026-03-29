package com.example.hara_gym.ui.screens

import android.view.LayoutInflater
import android.view.View
import androidx.compose.runtime.*
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hara_gym.databinding.LayoutDietPlanBinding
import com.example.hara_gym.ui.adapter.MealAdapter
import com.example.hara_gym.ui.viewmodel.DietUiState
import com.example.hara_gym.ui.viewmodel.DietViewModel

@Composable
fun DietPlanScreen(
    onBack: () -> Unit,
    viewModel: DietViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val mealAdapter = remember { MealAdapter() }

    AndroidView(
        factory = { context ->
            LayoutDietPlanBinding.inflate(LayoutInflater.from(context)).apply {
                toolbar.setNavigationOnClickListener { onBack() }
                
                recyclerViewMeals.layoutManager = LinearLayoutManager(context)
                recyclerViewMeals.adapter = mealAdapter
            }.root
        },
        update = { view ->
            val binding = LayoutDietPlanBinding.bind(view)
            
            when (val state = uiState) {
                is DietUiState.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is DietUiState.Success -> {
                    binding.progressBar.visibility = View.GONE
                    mealAdapter.submitList(state.plan?.meals)
                }
                is DietUiState.Error -> {
                    binding.progressBar.visibility = View.GONE
                }
            }
        }
    )
}
