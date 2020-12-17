package com.team5.android.tictactoe

import androidx.lifecycle.ViewModel

class GameViewModel : ViewModel() {
    var matrix: Array<IntArray> = arrayOf(intArrayOf(0, 0, 0), intArrayOf(0, 0, 0), intArrayOf(0, 0, 0))
    var player1turn = true
    var gameOver = false
}