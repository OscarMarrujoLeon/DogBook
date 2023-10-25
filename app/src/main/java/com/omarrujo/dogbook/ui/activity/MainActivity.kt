package com.omarrujo.dogbook.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import androidx.lifecycle.ViewModelProvider
import com.omarrujo.dogbook.databinding.ActivityMainBinding
import com.omarrujo.dogbook.viewmodel.DogBookViewModel
import com.omarrujo.dogbook.viewmodel.DogBookViewModelFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit  var dogBookViewmodel: DogBookViewModel
    private var dX = 0f
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate( layoutInflater )
        setContentView(binding.root)

        dogBookViewmodel = ViewModelProvider(this, DogBookViewModelFactory(applicationContext)).get(DogBookViewModel::class.java)
        binding.viewModel = dogBookViewmodel
        binding.lifecycleOwner = this

        binding.cardView.setOnTouchListener { view, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    dX = view.x - event.rawX
                }
                MotionEvent.ACTION_MOVE -> {
                    val newX = event.rawX + dX
                    dogBookViewmodel.setCardViewTranslationX(newX)
                }
            }
            true
        }
    }
}