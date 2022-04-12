import com.soywiz.korge.view.*
import com.soywiz.korim.color.Colors
import com.soywiz.korim.text.TextAlignment
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.log
import kotlin.math.sin

/*COSAS ANTIGUAS QUE NO CREO QUE VAYA A NECESITAR PERO QUIZAS SI */

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

/*
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

fun generarRing(cards: MutableList<Int>): Container {
    val radio = views.virtualWidth / 3.5
    val centro = Pair(views.virtualWidth / 2.0, views.virtualHeight / 2.0)
    val angulo = 315 / cards.size * PI / 180
    val rot = if (cards.size % 2 == 0) PI / 2 else PI / 2 + angulo / 2
    val persp = 3 //inclinación del círculo, vista superior = 1
    val alto = 130.0
    val ancho = 90.0
    val sep = ancho / 2 + 2 //separación entre las dos cartas frontales
    var anillo = container { }
    var x: Double
    var y: Double
    if (cards.size == 1) {
        roundRect(
            ancho,
            alto,
            2.0,
            2.0,
            fill = Colors["#ce4357"],
            stroke = Colors.BLACK,
            strokeThickness = 2.0
        ).center().position(centro.first, centro.second + radio / persp)
        text(cards.removeFirst().toString(), alignment = TextAlignment.MIDDLE_CENTER).position(
            centro.first,
            centro.second + radio / persp
        )
        return anillo
    }
    roundRect(
        ancho,
        alto,
        2.0,
        2.0,
        fill = Colors["#ce4357"],
        stroke = Colors.BLACK,
        strokeThickness = 2.0
    ).center().position(centro.first - sep, centro.second + radio / persp)
    roundRect(
        ancho,
        alto,
        2.0,
        2.0,
        fill = Colors["#ce4357"],
        stroke = Colors.BLACK,
        strokeThickness = 2.0
    ).center().position(centro.first + sep, centro.second + radio / persp)
    text(cards.removeFirst().toString(), alignment = TextAlignment.MIDDLE_CENTER).position(
        centro.first - sep,
        centro.second + radio / persp
    )
    text(cards.removeFirst().toString(), alignment = TextAlignment.MIDDLE_CENTER).position(
        centro.first + sep,
        centro.second + radio / persp
    )
    for (i in cards.size.downTo(1)) {
        val content: Int
        if (i % 2 != 0) {
            x = centro.first + radio * -cos(angulo * i / 2 - rot)
            y = centro.second + radio * sin(angulo * i / 2 - rot) / persp
            content = cards.removeLast()
        } else {
            x = centro.first + radio * -cos(-angulo * (i - 1) / 2 - rot)
            y = centro.second + radio * sin(-angulo * (i - 1) / 2 - rot) / persp
            content = cards.removeFirst()
        }
        var newText = Text(content.toString(), alignment = TextAlignment.MIDDLE_CENTER).position(x, y)
        var newCard = RoundRect(
            ancho * 0.9,
            alto * 0.9,
            2.0,
            2.0,
            fill = Colors["#8b93cd"],
            stroke = Colors.BLACK,
            strokeThickness = 2.0
        ).center().position(x, y)
        anillo.addChildAt(newText, 0)
        anillo.addChildAt(newCard, 0)
    }
    return anillo
}

fun generarAnillo2(cartas: MutableList<Int>): Container {
    //preparar todo lo necesario
    val total = cartas.size.toDouble()
    val mitad = total / 2 - 1
    val radio = views.virtualWidth / 3
    val centro = Pair((views.virtualWidth / 2.0), (views.virtualHeight / 2.0))
    val angulo = 300.0 / cartas.size * PI / 180
    val rot = PI - 60 * PI / 180 + angulo / 2
    val ancho = 40.0 * 2
    val alto = 60.0 * 2
    val sep = ancho / 2 + 10
    val persp = 3
    var anillo = container { }
    var x: Double
    var y: Double
    //si solo hay una carta, la colocamos en la posición central
    if (cartas.size == 1) {
        anillo.addChildren(
            listOf(
                RoundRect(
                    width = ancho * 1.2,
                    height = alto * 1.2,
                    rx = 2.0,
                    fill = Colors.GOLD,
                    stroke = Colors.BLACK,
                    strokeThickness = 2.0,
                ).center().position(centro.first, centro.second + radio / persp),
                Text(
                    text = cartas.removeFirst().toString(),
                    alignment = TextAlignment.MIDDLE_CENTER,
                    color = Colors.BLACK
                ).position(centro.first, centro.second + radio / persp)
            )
        )
        return anillo
    }
    //si hay dos o más, colocamos las dos primeras en la posición central con una pequeña separación
    val primera = RoundRect(
        width = ancho * 1.1,
        height = alto * 1.1,
        rx = 2.0,
        fill = Colors.GOLD,
        stroke = Colors.BLACK,
        strokeThickness = 2.0,
    ).center().position(centro.first - sep, centro.second + radio / persp)
    val segunda = RoundRect(
        width = ancho * 1.1,
        height = alto * 1.1,
        rx = 2.0,
        fill = Colors.GOLD,
        stroke = Colors.BLACK,
        strokeThickness = 2.0,
    ).center().position(centro.first + sep, centro.second + radio / persp)
    val primeraText = Text(
        text = cartas.removeFirst().toString(),
        alignment = TextAlignment.MIDDLE_CENTER,
        color = Colors.BLACK
    ).position(centro.first - sep, centro.second + radio / persp)
    val segundaText = Text(
        text = cartas.removeFirst().toString(),
        alignment = TextAlignment.MIDDLE_CENTER,
        color = Colors.BLACK
    ).position(centro.first + sep, centro.second + radio / persp)
    //averiguar la posición de cada carta
    for (i in cartas.size.downTo(1)) {
        x = centro.first + radio * cos(angulo * i + rot)
        y = centro.second + radio * sin(angulo * i + rot) / persp
        //añadir los elementos visuales de la carta al contenedor en esas posiciones
        if (i <= mitad) {
            anillo.addChildAt(
                RoundRect(
                    width = ancho,
                    height = alto,
                    rx = 2.0,
                    fill = Colors.CORAL,
                    stroke = Colors.BLACK,
                    strokeThickness = 2.0
                ).center().position(x, y), anillo.numChildren //se añade por encima de
            )
            anillo.addChildAt(
                Text(
                    text = cartas.removeFirst().toString(),
                    alignment = TextAlignment.MIDDLE_CENTER,
                    color = Colors.BLACK
                ).position(x, y), anillo.numChildren
            )
            anillo.addChildAt(
                RoundRect(
                    width = ancho,
                    height = alto,
                    rx = 2.0,
                    fill = Colors.BLACK,
                    stroke = Colors.BLACK,
                    strokeThickness = 2.0
                ).center().position(x, y).alpha(i/total), anillo.numChildren
            )
        } else {
            anillo.addChildAt(
                RoundRect(
                    width = ancho,
                    height = alto,
                    rx = 2.0,
                    fill = Colors.BLACK,
                    stroke = Colors.BLACK,
                    strokeThickness = 2.0
                ).center().position(x, y).alpha(1-i/total), 0 //se añade por debajo de
            )
            anillo.addChildAt(
                Text(
                    text = cartas.removeFirst().toString(),
                    alignment = TextAlignment.MIDDLE_CENTER,
                    color = Colors.BLACK
                ).position(x, y), 0
            )
            anillo.addChildAt(
                RoundRect(
                    width = ancho,
                    height = alto,
                    rx = 2.0,
                    fill = Colors.CORAL,
                    stroke = Colors.BLACK,
                    strokeThickness = 2.0
                ).center().position(x, y), 0
            )
            println(i/total)
        }
    }
    anillo.addChildren(listOf(primera, segunda, primeraText, segundaText))
    //devolver el contenedor con los elementos
    return anillo
}

 */