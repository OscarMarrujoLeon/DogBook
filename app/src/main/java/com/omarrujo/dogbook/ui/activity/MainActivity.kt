package com.omarrujo.dogbook.ui.activity

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.omarrujo.dogbook.R
import com.omarrujo.dogbook.databinding.ActivityMainBinding
import com.omarrujo.dogbook.utils.Constants
import com.omarrujo.dogbook.viewmodel.DogBookViewModel
import com.omarrujo.dogbook.viewmodel.DogBookViewModelFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit  var dogBookViewmodel: DogBookViewModel
    private var dX = 0f
    private var dY = 0f
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate( layoutInflater )
        setContentView(binding.root)

        dogBookViewmodel = ViewModelProvider(this, DogBookViewModelFactory(applicationContext)).get(DogBookViewModel::class.java)
        binding.viewModel = dogBookViewmodel
        binding.lifecycleOwner = this


        val originalX = binding.cardViewMain.x
        val originalY = binding.cardViewMain.y


        val halfWidthThreshold = this.resources.displayMetrics.widthPixels / 2
        val halfHeightThreshold = this.resources.displayMetrics.heightPixels / 2
        var movedToRight = false
        var movedToLeft = false
        var movedUp = false

        binding.cardViewMain.setOnTouchListener { view, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    dX = view.x - event.rawX
                    dY = view.y - event.rawY
                }
                MotionEvent.ACTION_MOVE -> {
                    val newX = event.rawX + dX
                    val newY = event.rawY + dY

                    // Restringe el movimiento de derecha a izquierda
                    if (newX >= 0 && newX <= (this.resources.displayMetrics.widthPixels - binding.cardViewMain.width)) {
                        view.x = newX
                    }

                    // Restringe el movimiento de abajo hacia arriba
                    if (newY >= 0 && newY <= (this.resources.displayMetrics.heightPixels - binding.cardViewMain.height)) {
                        view.y = newY
                    }

                    dogBookViewmodel.setCardViewTranslationX(newX)
                    dogBookViewmodel.setCardViewTranslationY(newY)

                    if (newX > originalX) {
                        movedToRight = true
                        movedToLeft = false
                    } else if (newX < originalX) {
                        movedToLeft = true
                        movedToRight = false
                    } else {
                        movedToRight = false
                        movedToLeft = false
                    }

                    if (newY < originalY) {
                        movedUp = true
                    } else {
                        movedUp = false
                    }
                }
                MotionEvent.ACTION_UP -> {
                    val currentX = view.x
                    val currentY = view.y
                    val distanceDraggedx = Math.abs(currentX - originalX)
                    val distanceDraggedy = Math.abs(currentY - originalY)

                    if (movedToRight && distanceDraggedx > halfWidthThreshold) {
                        binding.lottieAnimationView.visibility = View.VISIBLE
                        binding.lottieAnimationView.playAnimation()

                        // Define la duración en milisegundos (por ejemplo, 5 segundos)
                        val duracionAnimacion = 5000

                        // Programa una tarea para ocultar la animación después de la duración deseada
                        Handler(Looper.getMainLooper()).postDelayed({
                            binding.lottieAnimationView.visibility = View.GONE
                        }, duracionAnimacion.toLong())
                    } else if (movedToLeft && distanceDraggedx > halfWidthThreshold  ) {

                    }
                    else if (movedUp && distanceDraggedy > halfHeightThreshold) {

                    }
                    view.animate().x(originalX)
                    view.animate().y(originalY)
                }
            }
            true
        }



//        dogBookViewmodel.dogsData.observe(this, Observer { dogData ->
//            try {
//                Glide.with(this)
//                    .load(dogData[0].url)
//                    .transition(DrawableTransitionOptions.withCrossFade())
//                    .into(binding.ivDog)
//            }catch  (e: Exception){
//                Log.i("OML GLIDE",e.toString())
//            }
//        })

        dogBookViewmodel.getInformationDog( Constants.API_KEY )


    }
}