package com.ljwx.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import android.graphics.drawable.Drawable
import android.text.TextUtils
import android.view.View.MeasureSpec

import android.view.ViewGroup

class LessenTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : AppCompatTextView(context, attrs, defStyleAttr) {

    //默认颜色,大小
    private val defualtColor = currentTextColor
    private val defualtSize = textSize
    private var mNorColor = 0
    private var mNorSize = 0f

    private var mLeftString = ""
    private var mCenterString: String = ""
    private var mRightString: String = ""

    //文字颜色
    private var mColorLeft = defualtColor  //文字颜色
    private var mColorCenter = defualtColor  //文字颜色
    private var mColorRight = defualtColor

    //文字大小
    private var mSizeLeft = 0f  //文字大小
    private var mSizeCenter = 0f  //文字大小
    private var mSizeRight = 0f

    //调整文字距底部margin
    private var mMarBtmLeft = 0f  //调整文字距底部margin
    private var mMarBtmCenter = 0f  //调整文字距底部margin
    private var mMarBtmRight = 0f

    //字体是否加粗
    private var mBoldLeft = false  //字体是否加粗
    private var mBoldCenter = false  //字体是否加粗
    private var mBoldRight = false

    //中间文本的左右margin
    private var mCenterTvMarginLeft = 0f  //中间文本的左右margin
    private var mCenterTvMarginRight = 0f

    //字体分布模式(从左往右排列模式,整行平分模式)
    private var mGravityMode = 0

    //字体对齐(底部对齐,中间线对齐)
    private var mBaseLineMode = 0

    //绘制文字的画笔
    private val mPaintLeft: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val mPaintCenter: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val mPaintRight: Paint = Paint(Paint.ANTI_ALIAS_FLAG)

    //最大文字的画笔
    private var mMaxPaint: Paint? = null

    //最高文字基线
    private var mMaxBaseLine: Float? = null

    //drawable宽高
    private var mDrawableLeftWidth = 0f  //drawable宽高
    private var mDrawableRightWidth = 0f  //drawable宽高
    private var mDrawableLeftHeight = 0f  //drawable宽高
    private var mDrawableRightHeight = 0f

    //当有drawable图片时,距离文字的间距
    private var mDrawableLeftPadding = 0f  //当有drawable图片时,距离文字的间距
    private var mDrawableRightPadding = 0f

    //drawable图片数组
    private var drawables: Array<Drawable>? = null

    //文字的宽度
    private var mLeftWidth = 0f  //文字的宽度
    private var mCenterWidth = 0f  //文字的宽度
    private var mRightWidth = 0f

    //父布局宽度,控件宽度
    private var mParentWidth = 0  //父布局宽度,控件宽度
    private var mViewWidth = 0

    //文本所需总宽度
    private var mContentWidth = 0

    //字体大小自适应
    private var mAutoSize = false
    private var mAutoSizeRatio = 0.01f

    init {
        val attr = context.obtainStyledAttributes(attrs, R.styleable.LessenTextView)
        mNorColor = attr.getColor(R.styleable.LessenTextView_htvTvNorColor, defualtColor)
        mNorSize = attr.getDimension(R.styleable.LessenTextView_htvTvNorSize, defualtSize)
        mLeftString = attr.getString(R.styleable.LessenTextView_htvLeftString)?:""
        mCenterString = attr.getString(R.styleable.LessenTextView_htvCenterString)?:""
        mRightString = attr.getString(R.styleable.LessenTextView_htvRightString)?:""
        mColorLeft = attr.getColor(R.styleable.LessenTextView_htvLeftColor, mNorColor)
        mColorCenter = attr.getColor(R.styleable.LessenTextView_htvCenterColor, mNorColor)
        mColorRight = attr.getColor(R.styleable.LessenTextView_htvRightColor, mNorColor)
        mSizeLeft = attr.getDimension(R.styleable.LessenTextView_htvLeftSize, mNorSize)
        mSizeCenter = attr.getDimension(R.styleable.LessenTextView_htvCenterSize, mNorSize)
        mSizeRight = attr.getDimension(R.styleable.LessenTextView_htvRightSize, mNorSize)
        mBoldLeft = attr.getBoolean(R.styleable.LessenTextView_htvLeftBold, false)
        mBoldCenter = attr.getBoolean(R.styleable.LessenTextView_htvCenterBold, false)
        mBoldRight = attr.getBoolean(R.styleable.LessenTextView_htvRightBold, false)

        mMarBtmLeft = attr.getDimension(R.styleable.LessenTextView_htvLeftMarginBottom, 0f)
        mMarBtmCenter = attr.getDimension(R.styleable.LessenTextView_htvCenterMarginBottom, 0f)
        mMarBtmRight = attr.getDimension(R.styleable.LessenTextView_htvRightMarginBottom, 0f)
        mCenterTvMarginLeft = attr.getDimension(R.styleable.LessenTextView_htvCenterMarginLeft, 0f)
        mCenterTvMarginRight = attr.getDimension(R.styleable.LessenTextView_htvCenterMarginRight, 0f)

        mGravityMode = attr.getInt(R.styleable.LessenTextView_htvGravityMode, 0)
        mBaseLineMode = attr.getInt(R.styleable.LessenTextView_htvBaseLineMode, 0)

        mDrawableLeftWidth = attr.getDimension(R.styleable.LessenTextView_htvDrawLeftWidth, 0f)
        mDrawableLeftHeight = attr.getDimension(R.styleable.LessenTextView_htvDrawLeftHeight, 0f)
        mDrawableLeftPadding = attr.getDimension(R.styleable.LessenTextView_htvDrawLeftPadding, 0f)
        mDrawableRightWidth = attr.getDimension(R.styleable.LessenTextView_htvDrawRightWidth, 0f)
        mDrawableRightHeight = attr.getDimension(R.styleable.LessenTextView_htvDrawRightHeight, 0f)
        mDrawableRightPadding = attr.getDimension(R.styleable.LessenTextView_htvDrawRightPadding, 0f)

        mAutoSize = attr.getBoolean(R.styleable.LessenTextView_htvAutoSize, false)
        mAutoSizeRatio = attr.getDimension(R.styleable.LessenTextView_htvAutoSizeRatio, 0.01f)

        attr.recycle()


        mPaintLeft.setFakeBoldText(mBoldLeft);
        mPaintLeft.setTextSize(mSizeLeft);
        mPaintLeft.setColor(mColorLeft);
        mPaintLeft.setTypeface(getTypeface());

        mPaintCenter.setFakeBoldText(mBoldCenter);
        mPaintCenter.setTextSize(mSizeCenter);
        mPaintCenter.setColor(mColorCenter);
        mPaintCenter.setTypeface(getTypeface());

        mPaintRight.setFakeBoldText(mBoldRight);
        mPaintRight.setTextSize(mSizeRight);
        mPaintRight.setColor(mColorRight);
        mPaintRight.setTypeface(getTypeface());

        drawableSize();
    }

    /**
     * 当设置了drawable,没有手动设置宽高时,使用默认宽高
     */
    private fun drawableSize() {
        drawables = compoundDrawables
        setCompoundDrawables(null, drawables?.get(1), null, drawables?.get(3))
        if (drawables?.get(0) != null) {
            mDrawableLeftWidth =
                if (mDrawableLeftWidth > 0) mDrawableLeftWidth else drawables?.get(0)?.intrinsicWidth
                    ?.toFloat()?:0f
            mDrawableLeftHeight =
                if (mDrawableLeftHeight > 0) mDrawableLeftHeight else drawables?.get(0)?.intrinsicHeight
                    ?.toFloat()?:0f
        }
        if (drawables?.get(2) != null) {
            mDrawableRightWidth =
                if (mDrawableRightWidth > 0) mDrawableRightWidth else drawables?.get(2)?.intrinsicWidth
                    ?.toFloat()?:0f
            mDrawableRightHeight =
                if (mDrawableRightHeight > 0) mDrawableRightHeight else drawables?.get(2)?.intrinsicHeight
                    ?.toFloat()?:0f
        }
    }

    /**
     * 获取最大文字高度
     */
    private fun getMaxPaint() {
        mMaxPaint = mPaintLeft
        mMaxPaint = if (mPaintCenter.textSize >= mPaintLeft.textSize) mPaintCenter else mPaintLeft
        mMaxPaint = if (mPaintRight.textSize >= (mMaxPaint?.getTextSize()?:0f)) mPaintRight else mMaxPaint
        if (mMaxPaint == null) {
            mMaxPaint = paint
        }
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        var height = MeasureSpec.getSize(heightMeasureSpec)
        var width = MeasureSpec.getSize(widthMeasureSpec)
        //最大高度Paint
        getMaxPaint()
        //原生控件文本内容
        val txt = text.toString()
        //获取最大高度(1,没有尺寸限制,上限为父view 2,没有尺寸限制,没有上限)
        if (MeasureSpec.AT_MOST == heightMode || MeasureSpec.UNSPECIFIED == heightMode) {
            //高度上的padding值
            val ptm = (paddingTop + paddingBottom + 0.5).toFloat()
            height = if (!TextUtils.isEmpty(txt)) {
                //                height = (int) (getPaint().descent() - getPaint().ascent() + ptm);
                measuredHeight
            } else {
                (mMaxPaint!!.descent() - mMaxPaint!!.ascent() + ptm).toInt()
            }
        }
        //获取最大宽度(没有尺寸限制,上限为父view)
        if (MeasureSpec.AT_MOST == widthMode) {
            width = if (!TextUtils.isEmpty(txt)) {
                //                width = (int) (getPaint().measureText(getText().toString()) + plr);
                measuredWidth
            } else {
                //drawablePadding
                val drawablePadding = (mDrawableLeftPadding + mDrawableRightPadding + 0.5).toInt()
                contentWidth() + drawablePadding
            }
        }
        //获取最大高度(文字高度,图片高度比较)
        height = Math.max(height.toFloat(), Math.max(mDrawableLeftHeight, mDrawableRightHeight))
            .toInt()
        setMeasuredDimension(width, height)
        //计算内容宽度
        contentWidth()
    }

    /**
     * 计算内容所需总宽度
     */
    private fun contentWidth(): Int {
        //左右两边padding值
        val plr = (paddingLeft + paddingRight + 0.5).toFloat()
        //单个文本宽度
        var left = 0f
        var center = 0f
        var right = 0f
        if (!TextUtils.isEmpty(mLeftString)) {
            left = mPaintLeft.measureText(mLeftString)
        }
        if (!TextUtils.isEmpty(mCenterString)) {
            center = mPaintCenter.measureText(mCenterString)
        }
        if (!TextUtils.isEmpty(mRightString)) {
            right = mPaintRight.measureText(mRightString)
        }
        mContentWidth = (left + center + right + plr //margin值
                + mCenterTvMarginLeft + mCenterTvMarginRight //drawable所占宽度
                + mDrawableLeftWidth + mDrawableRightWidth).toInt()
        return mContentWidth
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        viewWidth()
    }

    /**
     * 布局宽度
     */
    private fun viewWidth() {
        mParentWidth = 0
        val parent = parent as ViewGroup
        if (parent != null) {
            mParentWidth = parent.width
        }
        mViewWidth = Math.max(width, measuredWidth)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawText(canvas)
        drawDrawable(canvas)
    }

    /**
     * 绘制文字
     */
    private fun drawText(canvas: Canvas) {
        if (TextUtils.isEmpty(text)) {
            autoSize()
            mMaxBaseLine = height / 2 + Math.abs(mMaxPaint!!.ascent() + mMaxPaint!!.descent()) / 2
            drawLeft(canvas)
            drawCenter(canvas)
            drawRight(canvas)
        }
    }

    /**
     * 自适应字体大小
     */
    private fun autoSize() {
        if (mAutoSize) {
            //等比例缩放
            val s1 = mPaintLeft.textSize * mAutoSizeRatio
            val s2 = mPaintCenter.textSize * mAutoSizeRatio
            val s3 = mPaintRight.textSize * mAutoSizeRatio
            for (i in 0..49) {
                if (mContentWidth > mParentWidth || mContentWidth > mViewWidth) {
                    mPaintLeft.textSize = mPaintLeft.textSize - s1
                    mPaintCenter.textSize = mPaintCenter.textSize - s2
                    mPaintRight.textSize = mPaintRight.textSize - s3
                } else {
                    break
                }
                //重新计算
                contentWidth()
            }
            requestLayout()
        }
    }

    /**
     * 左边文字
     */
    private fun drawLeft(canvas: Canvas) {
        if (!TextUtils.isEmpty(mLeftString)) {
            val xStartPoint = paddingLeft + mDrawableLeftWidth + mDrawableLeftPadding
            canvas.drawText(mLeftString,
                xStartPoint,
                countBaseLine(mPaintLeft) - mMarBtmLeft,
                mPaintLeft)
            mLeftWidth = xStartPoint + mPaintLeft.measureText(mLeftString)
        }
    }

    private fun drawCenter(canvas: Canvas) {
        if (!TextUtils.isEmpty(mCenterString)) {
            var xStartPoint = mCenterTvMarginLeft + mLeftWidth
            //平分模式时
            if (mGravityMode != 0) {
                //x方向的内容中间值
                val padding = paddingLeft + paddingRight
                val midX = (width - 0) / 2
                //获得中间文本的x起始值
                xStartPoint = midX - mPaintCenter.measureText(mCenterString) / 2
                //平分模式,右边文本为空
                if (TextUtils.isEmpty(mRightString)) {
                    xStartPoint = (width - paddingRight - mPaintCenter.measureText(mCenterString)
                            - mDrawableRightWidth - mDrawableRightPadding)
                }
            }
            canvas.drawText(mCenterString,
                xStartPoint,
                countBaseLine(mPaintCenter) - mMarBtmCenter,
                mPaintCenter)
            mCenterWidth =
                mCenterTvMarginLeft + mCenterTvMarginRight + mPaintCenter.measureText(mCenterString)
        }
    }

    private fun drawRight(canvas: Canvas) {
        if (!TextUtils.isEmpty(mRightString)) {
            var xStartPoint = mLeftWidth + mCenterWidth
            //平分模式
            if (mGravityMode != 0) {
                xStartPoint = (this.width - this.paddingRight).toFloat() - mPaintRight.measureText(
                    mRightString) - mDrawableRightWidth - mDrawableRightPadding
            }
            canvas.drawText(mRightString,
                xStartPoint,
                countBaseLine(mPaintRight) - mMarBtmRight,
                mPaintRight)
            mRightWidth = mPaintRight.measureText(mRightString)
        }
    }

    /**
     * 计算基线对齐
     */
    private fun countBaseLine(paint: Paint): Float {
        val tvLine = height / 2 + Math.abs(paint.ascent() + paint.descent()) / 2
        return if (mBaseLineMode == 0) mMaxBaseLine!! else tvLine
    }

    /**
     * drawable图片宽高
     */
    private fun drawDrawable(canvas: Canvas) {
        if (drawables!![0] != null) {
            val left = drawables!![0]
            val paddingLeft = paddingLeft
            //固定宽高
            if (mDrawableLeftWidth > 0 && mDrawableLeftHeight > 0) {
                val topPoint = (height / 2 - mDrawableLeftHeight / 2).toInt()
                left.setBounds(paddingLeft,
                    topPoint,
                    mDrawableLeftWidth.toInt() + paddingLeft,
                    mDrawableLeftHeight.toInt() + topPoint)
            } else {
                //原生宽高
                val topPoint = height / 2 - left.intrinsicHeight / 2
                drawables!![0].setBounds(paddingLeft,
                    topPoint,
                    left.intrinsicWidth + paddingLeft,
                    left.intrinsicHeight + topPoint)
            }
            left.draw(canvas)
        }
        if (drawables!![2] != null) {
            val right = drawables!![2]
            val paddingLeft = paddingLeft
            var paddingRight = paddingRight
            //固定宽高
            if (mDrawableRightWidth > 0 && mDrawableRightHeight > 0) {
                val rightStartPoint =
                    mLeftWidth + mCenterWidth + mRightWidth + mDrawableRightPadding
                val topPoint = (height / 2 - mDrawableRightHeight / 2).toInt()
                val leftPoint: Int
                //顺序排列模式
                if (mGravityMode == 0) {
                    leftPoint =
                        (if (width >= rightStartPoint + mDrawableRightWidth) rightStartPoint else width - mDrawableRightWidth).toInt()
                    paddingRight = 0
                } else {
                    leftPoint = (width - mDrawableRightWidth).toInt()
                }
                right.setBounds(leftPoint - paddingRight,
                    topPoint,
                    mDrawableRightWidth.toInt() + leftPoint - paddingRight,
                    mDrawableRightHeight.toInt() + topPoint)
            } else {
                //原生宽高
                val topPoint = height / 2 - right.intrinsicHeight / 2
                right.setBounds(paddingLeft - paddingRight,
                    topPoint,
                    right.intrinsicWidth + paddingLeft - paddingRight,
                    right.intrinsicHeight + topPoint)
            }
            right.draw(canvas)
        }
    }

}