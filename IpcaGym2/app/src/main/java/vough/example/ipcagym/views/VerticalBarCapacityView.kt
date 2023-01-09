package vough.example.ipcagym.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View

class VerticalBarCapacityView : View {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    var _percent = 0F

    fun setPercentagem(_percent : Float) {
        this._percent = _percent
        invalidate()
    }

    override fun draw(canvas: Canvas?) {
        super.draw(canvas)

        val paint = Paint()

        paint.color = Color.WHITE
        paint.style = Paint.Style.FILL

        canvas?.drawRect(Rect(5,  (height * _percent).toInt()-5, width-5, height-5),paint)

        paint.color = Color.GRAY
        paint.strokeWidth = 10F
        paint.style = Paint.Style.STROKE

        canvas?.drawRoundRect(0F, 0F, width.toFloat(), height.toFloat(),45F, 45F, paint)
    }
}