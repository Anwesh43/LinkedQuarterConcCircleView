package com.anwesh.uiprojects.linkedquarterconccircleview

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.anwesh.uiprojects.quarterconccircleview.QuarterConcCircleView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        QuarterConcCircleView.create(this)
    }
}
