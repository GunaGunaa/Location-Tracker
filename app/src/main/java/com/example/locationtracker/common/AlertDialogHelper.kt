package com.example.locationtracker.common

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.drawable.Drawable
import com.example.locationtracker.R

class AlertDialogHelper {
    private lateinit var dialog: Dialog
    companion object {

        fun showAlertDialog(
            context: Context?,
            content: String?
        ) {
            val builder = AlertDialog.Builder(
                context!!
            )
            builder.setMessage(content)
                .setPositiveButton(R.string.ok, null)
                .show()
        }


        fun showAlertDialog(
            context: Context?,
            title: String?,
            content: String?,
            icon: Drawable?,
            positiveClickListener: DialogInterface.OnClickListener?
        ) {
            val builder = AlertDialog.Builder(
                context!!
            )
            builder.setTitle(title)
                .setMessage(content)
                .setIcon(icon)
                .setPositiveButton(R.string.yes, positiveClickListener)
                .setNegativeButton(R.string.cancel, null)
                .show()
        }
    }
}