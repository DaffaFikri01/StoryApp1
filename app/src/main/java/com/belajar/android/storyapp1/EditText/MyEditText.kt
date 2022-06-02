package com.belajar.android.storyapp1.EditText

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Patterns.EMAIL_ADDRESS
import androidx.appcompat.widget.AppCompatEditText
import com.belajar.android.storyapp1.R

class MyEditText : AppCompatEditText {

    var editText = ""

    constructor(context: Context) : super(context){
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init(){
        addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //Do Nothing
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (editText == "password"){
                    if (s.length < 6){
                        error = resources.getString(R.string.password_error_msg)
                    }
                }else if (editText == "email"){
                    if (!EMAIL_ADDRESS.matcher(s).matches()){
                        error = resources.getString(R.string.invalid_email)
                    }
                }
            }

            override fun afterTextChanged(s: Editable?) {
                //Do Nothing
            }

        })
    }
}