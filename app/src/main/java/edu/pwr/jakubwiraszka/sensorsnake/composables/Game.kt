package edu.pwr.jakubwiraszka.sensorsnake.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import edu.pwr.jakubwiraszka.sensorsnake.domain.Cell
import edu.pwr.jakubwiraszka.sensorsnake.domain.ControlType
import edu.pwr.jakubwiraszka.sensorsnake.domain.Difficulty
import edu.pwr.jakubwiraszka.sensorsnake.domain.Direction
import kotlinx.coroutines.delay
import kotlin.random.Random

@Composable
fun Game(
    sensorPosition: Offset,
    difficulty: Difficulty,
    minSensorAngle: Float,
    controlType: ControlType,
    onBack: () -> Unit
) {
    val gridSize = 25 // Size of the game grid
    // Game state variables
    var direction by remember { mutableStateOf(Direction.RIGHT) } // Initial direction of the snake
    var snake by remember { mutableStateOf(listOf(Cell(5, 5))) } // Initial position of the snake
    var food by remember {
        mutableStateOf(
            generateFood(
                snake,
                gridSize
            )
        )
    } // Initial position of the food
    var isGameOver by remember { mutableStateOf(false) } // Game over state
    var gameId by remember { mutableIntStateOf(0) } // Game id used to restart the game
    var lastMoveTime by remember { mutableLongStateOf(0L) }

    LaunchedEffect(gameId) {
        while (!isGameOver) {
            val gameSpeed = getGameSpeed(difficulty)
            val snakeSpeed = getSnakeSpeed(difficulty)
            delay(gameSpeed) // Delay between game frames
            // Only move the snake if enough time has passed since the last move
            if (System.currentTimeMillis() - lastMoveTime >= snakeSpeed) {
                snake = moveSnake(snake, direction) // Move the snake in the current direction
                lastMoveTime = System.currentTimeMillis() // Update the last move time
            }
            if (snake.first() == food) {
                // Snake ate the food, generate new food and grow the snake
                food = generateFood(snake, gridSize)
                snake = growSnake(snake, direction, gridSize)
            }
            isGameOver = checkGameOver(snake, gridSize) // Check if the game is over
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        if (isGameOver) {
            // Game over screen
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = "Game Over!", color = Color.Red)
                Button(onClick = {
                    // Restart the game
                    snake = listOf(Cell(5, 5))
                    direction = Direction.RIGHT
                    food = generateFood(snake, gridSize)
                    isGameOver = false
                    gameId++ // Increment gameId to trigger a game restart
                    onBack()
                }) {
                    Text("Restart")
                }
            }
        } else {
            // Game board
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Grid(snake = snake, food = food, gridSize = gridSize)
                Spacer(modifier = Modifier.height(16.dp))
                Controls(
                    currentDirection = direction,
                    sensorPosition = sensorPosition,
                    isAccelerometer = controlType == ControlType.SENSOR,
                    minSensorAngle = minSensorAngle,
                    onDirectionChange = { direction = it }
                )
            }
            Column(
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize()
            ) {
                Button(onClick = onBack) {
                    Text(text = "Back")
                }
            }
        }
    }
}

fun moveSnake(snake: List<Cell>, direction: Direction): List<Cell> {
    val head = snake.first()
    val newHead = when (direction) {
        Direction.UP -> Cell(head.x, head.y - 1)
        Direction.DOWN -> Cell(head.x, head.y + 1)
        Direction.LEFT -> Cell(head.x - 1, head.y)
        Direction.RIGHT -> Cell(head.x + 1, head.y)
    }
    val newSnake = snake.toMutableList()
    newSnake.add(0, newHead)
    newSnake.removeAt(newSnake.size - 1)
    return newSnake
}

// Generates a new food cell not occupied by the snake
fun generateFood(snake: List<Cell>, gridSize: Int): Cell {
    val emptyCells = (0 until gridSize).flatMap { x ->
        (0 until gridSize).map { y -> Cell(x, y) }
    }.filter { it !in snake }
    return emptyCells[Random.nextInt(emptyCells.size)]
}

// Grows the snake in the given direction and returns the new snake
fun growSnake(snake: List<Cell>, direction: Direction, gridSize: Int): List<Cell> {
    val growth = when (direction) {
        Direction.UP -> Cell(snake.first().x, (snake.first().y - 1 + gridSize) % gridSize)
        Direction.DOWN -> Cell(snake.first().x, (snake.first().y + 1) % gridSize)
        Direction.LEFT -> Cell((snake.first().x - 1 + gridSize) % gridSize, snake.first().y)
        Direction.RIGHT -> Cell((snake.first().x + 1) % gridSize, snake.first().y)
    }
    return listOf(growth) + snake
}

// Checks if the game is over
fun checkGameOver(snake: List<Cell>, gridSize: Int): Boolean {
    val head = snake.first()
    return head in snake.drop(1) || head.x < 0 || head.y < 0 || head.x >= gridSize || head.y >= gridSize
}

fun getSnakeSpeed(difficulty: Difficulty): Long {
    return when (difficulty) {
        Difficulty.EASY -> 200L
        Difficulty.MEDIUM -> 250L
        Difficulty.HARD -> 300L
    }
}

fun getGameSpeed(difficulty: Difficulty): Long {
    return when (difficulty) {
        Difficulty.EASY -> 100L
        Difficulty.MEDIUM -> 50L
        Difficulty.HARD -> 30L
    }
}