package com.example.click

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ScoreViewModel(private val score:Int) :ViewModel() {
    private val _finalscore=MutableLiveData<Int>()
    val finalscore:LiveData<Int>get()=_finalscore

    private val _hasgambled=MutableLiveData<Boolean>()
    val hasgambled:LiveData<Boolean>get()=_hasgambled

    init{
        _finalscore.value=score
    }
    fun gamble(){
        val swing=(-5..5).random()
        _finalscore.value=_finalscore.value?.plus(swing)
        _hasgambled.value=true
    }

}
