package com.team5.android.tictactoe

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider

// End user does not see these string literals,
// if translations needed for developers, they can/should be included in documentation
private const val KEY_PLAYER1_TURN = "player1turn"
private const val KEY_GAME_OVER = "gameOver"
private const val MATRIX_ARRAY1 = "matrixArray1"
private const val MATRIX_ARRAY2 = "matrixArray2"
private const val MATRIX_ARRAY3 = "matrixArray3"

class MainActivity : AppCompatActivity() {

    private lateinit var row1 : Array<Button>
    private lateinit var row2 : Array<Button>
    private lateinit var row3 : Array<Button>
    private lateinit var columns : Array<Array<Button>>
    private lateinit var playerTurnText: TextView

    private val gameViewModel : GameViewModel by lazy {
        ViewModelProvider(this).get(GameViewModel::class.java)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(KEY_PLAYER1_TURN, gameViewModel.player1turn)
        outState.putBoolean(KEY_GAME_OVER, gameViewModel.gameOver)

        outState.putIntArray(MATRIX_ARRAY1, gameViewModel.matrix[0])
        outState.putIntArray(MATRIX_ARRAY2, gameViewModel.matrix[1])
        outState.putIntArray(MATRIX_ARRAY3, gameViewModel.matrix[2])
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        gameViewModel.player1turn =
                savedInstanceState?.getBoolean(KEY_PLAYER1_TURN, true) ?: true

        gameViewModel.gameOver =
            savedInstanceState?.getBoolean(KEY_GAME_OVER, false) ?: false

        gameViewModel.matrix[0] =
                savedInstanceState?.getIntArray(MATRIX_ARRAY1) ?: intArrayOf(0, 0, 0)

        gameViewModel.matrix[1] =
                savedInstanceState?.getIntArray(MATRIX_ARRAY2) ?: intArrayOf(0, 0, 0)

        gameViewModel.matrix[2] =
                savedInstanceState?.getIntArray(MATRIX_ARRAY3) ?: intArrayOf(0, 0, 0)

        row1 = arrayOf(
                findViewById(R.id.button1),
                findViewById(R.id.button2),
                findViewById(R.id.button3))
        row2 = arrayOf(
                findViewById(R.id.button4),
                findViewById(R.id.button5),
                findViewById(R.id.button6))
        row3 = arrayOf(
                findViewById(R.id.button7),
                findViewById(R.id.button8),
                findViewById(R.id.button9))
        columns = arrayOf(row1, row2, row3)

        playerTurnText = findViewById(R.id.player_turn_text)

        updateUI()

        for ((i, row) in columns.withIndex()) {
            for ((j, button) in row.withIndex()) {
                button.setOnClickListener() {
                    if (!gameViewModel.gameOver) {
                        if (gameViewModel.matrix[i][j] == 0) {
                            if (gameViewModel.player1turn) {
                                gameViewModel.matrix[i][j] = 1
                            } else {
                                gameViewModel.matrix[i][j] = 2
                            }
                            gameViewModel.player1turn = !gameViewModel.player1turn
                        }
                    }
                    checkForWinner()
                    updateUI()
                }
            }
        }
    }

    private fun updateUI() {
        for ((i, row) in columns.withIndex()) {
            for ((j, button) in row.withIndex()) {
                // These strings are hardcoded because they are being considered universal symbols.
                if (gameViewModel.matrix[i][j] == 0) {
                    button.setText("")
                } else if (gameViewModel.matrix[i][j] == 1) {
                    button.setText("X")
                } else if (gameViewModel.matrix[i][j] == 2) {
                    button.setText("O")
                }
            }
        }
        // Set text to display which player's turn it is. If gameOver show "Cat's Game"
        if (!gameViewModel.gameOver) {
            val playerTurn = if (gameViewModel.player1turn) {1} else {2}
            val playerTurnStringResource = resources.getString(R.string.player_turn)
            playerTurnText.setText(playerTurnStringResource + playerTurn.toString())
        } else {
            val playerTurnStringResource = resources.getString(R.string.game_over)
            playerTurnText.setText(playerTurnStringResource)
        }


    }

    private fun checkForWinner() {
        var foundWinner = false
        for (player in 1..2) {
            //  Check horizontal
            for (i in 0..2) {
                if (gameViewModel.matrix[i][0] == player
                        && gameViewModel.matrix[i][1] == player
                        && gameViewModel.matrix[i][2] == player) {
                    foundWinner = true
                }
            }
            //  Check vertical
            for (j in 0..2) {
                if (gameViewModel.matrix[0][j] == player
                        && gameViewModel.matrix[1][j] == player
                        && gameViewModel.matrix[2][j] == player) {
                    foundWinner = true
                }
            }
            //  Check diagonal top-left to bottom-right
            if (gameViewModel.matrix[0][0] == player
                    && gameViewModel.matrix[1][1] == player
                    && gameViewModel.matrix[2][2] == player) {
                foundWinner = true
            }
            //  Check diagonal top-right to bottom-left
            if (gameViewModel.matrix[0][2] == player
                    && gameViewModel.matrix[1][1] == player
                    && gameViewModel.matrix[2][0] == player) {
                foundWinner = true
            }
            if (foundWinner) {
                val toastText = resources.getString(R.string.player_wins_start) +
                        player.toString() +
                        resources.getString(R.string.player_wins_end)
                Toast.makeText(this, toastText, Toast.LENGTH_SHORT).show()
                gameViewModel.gameOver = true
                foundWinner = false
            }
        }
        //  Check for cat's game
        if (!gameViewModel.gameOver) {
            var blankSquares = 0
            for (i in 0..2) {
                for (j in 0..2) {
                    if (gameViewModel.matrix[i][j] != 0) {
                        blankSquares++
                    }
                }
            }
            if (blankSquares == 9) {
                val toastText = resources.getString(R.string.cats_game)
                Toast.makeText(this, toastText, Toast.LENGTH_SHORT).show()
                gameViewModel.gameOver = true
            }
        }
    }
}