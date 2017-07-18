package co.zsmb.pi

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.content_main.*
import org.jetbrains.anko.*
import kotlin.LazyThreadSafetyMode.NONE

class MainActivity : AppCompatActivity() {

    private val KEY_INDEX_REACHED = "KEY_INDEX_REACHED"
    private val digitsToShow = 10
    private val pi: String by lazy(NONE) { getString(R.string.pi) }

    private var dialogVisible = false
    private var indexReached = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        ScoreKeeper.setContext(applicationContext)

        setupButtons()

        val restoreIndex = savedInstanceState?.getInt(KEY_INDEX_REACHED) ?: 0
        resetState(restoreIndex)
    }

    private fun setupButtons(): Unit {
        btn0.onClick { inputDigit('0') }
        btn1.onClick { inputDigit('1') }
        btn2.onClick { inputDigit('2') }
        btn3.onClick { inputDigit('3') }
        btn4.onClick { inputDigit('4') }
        btn5.onClick { inputDigit('5') }
        btn6.onClick { inputDigit('6') }
        btn7.onClick { inputDigit('7') }
        btn8.onClick { inputDigit('8') }
        btn9.onClick { inputDigit('9') }
    }

    private fun inputDigit(digit: Char) {
        val indexToTest = indexReached + 1
        val correctDigit = pi[indexToTest]

        if (pi[indexToTest] == digit) {
            indexReached++
            tvPi.append(digit.toString())
            tvDigitCount.text = indexReached.toString()
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

        dialogVisible = false
    }

    private fun gameOver(digit: Char, correctDigit: Char) {
        ScoreKeeper.addScore(indexReached)

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
                onCancel { resetState() }
            }
        }.show()

        dialogVisible = true
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
        if (!dialogVisible) {
            outState?.putInt(KEY_INDEX_REACHED, indexReached)
        }
        super.onSaveInstanceState(outState)
    }

}
