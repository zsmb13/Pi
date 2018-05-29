package co.zsmb.pi

import android.content.Context
import java.io.File
import java.io.PrintWriter
import java.util.*

object ScoreKeeper {

    private const val MAX_SCORES = 10
    private const val FILENAME = "scores.txt"

    private var context: Context? = null

    val scores = mutableListOf<Int>()

    fun setContext(context: Context) {
        this.context = context
        loadList()
    }

    fun logScore(score: Int) {
        scores.add(score)
        scores.sortDescending()

        while (scores.size > MAX_SCORES) {
            scores.removeAt(scores.lastIndex)
        }

        saveList()
    }

    private fun loadList() {
        scores.clear()

        context?.apply {
            val inputFile = File(filesDir, FILENAME)
            if (!inputFile.exists()) {
                return
            }

            Scanner(openFileInput(FILENAME)).use {
                while (it.hasNextInt()) {
                    scores.add(it.nextInt())
                }
            }
        }
    }

    private fun saveList() {
        context?.apply {
            PrintWriter(openFileOutput(FILENAME, Context.MODE_PRIVATE)).use {
                for (score in scores) {
                    it.println(score)
                }
            }

        }
    }

}
