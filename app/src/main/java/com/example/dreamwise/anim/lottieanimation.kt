package com.example.dreamwise.anim

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.dreamwise.databinding.ActivityAddButtonBinding
import com.airbnb.lottie.LottieDrawable

class LottieAnimationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddButtonBinding
    private var isPlaying = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Inflate the layout using View Binding
        binding = ActivityAddButtonBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState != null) {
            // Restore state if exists
            isPlaying = savedInstanceState.getBoolean("IS_PLAYING", true)
        }

        // Setup the Lottie animation
        setupLottieAnimation()

        // Toggle play/pause on click
        binding.lottieAnimationView.setOnClickListener {
            toggleAnimation()
        }
    }


    private fun setupLottieAnimation() {
        binding.lottieAnimationView.setAnimation("animation.json")
        binding.lottieAnimationView.repeatCount = LottieDrawable.INFINITE

        if (isPlaying) {
            binding.lottieAnimationView.playAnimation()
        } else {
            binding.lottieAnimationView.pauseAnimation()
        }
    }

    private fun toggleAnimation() {
        isPlaying = if (isPlaying) {
            binding.lottieAnimationView.pauseAnimation()
            false
        } else {
            binding.lottieAnimationView.playAnimation()
            true
        }
    }

    override fun onResume() {
        super.onResume()
        // Resume Lottie animation based on state
        if (isPlaying) {
            binding.lottieAnimationView.resumeAnimation()
        }
    }

    override fun onPause() {
        // Pause Lottie animation to avoid consuming resources when the activity is not visible
        binding.lottieAnimationView.pauseAnimation()
        super.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean("IS_PLAYING", isPlaying)
    }
}
