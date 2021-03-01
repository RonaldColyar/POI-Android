package com.example.myapplication

import java.text.SimpleDateFormat
import java.util.*

class DateManager{
    private fun Date.toString(format: String, locale: Locale = Locale.getDefault()): String {
        val formatter = SimpleDateFormat(format, locale)
        return formatter.format(this)
    }

    private fun getCurrentDateTime(): Date {
        return Calendar.getInstance().time
    }
    open fun current_date():String{
        val date = DateManager().getCurrentDateTime()
        return date.toString("yyyy/MM/dd")
    }

}

