package com.duong.thoi.helloopenglesproject.data

import android.opengl.GLES20.*
import com.duong.thoi.helloopenglesproject.Constants.Constants.BYTES_PER_FLOAT
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

/**
 * Created by thoiduong on 11/8/18.
 */
class VertexArray(vertexData: FloatArray) {
    private var floatBuffer: FloatBuffer = ByteBuffer
            .allocateDirect(vertexData.size * BYTES_PER_FLOAT)
            .order(ByteOrder.nativeOrder())
            .asFloatBuffer()
            .put(vertexData)

    fun setVertexAttribPointer(dataOffset: Int, attributeLocation: Int, componentCount: Int, stride: Int) {
        floatBuffer.position(dataOffset)
        glVertexAttribPointer(attributeLocation, componentCount, GL_FLOAT, false, stride, floatBuffer)

        glEnableVertexAttribArray(attributeLocation)
        floatBuffer.position(0)
    }
}