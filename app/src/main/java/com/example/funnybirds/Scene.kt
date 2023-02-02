package com.example.funnybirds

import android.content.Context
import android.content.res.Resources
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.os.CountDownTimer
import android.view.MotionEvent
import android.view.View

class Scene(context: Context) : View(context) {

    private var mWidth  : Int = 0
    private var mHeight : Int = 0
    private var mPoints : Int = 0

    private var mPlayer: Sprite
    private var mEnemy: Sprite

    private var mTimer: CountDownTimer? = null
    private final val mTimerInterval: Int = 30

    init {
        var bitmap = BitmapFactory.decodeResource(resources, R.drawable.player)
        var w = bitmap.width / 5
        var h = bitmap.height / 3
        var firstFrame = Rect(0, 0, w, h)
        mPlayer = Sprite(10.0, 0.0, 0.0, 100.0, firstFrame, bitmap)
        for (i in 0..3) {
            for (j in 0..4) {
                if (i == 0 && j == 0) {
                    continue
                }
                if (i == 2 && j == 3) {
                    continue
                }
                mPlayer.frames.add(Rect(j * w, i * h, j * w + w,i * w + w))
            }
        }

        bitmap = BitmapFactory.decodeResource(resources, R.drawable.enemy)
        w = bitmap.width / 5
        h = bitmap.height / 3
        firstFrame = Rect(4 * w, 0, 5 * w, h)
        mEnemy = Sprite(2000.0, 250.0, -300.0, 0.0, firstFrame, bitmap)
        for (i in 0..3) {
            for (j in 4 downTo 0) {
                if (i == 0 && j == 4) {
                    continue
                }
                if (i == 2 && j == 0) {
                    continue
                }
                mEnemy.frames.add(Rect(j * w, i * h, j * w + w,i * w + w))
            }
        }

        startCountDownTimer()
    }

    private fun startCountDownTimer() {
        mTimer?.cancel()
        mTimer = object : CountDownTimer(Int.MAX_VALUE.toLong(), mTimerInterval.toLong()) {
            override fun onTick(p0: Long) {
                onUpdate()
            }

            override fun onFinish() {
                TODO("Not yet implemented")
            }
        }.start()
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val eventAction: Int? = event?.action
        if (eventAction == MotionEvent.ACTION_DOWN) {
            if (event.y < mPlayer.boundingBox.top) {
                mPlayer.velocityY = -100.0;
                mPoints--
            }
            else if (event.y > mPlayer.boundingBox.bottom) {
                mPlayer.velocityY = 100.0
                mPoints--
            }
        }
        return true
    }

    fun onUpdate() {
        mPlayer.onUpdate(mTimerInterval)
        mEnemy.onUpdate(mTimerInterval)
        invalidate()

        if (mPlayer.y + mPlayer.frameHeight > mHeight) {
            mPlayer.y = (mHeight - mPlayer.frameHeight).toDouble()
            mPlayer.velocityY = -mPlayer.velocityY
            mPoints--
        }
        else if (mPlayer.y < 0) {
            mPlayer.y = 0.0
            mPlayer.velocityY = -mPlayer.velocityY
            mPoints--
        }

        if (mEnemy.x < - mEnemy.frameWidth) {
            teleportEnemy()
            mPoints += 10
        }

        if (mEnemy.testIntersect(mPlayer)) {
            teleportEnemy()
            mPoints -= 40
        }
    }

    fun teleportEnemy() {
        mEnemy.x = mWidth + Math.random() * 500
        mEnemy.y = Math.random() * (mHeight - mEnemy.frameHeight)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mWidth  = w;
        mHeight = h;
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        /* Draw background */
        canvas?.drawARGB(250, 127, 199, 255)

        /* Draw points */
        val p = Paint()
        p.isAntiAlias = true
        p.textSize = 55.0f
        p.color = Color.WHITE
        canvas?.drawText("$mPoints", (mWidth - 100).toFloat(), 70F, p)

        /* Draw sprites */
        if (canvas != null) {
            mPlayer.draw(canvas)
            mEnemy.draw(canvas)
        }
    }
}