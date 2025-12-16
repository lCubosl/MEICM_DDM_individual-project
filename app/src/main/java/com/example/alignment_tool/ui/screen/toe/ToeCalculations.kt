package com.example.alignment_tool.ui.screen.toe

// ----------------------------------------------------------
//  CALCULATE TOE ANGLE
// ----------------------------------------------------------
fun calculateToeAngle(left: Float, right: Float, invert: Boolean = false): Float {
    // Shortest signed angle from left to right
    var delta = ((right - left + 540) % 360) - 180
    if (invert) delta *= -1   // Use for front wheels
    return delta
}

fun calculateFrontToe(wheelYaw: Map<String, Float>): Float? =
    wheelYaw["FL"]?.let { fl ->
        wheelYaw["FR"]?.let { fr ->
            calculateToeAngle(fl, fr, invert = true)
        }
    }

fun calculateRearToe(wheelYaw: Map<String, Float>): Float? =
    wheelYaw["RL"]?.let { rl ->
        wheelYaw["RR"]?.let { rr ->
            calculateToeAngle(rl, rr, invert = false)
        }
    }