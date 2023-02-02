package com.example.funnybirds

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import java.util.zip.DeflaterOutputStream
import kotlin.math.abs

class Sprite(var x: Double, var y: Double,
             var velocityX: Double, var velocityY: Double,
             private val initialFrame: Rect, private val bitmap: Bitmap) {

    var mCurrentFrame: Int = 0
    private var mPadding: Int = 20

    var frames = mutableListOf<Rect>(initialFrame)

    var frameWidth: Int = initialFrame.width()
        set(value) {
            field = abs(value)
        }

    var frameHeight: Int = initialFrame.height()
        set(value) {
            field = abs(value)
        }

    var timeForFrame: Double = 0.0
        set(value) {
            field = abs(value)
        }

    var frameTime: Double = 0.1
        set(value) {
            field = abs(value)
        }

    fun draw(canvas: Canvas) {
        val p = Paint()
        val pos = Rect(x.toInt(), y.toInt(), ((x + frameWidth).toInt()),
            ((y + frameHeight).toInt()))
        canvas.drawBitmap(bitmap, frames[mCurrentFrame], pos, p)
    }

    val boundingBox: Rect
        get() {
            return Rect(
                (x + mPadding).toInt(),
                (y + mPadding).toInt(), (x + frameWidth - 2 * mPadding).toInt(),
                (y + frameHeight - 2 * mPadding).toInt()
            )
        }

    fun testIntersect(s: Sprite): Boolean {
        return boundingBox.intersect(s.boundingBox)
    }

    fun onUpdate(ms: Int) {
        timeForFrame += ms;
        if (timeForFrame >= frameTime) {
            mCurrentFrame = (mCurrentFrame + 1) % frames.size
            timeForFrame -= frameTime
        }

        x += velocityX * ms / 1000.0
        y += velocityY * ms / 1000.0
    }
}