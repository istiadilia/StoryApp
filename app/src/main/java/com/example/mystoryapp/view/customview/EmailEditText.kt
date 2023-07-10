package com.example.mystoryapp.view.customview

import android.content.Context
import android.graphics.Canvas
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Patterns
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import com.example.mystoryapp.R

class EmailEditText : AppCompatEditText {
    var isValidEmail = false

    constructor(context: Context) : super(context) { init() }
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) { init() }
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) { init() }

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

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        hint = context.getString(R.string.insert_email)
        textAlignment = View.TEXT_ALIGNMENT_VIEW_START
        compoundDrawablePadding = 16
    }

    fun validate() {
        val email = text
        if (email.isNullOrEmpty()) {
            isValidEmail = false
            error = context.getString(R.string.require_email)
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            isValidEmail = false
            error = context.getString(R.string.invalid_email)
        } else {
            isValidEmail = true
        }
    }
}