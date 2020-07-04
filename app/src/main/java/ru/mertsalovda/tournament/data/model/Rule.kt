package ru.mertsalovda.tournament.data.model

import java.io.Serializable

data class Rule(
    var name: String = "Правила",
    var counterWin: Int = 10,
    var mutualLose: Int = 4,
    var duration: Long = 120000
) : Serializable