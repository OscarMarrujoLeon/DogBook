package com.omarrujo.dogbook.viewmodel

import DogData
import android.content.Context
import android.util.Log
import androidx.databinding.ObservableFloat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.omarrujo.dogbook.repository.RemoteDatabase
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DogBookViewModel(applicationContext: Context) : ViewModel() {
    private val remoteDatabase = RemoteDatabase()

    val cardViewTranslationX = ObservableFloat(0f)
    val cardViewTranslationY = ObservableFloat(0f)
    private val _dogData = MutableLiveData<List<DogData>>()
    val dogData: MutableLiveData<List<DogData>>
        get() = _dogData

    fun setCardViewTranslationX(translationX: Float) {
        cardViewTranslationX.set(translationX)
    }

    fun setCardViewTranslationY(newY: Float) {
        cardViewTranslationY.set(newY)
    }

    fun getInformationDog(apiKey: String) {

        remoteDatabase.service.getDog( apiKey ).enqueue(object : Callback<List<DogData>> {
            override fun onResponse(call: Call<List<DogData>> , response: Response<List<DogData>>) {
                if (response.isSuccessful) {
                    _dogData.value = response.body()
                }
            }

            override fun onFailure(call: Call<List<DogData>> , t: Throwable) {
                //_title.value = "INTENTALO DE NUEVO"
                Log.i("OML",t.toString())
            }
        })
    }

}