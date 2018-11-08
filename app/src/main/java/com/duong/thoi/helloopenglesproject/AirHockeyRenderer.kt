package com.duong.thoi.helloopenglesproject

import android.content.Context
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10
import android.opengl.GLSurfaceView.Renderer
import android.opengl.GLES20.*
import android.opengl.Matrix.*
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import com.duong.thoi.helloopenglesproject.util.MatrixHelper
import android.opengl.Matrix.setIdentityM
import com.duong.thoi.helloopenglesproject.data.Table
import com.duong.thoi.helloopenglesproject.programs.ColorShaderProgram
import com.duong.thoi.helloopenglesproject.programs.TextureShaderProgram
import com.duong.thoi.helloopenglesproject.objects.Mallet
import com.duong.thoi.helloopenglesproject.util.TextureHelper


/**
 * Created by thoiduong on 11/6/18.
 */
class AirHockeyRenderer(_context: Context): Renderer {
    private val context: Context = _context

    private val projectionMatrix = FloatArray(16)
    private val modelMatrix = FloatArray(16)

    private var table: Table? = null
    private var mallet: Mallet? = null
    private var textureProgram: TextureShaderProgram? = null
    private var colorProgram: ColorShaderProgram? = null
    private var texture: Int = 0


    override fun onSurfaceCreated(glUnused: GL10, config: EGLConfig) {
        glClearColor(0.5f, 0.0f, 0.0f, 0.0f)

        table = Table()
        mallet = Mallet()

        textureProgram = TextureShaderProgram(context)
        colorProgram = ColorShaderProgram(context)
        texture = TextureHelper.loadTexture(context, R.drawable.air_hockey_surface)
    }

    /**
     * onSurfaceChanged is called whenever the surface has changed. This is
     * called at least once when the surface is initialized. Keep in mind that
     * Android normally restarts an Activity on rotation, and in that case, the
     * renderer will be destroyed and a new one created.
     *
     * @param width
     * The new width, in pixels.
     * @param height
     * The new height, in pixels.
     */
    override fun onSurfaceChanged(glUnused: GL10, width: Int, height: Int) {
        // Set the OpenGL viewport to fill the entire surface.
        glViewport(0, 0, width, height)
//        val isWidthGreater = width > height
//        val aspectRatio = if (isWidthGreater) width.toFloat() / height.toFloat() else height.toFloat() / width.toFloat()
//
//        if (isWidthGreater) {
//            orthoM(projectionMatrix, 0, -aspectRatio, aspectRatio, -1f, 1f, -1f, 1f)
//        } else {
//            orthoM(projectionMatrix, 0, -1f, 1f, -aspectRatio, aspectRatio, -1f, 1f)
//        }

        MatrixHelper.perspectiveM(projectionMatrix, 45f, width.toFloat() / height.toFloat(), 1f, 10f)

        setIdentityM(modelMatrix, 0)

        translateM(modelMatrix, 0, 0f, 0f, -2.5f)
        rotateM(modelMatrix, 0, -60f, 1f, 0f, 0f)

        val temp = FloatArray(16)
        multiplyMM(temp, 0, projectionMatrix, 0, modelMatrix, 0)

        System.arraycopy(temp, 0, projectionMatrix, 0, temp.size)

    }

    /**
     * OnDrawFrame is called whenever a new frame needs to be drawn. Normally,
     * this is done at the refresh rate of the screen.
     */
    override fun onDrawFrame(glUnused: GL10) {
        // Clear the rendering surface.
        glClear(GL_COLOR_BUFFER_BIT)
        // Draw the table.
        if (textureProgram == null || colorProgram == null) return

        textureProgram?.useProgram()
        textureProgram?.setUniforms(projectionMatrix, texture)
        table?.bindData(textureProgram!!)
        table?.draw()
        // Draw the mallets.
        colorProgram?.useProgram()
        colorProgram?.setUniforms(projectionMatrix)
        mallet?.bindData(colorProgram!!)
        mallet?.draw()
    }
}