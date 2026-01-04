package com.example.studyapp01.view

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.example.studyapp01.R
import com.example.studyapp01.utils.DimensionExt.dp2px
import com.example.studyapp01.utils.DimensionExt.sp2px

import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

class CusSideBarView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private val TAG = this::class.java.simpleName
    var onIndexSelectedListener: OnIndexSelectedListener? = null

    companion object {
        const val HOT_LETTER = "*"
        private const val DEBUG = false
    }

    interface OnIndexSelectedListener {
        fun onIndexSelected(index: Int, letter: String, isTouchListener: Boolean)
    }

    //设置sidebar的titles,list初始值为A-Z
    var titles = ('A'..'Z').map { it.toString() }.toMutableList()
        set(value) {
            field = value
            invalidate()
        }

    //文字颜色
    var textColor = R.color.teal_200
        set(value) {
            field = value
            invalidate()
        }
    var textColorSelected = R.color.teal_700

    //文字大小
    var textSize = 11.sp2px
        set(value) {
            field = value
            invalidate()
        }

    //默认宽度
    private var viewWidth = 16.dp2px.toFloat()
        set(value) {
            field = value
            invalidate()
        }

    //热门图标
    var hotIcon = R.drawable.icon_sidebar_hot
        set(value) {
            field = value
            hotIconBitmap = value.getDrawable(context)?.toBitmap()
                ?: Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)
            invalidate()
        }
    private lateinit var hotIconBitmap: Bitmap

    //没有被选中的条目,透明度设为 0.3f
    private var unselectAlpha = 0.3f

    //内部状态
    private var itemHeight: Float = 0f
    private var mBarWidth: Float = 0f
    private var mMaxOffset: Float = 70.dp2px.toFloat()
    private var firstItemBaselineY: Float = 0f
    private var currentIndex: Int = -1
    private var selectedIndex: Int = -1
    private var touchY: Float = -1f
    private var isTouching: Boolean = false

    //文字 Paint
    private val paintText = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textAlign = Paint.Align.CENTER
    }

    //图标 Paint
    private val paintIcon = Paint()
    private val paint = Paint()

    //背景 Paint
    private val paintBackground = Paint(Paint.ANTI_ALIAS_FLAG)

    //选中 Paint
    private val paintSelectedBg = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = textColorSelected
    }

    //显示圆环
    private var showCircle = false

    //圆环半径
    private var circleSize = 26f
    private val iconRect by lazy { RectF() }

    init {
        context.obtainStyledAttributes(attrs, R.styleable.CusSideBarView, defStyleAttr, 0)
            .let { ta ->
                try {
                    this.showCircle = ta.getBoolean(R.styleable.CusSideBarView_showCircle, false)
                    this.circleSize =
                        ta.getDimension(R.styleable.CusSideBarView_circleRadius, 26f)
                    val hotIconDrawable = ta.getDrawable(R.styleable.CusSideBarView_hotIcon)
                        ?: hotIcon.getDrawable(context)
                        ?: ContextCompat.getDrawable(context, R.drawable.icon_sidebar_hot) // 再次尝试，或者使用其他默认
                    this.hotIconBitmap = (hotIconDrawable ?: return@let).toBitmap()
                    this.textColor =
                        ta.getColor(R.styleable.CusSideBarView_textColor, textColor.getColor())
                    this.textColorSelected =
                        ta.getColor(R.styleable.CusSideBarView_textColor, textColorSelected)
                } finally {
                    ta.recycle()
                }
            }
    }
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if (titles.isEmpty()) return

        paintText.apply {
            textSize = this@CusSideBarView.textSize
            itemHeight = fontMetrics.bottom - fontMetrics.top
        }
        mBarWidth = titles.maxOf { paintText.measureText(it) }
        viewWidth = itemHeight
        firstItemBaselineY = paddingTop * 1f + viewWidth //防止放大的时候显示不全

        if(DEBUG) {
            currentIndex = 5
            touchY = firstItemBaselineY + currentIndex * itemHeight
            isTouching = true
        }
    }
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (titles.isEmpty()) return

        canvas.drawRect(
            0f,
            0f,
            width.toFloat(),
            height.toFloat(),
            paintBackground.apply { color = Color.TRANSPARENT }
        )
        val maxX = width - viewWidth * 0.5f - paddingEnd + paddingStart
        if(DEBUG) {
            canvas.drawLine(
                maxX,
                0f,
                maxX,
                1f*height,
                paint.apply { color = Color.RED }
            )
            canvas.drawLine(
                0f,
                touchY,
                maxX,
                touchY,
                paint.apply { color = Color.RED }
            )
        }
        // 靜態狀態，沒有觸摸或動畫正在進行
        if (!isTouching) {
            for (i in 0 until titles.size) {
                //每条item的中心位置
                val baseLineY = firstItemBaselineY + i * itemHeight
                val letter = titles[i]
                val isSelected = i == selectedIndex
                val currentTextColor = this@CusSideBarView.textColor

                if (letter == HOT_LETTER) {
                    val drawSize = viewWidth
                    val rect = iconRect.apply {
                        set(
                            maxX - drawSize / 2,
                            baseLineY - itemHeight / 2,
                            maxX + drawSize / 2,
                            baseLineY + drawSize - itemHeight / 2
                        )
                    }
                    if(isSelected && showCircle) {
                        canvas.drawCircle(rect.centerX(),rect.centerY(),circleSize,paintSelectedBg)
                    }
                    if(DEBUG){
                        paint.let {
                            it.style = Paint.Style.STROKE
                            it.color = if(isSelected) Color.RED else Color.rgb(
                                (0..255).random(),
                                (0..255).random(),
                                (0..255).random())
                            it.strokeWidth = 1f
                            canvas.drawRect(rect, it)
                        }
                        //canvas.drawLine(0f,rect.centerY(),width*1f,baseLineY,textPaint)
                    }
                    canvas.drawBitmap(
                        hotIconBitmap,
                        null, rect,
                        paintIcon.apply {
                            //alpha = if (isSelected) 255 else (unselectAlpha * 255).toInt()
                            alpha = 255
                        }
                    )
                } else {
                    paintText.apply {
                        textSize = this@CusSideBarView.textSize
                        color = currentTextColor
                        alpha = if (isSelected) 255 else (unselectAlpha * 255).toInt()
                    }
                    val fm = paintText.fontMetrics
                    if(isSelected && showCircle) {
                        canvas.drawCircle(maxX, baseLineY, circleSize, paintSelectedBg)
                    }
                    val textBaseLine = baseLineY - (fm.ascent + fm.descent) / 2
                    if(DEBUG){
                        paint.let {
                            val textWidth = paintText.measureText(letter)
                            val rectLeft = maxX - textWidth / 2 - 0f
                            val rectRight = maxX + textWidth / 2 + 0f
                            val rectTop = textBaseLine + fm.top - 0f
                            val rectBottom = textBaseLine + fm.bottom + 0f

                            val rect = iconRect.apply { set(rectLeft, rectTop, rectRight, rectBottom) }
                            it.style = Paint.Style.STROKE
                            it.color = if(isSelected) Color.RED else
                                Color.rgb(
                                    (0..255).random(),
                                    (0..255).random(),
                                    (0..255).random())
                            it.strokeWidth = 1f
                            canvas.drawRect(rect, it)
                        }
                        //canvas.drawLine(0f,baseLineY,width*1f,baseLineY,textPaint)
                    }
                    canvas.drawText(
                        letter, maxX,
                        textBaseLine,
                        paintText
                    )
                }
            }
            return
        }

        // 動態狀態，有觸摸在進行
        for (i in 0 until titles.size) {
            //以每个item的中心线开始绘制的
            val baseLineY = firstItemBaselineY + i * itemHeight
            val letter = titles[i]
            val isSelected = i == currentIndex

            val finalScale = getItemScale(i,touchY)
            val centerX = maxX - mMaxOffset * (finalScale - 1)
            val finalAlpha = when{
                i == currentIndex -> 1f
                finalScale == 1f -> unselectAlpha
                else -> 0.1f * (finalScale - 1) + unselectAlpha
            }
            // 繪製內容
            if (letter == HOT_LETTER) {
                val drawSize = viewWidth * finalScale
                val rect = iconRect.apply {
                    set(
                        centerX - drawSize / 2,
                        baseLineY  - drawSize/2,
                        centerX + drawSize / 2,
                        baseLineY + drawSize/2
                    )
                }
                if(isSelected && showCircle) canvas.drawCircle(rect.centerX(),rect.centerY(),circleSize * finalScale,paintSelectedBg)
                canvas.drawBitmap(
                    hotIconBitmap,
                    null, rect,
                    paintIcon.apply {
                        //this.alpha = (finalAlpha * 255).toInt()
                        alpha = 255
                    }
                )
                if(DEBUG){
                    paint.let {
                        it.style = Paint.Style.STROKE
                        it.color = if(isSelected) Color.RED else Color.rgb(
                            (0..255).random(),
                            (0..255).random(),
                            (0..255).random())
                        it.strokeWidth = 1f
                        canvas.drawRect(rect, it)
                    }
                }
            } else {
                paintText.apply {
                    textSize = this@CusSideBarView.textSize * finalScale
                    color = this@CusSideBarView.textColor
                    alpha = (finalAlpha * 255).toInt()
                }
                val fm = paintText.fontMetrics
                val textBaseLine = baseLineY - (fm.ascent + fm.descent) / 2
                if(isSelected && showCircle) canvas.drawCircle(centerX,baseLineY,circleSize * finalScale,paintSelectedBg)
                canvas.drawText(letter, centerX, textBaseLine, paintText)
            }
        }

        //Reset paint
        paintText.alpha = 255
        paintText.textSize = textSize
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val y = event.y
        val letterIndex = letterIndex(y)
        val letter = titles.getOrNull(letterIndex) ?: ""

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                val touchX = event.x
                val touchLeftBoundary = width - viewWidth * 2f

                // 如果一開始就不在區域內，則不處理
                if (touchX < touchLeftBoundary) {
                    return false
                }

                isTouching = true
                currentIndex = letterIndex
                touchY = y
                onIndexSelectedListener?.onIndexSelected(currentIndex, letter, true)
                parent.requestDisallowInterceptTouchEvent(true)
                invalidate()
            }
            MotionEvent.ACTION_MOVE -> {
                if (!isTouching) {
                    return false
                }
                if (currentIndex != letterIndex) {
                    currentIndex = letterIndex
                    onIndexSelectedListener?.onIndexSelected(currentIndex, letter, true)
                }
                touchY = y
                invalidate()
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                if (isTouching) {
                    handleTouchEnd(event)
                }
            }
        }

        return isTouching || super.onTouchEvent(event)
    }


    private fun handleTouchEnd(event: MotionEvent) {
        isTouching = false

        if (currentIndex != -1) {
            selectedIndex = currentIndex
        } else {
            val letterIndex = letterIndex(event.y)
            selectedIndex = letterIndex
        }

        val letter = titles.getOrNull(selectedIndex) ?: ""

        onIndexSelectedListener?.onIndexSelected(selectedIndex, letter, false)
        parent.requestDisallowInterceptTouchEvent(false)
        invalidate()
    }

    // 設置選中索引
    fun setSelectedIndex(index: Int) {
        val index2 = max(min(index,titles.size - 1), 0)
        if(selectedIndex == index2) return //不刷新
        selectedIndex = index2
        invalidate()
    }

    /**
     * 設置選中標題
     * @param title 標題文字
     */
    fun setSelectedTitle(title: String?) {
        val index = titles.indexOf(title)
        //"MoreMatchDialog--setSelectedIndex----index:$index,selectedIndex:$selectedIndex,title:$title".logd(TAG)
        if (index != -1) {
            setSelectedIndex(index)
        }
    }

    /**
     * 获取index对应的缩放，离touchY越近，缩放越小；
     * 取值:[1,2]
     */
    private fun getItemScale(index: Int,touchY:Float): Float {
        if (!isTouching) return 0f
        val distance = abs(touchY - firstItemBaselineY - (itemHeight * index)) / itemHeight
        return 1 + max(0f, 1 - distance * distance / 16)
    }

    /**
     * 触摸Y换算字母Index
     */
    private fun letterIndex(y:Float):Int {
        //因为item的坐标点是基于中心点的，换算index时，要基于顶部的，需要偏移itemHeight/2
        return min(max(0, ((y - firstItemBaselineY + itemHeight/2) / itemHeight).toInt()), titles.size - 1)
    }

    fun @receiver:DrawableRes Int.getDrawable(ctx: Context = context): Drawable? {
        return ContextCompat.getDrawable(ctx, this)
    }

    fun @receiver:ColorRes Int.getColor(ctx: Context = context): Int {
        return ContextCompat.getColor(ctx, this)
    }

}