package com.github.tehras.charts.line

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.TweenSpec
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.unit.dp
import com.example.cryptoapp.presentation.common.components.line.LineDrawer
import com.example.cryptoapp.presentation.common.components.line.LineShader
import com.example.cryptoapp.presentation.common.components.line.NoLineShader
import com.example.cryptoapp.presentation.common.components.line.SolidLineDrawer
import com.example.cryptoapp.presentation.ui.theme.Shapes
import com.github.tehras.charts.line.LineChartUtils.calculateDrawableArea
import com.github.tehras.charts.line.LineChartUtils.calculateFillPath
import com.github.tehras.charts.line.LineChartUtils.calculateLinePath
import com.github.tehras.charts.line.LineChartUtils.calculatePointLocation
import com.github.tehras.charts.line.LineChartUtils.calculateXAxisDrawableArea
import com.github.tehras.charts.line.LineChartUtils.calculateXAxisLabelsDrawableArea
import com.github.tehras.charts.line.LineChartUtils.calculateYAxisDrawableArea
import com.github.tehras.charts.line.LineChartUtils.withProgress
import com.github.tehras.charts.line.renderer.xaxis.SimpleXAxisDrawer
import com.github.tehras.charts.line.renderer.xaxis.XAxisDrawer
import com.github.tehras.charts.line.renderer.yaxis.SimpleYAxisDrawer
import com.github.tehras.charts.line.renderer.yaxis.YAxisDrawer

@Composable
fun LineChart(
  lineChartData: LineChartData,
  modifier: Modifier = Modifier,
  animation: AnimationSpec<Float> = TweenSpec<Float>(durationMillis = 500),
  lineDrawer: LineDrawer = SolidLineDrawer(),
  lineShader: LineShader = NoLineShader,
  xAxisDrawer: XAxisDrawer = SimpleXAxisDrawer(),
  yAxisDrawer: YAxisDrawer = SimpleYAxisDrawer(),
  horizontalOffset: Float = 10f
) {
//  check(horizontalOffset in 0f..25f) {
//    "Horizontal offset is the % offset from sides, " +
//      "and should be between 0%-25%"
//  }

  val transitionAnimation = remember(lineChartData.points) { Animatable(initialValue = 0f) }

  LaunchedEffect(lineChartData.points) {
    transitionAnimation.snapTo(0f)
    transitionAnimation.animateTo(1f, animationSpec = animation)
  }

  Canvas(modifier = modifier.fillMaxSize()) {
    drawIntoCanvas { canvas ->
      val yAxisDrawableArea = calculateYAxisDrawableArea(
        xAxisLabelSize = xAxisDrawer.requiredHeight(this),
        size = size
      )
      val xAxisDrawableArea = calculateXAxisDrawableArea(
        yAxisWidth = yAxisDrawableArea.width,
        labelHeight = xAxisDrawer.requiredHeight(this),
        size = size
      )
      val xAxisLabelsDrawableArea = calculateXAxisLabelsDrawableArea(
        xAxisDrawableArea = xAxisDrawableArea,
        offset = horizontalOffset
      )
      val chartDrawableArea = calculateDrawableArea(
        xAxisDrawableArea = xAxisDrawableArea,
        yAxisDrawableArea = yAxisDrawableArea,
        size = size,
        offset = horizontalOffset
      )

      // Draw the chart line.
      lineDrawer.drawLine(
        drawScope = this,
        canvas = canvas,
        linePath = calculateLinePath(
          drawableArea = chartDrawableArea,
          lineChartData = lineChartData,
          transitionProgress = transitionAnimation.value
        )
      )

      lineShader.fillLine(
        drawScope = this,
        canvas = canvas,
        fillPath = calculateFillPath(
          drawableArea = chartDrawableArea,
          lineChartData = lineChartData,
          transitionProgress = transitionAnimation.value
        )
      )



      // Draw the X Axis line.
      xAxisDrawer.drawAxisLine(
        drawScope = this,
        drawableArea = xAxisDrawableArea,
        canvas = canvas
      )

      xAxisDrawer.drawAxisLabels(
        drawScope = this,
        canvas = canvas,
        drawableArea = xAxisLabelsDrawableArea,
        labels = lineChartData.points.map { it.label }
      )

      // Draw the Y Axis line.
      yAxisDrawer.drawAxisLine(
        drawScope = this,
        canvas = canvas,
        drawableArea = yAxisDrawableArea
      )

      yAxisDrawer.drawAxisLabels(
        drawScope = this,
        canvas = canvas,
        drawableArea = yAxisDrawableArea,
        minValue = lineChartData.minYValue,
        maxValue = lineChartData.maxYValue
      )
    }
  }
}
