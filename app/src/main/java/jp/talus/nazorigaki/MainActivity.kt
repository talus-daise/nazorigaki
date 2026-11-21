package jp.talus.nazorigaki

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import jp.talus.nazorigaki.data.Category
import jp.talus.nazorigaki.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val b = ActivityMainBinding.inflate(layoutInflater)
        setContentView(b.root)

        b.cardHiragana.setOnClickListener { openList(Category.HIRAGANA) }
        b.cardKatakana.setOnClickListener { openList(Category.KATAKANA) }
        b.cardNumber.setOnClickListener { openList(Category.NUMBER) }
        b.cardKanji.setOnClickListener { openList(Category.KANJI) }
    }

    private fun openList(cat: Category) {
        startActivity(Intent(this, ListActivity::class.java).apply {
            putExtra(ListActivity.EXTRA_CATEGORY, cat.id)
        })
    }
}
