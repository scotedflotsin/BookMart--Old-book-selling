package com.bookmart.bookmart.Required_App_operations

import android.content.ContentValues.TAG
import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View

class Navigaterclick @JvmOverloads constructor (
    context: Context,
    attrs: AttributeSet? = null,
    defStyleRes: Int = 0,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleRes, defStyleAttr) {

    var touchType = -1 // No user touch yet.

    var onClickListener: () -> Unit = {
        Log.d(TAG, "on click not yet implemented")
    }

    override fun onDraw(canvas: Canvas) {
        /* your code here */

    }

    override fun onTouchEvent(e: MotionEvent?): Boolean {
        val value = super.onTouchEvent(e)

        when(e?.action) {
            MotionEvent.ACTION_DOWN -> {
                /* Determine where the user has touched on the screen. */
                touchType = 1 // for eg.
                return true
            }
            MotionEvent.ACTION_UP -> {
                /* Now that user has lifted his finger. . . */
                when (touchType) {
                    1 -> onClickListener()
                }
            }
        }
        return value
    }
}