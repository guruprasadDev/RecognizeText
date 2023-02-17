package com.guru.recognizetext.customviews

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.animation.LinearInterpolator
import androidx.appcompat.widget.AppCompatButton

class ProcessingButton(context: Context, attrs: AttributeSet) : AppCompatButton(context, attrs) {

    var isLoading = false
        set(value) {
            field = value
            invalidate()
        }

    private var progressRectF: RectF = RectF()
    private val progressPaint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.STROKE
        strokeWidth = 8f
        color = Color.WHITE
    }
    private val progressAnimator = ValueAnimator().apply {
        interpolator = LinearInterpolator()
        addUpdateListener {
            val value = it.animatedValue as Float
            progressRectF.set(0f, 0f, width.toFloat(), height * value)
            invalidate()
        }
    }

    override fun onDraw(canvas: Canvas) {
        if (isLoading) {
            // Draw the progress bar on the button
            canvas.drawArc(progressRectF, 0f, 600f, false, progressPaint)
        } else {
            // Draw the button shape and style
            super.onDraw(canvas)
        }
    }

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        if (!enabled) {
            isLoading = false
        }
    }

    override fun setOnClickListener(l: OnClickListener?) {
        super.setOnClickListener {
            if (!isLoading) {
                isLoading = true
                progressAnimator.setFloatValues(0f, 1f)
                progressAnimator.duration = 1000L
                progressAnimator.start()
                l?.onClick(this)
            }
        }
    }

    fun isLoading(loading: Boolean) {
        isLoading = loading
        progressAnimator.cancel()
        progressRectF.setEmpty()
    }
}
