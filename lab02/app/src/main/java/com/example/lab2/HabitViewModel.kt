package com.example.lab2

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.lab2.data.HabitDao
import com.example.lab2.data.HabitDatabase
import com.example.lab2.data.HabitEntity
import com.example.lab2.data.HabitLogEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

// Data class to represent UI state of a Habit
data class HabitUiModel(
    val id: Int,
    val title: String,
    val isCompletedToday: Boolean,
    val currentStreak: Int
)

enum class HabitFilter {
    ALL, COMPLETED, PENDING
}

class HabitViewModel(application: Application) : AndroidViewModel(application) {

    private val habitDao: HabitDao = HabitDatabase.getDatabase(application).habitDao()

    // Current filter state
    private val _filter = MutableStateFlow(HabitFilter.ALL)
    val filter: StateFlow<HabitFilter> = _filter

    // Today's date string
    private val todayDateString: String = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

    // Combine habits and logs to create UI models
    val habits: StateFlow<List<HabitUiModel>> = combine(
        habitDao.getAllHabits(),
        habitDao.getAllLogs(),
        _filter
    ) { habits, logs, currentFilter ->
        val uiModels = habits.map { habit ->
            val habitLogs = logs.filter { it.habitId == habit.id }
            val isCompletedToday = habitLogs.any { it.date == todayDateString }
            val streak = calculateStreak(habitLogs)
            
            HabitUiModel(
                id = habit.id,
                title = habit.title,
                isCompletedToday = isCompletedToday,
                currentStreak = streak
            )
        }

        // Apply filter
        when (currentFilter) {
            HabitFilter.ALL -> uiModels
            HabitFilter.COMPLETED -> uiModels.filter { it.isCompletedToday }
            HabitFilter.PENDING -> uiModels.filter { !it.isCompletedToday }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun setFilter(newFilter: HabitFilter) {
        _filter.value = newFilter
    }

    fun addHabit(title: String) {
        viewModelScope.launch {
            habitDao.insertHabit(HabitEntity(title = title))
        }
    }

    fun toggleHabitCompletion(habitId: Int, isCompleted: Boolean) {
        viewModelScope.launch {
            if (isCompleted) {
                habitDao.insertLog(HabitLogEntity(habitId = habitId, date = todayDateString))
            } else {
                habitDao.deleteLog(habitId, todayDateString)
            }
        }
    }

    fun deleteHabit(habitId: Int) {
        viewModelScope.launch {
            habitDao.deleteHabit(habitId)
        }
    }

    private fun calculateStreak(logs: List<HabitLogEntity>): Int {
        if (logs.isEmpty()) return 0

        // Sort dates descending
        val sortedDates = logs.map { it.date }.sortedDescending()
        
        var streak = 0
        var currentCheckDate = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        // Check if completed today. If not, streak might still be valid if completed yesterday.
        // For a strict streak: if not completed today and not completed yesterday, streak is 0.
        val todayStr = dateFormat.format(currentCheckDate.time)
        currentCheckDate.add(Calendar.DAY_OF_YEAR, -1)
        val yesterdayStr = dateFormat.format(currentCheckDate.time)

        if (!sortedDates.contains(todayStr) && !sortedDates.contains(yesterdayStr)) {
            return 0
        }

        // Start counting backwards from today
        var dateToTest = Calendar.getInstance()
        var dateToTestStr = dateFormat.format(dateToTest.time)

        // If today is not in logs but yesterday is, we start checking from yesterday
        if (!sortedDates.contains(dateToTestStr)) {
            dateToTest.add(Calendar.DAY_OF_YEAR, -1)
            dateToTestStr = dateFormat.format(dateToTest.time)
        }

        while (sortedDates.contains(dateToTestStr)) {
            streak++
            dateToTest.add(Calendar.DAY_OF_YEAR, -1)
            dateToTestStr = dateFormat.format(dateToTest.time)
        }

        return streak
    }
}
