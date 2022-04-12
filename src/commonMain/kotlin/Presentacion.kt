import com.soywiz.klock.TimeSpan
import com.soywiz.klock.blockingSleep
import com.soywiz.korge.input.onClick
import com.soywiz.korge.scene.Scene
import com.soywiz.korge.scene.delay
import com.soywiz.korge.ui.UIButton
import com.soywiz.korge.ui.uiButton
import com.soywiz.korge.ui.uiRadioButton
import com.soywiz.korge.ui.uiSkin
import com.soywiz.korge.view.*
import com.soywiz.korim.color.Colors
import com.soywiz.korim.format.readBitmap
import com.soywiz.korim.text.TextAlignment
import com.soywiz.korim.text.TextRendererActions
import com.soywiz.korio.async.launch
import com.soywiz.korio.async.launchImmediately
import com.soywiz.korio.file.std.resourcesVfs
import com.soywiz.korma.geom.mutable

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
            opcion1 = Pair("Gracias",Conversacion("Buena suerte.")),
            opcion2 = Pair("Ok.",Conversacion("Ok.")),
            opcion3 = Pair("...",Conversacion(
                linea= "¿No dices nada?",
                opcion1 = Pair("Gracias.",Conversacion("De nada. Buena suerte")),
                opcion2 = Pair("No.",Conversacion("Pues buena suerte.")),
                opcion3= Pair("...",Conversacion("Buena suerte."))
            )),
        )


        var cuadroDialogo = text(conversacion.linea, textSize = 20.0, alignment = TextAlignment.MIDDLE_CENTER).centerXOnStage().alignBottomToTopOf(personaje, 20)
        var botones = container {}
        var texto2 = text(conversacion.opcion2?.first ?: "" , alignment = TextAlignment.MIDDLE_CENTER).centerXOnStage().alignTopToBottomOf(personaje, 20)
        var texto1 = text(conversacion.opcion1?.first ?: "" , alignment = TextAlignment.MIDDLE_CENTER).alignTopToBottomOf(personaje, 20).alignRightToLeftOf(texto2, 100)
        var texto3 = text(conversacion.opcion3?.first ?: "" , alignment = TextAlignment.MIDDLE_CENTER) .alignTopToBottomOf(personaje, 20).alignLeftToRightOf(texto2, 100)

        fun actualizarTexto() {
            texto1.text = conversacion.opcion1!!.first
            texto2.text = conversacion.opcion2!!.first
            texto3.text = conversacion.opcion3!!.first
        }

        fun ocultarTexto() {
            texto1.text = ""
            texto2.text = ""
            texto3.text = ""
        }

        var boton2 =
                this.uiButton(width = 100.0, height = 50.0, text = "").centerOn(texto2)
                    .addTo(botones)
                    .onClick { conversacion = conversacion.opcion2!!.second; cuadroDialogo.text = conversacion.linea; if (conversacion.opciones) { actualizarTexto() }  else {botones.removeChildren(); ocultarTexto()}}
        var boton1 =
                this.uiButton(width = 100.0, height = 50.0, text = "").centerOn(texto1)
                    .addTo(botones)
                    .onClick { conversacion = conversacion.opcion1!!.second; cuadroDialogo.text = conversacion.linea;  if (conversacion.opciones) { actualizarTexto() } else {botones.removeChildren(); ocultarTexto()}}
        var boton3 =
                this.uiButton(width = 100.0, height = 50.0, text = "").centerOn(texto3)
                    .addTo(botones)
                    .onClick { conversacion = conversacion.opcion3!!.second; cuadroDialogo.text = conversacion.linea;  if (conversacion.opciones) { actualizarTexto() }  else {botones.removeChildren(); ocultarTexto()}}
    }
}