package edu.pwr.jakubwiraszka.sensorsnake.composables

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import edu.pwr.jakubwiraszka.sensorsnake.domain.Cell

@Composable
fun Grid(snake: List<Cell>, food: Cell, gridSize: Int) {
    val cellSize = 16.dp

    Column(modifier = Modifier.background(color = Color.Red)) {
        for (i in 0 until gridSize) {
            Row {
                for (j in 0 until gridSize) {
                    Box(
                        modifier = Modifier
                            .size(cellSize)
                            .border(border = BorderStroke(0.2.dp,Color.LightGray))
                            .background(
                                when (Cell(j, i)) {
                                    in snake -> Color.Green // Snake cell
                                    food -> Color.Red // Food cell
                                    else -> Color.White // Empty cell
                                }
                            )
                    )
                }
            }
        }
    }
}