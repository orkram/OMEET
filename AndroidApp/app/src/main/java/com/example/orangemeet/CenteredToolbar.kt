package com.example.orangemeet

import com.example.orangemeet.R
import android.content.Context
import android.content.res.AssetManager
import android.graphics.Typeface
import android.graphics.fonts.FontFamily
import android.text.TextUtils
import android.util.AttributeSet
import android.view.Gravity
import android.widget.TextView
import androidx.annotation.Dimension
import androidx.appcompat.widget.Toolbar
import androidx.core.content.res.ResourcesCompat
import java.lang.reflect.Type


class CenteredToolbar : Toolbar {
    private var centeredTitleTextView: TextView? = null

    constructor(context: Context?) : super(context!!) {}
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
            centeredTitleTextView!!.gravity = Gravity.CENTER
            @Suppress("DEPRECATION")
            centeredTitleTextView!!.setTextAppearance(context, R.style.TextAppearance_AppCompat_Widget_ActionBar_Title)
            centeredTitleTextView!!.setTypeface(typeface, Typeface.BOLD)
            centeredTitleTextView!!.setTextSize(fontsize)
            val lp = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
            lp.gravity = Gravity.CENTER
            centeredTitleTextView!!.layoutParams = lp
            addView(centeredTitleTextView)
        }

        return centeredTitleTextView!!
    }
}