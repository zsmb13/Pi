package co.zsmb.pi

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Gravity
import android.view.MenuItem
import org.jetbrains.anko.*

class HighScoreActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.title = getString(R.string.high_scores)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        verticalLayout {
            padding = dip(16)
            verticalGravity = Gravity.CENTER

            tableLayout {
                tableRow {
                    horizontalGravity = Gravity.CENTER
                    cell("# ")
                    cell(getString(R.string.Score))
                }

                ScoreKeeper.scores.forEachIndexed { index, score ->
                    tableRow {
                        horizontalGravity = Gravity.CENTER
                        cell("${index + 1}.")
                        cell(score.toString())
                    }
                }
            }
        }
    }

    private fun _TableRow.cell(text: String) {
        textView(text) {
            padding = dip(4)
            textSize = 20f
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

}
