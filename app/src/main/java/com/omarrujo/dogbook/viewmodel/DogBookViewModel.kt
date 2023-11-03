package com.omarrujo.dogbook.viewmodel

import com.omarrujo.dogbook.model.DogData
import android.content.Context
import android.util.Log
import androidx.databinding.ObservableFloat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.omarrujo.dogbook.R
import com.omarrujo.dogbook.repository.RemoteDatabase
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class DogBookViewModel(applicationContext: Context) : ViewModel() {
    private val remoteDatabase = RemoteDatabase()
    private val context = applicationContext

    val cardViewTranslationX = ObservableFloat(0f)
    val cardViewTranslationY = ObservableFloat(0f)

    private val _dogsData = MutableLiveData<List<DogData>>()
    val dogsData: LiveData<List<DogData>>
        get() = _dogsData

    private val _title = MutableLiveData("")
    val title: LiveData<String> get() = _title

    private val _caption = MutableLiveData<String>()
    val caption: LiveData<String> get() = _caption

    val cblike = MutableLiveData(false)
    val cbnolike = MutableLiveData(false)
    val cbfav = MutableLiveData(false)

    private val _animationFileName = MutableLiveData<String>(null)
    val animationFileName: LiveData<String> get() = _animationFileName

    fun setAnimationFileName(file: String) {
        _animationFileName.value = file
    }
    fun setCardViewTranslationY(newY: Float) {
        cardViewTranslationY.set(newY)
    }
    fun setCardViewTranslationX(newY: Float) {
        cardViewTranslationX.set(newY)
    }

    fun getInformationDog(apiKey: String) {
        try {
            remoteDatabase.service.getDog( apiKey ).enqueue(object : Callback<List<DogData>> {
                override fun onResponse(call: Call<List<DogData>> , response: Response<List<DogData>>) {
                    if (response.isSuccessful) {
                        _dogsData.value = response.body()
                        setCaptionTitle()
                        setCorrectCheck()
                    }
                }

                override fun onFailure(call: Call<List<DogData>> , t: Throwable) {
                    Log.i("Error: ",t.toString())
                }
            })
        } catch (e: Exception){
            Log.i("Error: ",e.toString())
        }
    }

    private fun setCaptionTitle() {
        val noName = context.getString(R.string.no_name)
        val dogData = _dogsData.value?.takeIf { it.isNotEmpty() }?.firstOrNull()?.breeds?.takeIf { it.isNotEmpty() }?.firstOrNull()

        val title = dogData?.name ?: noName
        val bredFor = dogData?.bred_for ?: ""
        val breedGroup = dogData?.breed_group ?: ""
        val lifeSpan = dogData?.life_span ?: ""

        _title.value = title
        _caption.value = if (bredFor.isEmpty() || breedGroup.isEmpty() || lifeSpan.isEmpty()) {
            context.getString(R.string.no_information)
        } else {
            val bred = context.getString(R.string.bred)
            val breed_group = context.getString(R.string.breed_group)
            val life = context.getString(R.string.life)
            "$title $bred $bredFor $breed_group $breedGroup $life $lifeSpan"
        }
    }
    private fun setCorrectCheck(){
        cblike.value = false
        cbnolike.value = false
        cbfav.value = false
    }

}