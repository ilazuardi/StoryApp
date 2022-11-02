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

class StoryDefaultEditText : AppCompatEditText {

    var typeFieldText: String = "Field"

    constructor(context: Context) : super(context) { init() }
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) { init() }
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) { init() }

    private fun init() {
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0.isNullOrBlank()) {
                    error = context.getString(R.string.str_field_required, typeFieldText)
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        height = 50
        textAlignment = View.TEXT_ALIGNMENT_TEXT_START
        background =
            ContextCompat.getDrawable(context, R.drawable.bg_custom_rounded_input_edit_text)
    }
}