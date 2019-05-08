package ru.alexbaryzhikov.progressanimation

import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.vectordrawable.graphics.drawable.Animatable2Compat
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        parentView.setOnClickListener {
            val animated = AnimatedVectorDrawableCompat.create(this, R.drawable.avd_pbar)
            animated?.registerAnimationCallback(object : Animatable2Compat.AnimationCallback() {
                override fun onAnimationEnd(drawable: Drawable?) {
                    pbarView.post { animated.start() }
                }
            })
            pbarView.setImageDrawable(animated)
            animated?.start()
        }
    }
}
