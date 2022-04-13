import com.soywiz.klock.TimeSpan
import com.soywiz.klock.blockingSleep
import com.soywiz.korge.input.onClick
import com.soywiz.korge.scene.AlphaTransition
import com.soywiz.korge.scene.Scene
import com.soywiz.korge.scene.delay
import com.soywiz.korge.ui.uiButton
import com.soywiz.korge.view.*
import com.soywiz.korge.view.filter.TransitionFilter
import com.soywiz.korim.format.readBitmap
import com.soywiz.korim.text.TextAlignment
import com.soywiz.korio.async.launch
import com.soywiz.korio.async.launchImmediately
import com.soywiz.korio.file.std.resourcesVfs
import com.soywiz.korio.lang.Thread_sleep
import kotlinx.coroutines.GlobalScope
import com.soywiz.korge.scene.delay

/////////////////////////////////////////////////////////////////////////////////////////////////////////
                                /*ESCENA DE INTRODUCCIÓN AL JUEGO*/
/////////////////////////////////////////////////////////////////////////////////////////////////////////

class Presentacion() : Scene() {
    override suspend fun Container.sceneInit() {
        val spriteMapPersonaje = resourcesVfs["chicaPlaceholder.png"].readBitmap()
        val animacionPersonaje = SpriteAnimation(
            spriteMap = spriteMapPersonaje,
            spriteHeight = 454,
            spriteWidth = 416,
            marginTop = 0,
            marginLeft = 0,
            columns = 16,
            rows = 1,
            offsetBetweenColumns = 0,
            offsetBetweenRows = 0
        )

        val personaje = sprite(animacionPersonaje).scale(0.4,0.4).centerOnStage()
        personaje.playAnimationLooped()
        var conversacion = Conversacion(
            linea = "Hola. Bienvenido a Ring of Dread.",
            opcion1 = Pair("Gracias.",Conversacion("Buena suerte.")),
            opcion2 = Pair("Ok.",Conversacion("Ok.")),
            opcion3 = Pair("...",Conversacion(
                linea= "¿No dices nada?",
                opcion1 = Pair("Gracias.",Conversacion("De nada. Buena suerte")),
                opcion2 = Pair("No.",Conversacion("Pues buena suerte.")),
                opcion3= Pair("...",Conversacion("Pues buena suerte."))
            )),
        )


        var cuadroDialogo = text("", textSize = 20.0, alignment = TextAlignment.MIDDLE_CENTER).centerXOnStage().alignBottomToTopOf(personaje, 20)
        var botones = container {}.visible(false)
        var texto2 = text(conversacion.opcion2?.first ?: "" , alignment = TextAlignment.MIDDLE_CENTER).alignTopToBottomOf(personaje, 30).positionX(views.virtualWidth/2)
        var texto1 = text(conversacion.opcion1?.first ?: "" , alignment = TextAlignment.MIDDLE_CENTER).alignTopToBottomOf(personaje, 30).positionX(views.virtualWidth/2 - 120)
        var texto3 = text(conversacion.opcion3?.first ?: "" , alignment = TextAlignment.MIDDLE_CENTER) .alignTopToBottomOf(personaje, 30).positionX(views.virtualWidth/2 + 120)

        var botonComenzar =
            uiButton(width = 100.0, height = 50.0, text = "[Entrar]").visible(false)
                .centerOn(texto2)
                .onClick {
                    launchImmediately { sceneContainer.changeTo<MenuPrincipal>(time = TimeSpan(500.0), transition = AlphaTransition) }
                }

        fun actualizarTexto() {
            texto1.text = conversacion.opcion1!!.first
            texto2.text = conversacion.opcion2!!.first
            texto3.text = conversacion.opcion3!!.first
        }

        fun terminarConversacion() {
            texto1.text = ""
            texto2.text = ""
            texto3.text = ""
            botonComenzar!!.visible = true
        }

        fun textoLetraALetra(texto:String) {
            botones.visible = false
            GlobalScope.launch {
                for (i in 1..texto.length) {
                    cuadroDialogo.text = texto.take(i)
                    when(texto.elementAt(i-1)) {
                        ' ' -> delay(TimeSpan(0.0))
                        ',' -> delay(TimeSpan(100.0))
                        in listOf('?','!','.') -> delay(TimeSpan(500.0))
                        else -> delay(TimeSpan(50.0))
                    }
                }
                botones.visible = true
            }
        }

        textoLetraALetra(conversacion.linea)

        var boton2 =
                this.uiButton(width = 100.0, height = 50.0, text = "").centerOn(texto2)
                    .onClick {
                        conversacion = conversacion.opcion2!!.second
                        textoLetraALetra(conversacion.linea)
                        if (conversacion.opciones) {actualizarTexto()}  else {botones.removeChildren(); terminarConversacion()}}
        var boton1 =
                this.uiButton(width = 100.0, height = 50.0, text = "").centerOn(texto1)
                    .onClick {
                        conversacion = conversacion.opcion1!!.second
                        textoLetraALetra(conversacion.linea)
                        if (conversacion.opciones) {actualizarTexto()} else {botones.removeChildren(); terminarConversacion()}}
        var boton3 =
                this.uiButton(width = 100.0, height = 50.0, text = "").centerOn(texto3)
                    .onClick {
                        conversacion = conversacion.opcion3!!.second
                        textoLetraALetra(conversacion.linea)
                        if (conversacion.opciones) {actualizarTexto()}  else {botones.removeChildren(); terminarConversacion()}}

        botones.addChildren(listOf(boton1,boton2,boton3))
        botones.addChildren(listOf(texto1,texto2,texto3))
    }
}

/* TODO:
-No veo manera de hacer que los botones aparezcan sólo cuando el NPC ha terminado de hablar.
-Obviamente necesita un rediseño una vez sea completamente funcional.
-También necesita música.
 */

