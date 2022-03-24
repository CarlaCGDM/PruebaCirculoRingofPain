import com.soywiz.klogger.AnsiEscape
import com.soywiz.korge.*
import com.soywiz.korge.input.onClick
import com.soywiz.korge.view.*
import com.soywiz.korge.view.tween.hide
import com.soywiz.korge.view.tween.show
import com.soywiz.korim.color.Colors
import com.soywiz.korim.color.RGBA
import com.soywiz.korim.font.readBitmapFont
import com.soywiz.korim.format.readBitmap
import com.soywiz.korim.paint.DefaultPaint
import com.soywiz.korim.paint.Paint
import com.soywiz.korim.text.TextAlignment
import com.soywiz.korio.file.std.resourcesVfs
import com.soywiz.korma.geom.Rectangle
import com.soywiz.korma.geom.vector.roundRect
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.log
import kotlin.math.sin

suspend fun main() = Korge(
	width = 480,
	height = 640,
	title="2048",
	bgcolor = RGBA(253, 247, 240)
) {
	/* val font = resourcesVfs["clear_sans.fnt"].readBitmapFont()
	val restartImg = resourcesVfs["restart.png"].readBitmap()
	val undoImg = resourcesVfs["undo.png"].readBitmap()
	val cellSize = views.virtualWidth / 5.0
	val fieldSize = 50 + 4 * cellSize
	val leftIndent = (views.virtualWidth - fieldSize) / 2
	val topIndent = 150.0
	val bgField = roundRect(fieldSize, fieldSize, 5.0, fill = Colors["#b9aea0"]) {
		position(leftIndent, topIndent)
	}
	val bgLogo = roundRect(cellSize, cellSize, 5.0, fill = Colors["#edc403"]) {
		position(leftIndent, 30.0)
	}
	val bgBest = roundRect(cellSize * 1.5, cellSize * 0.8, 5.0, fill = Colors["#bbae9e"]) {
		alignRightToRightOf(bgField)
		alignTopToTopOf(bgLogo)
	}
	val bgScore = roundRect(cellSize * 1.5, cellSize * 0.8, 5.0, fill = Colors["#bbae9e"]) {
		alignRightToLeftOf(bgBest, 24)
		alignTopToTopOf(bgBest)
	}
	text("2048", cellSize * 0.5, Colors.WHITE, font).centerOn(bgLogo)
	text("BEST", cellSize * 0.25, RGBA(239, 226, 210), font) {
		centerXOn(bgBest)
		alignTopToTopOf(bgBest, 5.0)
	}
	text("0", cellSize * 0.5, Colors.WHITE, font) {
		setTextBounds(Rectangle(0.0, 0.0, bgBest.width, cellSize - 24.0))
		alignment = TextAlignment.MIDDLE_CENTER
		alignTopToTopOf(bgBest, 12.0)
		centerXOn(bgBest)
	}
	text("SCORE", cellSize * 0.25, RGBA(239, 226, 210), font) {
		centerXOn(bgScore)
		alignTopToTopOf(bgScore, 5.0)
	}
	text("0", cellSize * 0.5, Colors.WHITE, font) {
		setTextBounds(Rectangle(0.0, 0.0, bgScore.width, cellSize - 24.0))
		alignment = TextAlignment.MIDDLE_CENTER
		centerXOn(bgScore)
		alignTopToTopOf(bgScore, 12.0)
	}
	graphics {
		position(leftIndent, topIndent)
		fill(Colors["#cec0b2"]) {
			for (i in 0..3) {
				for (j in 0..3) {
					roundRect(10 + (10 + cellSize) * i, 10 + (10 + cellSize) * j, cellSize, cellSize, 5.0)
				}
			}
		}
	}


*/


	//-------------------------------------------------------------------------------------------

	fun pointsOnCircle(points: MutableList<Int>, r: Int, center: Pair<Int, Int>) {
		val num = points.size - 2
		val angle = 315.0 / num * PI / 180
		val rotacion = if (num % 2 == 0) PI / 2 else PI / 2 + angle / 2
		val perspectiva = 4
		graphics {
			fill(Colors["#b181ce"]) {
				circle(log(20.0 * points.size, 5.0)).center()
					.position(center.first - r / 7, center.second + r / perspectiva)
				circle(log(20.0 * points.size, 5.0)).center()
					.position(center.first + r / 7, center.second + r / perspectiva)
				text("${points.removeFirst()}").position(center.first - r / 7, center.second + r / perspectiva)
				text("${points.removeFirst()}").position(center.first + r / 7, center.second + r / perspectiva)
				for (i in num.downTo(1)) {
					if (i % 2 != 0) {
						var posx = center.first + r * -cos(angle * i / 2 - rotacion)
						var posy = center.second + r * sin(angle * i / 2 - rotacion) / perspectiva
						circle(log(20.0 * i, 5.0)).center().position(posx, posy)
						text(points.removeLast().toString()).position(posx, posy)
					} else {
						var posx = center.first + r * -cos(-angle * (i - 1) / 2 - rotacion)
						var posy = center.second + r * sin(-angle * (i - 1) / 2 - rotacion) / perspectiva
						circle(log(20.0 * i, 5.0)).center().position(posx, posy)
						text(points.removeFirst().toString()).position(posx, posy)
					}
				}
			}
		}
	}

	fun rotarCartasRight(cartas: MutableList<Int>): MutableList<Int> {
		var nuevasCartas = mutableListOf<Int>()
		nuevasCartas.add(cartas.removeLast())
		nuevasCartas += cartas
		return nuevasCartas
	}

	fun rotarCartasLeft(cartas: MutableList<Int>): MutableList<Int> {
		var nuevasCartas = mutableListOf<Int>()
		var primero = cartas.removeFirst()
		nuevasCartas.add(cartas.removeFirst())
		nuevasCartas += cartas
		nuevasCartas.add(primero)
		return nuevasCartas
	}

	fun generarRing(cartas: MutableList<Int>):Container {
		var contenedor = container {  }
		var cards = mutableListOf<Int>()
		cards += cartas
		val r = views.virtualWidth / 3.5
		val angle = 315 / cards.size * PI / 180
		val rot = if (cards.size % 2 == 0) PI / 2 else PI / 2 + angle / 2
		val persp = 4
		val center = Pair(views.virtualWidth / 2, views.virtualHeight / 2)
		graphics {
			roundRect(90.0, 130.0, 2.0,2.0,fill=Colors["#ce4357"],stroke=Colors.BLACK, strokeThickness = 2.0).center().position(center.first - r / 3, center.second + r / persp)
			roundRect(90.0, 130.0, 2.0,2.0,fill=Colors["#ce4357"],stroke=Colors.BLACK, strokeThickness = 2.0).center().position(center.first + r / 3, center.second + r / persp)
			text(cards.removeFirst().toString(), alignment = TextAlignment.MIDDLE_CENTER).position(
				center.first - r / 3,
				center.second + r / persp
			)
			text(cards.removeFirst().toString(), alignment = TextAlignment.MIDDLE_CENTER).position(
				center.first + r / 3,
				center.second + r / persp
			)
			for (i in cards.size.downTo(1)) {
				val content: Int
				val x: Double
				val y: Double
				val xSize = 90 * 0.90
				val ySize = 130 * 0.90
				if (i % 2 != 0) {
					x = center.first + r * -cos(angle * i / 2 - rot)
					y = center.second + r * sin(angle * i / 2 - rot) / persp
					content = cards.removeLast()
				} else {
					x = center.first + r * -cos(-angle * (i - 1) / 2 - rot)
					y = center.second + r * sin(-angle * (i - 1) / 2 - rot) / persp
					content = cards.removeFirst()
				}
				var newText = Text(content.toString(), alignment = TextAlignment.MIDDLE_CENTER).position(x, y)
				var newCard = RoundRect(xSize, ySize, 2.0,2.0,fill=Colors["#8b93cd"],stroke=Colors.BLACK, strokeThickness = 2.0).center().position(x, y)
				contenedor.addChildAt(newText,0)
				contenedor.addChildAt(newCard,0)
			}
		}
		return contenedor
	}


	//pointsOnCircle((1..15).toList() as MutableList<Int>,200, Pair(250,325))
	var cartas = (1..5).toMutableList()
	cartas = rotarCartasLeft(cartas)
	println(cartas)


	var contenedor = generarRing(cartas)

	println("se generó el anillo de la lista $cartas")
	graphics {
		text(">", alignment = TextAlignment.MIDDLE_CENTER, textSize = 60.0).position(
			views.virtualWidth / 2 + 90 + 20,
			(views.virtualHeight / 2 + views.virtualWidth / 3.5 / 4).toInt()
		).onClick {
			contenedor.removeChildren()
			cartas = rotarCartasRight(cartas)
			contenedor = container {generarRing(cartas)}
		}
		text("<", alignment = TextAlignment.MIDDLE_CENTER, textSize = 60.0).position(
			views.virtualWidth / 2 - 90 - 25,
			(views.virtualHeight / 2 + views.virtualWidth / 3.5 / 4).toInt()
		).onClick {
			cartas = rotarCartasLeft(cartas)
			generarRing(cartas)
		}


	}
}
