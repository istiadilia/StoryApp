package com.example.mystoryapp.view.customview

import android.content.Context
import android.graphics.Canvas
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import com.example.mystoryapp.R

class NameEditText : AppCompatEditText {
    var isValidName = false

    constructor(context: Context) : super(context) { init() }
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) { init() }
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) { init() }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        hint = context.getString(R.string.name_hint)
        textAlignment = View.TEXT_ALIGNMENT_VIEW_START
        compoundDrawablePadding = 16
    }

    private fun init() {
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                validate()
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })
    }

    fun validate() {
        val name = text
        if (name.isNullOrEmpty()) {
            isValidName = false
            error = context.getString(R.string.name_required)
        } else {
            isValidName = true
        }
    }
}