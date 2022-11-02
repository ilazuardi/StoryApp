package com.irfan.storyapp.customview

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.irfan.storyapp.R

class StoryEmailEditText : AppCompatEditText {

    private lateinit var emailImage: Drawable
    var isValidate: Boolean = false

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
        emailImage = ContextCompat.getDrawable(context, R.drawable.custom_ic_email) as Drawable
        setDrawables(startOfTheText = emailImage)
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(p0!!).matches()) {
                    error = context.getString(R.string.str_err_email_edt)
                    isValidate = false
                } else {
                    isValidate = true
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        hint = context.getString(R.string.email)
        height = 50
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
}