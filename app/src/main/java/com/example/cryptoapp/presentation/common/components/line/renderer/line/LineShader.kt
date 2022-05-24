package com.example.cryptoapp.presentation.common.components.line

import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope

interface LineShader {
    fun fillLine(
        drawScope: DrawScope,
        canvas: Canvas,
        fillPath: Path
    )
}
