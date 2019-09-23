package ru.niisokb.mcc.framework.utils

import android.content.Context
import android.widget.Toast

fun showToast(context: Context, msgRes: Int) {
    Toast.makeText(context, context.resources.getString(msgRes), Toast.LENGTH_SHORT).show()
}

fun showToast(context: Context, msg: String) {
    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
}
