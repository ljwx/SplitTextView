package com.ljwx.view

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.text.*
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import kotlin.math.abs
import kotlin.math.max

/**
 * @author ljwx
 * @since 2022/11/16
 *
 * 对内容中的特殊字段修改字体样式
 */
class SplitTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : AppCompatTextView(context, attrs, defStyleAttr) {

    /**
     * text属性对象
     */
    private var mLeft: TextAttributes? = null
    private var mCenter: TextAttributes? = null
    private var mRight: TextAttributes? = null

    /**
     * drawable相关属性
     */
    private var mDrawableLeft: Drawable? = null
    private var mDrawableLeftSize = 0f
    private var mDrawableLeftPadding = 0f
    private var mDrawableRight: Drawable? = null
    private var mDrawableRightSize = 0f
    private var mDrawableRightPadding = 0f

    /**
     * 中间位置左右间距
     */
    private var mCenterMarginLeft = 0f
    private var mCenterMarginRight = 0f

    /**
     * 基线位置
     */
    private var mBaseLine = 0f

    /**
     * 是否自动换行
     */
    private var mAutoWrap = false

    init {
        val attr = context.obtainStyledAttributes(attrs, R.styleable.SplitTextView)
        try {
            mAutoWrap = attr.getBoolean(R.styleable.SplitTextView_stvAutoWrap, false)
            // 中间文本的左右间隔
            mCenterMarginLeft =
                attr.getDimension(R.styleable.SplitTextView_stvCenterMarginLeft, 0f)
            mCenterMarginRight =
                attr.getDimension(R.styleable.SplitTextView_stvCenterMarginRight, 0f)
            getTextDrawable(attr)
            leftAttributes(attr)
            centerAttributes(attr)
            rightAttributes(attr)
        } finally {
            attr.recycle()
        }
    }

    /**
     * 获取Drawable相关属性
     *
     * @param attr TypedArray
     */
    private fun getTextDrawable(attr: TypedArray) {
        mDrawableLeft = attr.getDrawable(R.styleable.SplitTextView_stvDrawableLeft)
        mDrawableLeftSize = if (mDrawableLeft == null) 0f else
            attr.getDimension(R.styleable.SplitTextView_stvDrawableLeftSize, 42f)
        mDrawableLeftPadding = if (mDrawableLeft == null) 0f else
            attr.getDimension(R.styleable.SplitTextView_stvDrawableLeftTextPadding, 0f)
        mDrawableLeft?.setBounds(0, 0, mDrawableLeftSize.toInt(), mDrawableLeftSize.toInt())

        mDrawableRight = attr.getDrawable(R.styleable.SplitTextView_stvDrawableRight)
        mDrawableRightSize = if (mDrawableRight == null) 0f else
            attr.getDimension(R.styleable.SplitTextView_stvDrawableRightSize, 42f)
        mDrawableRightPadding = if (mDrawableRight == null) 0f else
            attr.getDimension(R.styleable.SplitTextView_stvDrawableRightTextPadding, 0f)
        mDrawableRight?.setBounds(0, 0, mDrawableRightSize.toInt(), mDrawableRightSize.toInt())
    }

    /**
     * 左边文字属性
     *
     * @param attr TypedArray
     */
    private fun leftAttributes(attr: TypedArray) {
        val text = attr.getString(R.styleable.SplitTextView_stvLeftText)
        val color = attr.getColor(R.styleable.SplitTextView_stvLeftColor, defaultColor())
        val size = attr.getDimension(R.styleable.SplitTextView_stvLeftSize, defaultSize())
        val bold = attr.getBoolean(R.styleable.SplitTextView_stvLeftBold, defaultBold())
        mLeft = createTextAttribute(mLeft, text, color, size, bold)
    }

    /**
     * 中间文字属性
     *
     * @param attr TypedArray
     */
    private fun centerAttributes(attr: TypedArray) {
        val text = attr.getString(R.styleable.SplitTextView_stvCenterText)
        val color = attr.getColor(R.styleable.SplitTextView_stvCenterColor, defaultColor())
        val size = attr.getDimension(R.styleable.SplitTextView_stvCenterSize, defaultSize())
        val bold = attr.getBoolean(R.styleable.SplitTextView_stvCenterBold, defaultBold())
        mCenter = createTextAttribute(mCenter, text, color, size, bold)
    }

    /**
     * 右边文字属性
     *
     * @param attr TypedArray
     */
    private fun rightAttributes(attr: TypedArray) {
        val text = attr.getString(R.styleable.SplitTextView_stvRightText)
        val color = attr.getColor(R.styleable.SplitTextView_stvRightColor, defaultColor())
        val size = attr.getDimension(R.styleable.SplitTextView_stvRightSize, defaultSize())
        val bold = attr.getBoolean(R.styleable.SplitTextView_stvRightBold, defaultBold())
        mRight = createTextAttribute(mRight, text, color, size, bold)
    }

    /**
     * 创建text属性对象
     *
     * @param attribute 当前属性对象
     * @param text 文字内容
     * @param color 文字颜色
     * @param size 文字大小
     * @param bold 文字加粗
     * @return 文字属性对象
     */
    private fun createTextAttribute(
        attribute: TextAttributes?,
        text: String?,
        color: Int,
        size: Float,
        bold: Boolean,
    ): TextAttributes? {
        // 是否满足创建对象条件
        if (!text.isNullOrEmpty() || color != defaultColor() || size != defaultSize() || bold != defaultBold()) {
            val attr = attribute ?: TextAttributes()
            attr.color = color
            attr.size = size
            attr.bold = bold
            attr.initPaint()
            attr.setText(text)
            return attr
        }
        return null
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        // 计算宽高
        setMeasuredDimension(computeWidth(widthMeasureSpec), computeHeight(heightMeasureSpec))
        // 计算基线
        computeBaseLine()
    }

    /**
     * 计算控件宽度
     *
     * @param widthMeasureSpec 宽度规格
     * @return 控件宽度
     */
    private fun computeWidth(widthMeasureSpec: Int): Int {
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        var width = MeasureSpec.getSize(widthMeasureSpec)
        // wrap_content/scrollview
        if (MeasureSpec.AT_MOST == widthMode || MeasureSpec.UNSPECIFIED == widthMode) {
            val centerMarginWidth = mCenterMarginLeft + mCenterMarginRight
            val drawableWidth = mDrawableLeftSize + mDrawableRightSize
            val drawablePadding = mDrawableLeftPadding + mDrawableRightPadding
            val textWidth = paint.measureText(text as String) + (mLeft?.textWidth ?: 0f) +
                    (mCenter?.textWidth ?: 0f) + (mRight?.textWidth ?: 0f)
            val totalWidth =
                textWidth + paddingLeft + paddingRight + drawableWidth + drawablePadding + centerMarginWidth
            if (totalWidth < width) {
                width = totalWidth.toInt()
            }
        }
        // 精确 match_parent/100dp
        if (MeasureSpec.EXACTLY == widthMode) {
        }
        return width
    }

    /**
     * 获取最大高度
     *
     * @return 文字最大高度
     */
    private fun getTextMaxHeight(): Float {
        var max = max((mLeft?.lineHeight ?: 0f), (mCenter?.lineHeight ?: 0f))
        max = max(max, (mRight?.lineHeight ?: 0f))
        max = max(lineHeight.toFloat(), max)
        return max(max, max(mDrawableLeftSize, mDrawableRightSize))
    }

    /**
     * 计算view高度
     *
     * @param heightMeasureSpec 高度规格
     * @return 控件高度
     */
    private fun computeHeight(heightMeasureSpec: Int): Int {
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        var height = MeasureSpec.getSize(heightMeasureSpec)
        // wrap_content/scrollview
        if (MeasureSpec.AT_MOST == heightMode || MeasureSpec.UNSPECIFIED == heightMode) {
            val totalHeight = getTextMaxHeight() + getPaddingHeight()
            if (totalHeight < height) {
                height = totalHeight.toInt()
            }
        }
        // 精确 match_parent/100dp
        if (MeasureSpec.EXACTLY == heightMode) {
        }
        return height
    }

    /**
     * 计算当前控件的baseline
     */
    private fun computeBaseLine() {
        var max = max((mLeft?.baselineDiff ?: 0f), (mCenter?.baselineDiff ?: 0f))
        max = max(max, (mRight?.baselineDiff ?: 0f))
        max += ((measuredHeight - getPaddingHeight()) / 2) + paddingTop
        mBaseLine = if (baseline > max) baseline.toFloat() else max
    }

    override fun onDraw(canvas: Canvas) {

        canvas.save()
        // 原始text内容位置调整
        canvas.translate(mDrawableLeftSize + mDrawableLeftPadding, abs(mBaseLine - baseline))
        super.onDraw(canvas)
        canvas.restore()

//        if (mAutoWrap) {
//        } else {
//        }
        val leftPadding = paddingLeft + mDrawableLeftSize + mDrawableLeftPadding
        // 绘制文字
        canvas.save()
        canvas.translate(leftPadding + paint.measureText(text as String), 0f)
        drawText(canvas, mLeft, 0f, mBaseLine)
        drawText(canvas, mCenter, mCenterMarginLeft, mBaseLine)
        drawText(canvas, mRight, mCenterMarginRight, mBaseLine)
        canvas.restore()
        // 绘制图片
        drawIcon(canvas)
    }

    /**
     * 绘制文字
     *
     * @param canvas 画布
     * @param text 内容属性对象
     * @param x x轴起点位置
     * @param y baseline位置
     */
    private fun drawText(canvas: Canvas, text: TextAttributes?, x: Float, y: Float) {
        text?.let {
            canvas.drawText(it.getText(), x, y, it.paint)
            canvas.translate(x + text.textWidth, 0f)
        }
    }

    /**
     * 绘制左右Drawable
     *
     * @param canvas 画布
     */
    private fun drawIcon(canvas: Canvas) {
        mDrawableLeft?.let {
            canvas.save()
            canvas.translate(paddingLeft.toFloat(),
                ((measuredHeight - getPaddingHeight()) - mDrawableLeftSize) / 2f + compoundPaddingTop) // 去掉高度padding,再加上paddingTop
            it.draw(canvas)
            canvas.restore()
        }
        mDrawableRight?.let {
            canvas.save()
            canvas.translate(
                measuredWidth.toFloat() - paddingRight - mDrawableRightSize,
                ((measuredHeight - getPaddingHeight()) - mDrawableRightSize) / 2f + compoundPaddingTop) // 去掉高度padding,再加上paddingTop
            it.draw(canvas)
            canvas.restore()
        }
    }


    /**
     * 高度padding值
     */
    private fun getPaddingHeight(): Int {
        return compoundPaddingTop + compoundPaddingBottom
    }

    /**
     * textview默认颜色
     */
    private fun defaultColor(): Int {
        return currentTextColor
    }

    /**
     * textview默认大小
     */
    private fun defaultSize(): Float {
        return textSize
    }

    /**
     * textview是否默认加粗
     */
    private fun defaultBold(): Boolean {
        return paint.isFakeBoldText
    }

    private inner class TextAttributes {

        /**
         * 文字内容
         */
        private var text: String? = null

        /**
         * 文字颜色
         */
        var color = defaultColor()

        /**
         * 文字大小
         */
        var size = defaultSize()

        /**
         * 文字加粗
         */
        var bold = defaultBold()

        /**
         * 文字所占宽度
         */
        var textWidth = 0f

        /**
         * 文字所占高度
         */
        var lineHeight = 0f

        /**
         * 基线差值
         */
        var baselineDiff = 0f

        /**
         * 文字画笔
         */
        var paint = TextPaint(Paint.ANTI_ALIAS_FLAG)

        init {
            initPaint()
        }

        /**
         * 初始化画笔
         */
        fun initPaint() {
            paint.color = color
            paint.textSize = size
            paint.isFakeBoldText = bold
            baselineDiff = (abs(paint.ascent() + paint.descent()) / 2)
        }

        /**
         * 设置文字内容
         *
         * @param text 内容
         */
        fun setText(text: String?) {
            this.text = text
            if (text.isNullOrEmpty()) {
                textWidth = 0f
                lineHeight = 0f
            } else {
                textWidth = paint.measureText(text)
                val fm = paint.fontMetrics
                lineHeight = fm.bottom - fm.top + fm.leading

            }
        }

        fun getText(): String {
            return text ?: ""
        }

    }

    /**
     * 设置文本内容
     */
    fun setTextLeft(text: String?) {
        mLeft = mLeft ?: TextAttributes()
        mLeft?.setText(text)
        requestLayout()
        invalidate()
    }

    fun getTextLeft(): String? {
        return mLeft?.getText()
    }

    fun setTextCenter(text: String?) {
        mCenter = mCenter ?: TextAttributes()
        mCenter?.setText(text)
        requestLayout()
        invalidate()
    }

    fun getTextCenter(): String? {
        return mCenter?.getText()
    }

    fun setTextRight(text: String?) {
        mRight = mRight ?: TextAttributes()
        mRight?.setText(text)
        requestLayout()
        invalidate()
    }

    fun getTextRight(): String? {
        return mRight?.getText()
    }

    /**
     * 设置文本颜色
     */
    fun setColorLeft(color: Int) {
        mLeft = mLeft ?: TextAttributes()
        mLeft?.paint?.color = color
        invalidate()
    }

    fun setColorCenter(color: Int) {
        mCenter = mCenter ?: TextAttributes()
        mCenter?.paint?.color = color
        invalidate()
    }

    fun setColorRight(color: Int) {
        mRight = mRight ?: TextAttributes()
        mRight?.paint?.color = color
        invalidate()
    }

}
