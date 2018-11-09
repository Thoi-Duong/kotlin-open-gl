package com.duong.thoi.helloopenglesproject.util

import android.R.attr.y
import android.R.attr.x



/**
 * Created by thoiduong on 11/9/18.
 */

class Point(val x: Float, val y: Float, val z:Float) {
    fun translateY(distance: Float): Point {
        return Point(x, y + distance, z)
    }
}

class Circle(val center: Point, val radius: Float) {
    fun scale(scale: Float): Circle {
        return Circle(center, radius * scale)
    }
}

class Cylinder(val center: Point, val radius: Float, val height: Float)