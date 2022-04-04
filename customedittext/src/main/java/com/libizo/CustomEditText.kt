package com.libizo

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.support.annotation.ColorInt
import android.support.v4.content.ContextCompat
import android.support.v4.graphics.drawable.DrawableCompat
import android.support.v7.widget.AppCompatEditText
import android.text.Editable
import android.text.TextUtils
import android.text.method.DigitsKeyListener
import android.text.method.PasswordTransformationMethod
import android.util.AttributeSet
import android.util.TypedValue
import android.view.MotionEvent
import android.view.inputmethod.EditorInfo
import android.widget.TextView

import java.util.Objects

/*
 * Created by ${Raja} on 25-Dec-17.
 */

class CustomEditText : AppCompatEditText {
    private val DEFAULTCOLOR = Color.parseColor("#808080")
    private var mBackgroundColor: Int = 0
    private var clearIconTint: Int = 0
    private var hideShowIconTint: Int = 0
    private var prefixTextColor: Int = 0
    private var cPadding: Int = 0
    private var cPaddingLeft: Int = 0
    private var cPaddingTop: Int = 0
    private var cPaddingRight: Int = 0
    private var cPaddingBottom: Int = 0
    private var mCornerRadius: Float = 0.toFloat()
    private var mStrokeWidth = 1f
    private var mOriginalLeftPadding = -1f
    private var isClearIconVisible: Boolean = false
    private var isPassword = false
    private var isShowingPassword = false
    private var imgCloseButton: Drawable? = null
    private var drawableEnd: Drawable? = null
    var font: String? = null
        private set
    private var mPrefix: String? = null

    var prefix: String?
        get() = this.mPrefix
        set(prefix) {
            this.mPrefix = prefix
            calculatePrefix()
            invalidate()
        }

    constructor(context: Context) : super(context) {
        init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet?) {

        val a = context.obtainStyledAttributes(attrs, R.styleable.CustomEditText)
        imgCloseButton = ContextCompat.getDrawable(getContext(), android.R.drawable.ic_menu_close_clear_cancel)
        cPadding = a.getDimensionPixelSize(R.styleable.CustomEditText_android_padding, -1)
        cPaddingLeft = a.getDimensionPixelSize(R.styleable.CustomEditText_android_paddingLeft, DEFAULT_PADDING)
        cPaddingTop = a.getDimensionPixelSize(R.styleable.CustomEditText_android_paddingTop, DEFAULT_PADDING)
        cPaddingRight = a.getDimensionPixelSize(R.styleable.CustomEditText_android_paddingRight, DEFAULT_PADDING)
        cPaddingBottom = a.getDimensionPixelSize(R.styleable.CustomEditText_android_paddingBottom, DEFAULT_PADDING)
        isClearIconVisible = a.getBoolean(R.styleable.CustomEditText_libIZO_setClearIconVisible, false)
        val isBorderView = a.getBoolean(R.styleable.CustomEditText_libIZO_setBorderView, false)
        val mNormalColor = a.getColor(R.styleable.CustomEditText_libIZO_setBorderColor, DEFAULTCOLOR)
        val cursorColor = a.getColor(R.styleable.CustomEditText_libIZO_setCursorColor, 0)
        mBackgroundColor = a.getColor(R.styleable.CustomEditText_libIZO_setBackgroundColor, Color.TRANSPARENT)
        mStrokeWidth = a.getDimension(R.styleable.CustomEditText_libIZO_setStrokeWidth, mStrokeWidth)
        hideShowIconTint = a.getColor(R.styleable.CustomEditText_libIZO_hideShowPasswordIconTint, DEFAULTCOLOR)
        clearIconTint = a.getColor(R.styleable.CustomEditText_libIZO_clearIconTint, DEFAULTCOLOR)
        this.font = a.getString(R.styleable.CustomEditText_libIZO_setFont)
        mPrefix = a.getString(R.styleable.CustomEditText_libIZO_setPrefix)
        prefixTextColor = a.getColor(R.styleable.CustomEditText_libIZO_setPrefixTextColor, 0)
        mCornerRadius = a.getDimension(R.styleable.CustomEditText_libIZO_setCornerRadius, 1f)

        if (isBorderView) {
            setBackGroundOfLayout(getShapeBackground(mNormalColor))
            setCursorColor(cursorColor)
        } else {
            padding(false)
        }
        if (inputType == TYPE_TEXT_VARIATION_PASSWORD || inputType == TYPE_NUMBER_VARIATION_PASSWORD) {
            isPassword = true
            this.maxLines = 1
        } else if (inputType == EditorInfo.TYPE_CLASS_PHONE) {
            this.keyListener = DigitsKeyListener.getInstance("0123456789")
        }
        if (!isPassword && isClearIconVisible) {
            handleClearButton()
        }

        if (mPrefix != null && mPrefix!!.length > 0) {
            calculatePrefix()
        }

        if (isPassword)
            if (!TextUtils.isEmpty(text)) {
                showPasswordVisibilityIndicator(true)
            } else {
                showPasswordVisibilityIndicator(false)
            }
        setOnTouchListener(OnTouchListener { view, event ->
            val editText = this@CustomEditText
            if (editText.compoundDrawables[2] == null)
                return@OnTouchListener false
            if (event.action != MotionEvent.ACTION_UP)
                return@OnTouchListener false
            if (isPassword) {
                val width = if (drawableEnd == null) 0 else drawableEnd!!.intrinsicWidth
                if (event.x > editText.width - editText.paddingRight - width) {
                    togglePasswordVisibility()
                    event.action = MotionEvent.ACTION_CANCEL
                }
            } else if (isClearIconVisible) {
                val width = if (imgCloseButton == null) 0 else imgCloseButton!!.intrinsicWidth
                if (event.x > editText.width - editText.paddingRight - width) {
                    editText.setText("")
                    this@CustomEditText.handleClearButton()
                }
            }
            false
        })
        setFont()
        a.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int,
                           heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        if (mPrefix != null)
            calculatePrefix()
    }


    public  fun setCursorColor(@ColorInt color: Int) = try {
        var c = color
        if (c == 0) c = getThemeAccentColor()
        // Get the cursor resource id
        var field = TextView::class.java.getDeclaredField("mCursorDrawableRes")
        field.isAccessible = true
        val drawableResId = field.getInt(this)

        // Get the editor
        field = TextView::class.java.getDeclaredField("mEditor")
        field.isAccessible = true
        val editor = field.get(this)

        // Get the drawable and set a color filter
        val drawable = ContextCompat.getDrawable(this.context, drawableResId)
        drawable?.setColorFilter(c, PorterDuff.Mode.SRC_IN)
        val drawables = arrayOf<Drawable>(drawable!!, drawable)

        // Set the drawables
        field = editor.javaClass.getDeclaredField("mCursorDrawable")
        field.isAccessible = true
        field.set(editor, drawables)
    } catch (ignored: Exception) {
    }

/**
     * This method is used to set the custom font
     */
    private fun setFont() {
        if (font != null) {
            try {
                typeface = Typefaces[context, font!!]
            } catch (ignored: Exception) {
            }

        }
    }

    /**
     * This method is used to set the rectangle box on EditText
     */
    private fun setBackGroundOfLayout(shape: Drawable?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            background = shape
        } else {
            setBackgroundDrawable(shape)
        }
        padding(true)
    }
/**
     * This method is used to set the padding
     */
    private fun padding(isRound: Boolean) {
        val extraPadding: Int
        val extraPad: Int
        if (isRound) {
            extraPadding = 5
            extraPad = 0
        } else {
            extraPad = 5
            extraPadding = 0
        }
        if (cPadding != -1) {
            super.setPadding(cPadding + extraPadding, cPadding, cPadding, cPadding + extraPad)
        } else {
            super.setPadding(cPaddingLeft + extraPadding, cPaddingTop, cPaddingRight, cPaddingBottom + extraPad)
        }
    }


    /**
     * This method is used to draw the rectangle border view with color
     */
    @SuppressLint("WrongConstant")
    private fun getShapeBackground(@ColorInt color: Int): Drawable {

        val shape = GradientDrawable()
        shape.shape = GradientDrawable.RECTANGLE
        shape.cornerRadius = mCornerRadius
        shape.setColor(mBackgroundColor)
        shape.setStroke(mStrokeWidth.toInt(), color)
        return shape
    }


    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (mPrefix != null) {
            val prefix = mPrefix
            var myPaint: Paint? = null
            if (prefixTextColor != 0) {
                myPaint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG)
                myPaint.color = prefixTextColor
                myPaint.textAlign = Paint.Align.LEFT
                myPaint.textSize = textSize
            }
            canvas.drawText(prefix!!, mOriginalLeftPadding, getLineBounds(0, null).toFloat(), myPaint
                    ?: paint)
        }
    }


    @SuppressLint("NewApi")
    private fun handleClearButton() {
        if (isClearIconVisible) {
            DrawableCompat.setTint(imgCloseButton!!, clearIconTint)
            imgCloseButton!!.setBounds(0, 0, 43, 43)
            if (Objects.requireNonNull<Editable>(this.text).isEmpty()) {
                this.setCompoundDrawables(this.compoundDrawables[0], this.compoundDrawables[1], null, this.compoundDrawables[3])
            } else {
                this.setCompoundDrawables(this.compoundDrawables[0], this.compoundDrawables[1], imgCloseButton, this.compoundDrawables[3])
            }
        }
    }


    public override fun onTextChanged(s: CharSequence, i: Int, i1: Int, i2: Int) {
        try {
            if (isPassword) {
                if (s.isNotEmpty()) {
                    showPasswordVisibilityIndicator(true)
                } else {
                    isShowingPassword = false
                    maskPassword()
                    showPasswordVisibilityIndicator(false)
                }
            } else if (isClearIconVisible)
                this@CustomEditText.handleClearButton()

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun showPasswordVisibilityIndicator(show: Boolean) {
        if (show) {
            val original = if (isShowingPassword)
                ContextCompat.getDrawable(context, R.drawable.ic_visibility_on)
            else
                ContextCompat.getDrawable(context, R.drawable.ic_visibility_off)
            if (original != null) {
                original.mutate()
                DrawableCompat.setTint(original, hideShowIconTint)
                original.setBounds(0, 0, 43, 43)
                drawableEnd = original
                this.setCompoundDrawables(this.compoundDrawables[0], this.compoundDrawables[1], original, this.compoundDrawables[3])
            }
        } else {
            this.setCompoundDrawables(this.compoundDrawables[0], this.compoundDrawables[1], null, this.compoundDrawables[3])
        }
    }

    //make it visible
    private fun unmaskPassword() {
        transformationMethod = null
    }

    //hide it
    private fun maskPassword() {
        transformationMethod = PasswordTransformationMethod.getInstance()
    }

    private fun getThemeAccentColor(): Int {
        val colorAttr: Int = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            android.R.attr.colorAccent
        } else {
            context.resources.getIdentifier("colorAccent", "attr", context.packageName)
        }
        val outValue = TypedValue()
        context.theme.resolveAttribute(colorAttr, outValue, true)
        return outValue.data
    }

    private fun togglePasswordVisibility() {
        // Store the selection
        val selectionStart = this.selectionStart
        val selectionEnd = this.selectionEnd
        // Set transformation method to show/hide password
        if (isShowingPassword) {
            maskPassword()
        } else {
            unmaskPassword()
        }
        // Restore selection
        this.setSelection(selectionStart, selectionEnd)
        // Toggle flag and show indicator
        isShowingPassword = !isShowingPassword
        showPasswordVisibilityIndicator(true)
    }

    public fun setFontName(fontName: String) {
        this.font = fontName
        setFont()
    }


    private fun calculatePrefix() {
        if (mOriginalLeftPadding == -1f) {
            val prefix = mPrefix
            val widths = FloatArray(prefix!!.length)
            paint.getTextWidths(prefix, widths)
            var textWidth = 0f
            for (w in widths) {
                textWidth += w
            }
            mOriginalLeftPadding = compoundPaddingLeft.toFloat()
            setPadding((textWidth + mOriginalLeftPadding).toInt(),
                    paddingRight, paddingTop,
                    paddingBottom)
        }
    }

    public fun setPrefixTextColor(prefixTextColor: Int) {
        this.prefixTextColor = prefixTextColor
        invalidate()
    }


    companion object {

        private const val TYPE_TEXT_VARIATION_PASSWORD = 129
        private const val TYPE_NUMBER_VARIATION_PASSWORD = 18
        private const val DEFAULT_PADDING = 15
    }
}
