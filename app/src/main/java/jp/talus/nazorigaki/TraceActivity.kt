package jp.talus.nazorigaki

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import jp.talus.nazorigaki.data.Category
import jp.talus.nazorigaki.data.CharacterData
import jp.talus.nazorigaki.databinding.ActivityTraceBinding

class TraceActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_CATEGORY = "category"
        const val EXTRA_INDEX = "index"
    }

    private lateinit var b: ActivityTraceBinding
    private var index: Int = 0
    private lateinit var category: Category

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityTraceBinding.inflate(layoutInflater)
        setContentView(b.root)
        category = Category.fromId(intent.getStringExtra(EXTRA_CATEGORY) ?: "hiragana")
        index = intent.getIntExtra(EXTRA_INDEX, 0)

        b.clearButton.setOnClickListener { b.traceView.clear(); b.scoreText.text = "" }
        b.undoButton.setOnClickListener { b.traceView.undo() }
        b.guideSwitch.setOnCheckedChangeListener { _, checked -> b.traceView.showGuide = checked }
        b.checkButton.setOnClickListener { showScore() }
        b.prevButton.setOnClickListener { move(-1) }
        b.nextButton.setOnClickListener { move(1) }
        b.backButton.setOnClickListener { finish() }
        showCurrent()
    }

    private fun move(delta: Int) {
        val size = CharacterData.items(category).size
        index = (index + delta + size) % size
        showCurrent()
    }

    private fun showCurrent() {
        val items = CharacterData.items(category)
        val item = items[index]
        b.traceView.guideChar = item.char
        b.traceView.clear()
        b.headerChar.text = item.char
        b.readingText.text = "よみかた： ${item.reading}（${item.romaji}）"
        b.strokeText.text = "かくすう： ${item.strokes}かく ／ ${index + 1} / ${items.size}"
        b.scoreText.text = "ゆびで もじを なぞってみよう！"
    }

    private fun showScore() {
        if (!b.traceView.hasDrawn()) {
            Toast.makeText(this, "まずは なぞってみてね", Toast.LENGTH_SHORT).show(); return
        }
        val pct = b.traceView.coveragePercent()
        val msg = when {
            pct >= 80 -> "🌸 $pct%！ すごい！ とても じょうず！"
            pct >= 55 -> "⭐ $pct%！ よく なぞれているね！"
            pct >= 30 -> "👍 $pct%！ もう少し ゆっくり なぞろう"
            else -> "$pct% … お手本の上を ゆっくり なぞってみよう"
        }
        b.scoreText.text = msg
    }
}
