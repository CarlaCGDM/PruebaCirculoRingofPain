import com.soywiz.kds.rotated
import com.soywiz.korge.*
import com.soywiz.korge.input.onClick
import com.soywiz.korge.view.*
import com.soywiz.korim.color.Colors
import com.soywiz.korim.text.TextAlignment
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.log
import kotlin.math.sin

suspend fun main() = Korge(
	width = 480,
	height = 640,
	title="Anillo",
	bgcolor =  Colors.ANTIQUEWHITE
) {
	fun generarAnillo(cartas:MutableList<Int>):Container {
		//preparar lo necesario
		val total = cartas.size
		val mitad = total/2 - 1
		val radio = views.virtualWidth/3
		val centro = Pair(views.virtualWidth/2.0,views.virtualHeight/2.0)
		val angulo = 360.0/cartas.size * PI/180
		val rot = PI/2 + angulo/2
		val persp = 3
		val ancho = 40.0
		val alto = 60.0
		val sep = ancho/2 + 5
		var anillo = container { }
		var x: Double
		var y: Double
		if (cartas.size == 1) {
			anillo.addChildren(listOf(
				RoundRect(width = ancho*1.1, height = alto*1.1, rx = 2.0, fill=Colors.GOLD, stroke=Colors.BLACK, strokeThickness = 2.0).center().position(centro.first, centro.second+radio /persp),
				Text(text = cartas.removeFirst().toString(), alignment = TextAlignment.MIDDLE_CENTER, color = Colors.BLACK).position(centro.first, centro.second+radio /persp)
			))
			return anillo
		}
		//si hay dos o más, colocamos las dos primeras en la posición central con una pequeña separación
		//el bucle se encargará del resto
		val primeraCarta = RoundRect(width = ancho*1.1, height = alto*1.1, rx = 2.0, fill=Colors.GOLD, stroke=Colors.BLACK, strokeThickness = 2.0).center().position(centro.first-sep, centro.second+radio/persp)
		val segundaCarta =	RoundRect(width = ancho*1.1, height = alto*1.1, rx = 2.0, fill=Colors.GOLD, stroke=Colors.BLACK, strokeThickness = 2.0).center().position(centro.first+sep, centro.second+radio/persp)
		val primerTexto = Text(text = cartas.removeFirst().toString(), alignment = TextAlignment.MIDDLE_CENTER, color = Colors.BLACK).position(centro.first-sep, centro.second+radio/persp)
		val segundoTexto = Text(text = cartas.removeFirst().toString(), alignment = TextAlignment.MIDDLE_CENTER, color = Colors.BLACK).position(centro.first+sep, centro.second+radio/persp)
		//averiguar la posición de cada carta
		for (i in cartas.size.downTo(1)) {
			x = centro.first + radio*cos(angulo*i + rot)
			y = centro.second + radio*sin(angulo*i + rot)/persp
			//añadir los elementos visuales de la carta al contenedor en esas posiciones
			if (i <= mitad) {
				anillo.addChildAt(RoundRect(width = ancho, height = alto, rx = 2.0, fill=Colors.CORAL, stroke=Colors.BLACK, strokeThickness = 2.0).center().position(x,y), anillo.numChildren)
				anillo.addChildAt(Text(text = cartas.removeFirst().toString(), alignment = TextAlignment.MIDDLE_CENTER, color = Colors.BLACK).position(x, y), anillo.numChildren)
				anillo.addChildAt(RoundRect(width = ancho, height = alto, rx = 2.0, fill=Colors.BLACK, stroke=Colors.BLACK, strokeThickness = 2.0).center().position(x,y).alpha(i/total.toDouble()), anillo.numChildren)
			} else {
				anillo.addChildAt(RoundRect(width = ancho, height = alto, rx = 2.0, fill=Colors.BLACK, stroke=Colors.BLACK, strokeThickness = 2.0).center().position(x,y).alpha(1 - i/total.toDouble()), 0)
				anillo.addChildAt(Text(text = cartas.removeFirst().toString(), alignment = TextAlignment.MIDDLE_CENTER, color = Colors.BLACK).position(x, y), 0)
				anillo.addChildAt(RoundRect(width = ancho, height = alto, rx = 2.0, fill=Colors.CORAL, stroke=Colors.BLACK, strokeThickness = 2.0).center().position(x,y), 0)
			}
		}
		anillo.addChildren(listOf(primeraCarta,segundaCarta,primerTexto,segundoTexto))
		//devolver el contenedor con los elementos
			return anillo
	}

	var cartas = (1..9).toMutableList()
	var contenedorPrincipal: Container = container { }
	var anillo: Container =  generarAnillo(ArrayList(cartas))
	var botones: Container = Container()
	var botonDerecho = Circle(radius=10.0, fill=Colors.AQUAMARINE, stroke=Colors.BLACK, strokeThickness = 2.0).center().position(views.virtualWidth/2 + 60, views.virtualHeight/2 + views.virtualWidth /3 / 3 ).onClick{cartas = cartas.rotated(-1).toMutableList(); anillo=generarAnillo(ArrayList(cartas))}
	var textoDerecho = Text(text = ">", alignment = TextAlignment.MIDDLE_CENTER, color = Colors.BLACK).position(views.virtualWidth/2 + 60, views.virtualHeight/2 + views.virtualWidth /3 / 3)
	var botonIzquierdo = Circle(radius=10.0, fill=Colors.AQUAMARINE, stroke=Colors.BLACK, strokeThickness = 2.0).center().position(views.virtualWidth/2 - 60, views.virtualHeight/2 + views.virtualWidth /3 / 3).onClick{cartas = cartas.rotated(1).toMutableList(); anillo=generarAnillo(ArrayList(cartas))}
	var textoIzquierdo = Text(text = "<", alignment = TextAlignment.MIDDLE_CENTER, color = Colors.BLACK).position(views.virtualWidth/2 - 60, views.virtualHeight/2 + views.virtualWidth /3 / 3)
	botones.addChild(botonDerecho!!)
	botones.addChild(textoDerecho)
	botones.addChild(botonIzquierdo!!)
	botones.addChild(textoIzquierdo)
	contenedorPrincipal.addChild(anillo)
	contenedorPrincipal.addChild(botones)

	var cuadroDialogo:Container = container { }
	var fondo = RoundRect(400.0, height = 100.0, 2.0, stroke = Colors.BLACK, strokeThickness = 2.0).center().position(views.virtualWidth/2, views.virtualHeight/5*4)
	cuadroDialogo.addChild(fondo)
}

