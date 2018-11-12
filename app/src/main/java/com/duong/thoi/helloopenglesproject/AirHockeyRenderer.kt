package com.duong.thoi.helloopenglesproject

import android.content.Context
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10
import android.opengl.GLSurfaceView.Renderer
import android.opengl.GLES20.*
import android.opengl.Matrix.*
import android.opengl.Matrix.setIdentityM
import com.duong.thoi.helloopenglesproject.data.Table
import com.duong.thoi.helloopenglesproject.programs.ColorShaderProgram
import com.duong.thoi.helloopenglesproject.programs.TextureShaderProgram
import com.duong.thoi.helloopenglesproject.objects.Mallet
import com.duong.thoi.helloopenglesproject.objects.Puck
import android.opengl.Matrix.setLookAtM
import android.opengl.Matrix.multiplyMM
import com.duong.thoi.helloopenglesproject.util.*
import kotlin.math.PI


/**
 * Created by thoiduong on 11/6/18.
 */
class AirHockeyRenderer(_context: Context): Renderer {
    private val context: Context = _context

    private val projectionMatrix = FloatArray(16)
    private val modelMatrix = FloatArray(16)

    private val viewMatrix = FloatArray(16)
    private val viewProjectionMatrix = FloatArray(16)
    private val modelViewProjectionMatrix = FloatArray(16)

    private var table: Table? = null
    private var mallet: Mallet? = null
    private var puck: Puck? = null

    private var textureProgram: TextureShaderProgram? = null
    private var colorProgram: ColorShaderProgram? = null
    private var texture: Int = 0

    private var malletPressed = false
    private var blueMalletPosition: Point? = null
    private val invertedViewProjectionMatrix = FloatArray(16)


    override fun onSurfaceCreated(glUnused: GL10, config: EGLConfig) {
        glClearColor(0.5f, 0.0f, 0.0f, 0.0f)

        table = Table()
        mallet = Mallet(0.08f, 0.15f, 256)
        puck = Puck( 0.06f, 0.02f, 32)

        textureProgram = TextureShaderProgram(context)
        colorProgram = ColorShaderProgram(context)
        texture = TextureHelper.loadTexture(context, R.drawable.air_hockey_surface)

        blueMalletPosition = Point(0f, mallet!!.height / 2f, 0.4f)
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

//        MatrixHelper.perspectiveM(projectionMatrix, 45f, width.toFloat() / height.toFloat(), 1f, 10f)
//
//        setIdentityM(modelMatrix, 0)
//
//        translateM(modelMatrix, 0, 0f, 0f, -2.5f)
//        rotateM(modelMatrix, 0, -60f, 1f, 0f, 0f)
//
//        val temp = FloatArray(16)
//        multiplyMM(temp, 0, projectionMatrix, 0, modelMatrix, 0)
//
//        System.arraycopy(temp, 0, projectionMatrix, 0, temp.size)

        val aspect = width.toFloat() / height.toFloat()
        MatrixHelper.perspectiveM(projectionMatrix, 45f, aspect, 1f, 10f)
        setLookAtM(viewMatrix, 0, 0f, 1.2f, 2.2f, 0f, 0f, 0f, 0f, 1f, 0f)

    }

    /**
     * OnDrawFrame is called whenever a new frame needs to be drawn. Normally,
     * this is done at the refresh rate of the screen.
     */
    override fun onDrawFrame(glUnused: GL10) {
        // Clear the rendering surface.
        glClear(GL_COLOR_BUFFER_BIT)

        multiplyMM(viewProjectionMatrix, 0, projectionMatrix, 0, viewMatrix, 0)
        invertM(invertedViewProjectionMatrix, 0, viewProjectionMatrix, 0)

        // Draw the table.

        if (textureProgram == null || colorProgram == null || mallet == null || puck == null) return


        positionTableInScene()
        textureProgram!!.useProgram()
        textureProgram!!.setUniforms(modelViewProjectionMatrix, texture)
        table!!.bindData(textureProgram!!)
        table!!.draw()
        // Draw the mallets.

        positionObjectInScene(0f, mallet!!.height / 2f, -0.4f)
        colorProgram!!.useProgram()
        colorProgram!!.setUniforms(modelViewProjectionMatrix, 1f, 0f, 0f)
        mallet!!.bindData(colorProgram!!)
        mallet!!.draw()

//        positionObjectInScene(0f, mallet!!.height / 2f, 0.4f);

        positionObjectInScene(blueMalletPosition!!.x, blueMalletPosition!!.y,
                blueMalletPosition!!.z)

        colorProgram!!.setUniforms(modelViewProjectionMatrix, 0f, 0f, 1f)
        // Note that we don't have to define the object data twice -- we just // draw the same mallet again but in a different position and with a // different color.
        mallet!!.draw()
        // Draw the puck.
        positionObjectInScene(0f, puck!!.height / 2f, 0f)
        colorProgram!!.setUniforms(modelViewProjectionMatrix, 0.8f, 0.8f, 1f)
        puck!!.bindData(colorProgram!!)
        puck!!.draw()
    }

    fun handleTouchPress(normalizedX: Float, normalizedY: Float) {
        val ray = convertNormalized2DPointToRay(normalizedX, normalizedY)
        val p = Point(blueMalletPosition!!.x, blueMalletPosition!!.y, blueMalletPosition!!.z)

        val malletBoundingSphere = Sphere(p, mallet!!.height / 2f)
        malletPressed = Geometry.intersects(malletBoundingSphere, ray)
    }
    fun handleTouchDrag(normalizedX: Float, normalizedY: Float) {
        if (malletPressed) {
            val ray = convertNormalized2DPointToRay(normalizedX, normalizedY)
            val plane = Plane(Point(0f,0f,0f), Vector(0f, 1f, 0f))

            val touchPoint = Geometry.intersectionPoint(ray, plane)
            blueMalletPosition = Point(touchPoint.x, mallet!!.height / 2f, touchPoint.z)
        }
    }

    private fun convertNormalized2DPointToRay(normalizedX: Float, normalizedY: Float): Ray {
        val nearPointNdc = floatArrayOf(normalizedX, normalizedY, -1f, 1f)
        val farPointNdc = floatArrayOf(normalizedX, normalizedY, 1f, 1f)

        val nearPointWorld = FloatArray(4)
        val farPointWorld = FloatArray(4)

        multiplyMV(nearPointWorld, 0, invertedViewProjectionMatrix, 0, nearPointNdc, 0)
        multiplyMV(farPointWorld, 0, invertedViewProjectionMatrix, 0, farPointNdc, 0)

        divideByW(nearPointWorld)
        divideByW(farPointWorld)

        val nearPointRay = Point(nearPointWorld[0], nearPointWorld[1], nearPointWorld[2])
        val farPointRay = Point(farPointWorld[0], farPointWorld[1], farPointWorld[2])

        return Ray(nearPointRay, Geometry.vectorBetween(nearPointRay, farPointRay))
    }

    private fun divideByW(vector: FloatArray) {
        vector[0] /= vector[3]
        vector[1] /= vector[3]
        vector[2] /= vector[3]
    }

    private fun positionTableInScene() {
        // The table is defined in terms of X & Y coordinates, so we rotate it // 90 degrees to lie flat on the XZ plane.
        setIdentityM(modelMatrix, 0)
        rotateM(modelMatrix, 0, -90f, 1f, 0f, 0f)
        multiplyMM(modelViewProjectionMatrix, 0, viewProjectionMatrix, 0, modelMatrix, 0)
    }

    private fun positionObjectInScene(x: Float, y: Float, z: Float) {
        setIdentityM(modelMatrix, 0)
        translateM(modelMatrix, 0, x, y, z)
        multiplyMM(modelViewProjectionMatrix, 0, viewProjectionMatrix,
                0, modelMatrix, 0)
    }
}