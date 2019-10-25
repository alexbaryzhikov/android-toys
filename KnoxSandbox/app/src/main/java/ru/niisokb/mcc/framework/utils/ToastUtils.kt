package ru.niisokb.mcc.framework.utils

import android.content.Context
import android.widget.Toast

fun showToast(context: Context, messageResId: Int) {
    Toast.makeText(context, context.resources.getString(messageResId), Toast.LENGTH_SHORT).show()
}

fun showToast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}
