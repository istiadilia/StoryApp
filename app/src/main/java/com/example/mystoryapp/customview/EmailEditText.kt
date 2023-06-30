package com.example.mystoryapp.customview

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Patterns
import androidx.appcompat.widget.AppCompatEditText
import com.example.mystoryapp.R

class EmailEditText : AppCompatEditText {
    var isValidEmail: Boolean = false

    constructor(context: Context): super(context) { init() }
    constructor(context: Context, attrs: AttributeSet?): super(context, attrs) { init() }
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int): super(context, attrs, defStyleAttr) { init() }

    private fun init() {
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                val email = text?.trim()
                isValidEmail = if (email.isNullOrEmpty()) {
                    error = resources.getString(R.string.input_email)
                    false
                } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    error = resources.getString(R.string.invalid_email)
                    false
                } else {
                    true
                }
            }
        })
    }
}