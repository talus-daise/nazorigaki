package jp.talus.nazorigaki.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import kotlin.math.hypot

/**
 * なぞり書き用カスタムView
 * - 背景に薄いお手本文字と田の字ガイド（点線の十字）を描く
 * - 指の軌跡を太いインクで描く
 * - お手本のピクセルをサンプリングして「どれだけなぞれているか」を採点する
 */
class TraceView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : View(context, attrs) {

    var guideChar: String = "あ"
        set(value) { field = value; rebuildGuide(); invalidate() }
    var showGuide: Boolean = true
        set(value) { field = value; invalidate() }

    private val guidePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#D9D9D9")
        textAlign = Paint.Align.CENTER
        typeface = Typeface.DEFAULT_BOLD
    }
    private val inkPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#1B2A4E")
        style = Paint.Style.STROKE
        strokeWidth = 22f
        strokeCap = Paint.Cap.ROUND
        strokeJoin = Paint.Join.ROUND
    }
    private val gridPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#B0BEC5")
        strokeWidth = 2f
        pathEffect = DashPathEffect(floatArrayOf(14f, 12f), 0f)
        style = Paint.Style.STROKE
    }
    private val borderPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#1B2A4E"); strokeWidth = 3f; style = Paint.Style.STROKE
    }

    private val paths = mutableListOf<Path>()
    private var currentPath: Path? = null
    private val drawnPoints = mutableListOf<PointF>()

    // お手本サンプル点（採点用）
    private var guidePoints: List<PointF> = emptyList()
    private val covered = mutableSetOf<Int>()

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        rebuildGuide()
    }

    private fun rebuildGuide() {
        if (width == 0 || height == 0) return
        val size = minOf(width, height).toFloat()
        guidePaint.textSize = size * 0.72f
        // オフスクリーンに文字を描いてピクセルをサンプリング
        val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val c = Canvas(bmp)
        val fm = guidePaint.fontMetrics
        val baseline = height / 2f - (fm.ascent + fm.descent) / 2f
        c.drawText(guideChar, width / 2f, baseline, guidePaint)
        val pts = mutableListOf<PointF>()
        val step = 10
        for (x in 0 until width step step) {
            for (y in 0 until height step step) {
                if (Color.alpha(bmp.getPixel(x, y)) > 30) pts.add(PointF(x.toFloat(), y.toFloat()))
            }
        }
        guidePoints = pts
        covered.clear()
        bmp.recycle()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        // 背景（白）
        canvas.drawColor(Color.WHITE)
        // 田の字ガイド
        canvas.drawRect(4f, 4f, width - 4f, height - 4f, borderPaint)
        canvas.drawLine(width / 2f, 0f, width / 2f, height.toFloat(), gridPaint)
        canvas.drawLine(0f, height / 2f, width.toFloat(), height / 2f, gridPaint)
        // お手本
        if (showGuide) {
            val fm = guidePaint.fontMetrics
            val baseline = height / 2f - (fm.ascent + fm.descent) / 2f
            canvas.drawText(guideChar, width / 2f, baseline, guidePaint)
        }
        // ユーザーの軌跡
        for (p in paths) canvas.drawPath(p, inkPaint)
        currentPath?.let { canvas.drawPath(it, inkPaint) }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x; val y = event.y
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                currentPath = Path().apply { moveTo(x, y) }
                addPoint(x, y)
            }
            MotionEvent.ACTION_MOVE -> {
                currentPath?.lineTo(x, y)
                addPoint(x, y)
                invalidate()
            }
            MotionEvent.ACTION_UP -> {
                currentPath?.let { paths.add(it) }
                currentPath = null
                invalidate()
                performClick()
            }
        }
        return true
    }

    private fun addPoint(x: Float, y: Float) {
        drawnPoints.add(PointF(x, y))
        // 近くのお手本点をカバー済みにする（半径36px）
        guidePoints.forEachIndexed { i, gp ->
            if (hypot((gp.x - x).toDouble(), (gp.y - y).toDouble()) < 38) covered.add(i)
        }
    }

    override fun performClick(): Boolean { super.performClick(); return true }

    fun clear() { paths.clear(); drawnPoints.clear(); covered.clear(); invalidate() }
    fun undo() { if (paths.isNotEmpty()) { paths.removeAt(paths.lastIndex); invalidate() } }

    /** 0〜100 のなぞり率。お手本点のうちカバーできた割合 */
    fun coveragePercent(): Int {
        if (guidePoints.isEmpty()) return 0
        return (covered.size * 100 / guidePoints.size).coerceIn(0, 100)
    }
    fun hasDrawn(): Boolean = paths.isNotEmpty() || drawnPoints.isNotEmpty()
}
