package com.example.click

import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

private val PANIC_BUZZ_PATTERN = longArrayOf(0, 200)
private val GAME_OVER_BUZZ_PATTERN = longArrayOf(0, 1000)
private val NO_BUZZ_PATTERN = longArrayOf(0)
//private const val COUNTDOWN_PANIC_SECONDS = 3L
class GameViewModel: ViewModel() {

    enum class BuzzType(val pattern: LongArray) {
        GAME_OVER(GAME_OVER_BUZZ_PATTERN),
        COUNTDOWN_PANIC(PANIC_BUZZ_PATTERN),
        NO_BUZZ(NO_BUZZ_PATTERN)
    }

    private val timer : CountDownTimer
    private val _currenttime=MutableLiveData<Long>()
    val currenttime:LiveData<Long> get()=_currenttime

    private val _score=MutableLiveData<Int>()
    val score:LiveData<Int> get()=_score

    private val _currentbutton= MutableLiveData<Int>()
    val currentbutton:LiveData<Int> get()=_currentbutton

    private val _scorecolor=MutableLiveData<String>()
    val scorecolor:LiveData<String> get()=_scorecolor

    private val _gamefinished=MutableLiveData<Boolean>()
    val gamefinished:LiveData<Boolean> get()=_gamefinished

    private val _eventBuzz = MutableLiveData<BuzzType>()
    val eventBuzz: LiveData<BuzzType> get() = _eventBuzz

    init{
        _currentbutton.value=(1..4).random()
        _gamefinished.value=false
        _score.value=0
        _scorecolor.value="coral"

        timer = object : CountDownTimer(16000,1000){
            override fun onTick(millisUntilFinished: Long) {
                _currenttime.value=millisUntilFinished/1000
                _scorecolor.value="coral"
            }

            override fun onFinish() {
                _gamefinished.value=true
                _eventBuzz.value=BuzzType.GAME_OVER
            }

        }
        timer.start()
    }

    fun gainPoint(){
        _score.value=_score.value?.plus(1)
        _currentbutton.value=(1..4).random()
        _scorecolor.value="coral"

    }
    fun losePoint(){
        _score.value=_score.value?.minus(1)
        _currentbutton.value=(1..4).random()
        _scorecolor.value="red"
        _eventBuzz.value=BuzzType.COUNTDOWN_PANIC
    }
    fun onBuzzComplete() {
        _eventBuzz.value = BuzzType.NO_BUZZ
    }


}