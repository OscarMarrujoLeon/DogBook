package com.omarrujo.dogbook.viewmodel

import DogData
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

    private val _title = MutableLiveData<String>("")
    val title: LiveData<String> get() = _title

    private val _caption = MutableLiveData<String>()
    val caption: LiveData<String> get() = _caption

    fun setCardViewTranslationX(translationX: Float) {
        cardViewTranslationX.set(translationX)
    }

    fun setCardViewTranslationY(newY: Float) {
        cardViewTranslationY.set(newY)
    }

    fun getInformationDog(apiKey: String) {
        try {
            remoteDatabase.service.getDog( apiKey ).enqueue(object : Callback<List<DogData>> {
                override fun onResponse(call: Call<List<DogData>> , response: Response<List<DogData>>) {
                    if (response.isSuccessful) {
                        _dogsData.value = response.body()
                        setCaptionTitle()
                    }
                }

                override fun onFailure(call: Call<List<DogData>> , t: Throwable) {
                    //_title.value = "INTENTALO DE NUEVO"
                    Log.i("OML",t.toString())
                }
            })
        } catch (e: Exception){
            Log.i("OML",e.toString())
        }
    }

    private fun setCaptionTitle(  ){
        val bred = context.getString(R.string.bred)
        val breed_group = context.getString(R.string.breed_group)
        val life = context.getString(R.string.life)
        val noName = context.getString(R.string.no_name)

        val bredFor = _dogsData.value?.takeIf { it.isNotEmpty() }?.firstOrNull()?.breeds?.takeIf { it.isNotEmpty() }?.firstOrNull()?.bred_for ?: noName
        val breedGroup = _dogsData.value?.takeIf { it.isNotEmpty() }?.firstOrNull()?.breeds?.takeIf { it.isNotEmpty() }?.firstOrNull()?.breed_group
        val lifeSpan = _dogsData.value?.takeIf { it.isNotEmpty() }?.firstOrNull()?.breeds?.takeIf { it.isNotEmpty() }?.firstOrNull()?.life_span

        _title.value = _dogsData.value?.takeIf { it.isNotEmpty() }?.firstOrNull()?.breeds?.takeIf { it.isNotEmpty() }?.firstOrNull()?.name ?: noName
        if ( bredFor.isNullOrEmpty() || breedGroup.isNullOrEmpty() || lifeSpan.isNullOrEmpty() ){
            _caption.value = "No information"
        }else{
            _caption.value = "${_title.value} $bred $bredFor $breed_group $breedGroup $life $lifeSpan"
        }
    }

}