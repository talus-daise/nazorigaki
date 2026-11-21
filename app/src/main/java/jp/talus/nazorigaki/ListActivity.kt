package jp.talus.nazorigaki

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import jp.talus.nazorigaki.data.Category
import jp.talus.nazorigaki.data.CharacterData
import jp.talus.nazorigaki.data.KanaItem
import jp.talus.nazorigaki.databinding.ActivityListBinding

class ListActivity : AppCompatActivity() {
    companion object { const val EXTRA_CATEGORY = "category" }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val b = ActivityListBinding.inflate(layoutInflater)
        setContentView(b.root)
        val cat = Category.fromId(intent.getStringExtra(EXTRA_CATEGORY) ?: "hiragana")
        b.titleText.text = cat.title
        b.subtitleText.text = "すきな もじを タップしてね（ぜんぶで ${CharacterData.items(cat).size}もじ）"
        val items = CharacterData.items(cat)
        b.grid.adapter = CharAdapter(items)
        b.grid.setOnItemClickListener { _, _, pos, _ ->
            startActivity(Intent(this, TraceActivity::class.java).apply {
                putExtra(TraceActivity.EXTRA_CATEGORY, cat.id)
                putExtra(TraceActivity.EXTRA_INDEX, pos)
            })
        }
        b.backButton.setOnClickListener { finish() }
    }

    private inner class CharAdapter(private val items: List<KanaItem>) : BaseAdapter() {
        override fun getCount() = items.size
        override fun getItem(pos: Int) = items[pos]
        override fun getItemId(pos: Int) = pos.toLong()
        override fun getView(pos: Int, convertView: View?, parent: ViewGroup?): View {
            val v = convertView ?: LayoutInflater.from(this@ListActivity)
                .inflate(R.layout.item_character, parent, false)
            val item = items[pos]
            v.findViewById<TextView>(R.id.charText).text = item.char
            v.findViewById<TextView>(R.id.readingText).text = item.reading
            return v
        }
    }
}
