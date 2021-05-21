package com.example.orangemeet

import android.content.Context
import android.content.res.Resources
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.StateListDrawable
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.TypedValue
import android.widget.ImageButton
import androidx.core.content.res.ResourcesCompat
import com.google.android.material.textfield.TextInputEditText
import java.nio.charset.Charset
import java.util.*

class Util {
    companion object{
        fun generateRandomString(length : Int) : String{
            val array = ByteArray(length)
            Random().nextBytes(array)
            for(i in array.indices){
                if(array[i] < 0)
                    array[i] = (-array[i]).toByte()
                array[i] = (array[i] % 26 + 97).toByte()
            }
            return String(array, Charset.forName("UTF-8"))
        }

        fun createTintedBackground(context: Context, evenView : Boolean) : StateListDrawable{
            val theme = context.theme
            val pressedColor = TypedValue()
            theme.resolveAttribute(R.attr.secondaryBackground, pressedColor, true)

            val idleColor = TypedValue()
            theme.resolveAttribute(R.attr.itemTintColor, idleColor, true)

            val background = StateListDrawable()
            if(!evenView){
                background.addState(intArrayOf(-android.R.attr.state_pressed), ColorDrawable(context.resources.getColor(idleColor.resourceId, null)))
            }
            background.addState(intArrayOf(android.R.attr.state_pressed), ColorDrawable(context.resources.getColor(pressedColor.resourceId, null)))
            background.setEnterFadeDuration(200)
            background.setExitFadeDuration(200)

            return background
        }

        fun showHidePassword(passwordEditText: TextInputEditText, visibilityButton : ImageButton, context: Context){
            if(passwordEditText.transformationMethod is HideReturnsTransformationMethod){
                passwordEditText.transformationMethod = PasswordTransformationMethod.getInstance()
                visibilityButton.setImageDrawable(ResourcesCompat.getDrawable(context.resources, R.drawable.outline_visibility_off_24, context.theme))
            } else{
                passwordEditText.transformationMethod = HideReturnsTransformationMethod.getInstance()
                visibilityButton.setImageDrawable(ResourcesCompat.getDrawable(context.resources, R.drawable.outline_visibility_24, context.theme))
            }
        }
    }
}