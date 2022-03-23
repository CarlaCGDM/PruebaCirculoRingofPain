import com.soywiz.korge.*
import com.soywiz.korge.view.*
import com.soywiz.korim.color.Colors
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.log
import kotlin.math.sin

suspend fun main() = Korge(width = 500, height = 650, bgcolor = Colors["#2b2b2b"]) {
	/* val cellSize = views.virtualWidth / 5.0
	val fieldSize = 50 + 4 * cellSize
	val leftIndent = (views.virtualWidth - fieldSize) / 2
	val topIndent = 150.0
	val bgField = roundRect(fieldSize, fieldSize, 5.0, fill = Colors["#b9aea0"]).position(leftIndent,topIndent)
	graphics {
		position(leftIndent, topIndent)
		fill(Colors["#cec0b2"]) {
			for (i in 0..3) {
				for (j in 0..3) {
					roundRect(10 + (10 + cellSize) * i, 10 + (10 + cellSize) * j, cellSize, cellSize, 5.0)
				}
			}
		}
	} */

	fun pointsOnCircle(points:MutableList<Int>,r:Int, center:Pair<Int,Int>) {
		val num = points.size-2
		val angle = 315.0/num * PI/180
		val rotacion = PI/2
		val perspectiva = 4
		graphics {
			fill(Colors["#b181ce"]) {
				circle(log(20.0*points.size,5.0)).center().position(center.first-r/7,center.second+r/perspectiva)
				circle(log(20.0*points.size,5.0)).center().position(center.first+r/7,center.second+r/perspectiva)
				text("${points.removeFirst()}").position(center.first-r/7,center.second+r/perspectiva)
				text("${points.removeFirst()}").position(center.first+r/7,center.second+r/perspectiva)
				for (i in num.downTo(1)) {
					if (i%2!=0) {
						var posx = center.first+r*-cos(angle*i/2-rotacion)
						var posy = center.second+r*sin(angle*i/2 - rotacion)/perspectiva
						circle(log(20.0*i, 5.0)).center().position(posx,posy)
						text(points.removeLast().toString()).position(posx,posy)
					} else {
						var posx = center.first+r*-cos(-angle*(i-1)/2 - rotacion)
						var posy = center.second+r*sin(-angle*(i-1)/2 - rotacion)/perspectiva
						circle(log(20.0*i, 5.0)).center().position(posx,posy)
						text(points.removeFirst().toString()).position(posx,posy)
					}
				}
			}
		}
	}

	pointsOnCircle((1..40).toList() as MutableList<Int>,200, Pair(250,325))

}