package com.duong.thoi.helloopenglesproject.objects

import com.duong.thoi.helloopenglesproject.data.VertexArray
import com.duong.thoi.helloopenglesproject.programs.ColorShaderProgram
import com.duong.thoi.helloopenglesproject.util.Point




/**
 * Created by thoiduong on 11/8/18.
 */

class Mallet(val radius: Float, val height: Float, numPointsAroundMallet: Int) {

    private val vertexArray: VertexArray
    private val drawList: List<DrawCommand>

    init {
        val generatedData = ObjectBuilder.createMallet(
                Point(0f, 0f, 0f)
                ,radius, height
                , numPointsAroundMallet
        )

        vertexArray = VertexArray(generatedData.vertexData)
        drawList = generatedData.drawList
    }

    fun bindData(colorProgram: ColorShaderProgram) {
        vertexArray.setVertexAttribPointer(0,
                colorProgram.getPositionAttributeLocation(),
                POSITION_COMPONENT_COUNT, 0)
    }

    fun draw() {
        for (drawCommand in drawList) drawCommand.draw()
    }

    companion object {
        const val POSITION_COMPONENT_COUNT = 3
    }
}