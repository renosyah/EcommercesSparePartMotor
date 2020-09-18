package com.syaiful.ecommercessparepartmotor.util

import java.text.DecimalFormat

class Formatter {
    companion object {
        val f = DecimalFormat("##,###")
        fun decimalFormat(i : Int) : String{
            return "${f.format(i).toString()},-"
        }
    }
}