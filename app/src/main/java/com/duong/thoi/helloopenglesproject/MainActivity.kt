package com.duong.thoi.helloopenglesproject

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.content.Context
import android.content.pm.ConfigurationInfo
import android.opengl.GLSurfaceView
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    private lateinit var mSurfaceView: GLSurfaceView
    private var mRenderSet = false

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mSurfaceView = GLSurfaceView(this)

        val  activityManager: ActivityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val configurationInfo: ConfigurationInfo = activityManager.deviceConfigurationInfo

        val supportsEs2: Boolean = configurationInfo.reqGlEsVersion >= 0x20000
        configurationInfo.toString()

        val airHockeyRenderer = AirHockeyRenderer(this)

        if (supportsEs2) {
            mSurfaceView.setEGLContextClientVersion(2)
            mSurfaceView.setRenderer(airHockeyRenderer)
            mRenderSet = true
        } else {
            Toast.makeText(this, "This device dose not support OpenGL ES 2.0.", Toast.LENGTH_LONG).show()
            return
        }

        mSurfaceView.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                if (event == null || v == null) return false

                val normalizedX = (event.x/v.width.toFloat()) * 2 - 1
                val normalizedY = (event.y/v.height.toFloat()) * 2 - 1

                if (event.action == MotionEvent.ACTION_DOWN) {
                    mSurfaceView.queueEvent(object : Runnable {
                        override fun run() {
                            airHockeyRenderer.handleTouchPress(normalizedX, normalizedY)
                            Log.e("setOnTouchListener", "ACTION_DOWN")
                        }
                    })
                } else if ( event.action == MotionEvent.ACTION_MOVE) {
                    mSurfaceView.queueEvent(object : Runnable {
                        override fun run() {
                            airHockeyRenderer.handleTouchDrag(normalizedX, normalizedY)
                            Log.e("setOnTouchListener", "ACTION_MOVE")
                        }
                    })
                }
                return true
            }
        })

        setContentView(mSurfaceView)
    }

    override fun onPause() {
        super.onPause()

        if (mRenderSet) mSurfaceView.onPause()
    }

    override fun onResume() {
        super.onResume()
        if (mRenderSet) mSurfaceView.onResume()
    }
}
