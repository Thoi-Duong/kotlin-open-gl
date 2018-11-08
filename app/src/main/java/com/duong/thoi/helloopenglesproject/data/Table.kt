package com.duong.thoi.helloopenglesproject.data

import android.opengl.GLES20.GL_TRIANGLE_FAN
import android.opengl.GLES20.glDrawArrays
import com.duong.thoi.helloopenglesproject.Constants.Constants.BYTES_PER_FLOAT
import com.duong.thoi.helloopenglesproject.programs.TextureShaderProgram

/**
 * Created by thoiduong on 11/8/18.
 */
class Table {

    private val vertexArray: VertexArray

    init {
        vertexArray = VertexArray(VERTEX_DATA)
    }

    fun bindData(textureProgram: TextureShaderProgram) {
        vertexArray.setVertexAttribPointer(
                0,
                textureProgram.getPositionAttributeLocation(),
                POSITION_COMPONENT_COUNT,
                STRIDE)

        vertexArray.setVertexAttribPointer(
                POSITION_COMPONENT_COUNT,
                textureProgram.getTextureCoordinatesAttributeLocation(),
                TEXTURE_COORDINATES_COMPONENT_COUNT,
                STRIDE)
    }

    fun draw() {
        glDrawArrays(GL_TRIANGLE_FAN, 0, 6)
    }

    companion object {
        private const val POSITION_COMPONENT_COUNT = 2
        private const val TEXTURE_COORDINATES_COMPONENT_COUNT = 2
        private const val STRIDE = (POSITION_COMPONENT_COUNT + TEXTURE_COORDINATES_COMPONENT_COUNT) * BYTES_PER_FLOAT

        private val VERTEX_DATA = floatArrayOf(
                // Order of coordinates: X, Y, S, T

                // Triangle Fan
                0f, 0f, 0.5f, 0.5f, -0.5f, -0.8f, 0f, 0.9f, 0.5f, -0.8f, 1f, 0.9f, 0.5f, 0.8f, 1f, 0.1f, -0.5f, 0.8f, 0f, 0.1f, -0.5f, -0.8f, 0f, 0.9f)
    }
}