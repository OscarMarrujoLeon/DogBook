package com.omarrujo.dogbook.ui.adapters

import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import com.airbnb.lottie.LottieAnimationView
import com.omarrujo.dogbook.utils.Constants
import com.omarrujo.dogbook.viewmodel.DogBookViewModel

object LottieAnimationViewBindingAdapter {
    @JvmStatic
    @BindingAdapter("app:playAnimationWithDelay", "app:viewModel")
    fun playAnimationWithDelay(view: LottieAnimationView, animationLiveData: LiveData<String>?, viewModel: DogBookViewModel?) {
        animationLiveData?.observe(view.context as LifecycleOwner) { animationFileName ->

            if (!animationFileName.isNullOrEmpty()) {
                val handler = Handler(Looper.myLooper() ?: Looper.getMainLooper())
                view.setAnimation(animationFileName)
                view.visibility = View.VISIBLE
                view.playAnimation()


                viewModel?.getInformationDog( Constants.API_KEY )
                viewModel?.setAnimationFileName( "" )
                handler.postDelayed({
                    view.visibility = View.GONE
                }, 2000)
            }
        }
    }
}
