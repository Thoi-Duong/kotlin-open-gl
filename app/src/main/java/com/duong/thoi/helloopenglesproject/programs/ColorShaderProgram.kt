package com.duong.thoi.helloopenglesproject.programs

import android.content.Context
import android.opengl.GLES20.*
import com.duong.thoi.helloopenglesproject.R

/**
 * Created by thoiduong on 11/8/18.
 */

class ColorShaderProgram(context: Context): ShaderProgram(context, R.raw.simple_vertex_shader,
        R.raw.simple_fragment_shader){
    // Uniform locations
    private var uMatrixLocation: Int = glGetUniformLocation(program, U_MATRIX)

    // Attribute locations
    private var aPositionLocation: Int = 0
    private var aColorLocation: Int = 0

    init {
        uMatrixLocation = glGetUniformLocation(program, U_MATRIX)
        // Retrieve attribute locations for the shader program.
        aPositionLocation = glGetAttribLocation(program, A_POSITION)
        aColorLocation = glGetAttribLocation(program, A_COLOR)
    }

    fun setUniforms(matrix: FloatArray) {
        // Pass the matrix into the shader program.
        glUniformMatrix4fv(uMatrixLocation, 1, false, matrix, 0)
    }

    fun getPositionAttributeLocation(): Int {
        return aPositionLocation
    }

    fun getColorAttributeLocation(): Int {
        return aColorLocation
    }

}