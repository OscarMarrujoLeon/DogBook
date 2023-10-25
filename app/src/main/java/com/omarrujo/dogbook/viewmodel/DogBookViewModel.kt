package com.omarrujo.dogbook.viewmodel

import android.content.Context
import androidx.databinding.ObservableFloat
import androidx.lifecycle.ViewModel

class DogBookViewModel(applicationContext: Context) : ViewModel() {
    val cardViewTranslationX = ObservableFloat(0f)

    fun setCardViewTranslationX(translationX: Float) {
        cardViewTranslationX.set(translationX)
    }

}