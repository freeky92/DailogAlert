package com.asurspace.whatisalertdialog_pl.custom.layout

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.Checkable
import androidx.constraintlayout.widget.ConstraintLayout
import java.lang.IllegalStateException

class CheckableLayout(context: Context, attribute: AttributeSet?, defStyleAttr: Int) :
    ConstraintLayout(context, attribute, defStyleAttr), Checkable {

    constructor(context: Context, attribute: AttributeSet?) : this(context, attribute, 0)

    private val checkableChild: Checkable by lazy { findCheckableChild(this) }

    override fun setChecked(checked: Boolean) {
        checkableChild.isChecked = checked
    }

    override fun isChecked(): Boolean {
        return checkableChild.isChecked
    }

    override fun toggle() {
        checkableChild.toggle()
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        val checkableView = checkableChild as View
        checkableView.isFocusableInTouchMode = false
        checkableView.isFocusable = false
        checkableView.isClickable = false
    }

    private fun findCheckableChild(root: ViewGroup): Checkable {
        for (i in 0..root.childCount){
            val child = root.getChildAt(i)
            if (child is Checkable){return child}
            if (child is ViewGroup){return findCheckableChild(root)}
        }
        throw IllegalStateException("Can't find Checkable child view")
    }

}