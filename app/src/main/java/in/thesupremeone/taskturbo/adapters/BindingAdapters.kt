package `in`.thesupremeone.taskturbo.adapters

import android.graphics.Paint
import android.widget.TextView
import androidx.databinding.BindingAdapter

@BindingAdapter("strikeThrough")
fun TextView.strikeThrough(boolean: Boolean){
    if(boolean){
        this.paintFlags = this.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
    }else{
        val max = 2048-1
        val mask = Paint.STRIKE_THRU_TEXT_FLAG xor max
        this.paintFlags = this.paintFlags and mask
    }
}