package com.irfan.storyapp.ui.add.newpost

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.irfan.storyapp.databinding.ActivityNewPostBinding

class NewPostActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNewPostBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        ...
        playAnimation()
    }

    private fun playAnimation() {

        val imageIv = ObjectAnimator.ofFloat(binding.imageAddIv, View.ALPHA, 1f).setDuration(500)
        val cameraBtn =
            ObjectAnimator.ofFloat(binding.cameraAddBtn, View.ALPHA, 1f).setDuration(200)
        val galleryBtn =
            ObjectAnimator.ofFloat(binding.galleryAddBtn, View.ALPHA, 1f).setDuration(500)
        val descTil = ObjectAnimator.ofFloat(binding.descAddTil, View.ALPHA, 1f).setDuration(500)
        val postBtn = ObjectAnimator.ofFloat(binding.postAddBtn, View.ALPHA, 1f).setDuration(500)

        AnimatorSet().apply {
            playSequentially(
                imageIv,
                cameraBtn,
                galleryBtn,
                descTil,
                postBtn
            )
            startDelay = 500
        }.start()
    }
}