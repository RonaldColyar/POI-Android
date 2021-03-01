package com.example.myapplication

import android.app.Dialog
import android.content.Context
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.provider.MediaStore
import android.view.Window
import android.widget.ImageView

class GenericFunctionality(val context: Context){
    open fun configured_dialog(id:Int): Dialog {
        val dialog  = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(id)
        return dialog
    }
    open fun update_photo(Profileimage: ImageView, data: Uri){
        try {
            //gather file resource data
            val bitmapforphoto = MediaStore.Images.Media.getBitmap(context.contentResolver, data)
            val bitmapdraw = BitmapDrawable(bitmapforphoto)
            //update U.I with image
            Profileimage.setImageResource(0)
            Profileimage.setBackgroundDrawable(bitmapdraw)
        }
        catch (e:Throwable){

        }
    }
}


