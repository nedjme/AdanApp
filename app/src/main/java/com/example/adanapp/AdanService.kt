package com.example.adanap

import android.annotation.SuppressLint
import android.app.*
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.adanapp.LocationPrefrence
import com.example.adanapp.R
import com.example.adanapp.TimerPrayer



class AdanService ( ) : Service() {


            private var mMediaPlayer: MediaPlayer? = null
            private var mAudioManager: AudioManager? = null
            private val CHANNEL_ID = "com.example.prayerapp"
            private var Description = "It's time to test the prayer notification"


            override fun onBind(intent: Intent): IBinder?{
                return null
            }

            override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
                return START_STICKY
            }

            @SuppressLint("PrivateResource")
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onCreate() {
                val soundUri =
                    Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + applicationContext.packageName + "/" + R.raw.azan1)

                val builder = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
                    .setContentTitle("Prayer time")
                    .setContentText("It's prayer time")
                    .setSmallIcon(R.drawable.notification_icon_background)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                  .setSound(soundUri)


                val name = "chanel name"
                val DescriptionText = Description
                val importance = NotificationManager.IMPORTANCE_DEFAULT
                val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                    Description = DescriptionText
                }
                val notificationManager: NotificationManager =
                    getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.createNotificationChannel(channel)

                val prefrences = LocationPrefrence ( this)

                val timerPrayer = TimerPrayer(this)
                TimerPrayer.list.clear()
                Log.e("Imsak", prefrences.getImsak())
                Log.e("Maghrein", prefrences.getMaghrib())
                timerPrayer.addTolist(prefrences.getImsak())
                timerPrayer.addTolist(prefrences.getFadjr())
                timerPrayer.addTolist(prefrences.getShuruq())
                timerPrayer.addTolist(prefrences.getThuhr())
                timerPrayer.addTolist(prefrences.getAssr())
                timerPrayer.addTolist(prefrences.getMaghrib())
                timerPrayer.addTolist(prefrences.getIshaa())

                timerPrayer.nextPrayer()
                Thread{
                    while(true) {
                        while (timerPrayer.getTimeRemaining() > 0) {
                            Log.e("testing","remaining time ... : " + timerPrayer.getTimeRemaining())
                        }

                        mAudioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
                        mMediaPlayer = MediaPlayer.create(this, R.raw.azan1 )

                        mMediaPlayer!!.start()
                        mMediaPlayer!!.setOnCompletionListener { mMediaPlayer?.release() }
                        with(NotificationManagerCompat.from(applicationContext)) {
                            notify(12345, builder.build())
                        }

                        timerPrayer.nextPrayer()

                    }
                }.start()
            }



        }





