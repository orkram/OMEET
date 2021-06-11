//Autorzy kodu źródłowego: Konrad Stręk, Michał Skrok
//Kod został utworzony w ramach kursu Projekt Zespołowy
//na Politechnice Wrocławskiej
package com.example.orangemeet.ui.utils

import android.content.Context
import android.graphics.Typeface
import android.text.TextUtils
import android.util.AttributeSet
import android.view.Gravity
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.res.ResourcesCompat
import com.example.orangemeet.R


class CustomToolbar : Toolbar {
    private var centeredTitleTextView: TextView? = null

    constructor(context: Context?) : super(context!!)
    constructor(context: Context?, attrs: AttributeSet?) : super(context!!, attrs) {init(context, attrs)}
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context!!, attrs, defStyleAttr) {init(context, attrs)}

    private lateinit var typeface: Typeface
    private var fontsize: Float = 30f

    fun init(context: Context?, attrs: AttributeSet?) {
        context!!.theme.obtainStyledAttributes(
            attrs,
                R.styleable.CenteredToolbar,
            0, 0).apply {
            try {
                typeface = ResourcesCompat.getFont(context, getResourceId(R.styleable.CenteredToolbar_typeface, 0))!!
                fontsize = getDimension(R.styleable.CenteredToolbar_fontsize, -1f)
            } finally {
                recycle()
            }
        }
    }

    override fun setTitle(title: CharSequence) {
        getCenteredTitleTextView().text = title
    }

    override fun getTitle(): CharSequence {
        return getCenteredTitleTextView().text.toString()
    }

    fun setTypeface(font: Typeface?) {
        getCenteredTitleTextView().typeface = font
    }

    private fun getCenteredTitleTextView(): TextView {
        if (centeredTitleTextView == null) {
            centeredTitleTextView = TextView(context)
            centeredTitleTextView!!.setSingleLine()
            centeredTitleTextView!!.ellipsize = TextUtils.TruncateAt.END
            //centeredTitleTextView!!.gravity = Gravity.CENTER
            centeredTitleTextView!!.setTextAppearance(R.style.TextAppearance_AppCompat_Widget_ActionBar_Title)
            centeredTitleTextView!!.setTypeface(typeface, Typeface.BOLD)
            centeredTitleTextView!!.textSize = fontsize
            val lp = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
            lp.gravity = Gravity.CENTER
            centeredTitleTextView!!.layoutParams = lp
            addView(centeredTitleTextView)
        }

        return centeredTitleTextView!!
    }
}