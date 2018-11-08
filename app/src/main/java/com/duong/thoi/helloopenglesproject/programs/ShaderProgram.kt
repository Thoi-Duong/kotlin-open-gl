package com.duong.thoi.helloopenglesproject.programs

import android.content.Context
import android.opengl.GLES20.glUseProgram
import com.duong.thoi.helloopenglesproject.util.TextResourceReader
import com.duong.thoi.helloopenglesproject.util.ShaderHelper



/**
 * Created by thoiduong on 11/8/18.
 */

abstract class ShaderProgram constructor(context: Context, vertexShaderResourceId: Int,
                                                            fragmentShaderResourceId: Int) {

    // Shader program
    protected val program: Int = ShaderHelper.buildProgram(
            TextResourceReader.readTextFileFromResource(
                    context, vertexShaderResourceId),
            TextResourceReader.readTextFileFromResource(
                    context, fragmentShaderResourceId))

    fun useProgram() {
        // Set the current OpenGL shader program to this program.
        glUseProgram(program)
    }

    companion object {
        // Uniform constants
        const val U_MATRIX = "u_Matrix"
        const val U_TEXTURE_UNIT = "u_TextureUnit"

        // Attribute constants
        const  val A_POSITION = "a_Position"
        const val A_COLOR = "a_Color"
        const val A_TEXTURE_COORDINATES = "a_TextureCoordinates"
    }
}