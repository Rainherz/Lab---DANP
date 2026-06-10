package com.example.lab2

sealed class ScreenRoute(val route: String) {
    object Login : ScreenRoute("login")
    object Home : ScreenRoute("home")
    object HabitDetail : ScreenRoute("habit_detail/{habitId}") {
        // Función de ayuda para crear la ruta con el parámetro
        fun createRoute(habitId: Int): String {
            return "habit_detail/$habitId"
        }
    }
}
