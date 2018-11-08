package com.duong.thoi.helloopenglesproject.programs

import android.content.Context
import android.opengl.GLES20.*
import com.duong.thoi.helloopenglesproject.R


/**
 * Created by thoiduong on 11/8/18.
 */
class TextureShaderProgram(context: Context): ShaderProgram(context, R.raw.texture_vertex_shader, R.raw.texture_fragment_shader) {
    private var uMatrixLocation: Int = 0
    private var uTextureUnitLocation: Int = 0
    // Attribute locations
    private var aPositionLocation: Int = 0
    private var aTextureCoordinatesLocation: Int = 0

    init {
        uMatrixLocation = glGetUniformLocation(program, U_MATRIX)
        uTextureUnitLocation = glGetUniformLocation(program, U_TEXTURE_UNIT)

        // Retrieve attribute locations for the shader program.
        aPositionLocation = glGetAttribLocation(program, A_POSITION)
        aTextureCoordinatesLocation =
                glGetAttribLocation(program, A_TEXTURE_COORDINATES)
    }

    fun setUniforms(matrix: FloatArray, textureId: Int) {
        // Pass the matrix into the shader program.
        glUniformMatrix4fv(uMatrixLocation, 1, false, matrix, 0)

        // Set the active texture unit to texture unit 0.
        glActiveTexture(GL_TEXTURE0)

        // Bind the texture to this unit.
        glBindTexture(GL_TEXTURE_2D, textureId)

        // Tell the texture uniform sampler to use this texture in the shader by
        // telling it to read from texture unit 0.
        glUniform1i(uTextureUnitLocation, 0)
    }

    fun getPositionAttributeLocation(): Int {
        return aPositionLocation
    }

    fun getTextureCoordinatesAttributeLocation(): Int {
        return aTextureCoordinatesLocation
    }
}