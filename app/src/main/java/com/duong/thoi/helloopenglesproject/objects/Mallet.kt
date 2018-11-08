package com.duong.thoi.helloopenglesproject.objects

import android.opengl.GLES20.GL_POINTS
import android.opengl.GLES20.glDrawArrays
import com.duong.thoi.helloopenglesproject.Constants.Constants.BYTES_PER_FLOAT
import com.duong.thoi.helloopenglesproject.data.VertexArray
import com.duong.thoi.helloopenglesproject.programs.ColorShaderProgram


/**
 * Created by thoiduong on 11/8/18.
 */

class Mallet {
    private val vertexArray: VertexArray

    init {
        vertexArray = VertexArray(VERTEX_DATA)
    }

    fun bindData(colorProgram: ColorShaderProgram) {
        vertexArray.setVertexAttribPointer(
                0,
                colorProgram.getPositionAttributeLocation(),
                POSITION_COMPONENT_COUNT,
                STRIDE)
        vertexArray.setVertexAttribPointer(
                POSITION_COMPONENT_COUNT,
                colorProgram.getColorAttributeLocation(),
                COLOR_COMPONENT_COUNT,
                STRIDE)
    }

    fun draw() {
        glDrawArrays(GL_POINTS, 0, 2)
    }

    companion object {
        private const val POSITION_COMPONENT_COUNT = 2
        private const val COLOR_COMPONENT_COUNT = 3
        private const val STRIDE = (POSITION_COMPONENT_COUNT + COLOR_COMPONENT_COUNT) * BYTES_PER_FLOAT
        private val VERTEX_DATA = floatArrayOf(
                // Order of coordinates: X, Y, R, G, B
                0f, -0.4f, 0f, 0f, 1f, 0f, 0.4f, 1f, 0f, 0f)
    }
}