package ru.mertsalovda.tournament.ui.board

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.mertsalovda.tournament.data.model.Rule
import java.util.concurrent.TimeUnit

class BoardViewModel : ViewModel() {

    private val mRule = Rule("Правила", 10, 4, TimeUnit.MINUTES.toMillis(2))

    private val _redPoints = MutableLiveData(0)
    val redPoints: MutableLiveData<Int> = _redPoints
    private val _bluePoints = MutableLiveData(0)
    val bluePoints: MutableLiveData<Int> = _bluePoints
    private val _winner = MutableLiveData("")
    val winner: MutableLiveData<String> = _winner

    fun plusRedPoints(points: Int) {
        val currentPoints = redPoints.value ?: 0
        if (currentPoints + points < 0) {
            redPoints.postValue(0)
        } else {
            redPoints.postValue(currentPoints + points)
        }

        if (currentPoints + points >= mRule.counterWin && currentPoints + points > bluePoints.value!!){
            winner.postValue("Красный победил")
        }
    }

    fun plusBluePoints(points: Int) {
        val currentPoints = bluePoints.value ?: 0
        if (currentPoints + points < 0) {
            bluePoints.postValue(0)
        } else {
            bluePoints.postValue(currentPoints + points)
        }

        if (currentPoints + points >= mRule.counterWin && currentPoints + points > redPoints.value!!){
            winner.postValue("Синий победил")
        }
    }
}