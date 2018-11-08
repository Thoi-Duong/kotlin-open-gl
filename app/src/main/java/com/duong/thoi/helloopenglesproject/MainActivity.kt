package com.duong.thoi.helloopenglesproject

import android.app.ActivityManager
import android.content.Context
import android.content.pm.ConfigurationInfo
import android.opengl.GLSurfaceView
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    private lateinit var mSurfaceView: GLSurfaceView
    private var mRenderSet = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mSurfaceView = GLSurfaceView(this)

        setContentView(mSurfaceView)

        val  activityManager: ActivityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val configurationInfo: ConfigurationInfo = activityManager.deviceConfigurationInfo

        val supportsEs2: Boolean = configurationInfo.reqGlEsVersion >= 0x20000
        configurationInfo.toString()

        if (supportsEs2) {
            mSurfaceView.setEGLContextClientVersion(2)
            mSurfaceView.setRenderer(AirHockeyRenderer(this))
            mRenderSet = true
        } else {
            Toast.makeText(this, "This device dose not support OpenGL ES 2.0.", Toast.LENGTH_LONG).show()
            return
        }
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
