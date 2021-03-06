package com.anwesh.uiprojects.linkedquarterconccircleview

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import com.anwesh.uiprojects.quarterconccircleview.QuarterConcCircleView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val view : QuarterConcCircleView = QuarterConcCircleView.create(this)
        fullScreen()
        view.addOnAnimationCompleteListener({
            createToast("Animation number ${it} completed")
        }) {
            createToast("Animation number $it is reset")
        }

    }

    fun createToast(txt : String) {
        Toast.makeText(this, txt, Toast.LENGTH_SHORT).show()
    }
}

fun MainActivity.fullScreen() {
    supportActionBar?.hide()
    window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
}