package com.duong.thoi.helloopenglesproject.programs

import android.content.Context
import android.opengl.GLES20.*
import com.duong.thoi.helloopenglesproject.R
import android.opengl.GLES20.glUniform4f



/**
 * Created by thoiduong on 11/8/18.
 */

class ColorShaderProgram(context: Context): ShaderProgram(context, R.raw.simple_vertex_shader,
        R.raw.simple_fragment_shader){
    // Uniform locations
    private var uMatrixLocation: Int = glGetUniformLocation(program, U_MATRIX)

    // Attribute locations
    private var aPositionLocation: Int = 0
    private var uColorLocation: Int = 0

    init {
        uMatrixLocation = glGetUniformLocation(program, U_MATRIX)
        // Retrieve attribute locations for the shader program.
        aPositionLocation = glGetAttribLocation(program, A_POSITION)
        uColorLocation = glGetUniformLocation(program, U_COLOR)
    }

    fun setUniforms(matrix: FloatArray, r: Float, g: Float, b: Float) {
        glUniformMatrix4fv(uMatrixLocation, 1, false, matrix, 0)
        glUniform4f(uColorLocation, r, g, b, 1f)
    }

    fun getPositionAttributeLocation(): Int {
        return aPositionLocation
    }
}