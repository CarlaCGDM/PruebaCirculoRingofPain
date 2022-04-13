import com.soywiz.kds.rotated
import com.soywiz.korge.*
import com.soywiz.korge.input.onClick
import com.soywiz.korge.input.onOver
import com.soywiz.korge.scene.Scene
import com.soywiz.korge.ui.uiButton
import com.soywiz.korge.ui.uiRadioButton
import com.soywiz.korge.view.*
import com.soywiz.korim.color.Colors
import com.soywiz.korim.format.readBitmap
import com.soywiz.korim.text.TextAlignment
import com.soywiz.korio.file.std.resourcesVfs
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

/* NO ESTA CONECTADO AUN A NADA*/

/////////////////////////////////////////////////////////////////////////////////////////////////////////
                                        /*ESCENA DE SALA PRINCIPAL*/
/////////////////////////////////////////////////////////////////////////////////////////////////////////


class Anillo() : Scene() {
    override suspend fun Container.sceneInit() {

        val fondo1 = sprite(resourcesVfs["fondos/cielo.png"].readBitmap()).size(views.virtualWidth,views.virtualHeight)
        val fondo2 = sprite(resourcesVfs["fondos/ruinas.png"].readBitmap()).size(views.virtualWidth,views.virtualHeight)
        val fondo3 = sprite(resourcesVfs["fondos/arboles.png"].readBitmap()).size(views.virtualWidth,views.virtualHeight)
        val fondo4 = sprite(resourcesVfs["fondos/hierba.png"].readBitmap()).size(views.virtualWidth,views.virtualHeight)

        fun generarAnillo(cartas:MutableList<Int>):Container {
            //preparar lo necesario
            val total = cartas.size
            val mitad = total/2 - 1
            val radio = views.virtualWidth/4.0
            val centro = Pair(views.virtualWidth/2.0,views.virtualHeight/2.0-80)
            val angulo = 360.0/cartas.size * PI/180
            val rot = PI/2 + angulo/2
            val persp = 3
            val ancho = 40.0*3
            val alto = 60.0*3
            val sep = ancho*1.5/2 + 5
            var anillo = container { }
            var x: Double
            var y: Double

            if (cartas.size == 1) {
                anillo.addChildren(listOf(
                    RoundRect(width = ancho*1.5, height = alto*1.3, rx = 20.0, fill=Colors["#ff587f"], stroke=Colors["#a5253a"], strokeThickness = 2.0).center().position(centro.first, centro.second+radio /persp),
                    Text(text = cartas.removeFirst().toString(), alignment = TextAlignment.MIDDLE_CENTER, color = Colors.BLACK).position(centro.first, centro.second+radio /persp)
                ))
                return anillo
            }
            //si hay dos o más, colocamos las dos primeras en la posición central con una pequeña separación
            //el bucle se encargará del resto
            val primeraCarta = RoundRect(width = ancho*1.3, height = alto*1.3, rx = 20.0, fill=Colors["#ff587f"], stroke=Colors["#a5253a"], strokeThickness = 2.0).center().position(centro.first-sep, centro.second+radio/persp).onOver {/*cambiar tamaño*/}
            val segundaCarta =	RoundRect(width = ancho*1.3, height = alto*1.3, rx = 20.0, fill=Colors["#ff587f"], stroke=Colors["#a5253a"], strokeThickness = 2.0).center().position(centro.first+sep, centro.second+radio/persp)
            val primerTexto = Text(text = cartas.removeFirst().toString(), alignment = TextAlignment.MIDDLE_CENTER, color = Colors.BLACK).position(centro.first-sep, centro.second+radio/persp)
            val segundoTexto = Text(text = cartas.removeFirst().toString(), alignment = TextAlignment.MIDDLE_CENTER, color = Colors.BLACK).position(centro.first+sep, centro.second+radio/persp)
            //averiguar la posición de cada carta
            for (i in cartas.size.downTo(1)) {
                x = centro.first + radio*cos(angulo*i + rot)
                y = centro.second + radio*sin(angulo*i + rot)/persp
                //añadir los elementos visuales de la carta al contenedor en esas posiciones
                if (i <= mitad) {
                    anillo.addChildAt(RoundRect(width = ancho, height = alto, rx = 20.0, fill=Colors["#b181ce"], stroke=Colors["#a046b9"], strokeThickness = 2.0).center().position(x,y), anillo.numChildren)
                    anillo.addChildAt(Text(text = cartas.removeFirst().toString(), alignment = TextAlignment.MIDDLE_CENTER, color = Colors.BLACK).position(x, y), anillo.numChildren)
                    anillo.addChildAt(RoundRect(width = ancho, height = alto, rx = 20.0, fill=Colors.BLACK, stroke=Colors.BLACK, strokeThickness = 2.0).center().position(x,y).alpha(i/total.toDouble()), anillo.numChildren)
                } else {
                    anillo.addChildAt(RoundRect(width = ancho, height = alto, rx = 20.0, fill=Colors.BLACK, stroke=Colors.BLACK, strokeThickness = 2.0).center().position(x,y).alpha(1 - i/total.toDouble()), 0)
                    anillo.addChildAt(Text(text = cartas.removeFirst().toString(), alignment = TextAlignment.MIDDLE_CENTER, color = Colors.BLACK).position(x, y), 0)
                    anillo.addChildAt(RoundRect(width = ancho, height = alto, rx = 20.0, fill=Colors["#b181ce"], stroke=Colors["#a046b9"], strokeThickness = 2.0).center().position(x,y), 0)
                }
            }
            anillo.addChildren(listOf(primeraCarta,segundaCarta,primerTexto,segundoTexto))
            anillo.addChildAt(circle(radius=radio, fill=Colors.BLACK).alpha(0.25).center().position(centro.first,centro.second+180).size(radio*2, radio*2/persp),0)

            //devolver el contenedor con los elementos
            return anillo
        }
        var cartas = (1..9).toMutableList()
        var contenedorPrincipal: Container = container { }
        var anillo: Container =  generarAnillo(ArrayList(cartas))
        var botones: Container = Container()

        var botonDerecho = uiButton(width=30.0, height=80.0,text=">").position(views.virtualWidth/2.0 + 180, views.virtualHeight/2.0).onClick{cartas = cartas.rotated(-1).toMutableList(); anillo.removeChildren(); anillo=generarAnillo(ArrayList(cartas))}
        var botonIzquierdo = uiButton(width=30.0, height=80.0,text="<").position(views.virtualWidth/2.0 - 210, views.virtualHeight/2.0).onClick{cartas = cartas.rotated(1).toMutableList(); anillo.removeChildren(); anillo=generarAnillo(ArrayList(cartas))}

        botones.addChild(botonDerecho!!)
        contenedorPrincipal.addChild(anillo)
        contenedorPrincipal.addChild(botones)
    }
}

/* TODO:
-Falta poner bien los botones y estructurar la escena mejor.
-Falta crear el resto de los elementos de la interfaz.
 */