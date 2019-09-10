package com.consolediscounts.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.util.TypedValue.*
import android.view.View
import androidx.core.graphics.drawable.toBitmap
import com.consolediscounts.R
import kotlin.math.min
import kotlin.random.Random

class TagView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private var rect: GradientDrawable
    private var textPaint: Paint
    private var textWidth = 0f
    private var textHeight = 0f
    private val textBounds = Rect()
    private val deleteIconSize: Float
    private val borderSize: Float

    var textColor: Int = -1
    var color: Int = -1
    var text: String
    var deletable: Boolean = false

    init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.TagView,
            0, 0).apply {
                try {
                    deletable = getBoolean(R.styleable.TagView_deletable, false)
                    color = getColor(R.styleable.TagView_color, randomColor())
                    textColor = getColor(R.styleable.TagView_textColor, Color.WHITE)
                    text = getString(R.styleable.TagView_text) ?: ""
                } finally {
                    recycle()
                }
        }

        rect = context.getDrawable(R.drawable.tag) as GradientDrawable

        textPaint = Paint(Paint.ANTI_ALIAS_FLAG.or(Paint.FAKE_BOLD_TEXT_FLAG))
        textPaint.color = textColor
        textPaint.style = Paint.Style.FILL
        textPaint.textSize = applyDimension(COMPLEX_UNIT_SP, 10f, resources.displayMetrics)
        textPaint.textAlign = Paint.Align.LEFT

        deleteIconSize = applyDimension(COMPLEX_UNIT_PX, 10f, resources.displayMetrics)
        borderSize = applyDimension(COMPLEX_UNIT_PX, 10f, resources.displayMetrics)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        var newMeasuredWidth = measuredWidth
        var newMeasuredHeight = measuredHeight

        textPaint.getTextBounds(text, 0, text.length, textBounds)
        textHeight = textBounds.height().toFloat()
        textWidth = textBounds.width().toFloat()
        val desiredWidth = (borderSize + textWidth + borderSize + (if (deletable) deleteIconSize + borderSize else 0f)).toInt()
        val desiredHeight = (2 * borderSize + textHeight).toInt()

        when (MeasureSpec.getMode(widthMeasureSpec)) {
            MeasureSpec.UNSPECIFIED -> {
                newMeasuredWidth = desiredWidth
            }
            MeasureSpec.AT_MOST -> {
                newMeasuredWidth = min(desiredWidth, measuredWidth)
            }
            MeasureSpec.EXACTLY -> {}
        }

        when (MeasureSpec.getMode(heightMeasureSpec)) {
            MeasureSpec.UNSPECIFIED -> {
                newMeasuredHeight = desiredHeight
            }
            MeasureSpec.AT_MOST -> {
                newMeasuredHeight = min(desiredHeight, measuredHeight)
            }
            MeasureSpec.EXACTLY -> {}
        }

        setMeasuredDimension(newMeasuredWidth, newMeasuredHeight)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        rect.setColor(color)
        canvas?.drawBitmap(rect.toBitmap(width, height), 0f, 0f, null)

        canvas?.drawText(text, borderSize, textHeight + borderSize , textPaint)

        if (deletable) {
            val deleteIconX = 2 * borderSize + textWidth
            val deleteIconY = (height - deleteIconSize) / 2
            canvas?.drawLines(
                floatArrayOf(
                    deleteIconX,
                    deleteIconY,
                    deleteIconX + deleteIconSize,
                    deleteIconY + deleteIconSize,
                    deleteIconX,
                    deleteIconY + deleteIconSize,
                    deleteIconX + deleteIconSize,
                    deleteIconY
                ), textPaint
            )
        }
    }

    private fun randomColor(): Int {
        val random = Random(System.currentTimeMillis())
        var letters = "012345".toList()
        var color = "#" + letters[random.nextInt(0, letters.size)]
        letters = "0123456789ABCDEF".toList()
        for (i in 0..4) {
            color += letters[random.nextInt(0, letters.size)]
        }
        return Color.parseColor(color)
    }
}