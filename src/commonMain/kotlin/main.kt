import com.soywiz.klock.TimeSpan
import com.soywiz.klock.blockingSleep
import com.soywiz.klock.seconds
import com.soywiz.klogger.AnsiEscape
import com.soywiz.korge.*
import com.soywiz.korge.animate.waitStop
import com.soywiz.korge.time.delay
import com.soywiz.korge.tween.*
import com.soywiz.korge.view.*
import com.soywiz.korim.color.Colors
import com.soywiz.korim.format.*
import com.soywiz.korio.file.std.*
import com.soywiz.korma.geom.Angle
import com.soywiz.korma.geom.degrees
import com.soywiz.korma.geom.sin
import com.soywiz.korma.geom.vector.roundRect
import com.soywiz.korma.interpolation.Easing
import kotlinx.coroutines.awaitAll
import kotlin.math.PI
import kotlin.math.cos
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
		val angle = 270.0/num * PI/180
		val rotacion = PI/2
		graphics {
			fill(Colors["#b181ce"]) {
				circle(4.0).center().position(center.first-10,center.second+100)
				circle(4.0).center().position(center.first+10,center.second+100)
				text("${points.removeFirst()}").position(center.first-10,center.second+100)
				text("${points.removeFirst()}").position(center.first+10,center.second+100)
				for (i in num.downTo(1)) {
					if (i%2!=0) {
						circle(2.0).center().position(center.first+r*-cos(angle*i/2 - rotacion),center.second+r*sin(angle*i/2 - rotacion))
						text("${points.removeLast()}").position(center.first+r*-cos(angle*i/2 - rotacion),center.second+r*sin(angle*i/2 - rotacion))
					} else {
						circle(2.0).center().position(center.first+r*-cos(-angle*(i-1)/2 - rotacion),center.second+r*sin(-angle*(i-1)/2 - rotacion))
						text("${points.removeFirst()}").position(center.first+r*-cos(-angle*(i-1)/2 - rotacion),center.second+r*sin(-angle*(i-1)/2 - rotacion))
					}
				}
			}
		}
	}

	pointsOnCircle((1..20).toList() as MutableList<Int>,100, Pair(250,325))

}