package com.irfan.storyapp.customview

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.irfan.storyapp.R

class StoryPasswordEditText : AppCompatEditText, OnTouchListener {

    private lateinit var lockImage: Drawable
    private lateinit var hideOrShowTextButtonImage: Drawable
    private var isPasswordHide = true

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context, attrs, defStyleAttr
    ) {
        init()
    }

    private fun init() {
        transformationMethod = PasswordTransformationMethod.getInstance()
        lockImage = ContextCompat.getDrawable(context, R.drawable.custom_ic_lock) as Drawable
        hideOrShowTextButtonImage =
            ContextCompat.getDrawable(context, R.drawable.ic_baseline_eye_24) as Drawable
        setDrawables(startOfTheText = lockImage, endOfTheText = hideOrShowTextButtonImage)
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s?.length!! < 6) {
                    setError(context.getString(R.string.str_err_password_edt), hideOrShowTextButtonImage)
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })
        setOnTouchListener(this)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        hint = context.getString(R.string.password)
        height = 50
        setPadding(0, 0, 30, 0)
        textAlignment = View.TEXT_ALIGNMENT_TEXT_START
        background =
            ContextCompat.getDrawable(context, R.drawable.bg_custom_rounded_input_edit_text)
    }

    private fun setDrawables(
        startOfTheText: Drawable? = null,
        topOfTheText: Drawable? = null,
        endOfTheText: Drawable? = null,
        bottomOfTheText: Drawable? = null
    ) {
        setCompoundDrawablesRelativeWithIntrinsicBounds(
            startOfTheText, topOfTheText, endOfTheText, bottomOfTheText
        )
    }

    override fun onTouch(v: View?, event: MotionEvent): Boolean {
        if (compoundDrawables[2] != null) {
            val hideOrShowTextButton: Float
            var isHideOrShowTextButtonClicked = false

            if (layoutDirection == View.LAYOUT_DIRECTION_RTL) {
                hideOrShowTextButton =
                    (hideOrShowTextButtonImage.intrinsicWidth + paddingStart).toFloat()
                when {
                    event.x < hideOrShowTextButton -> isHideOrShowTextButtonClicked = true
                }
            } else {
                hideOrShowTextButton =
                    (width - paddingEnd - hideOrShowTextButtonImage.intrinsicWidth).toFloat()
                when {
                    event.x > hideOrShowTextButton -> isHideOrShowTextButtonClicked = true
                }
            }

            return if (isHideOrShowTextButtonClicked) {
                when (event.action) {
                    MotionEvent.ACTION_UP -> {
                        if (isPasswordHide) {
                            isPasswordHide = false
                            transformationMethod = HideReturnsTransformationMethod.getInstance()
                        } else {
                            isPasswordHide = true
                            transformationMethod = PasswordTransformationMethod.getInstance()
                        }
                        true
                    }
                    else -> false
                }
            } else false
        }
        return false
    }
}