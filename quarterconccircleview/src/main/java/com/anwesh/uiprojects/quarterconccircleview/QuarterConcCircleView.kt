package com.anwesh.uiprojects.quarterconccircleview

/**
 * Created by anweshmishra on 04/11/18.
 */

import android.view.View
import android.view.MotionEvent
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Color
import android.graphics.RectF
import android.app.Activity
import android.util.Log

val nodes : Int = 5

val lines : Int = 4

val SCALE_GAP : Float = 0.05f
fun Float.scaleFactor() : Float = Math.floor(this/0.5).toFloat()

fun Float.scaleMultiplier(dir : Float) : Float = (scaleFactor()+ (1 - scaleFactor()) / lines) * dir * SCALE_GAP

class QuarterConcCircleView(ctx : Context) : View(ctx) {

    private val paint : Paint = Paint(Paint.ANTI_ALIAS_FLAG)

    override fun onDraw(canvas : Canvas) {

    }

    override fun onTouchEvent(event : MotionEvent) : Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {

            }
        }
        return true
    }

    data class State(var scale : Float = 0f, var dir : Float = 0f, var prevScale : Float = 0f) {

        fun update(cb : (Float) -> Unit) {
            val k : Float = scale.scaleMultiplier(dir)
            scale += k
            Log.d("scale multiplier", "${k}")
            if (Math.abs(scale - prevScale) > 1) {
                scale = prevScale + dir
                dir = 0f
                prevScale = scale
                cb(prevScale)
            }
        }

        fun startUpdating(cb : () -> Unit) {
            if (dir == 0f) {
                dir = 1f - 2 * prevScale
                cb()
            }
        }
    }

    data class Animator(var animated : Boolean = false) {

        fun animate(cb : () -> Unit, viewcb : () -> Unit) {
            if (animated) {
                cb()
                try {
                    Thread.sleep(50)
                    viewcb()
                } catch(ex : Exception) {

                }
            }
        }

        fun start(cb : () -> Unit) {
            if (!animated) {
                animated = true
                cb()
            }
        }

        fun stop() {
            if (animated) {
                animated = false
            }
        }
    }
}