package jp.talus.nazorigaki.data

import java.io.Serializable

enum class Category(val id: String, val title: String, val subtitle: String, val emojiLabel: String) {
    HIRAGANA("hiragana", "ひらがな", "あいうえお から 46もじ", "あ"),
    KATAKANA("katakana", "カタカナ", "アイウエオ から 46もじ", "ア"),
    NUMBER("number", "すうじ", "0 から 10 まで", "1"),
    KANJI("kanji", "かんたん かんじ", "小学1年生レベル 40じ", "山");

    companion object {
        fun fromId(id: String): Category = entries.first { it.id == id }
    }
}

data class KanaItem(
    val char: String,
    val reading: String,   // ひらがな読み
    val romaji: String,    // ローマ字 / 意味
    val strokes: Int,      // 画数（目安）
    val categoryId: String
) : Serializable

object CharacterData {

    fun items(category: Category): List<KanaItem> = when (category) {
        Category.HIRAGANA -> hiragana
        Category.KATAKANA -> katakana
        Category.NUMBER -> numbers
        Category.KANJI -> kanji
    }

    private fun kana(list: List<Triple<String, String, String>>, strokes: List<Int>, cat: String) =
        list.mapIndexed { i, (c, r, roma) -> KanaItem(c, r, roma, strokes.getOrElse(i) { 2 }, cat) }

    // ---- ひらがな 46文字 ----
    private val hiragana = kana(
        listOf(
            Triple("あ","あ","a"), Triple("い","い","i"), Triple("う","う","u"), Triple("え","え","e"), Triple("お","お","o"),
            Triple("か","か","ka"), Triple("き","き","ki"), Triple("く","く","ku"), Triple("け","け","ke"), Triple("こ","こ","ko"),
            Triple("さ","さ","sa"), Triple("し","し","shi"), Triple("す","す","su"), Triple("せ","せ","se"), Triple("そ","そ","so"),
            Triple("た","た","ta"), Triple("ち","ち","chi"), Triple("つ","つ","tsu"), Triple("て","て","te"), Triple("と","と","to"),
            Triple("な","な","na"), Triple("に","に","ni"), Triple("ぬ","ぬ","nu"), Triple("ね","ね","ne"), Triple("の","の","no"),
            Triple("は","は","ha"), Triple("ひ","ひ","hi"), Triple("ふ","ふ","fu"), Triple("へ","へ","he"), Triple("ほ","ほ","ho"),
            Triple("ま","ま","ma"), Triple("み","み","mi"), Triple("む","む","mu"), Triple("め","め","me"), Triple("も","も","mo"),
            Triple("や","や","ya"), Triple("ゆ","ゆ","yu"), Triple("よ","よ","yo"),
            Triple("ら","ら","ra"), Triple("り","り","ri"), Triple("る","る","ru"), Triple("れ","れ","re"), Triple("ろ","ろ","ro"),
            Triple("わ","わ","wa"), Triple("を","を","wo"), Triple("ん","ん","n"),
        ),
        listOf(3,2,2,2,3, 3,3,1,3,2, 3,1,2,3,1, 3,3,1,1,2, 2,3,2,3,1, 3,1,2,1,4, 2,2,1,2,3, 2,2,3, 2,2,1,2,1, 3,2,1),
        "hiragana"
    )

    // ---- カタカナ 46文字 ----
    private val katakana = kana(
        listOf(
            Triple("ア","あ","a"), Triple("イ","い","i"), Triple("ウ","う","u"), Triple("エ","え","e"), Triple("オ","お","o"),
            Triple("カ","か","ka"), Triple("キ","き","ki"), Triple("ク","く","ku"), Triple("ケ","け","ke"), Triple("コ","こ","ko"),
            Triple("サ","さ","sa"), Triple("シ","し","shi"), Triple("ス","す","su"), Triple("セ","せ","se"), Triple("ソ","そ","so"),
            Triple("タ","た","ta"), Triple("チ","ち","chi"), Triple("ツ","つ","tsu"), Triple("テ","て","te"), Triple("ト","と","to"),
            Triple("ナ","な","na"), Triple("ニ","に","ni"), Triple("ヌ","ぬ","nu"), Triple("ネ","ね","ne"), Triple("ノ","の","no"),
            Triple("ハ","は","ha"), Triple("ヒ","ひ","hi"), Triple("フ","ふ","fu"), Triple("ヘ","へ","he"), Triple("ホ","ほ","ho"),
            Triple("マ","ま","ma"), Triple("ミ","み","mi"), Triple("ム","む","mu"), Triple("メ","め","me"), Triple("モ","も","mo"),
            Triple("ヤ","や","ya"), Triple("ユ","ゆ","yu"), Triple("ヨ","よ","yo"),
            Triple("ラ","ら","ra"), Triple("リ","り","ri"), Triple("ル","る","ru"), Triple("レ","れ","re"), Triple("ロ","ろ","ro"),
            Triple("ワ","わ","wa"), Triple("ヲ","を","wo"), Triple("ン","ん","n"),
        ),
        listOf(2,2,3,3,3, 2,3,2,3,2, 3,3,2,2,2, 3,3,3,3,1, 2,2,2,4,1, 2,2,1,1,4, 2,3,2,2,3, 2,2,3, 2,2,2,1,3, 2,2,2),
        "katakana"
    )

    // ---- 数字 0-10 ----
    private val numbers = listOf(
        KanaItem("0","れい","zero",1,"number"), KanaItem("1","いち","one",1,"number"),
        KanaItem("2","に","two",1,"number"), KanaItem("3","さん","three",1,"number"),
        KanaItem("4","よん","four",1,"number"), KanaItem("5","ご","five",1,"number"),
        KanaItem("6","ろく","six",1,"number"), KanaItem("7","なな","seven",1,"number"),
        KanaItem("8","はち","eight",1,"number"), KanaItem("9","きゅう","nine",1,"number"),
        KanaItem("10","じゅう","ten",2,"number"),
    )

    // ---- 簡単な漢字（小学1年配当中心）40字 ----
    private val kanji = listOf(
        KanaItem("一","いち","ひとつ",1,"kanji"), KanaItem("二","に","ふたつ",2,"kanji"),
        KanaItem("三","さん","みっつ",3,"kanji"), KanaItem("四","よん","よっつ",5,"kanji"),
        KanaItem("五","ご","いつつ",4,"kanji"), KanaItem("六","ろく","むっつ",4,"kanji"),
        KanaItem("七","なな","ななつ",2,"kanji"), KanaItem("八","はち","やっつ",2,"kanji"),
        KanaItem("九","きゅう","ここのつ",2,"kanji"), KanaItem("十","じゅう","とお",2,"kanji"),
        KanaItem("日","ひ","にち",4,"kanji"), KanaItem("月","つき","げつ",4,"kanji"),
        KanaItem("火","ひ","か",4,"kanji"), KanaItem("水","みず","すい",4,"kanji"),
        KanaItem("木","き","もく",4,"kanji"), KanaItem("金","かね","きん",8,"kanji"),
        KanaItem("土","つち","ど",3,"kanji"), KanaItem("山","やま","さん",3,"kanji"),
        KanaItem("川","かわ","せん",3,"kanji"), KanaItem("田","た","でん",5,"kanji"),
        KanaItem("雨","あめ","う",8,"kanji"), KanaItem("空","そら","くう",8,"kanji"),
        KanaItem("花","はな","か",7,"kanji"), KanaItem("林","はやし","りん",8,"kanji"),
        KanaItem("人","ひと","じん",2,"kanji"), KanaItem("口","くち","こう",3,"kanji"),
        KanaItem("目","め","もく",5,"kanji"), KanaItem("耳","みみ","じ",6,"kanji"),
        KanaItem("手","て","しゅ",4,"kanji"), KanaItem("足","あし","そく",7,"kanji"),
        KanaItem("力","ちから","りょく",2,"kanji"), KanaItem("大","おおきい","だい",3,"kanji"),
        KanaItem("小","ちいさい","しょう",3,"kanji"), KanaItem("中","なか","ちゅう",4,"kanji"),
        KanaItem("上","うえ","じょう",3,"kanji"), KanaItem("下","した","げ",3,"kanji"),
        KanaItem("左","ひだり","さ",5,"kanji"), KanaItem("右","みぎ","う",5,"kanji"),
        KanaItem("円","えん","まるい",4,"kanji"), KanaItem("王","おう","おおきみ",4,"kanji"),
    )
}
