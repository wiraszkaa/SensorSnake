package edu.pwr.jakubwiraszka.sensorsnake.composables

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edu.pwr.jakubwiraszka.sensorsnake.domain.Direction

@Composable
fun Controls(
    currentDirection: Direction,
    sensorPosition: Offset,
    isAccelerometer: Boolean = true,
    minSensorAngle: Float,
    onDirectionChange: (Direction) -> Unit
) {
    if (isAccelerometer) {
        val newDirection =
            handleAccelerometerControl(currentDirection, sensorPosition, minSensorAngle)
        if (newDirection != currentDirection) onDirectionChange(newDirection)
        AccelerometerControls(sensorPosition = sensorPosition, minSensorAngle = minSensorAngle)
    } else {
        ButtonControls(currentDirection = currentDirection, onDirectionChange = onDirectionChange)
    }
}

@Composable
fun AccelerometerControls(
    sensorPosition: Offset,
    minSensorAngle: Float,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(16.dp)
    ) {
        val textMeasurer = rememberTextMeasurer()
        Canvas(modifier = Modifier.fillMaxSize()) {
            val radius = 20.dp.toPx()
            drawLine(
                start = Offset(x = 0.dp.toPx(), y = 0.dp.toPx()),
                end = Offset(x = size.width, y = size.height),
                color = Color.Black,
                strokeWidth = 2.dp.toPx() // instead of 5.dp.toPx() , you can also pass 5f
            )
            drawLine(
                start = Offset(x = 0.dp.toPx(), y = size.height),
                end = Offset(x = size.width, y = 0.dp.toPx()),
                color = Color.Black,
                strokeWidth = 2.dp.toPx() // instead of 5.dp.toPx() , you can also pass 5f
            )
            drawRect(
                color = Color.Black,
                topLeft = Offset(size.width / 2 - minSensorAngle, size.height / 2 - minSensorAngle),
                size = androidx.compose.ui.geometry.Size(
                    minSensorAngle * 2, minSensorAngle * 2,
                ),
                style = Stroke(5F)
            )
            drawText(
                textMeasurer = textMeasurer,
                text = "UP",
                style = TextStyle(
                    color = Color.Black,
                    fontSize = 20.sp
                ),
                topLeft = Offset(size.width / 2 - 10.dp.toPx(), 0.dp.toPx())
            )

            drawText(
                textMeasurer = textMeasurer,
                text = "DOWN",
                style = TextStyle(
                    color = Color.Black,
                    fontSize = 20.sp
                ),
                topLeft = Offset(size.width / 2 - 25.dp.toPx(), size.height - 30.dp.toPx())
            )
            drawText(
                textMeasurer = textMeasurer,
                text = "LEFT",
                style = TextStyle(
                    color = Color.Black,
                    fontSize = 20.sp
                ),
                topLeft = Offset(0.dp.toPx(), size.height / 2 - 15.dp.toPx())
            )
            drawText(
                textMeasurer = textMeasurer,
                text = "RIGHT",
                style = TextStyle(
                    color = Color.Black,
                    fontSize = 20.sp
                ),
                topLeft = Offset(size.width - 60.dp.toPx(), size.height / 2 - 15.dp.toPx())
            )
            drawCircle(
                color = Color.White,
                radius = radius + 5F,
                center = Offset(size.width / 2, size.height / 2)
            )
            drawCircle(
                color = Color.Black,
                radius = radius + 5F,
                center = Offset(size.width / 2, size.height / 2),
                style = Stroke(5F)
            )
            drawCircle(
                color = Color.Red,
                radius = radius,
                center = Offset(
                    (size.width / 2) + sensorPosition.x,
                    (size.height / 2) - sensorPosition.y
                )
            )

        }
    }
}

@Composable
fun ButtonControls(
    currentDirection: Direction,
    onDirectionChange: (Direction) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(16.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(8.dp)
        ) {
            // Up button
            ControlButton(
                imageVector = Icons.Default.KeyboardArrowUp,
                contentDescription = "Up"
            ) {
                if (currentDirection != Direction.DOWN) onDirectionChange(Direction.UP)
            }
        }
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.padding(8.dp)
        ) {
            // Left button
            ControlButton(
                imageVector = Icons.Default.KeyboardArrowLeft,
                contentDescription = "Left"
            ) {
                if (currentDirection != Direction.RIGHT) onDirectionChange(Direction.LEFT)
            }

            Spacer(modifier = Modifier.width(40.dp))

            // Right button
            ControlButton(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = "Right"
            ) {
                if (currentDirection != Direction.LEFT) onDirectionChange(Direction.RIGHT)
            }
        }
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(8.dp)
        ) {
            // Down button
            ControlButton(
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = "Down"
            ) {
                if (currentDirection != Direction.UP) onDirectionChange(Direction.DOWN)
            }

        }
    }
}

@Composable
fun ControlButton(
    imageVector: ImageVector,
    contentDescription: String,
    onClick: () -> Unit
) {
    IconButton(
        modifier = Modifier
            .clip(
                RoundedCornerShape(20.dp)
            )
            .background(color = Color.LightGray),
        onClick = onClick
    ) {
        Icon(
            modifier = Modifier
                .size(80.dp), imageVector = imageVector, contentDescription = contentDescription
        )
    }
}

fun handleAccelerometerControl(
    currentDirection: Direction,
    sensorPosition: Offset,
    minSensorAngle: Float
): Direction {
    val x = sensorPosition.x
    val y = sensorPosition.y
    return when {
        currentDirection !== Direction.LEFT && x > minSensorAngle && y < x && y > -x -> Direction.RIGHT
        currentDirection !== Direction.RIGHT && x < minSensorAngle && y > x && y < -x -> Direction.LEFT
        currentDirection !== Direction.UP && y < minSensorAngle && y < x && y < -x -> Direction.DOWN
        currentDirection !== Direction.DOWN && y > minSensorAngle && y > x && y > -x -> Direction.UP
        else -> currentDirection
    }
}