package edu.pwr.jakubwiraszka.sensorsnake.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import edu.pwr.jakubwiraszka.sensorsnake.domain.ControlType
import edu.pwr.jakubwiraszka.sensorsnake.domain.Difficulty

@Composable
fun Options(
    controlType: ControlType,
    onControlType: (ControlType) -> Unit,
    difficulty: Difficulty,
    onDifficulty: (Difficulty) -> Unit,
    sensitivity: Float,
    onSensitivity: (Float) -> Unit,
    minSensorAngle: Float,
    onMinSensorAngle: (Float) -> Unit,
    onBack: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(text = "Difficulty")
        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
            Button(
                onClick = { onDifficulty(Difficulty.EASY) },
                enabled = difficulty != Difficulty.EASY
            ) {
                Text(text = "EASY")
            }
            Button(
                onClick = { onDifficulty(Difficulty.MEDIUM) },
                enabled = difficulty != Difficulty.MEDIUM
            ) {
                Text(text = "MEDIUM")
            }
            Button(
                onClick = { onDifficulty(Difficulty.HARD) },
                enabled = difficulty != Difficulty.HARD
            ) {
                Text(text = "HARD")
            }
        }
        Spacer(modifier = Modifier.fillMaxWidth())

        Text(text = "Controls")
        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
            Button(
                onClick = { onControlType(ControlType.BUTTONS) },
                enabled = controlType != ControlType.BUTTONS
            ) {
                Text(text = "BUTTONS")
            }
            Button(
                onClick = { onControlType(ControlType.SENSOR) },
                enabled = controlType != ControlType.SENSOR
            ) {
                Text(text = "SENSOR")
            }
        }

        Spacer(modifier = Modifier.fillMaxWidth())
        Text(text = "Sensor Options")
        TextField(
            label = { Text("Sensitivity") },
            value = sensitivity.toString(),
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            onValueChange = {
                onSensitivity(it.toFloat())
            }
        )

        TextField(
            label = { Text("Minimum Angle") },
            value = minSensorAngle.toString(),
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            onValueChange = {
                onMinSensorAngle(it.toFloat())
            }
        )
        Spacer(modifier = Modifier.weight(1f))
        Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
            Button(onClick = onBack) {
                Text(text = "Back")
            }
        }
    }

}