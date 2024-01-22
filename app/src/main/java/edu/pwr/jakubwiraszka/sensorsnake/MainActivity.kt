package edu.pwr.jakubwiraszka.sensorsnake

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.dp
import edu.pwr.jakubwiraszka.sensorsnake.composables.Game
import edu.pwr.jakubwiraszka.sensorsnake.composables.Options
import edu.pwr.jakubwiraszka.sensorsnake.domain.ControlType
import edu.pwr.jakubwiraszka.sensorsnake.domain.Difficulty
import edu.pwr.jakubwiraszka.sensorsnake.domain.View
import edu.pwr.jakubwiraszka.sensorsnake.ui.theme.SensorSnakeTheme

class MainActivity : ComponentActivity(), SensorEventListener {
    private lateinit var sensorManager: SensorManager
    private var accelerometer: Sensor? = null
    private val sensorPosition = mutableStateOf(Offset(0f, 0f))
    private var sensitivity = mutableFloatStateOf(2f)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME)

        setContent {
            SensorSnakeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    var minSensorAngle by remember { mutableFloatStateOf(0f) }
                    var difficulty by remember { mutableStateOf(Difficulty.MEDIUM) }
                    var controlType by remember { mutableStateOf(ControlType.SENSOR) }
                    var view by remember { mutableStateOf(View.MAIN) }

                    when (view) {
                        View.OPTIONS -> Options(
                            controlType = controlType,
                            onControlType = { controlType = it },
                            difficulty = difficulty,
                            onDifficulty = { difficulty = it },
                            sensitivity = sensitivity.floatValue,
                            onSensitivity = { sensitivity.floatValue = it },
                            minSensorAngle = minSensorAngle,
                            onMinSensorAngle = { minSensorAngle = it }
                        ) { view = View.MAIN }

                        View.GAME -> Game(
                            sensorPosition = sensorPosition.value,
                            difficulty = difficulty,
                            minSensorAngle = minSensorAngle,
                            controlType = controlType
                        ) { view = View.MAIN }

                        else -> {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.SpaceEvenly,
                                modifier = Modifier.fillMaxSize()
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                                    Text(text = "Snake", style = MaterialTheme.typography.titleMedium)
                                    Text(text = "by Jakub Wiraszka", style = MaterialTheme.typography.bodySmall)
                                    Spacer(modifier = Modifier.height(10.dp))
                                    Text(text = "Use device tilt or buttons to control the snake. Change in Options.", style=MaterialTheme.typography.titleSmall)
                                }
                                Button(onClick = { view = View.GAME }) {
                                    Text(text = "Play")
                                }
                                Button(onClick = { view = View.OPTIONS }) {
                                    Text(text = "Options")
                                }
                            }

                        }
                    }
                }
            }
        }
    }

    override fun onSensorChanged(p0: SensorEvent) {
        if (p0.sensor.type != Sensor.TYPE_ACCELEROMETER) return

        val xChange = -p0.values[0] * sensitivity.floatValue
        val yChange = -p0.values[1] * sensitivity.floatValue

        sensorPosition.value = Offset(xChange, yChange)
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {}

    override fun onResume() {
        super.onResume()
        accelerometer?.also { acc ->
            sensorManager.registerListener(this, acc, SensorManager.SENSOR_DELAY_GAME)
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }
}