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

val color : Int = Color.parseColor("#43A047")

val SCALE_GAP : Float = 0.05f

fun Int.getInverse() : Float = 1f/this

fun Float.divideScale(i : Int, n : Int) : Float = Math.min(n.getInverse(), Math.max(0f, this - n.getInverse() * i)) * n

fun Canvas.drawQCCNode(i : Int, scale : Float, paint : Paint) {
    val w : Float = width.toFloat()
    val h : Float = height.toFloat()
    val gap : Float = w / (nodes + 1)
    paint.strokeWidth = Math.min(w, h) / 60
    paint.strokeCap = Paint.Cap.ROUND
    paint.color = color
    paint.style = Paint.Style.STROKE
    val sc1 : Float = scale.divideScale(0, 2)
    val sc2 : Float = scale.divideScale(1, 2)
    val size : Float = gap / 3
    val lGap : Float = size / lines
    val dr : Float = size / 2
    val deg : Float = 360f / lines
    save()
    translate(gap + gap * i, h/2)
    for (j in 0..(lines - 1)) {
        val sr : Float = (j + 1) * lGap
        val sc : Float = sc1.divideScale(j, lines)
        val r : Float = sr + (dr - sr) * sc2
        save()
        rotate(deg * j)
        drawArc(RectF(-r, -r, r, r), 0f, 360f * sc, false, paint)
        restore()
    }
    restore()
}

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

    data class QCCNode(var i : Int, val state : State = State()) {

        private var next : QCCNode? = null

        private var prev : QCCNode? = null

        init {
            addNeighbor()
        }

        fun addNeighbor() {
            if (i < nodes - 1) {
                next = QCCNode(i + 1)
                next?.prev = this
            }
        }

        fun draw(canvas : Canvas, paint : Paint) {
            canvas.drawQCCNode(i, state.scale, paint)
            prev?.draw(canvas, paint)
        }

        fun update(cb : (Int, Float) -> Unit) {
            state.update {
                cb(i, it)
            }
        }

        fun startUpdating(cb : () -> Unit) {
            state.startUpdating(cb)
        }

        fun getNext(dir : Int, cb : () -> Unit) : QCCNode {
            var curr : QCCNode? = prev
            if (dir == 1) {
                curr = next
            }
            if (curr != null) {
                return curr
            }
            cb()
            return this
        }
    }

    data class QuarterConcCircle(var i : Int) {

        private var curr : QCCNode = QCCNode(0)

        private var dir : Int = 1

        fun draw(canvas : Canvas, paint : Paint) {
            curr.draw(canvas, paint)
        }

        fun update(cb : (Int, Float) -> Unit) {
            curr.update {i, scl ->
                curr = curr.getNext(dir) {
                    dir *= -1
                }
            }
        }

        fun startUpdating(cb : () -> Unit) {
            curr.startUpdating(cb)
        }
    }
}