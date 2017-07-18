package co.zsmb.pi

import android.content.Context
import java.io.File
import java.io.PrintWriter
import java.util.*

object ScoreKeeper {

    private val max = 10
    private val fileName = "scores.txt"

    private var context: Context? = null
    private val list: MutableList<Int>

    val scores: IntArray
        get() = list.toIntArray()

    init {
        list = mutableListOf<Int>()
    }

    fun setContext(context: Context) {
        this.context = context
        loadList()
    }

    private fun logScore(score: Int) {
        list.add(score)
        list.sortDescending()

        while (list.size > max) {
            list.removeAt(list.size - 1)
        }

        saveList()
    }

    fun addScore(score: Int) {
        if (list.size < max) {
            logScore(score)
            return
        }

        val minScore = list.min() ?: return

        if (score > minScore) {
            logScore(score)
        }
    }

    private fun loadList() {
        list.clear()

        context?.apply {

            val inputFile = File(filesDir, fileName)
            if (!inputFile.exists()) {
                return
            }

            Scanner(openFileInput(fileName)).use {
                while (it.hasNextInt()) {
                    list.add(it.nextInt())
                }
            }

        }
    }

    private fun saveList() {
        context?.apply {

            PrintWriter(openFileOutput(fileName, Context.MODE_PRIVATE)).use {
                for (score in list) {
                    it.println(score)
                }
            }

        }
    }

}
