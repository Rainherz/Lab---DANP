package com.tuapp.cineapp.domain.model

enum class Genre(val id: Int, val displayName: String) {
    ALL(0, "Todos"),
    ACTION(28, "Acción"),
    ADVENTURE(12, "Aventura"),
    ANIMATION(16, "Animación"),
    COMEDY(35, "Comedia"),
    DRAMA(18, "Drama"),
    SCI_FI(878, "Ciencia Ficción"),
    THRILLER(53, "Suspenso"),
    HORROR(27, "Terror")
}
