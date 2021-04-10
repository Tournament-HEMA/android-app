package ru.mertsalovda.tournament.ui.board

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.mertsalovda.tournament.data.model.Rule
import java.util.concurrent.TimeUnit

class BoardViewModel : ViewModel() {

    private val mRule = Rule("Правила", 10, 4, TimeUnit.MINUTES.toMillis(2))

    private val _redPoints = MutableLiveData(0)
    val redPoints: LiveData<Int> = _redPoints
    private val _bluePoints = MutableLiveData(0)
    val bluePoints: LiveData<Int> = _bluePoints
    private val _winner = MutableLiveData("")
    val winner: LiveData<String> = _winner
    private val _lose = MutableLiveData(false)
    val lose: LiveData<Boolean> = _lose
    private val _mutual = MutableLiveData(0)
    val mutual: LiveData<Int> = _mutual

    fun plusRedPoints(points: Int) {
        val currentPoints = redPoints.value ?: 0
        if (currentPoints + points < 0) {
            _redPoints.postValue(0)
        } else {
            _redPoints.postValue(currentPoints + points)
        }

        if (currentPoints + points >= mRule.counterWin && currentPoints + points > bluePoints.value!!) {
            _winner.postValue("Красный победил")
        }
    }

    fun plusBluePoints(points: Int) {
        val currentPoints = bluePoints.value ?: 0
        if (currentPoints + points < 0) {
            _bluePoints.postValue(0)
        } else {
            _bluePoints.postValue(currentPoints + points)
        }

        if (currentPoints + points >= mRule.counterWin && currentPoints + points > redPoints.value!!) {
            _winner.postValue("Синий победил")
        }
    }

    fun addMutual(delta: Int) {
        val currentMutual = mutual.value ?: 0
        val newMutual = currentMutual + delta
        when {
            newMutual <= 0 -> _mutual.postValue(0)
            newMutual >= mRule.mutualLose -> {
                _mutual.postValue(mRule.mutualLose)
                _lose.postValue(true)
            }
            else -> _mutual.postValue(newMutual)
        }
    }
}