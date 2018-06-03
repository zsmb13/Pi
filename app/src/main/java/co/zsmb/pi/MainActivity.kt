package co.zsmb.pi

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk15.listeners.onClick
import kotlin.LazyThreadSafetyMode.NONE

class MainActivity : AppCompatActivity() {

    private val KEY_INDEX_REACHED = "KEY_INDEX_REACHED"
    private val digitsToShow = 10
    private val pi: String by lazy(NONE) { getString(R.string.pi) }

    private var indexReached = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ScoreKeeper.setContext(applicationContext)

        setupButtons()

        val restoreIndex = savedInstanceState?.getInt(KEY_INDEX_REACHED) ?: 0
        resetState(restoreIndex)
    }

    private fun setupButtons() {
        val buttons = arrayOf(btn0, btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9)

        fun Int.toCharValue() = this.toChar() + '0'.toInt()

        buttons.forEachIndexed { index, button ->
            button.onClick { inputDigit(index.toCharValue()) }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun inputDigit(digit: Char) {
        val indexToTest = indexReached + 1
        val correctDigit = pi[indexToTest]

        if (digit == correctDigit) {
            indexReached++
            tvPi.append(digit.toString())
            tvDigitCount.text = indexReached.toString()
            scrollView.fullScroll(View.FOCUS_DOWN)
        }
        else {
            gameOver(digit, correctDigit)
        }
    }

    private fun resetState(currentIndex: Int = 0) {
        indexReached = currentIndex
        tvDigitCount.text = currentIndex.toString()
        val piText = "3" + pi.substring(0, currentIndex + 1)
        tvPi.text = piText
    }

    private fun gameOver(digit: Char, correctDigit: Char) {
        ScoreKeeper.logScore(indexReached)

        alert {
            customView {
                verticalLayout {
                    padding = dip(24)

                    textView {
                        text = getString(R.string.decimal_reached, indexReached)
                        textSize = 16f
                    }
                    textView {
                        text = getString(R.string.digit_info, digit, correctDigit)
                        textSize = 16f
                    }
                    textView {
                        val nextDigit = indexReached + 1
                        text = getString(R.string.next_digits, digitsToShow,
                                pi.substring(nextDigit, nextDigit + digitsToShow))
                        textSize = 16f
                    }
                }

                positiveButton(getString(R.string.OK)) { resetState() }
                onCancelled { resetState() }
            }
        }.show()

        indexReached = 0
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_high_scores -> startActivity(Intent(this, HighScoreActivity::class.java))
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        outState?.putInt(KEY_INDEX_REACHED, indexReached)
        super.onSaveInstanceState(outState)
    }

}
