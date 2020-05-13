package com.example.adanapp

import android.content.Context
import java.util.*


class TimerPrayer(val context: Context) {

    val prefrences = LocationPrefrence (context)

    companion object{
        var nextPrayer = 0
        var timeRemaining:Long = 0
        var nextAlarm :Calendar = Calendar.getInstance()
        var list = mutableListOf<Calendar>()

    }


    fun nextPrayer(): Int {

        var calendar = Calendar.getInstance()
//        val date = response.data.date.gregorian
//        calendar.set(date.year.toInt(),date.month.number,date.day.toInt())
        calendar.set(2020,5,13)
        println("calendar : $calendar")
        var minlist = mutableListOf<Boolean>()
        list.forEach{
            minlist.add(it.after(calendar))
            println("it : $it")
        }
        println("minlist : $minlist")
        nextPrayer = minlist.indexOf(true)
        if(nextPrayer == -1){
            updateList(0,prefrences.getImsak(),1)
            nextPrayer = 0
        }
        timeRemaining = list[nextPrayer].timeInMillis - calendar.timeInMillis
        nextAlarm = list[nextPrayer]
        return nextPrayer
    }

    fun getTimeRemaining(): Long {
        var calendar = Calendar.getInstance()
        calendar.set(2020,5,13)
        timeRemaining = nextAlarm.timeInMillis - calendar.timeInMillis
        return timeRemaining
    }
    fun setNextPrayer():Int{
        nextPrayer = (nextPrayer + 1).rem(5)
        return nextPrayer
    }

    fun addTolist(prayer:String,add:Int = 0){
        var time = prayer.split(":")
        val min = time[1].toInt()
        val hour = time[0].toInt()
        val calendar = Calendar.getInstance()
        calendar.set(2020,5,13,hour,min)
        list.add(calendar)
    }
    fun updateList(index:Int,prayer: String,add: Int=0){
        var time = prayer.split(":")
        val min = time[1].toInt()
        val hour = time[0].toInt()
//        val date = response.data.date.gregorian
        val calendar = Calendar.getInstance()
//        calendar.set(date.year.toInt(),date.month.number,date.day.toInt()+add,hour,min)
        calendar.set(2020,5,14,hour,min)
        list[index]=calendar
    }




}