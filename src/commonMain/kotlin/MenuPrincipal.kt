import com.soywiz.klogger.AnsiEscape
import com.soywiz.korge.html.Html
import com.soywiz.korge.scene.AlphaTransition
import com.soywiz.korge.scene.Scene
import com.soywiz.korge.ui.DefaultUIFont
import com.soywiz.korge.ui.uiButton
import com.soywiz.korge.view.*
import com.soywiz.korim.color.Colors
import com.soywiz.korim.font.DefaultTtfFont
import com.soywiz.korim.font.Font
import com.soywiz.korim.font.readBitmapFont
import com.soywiz.korim.format.readBitmap
import com.soywiz.korim.text.TextAlignment
import com.soywiz.korio.async.launchImmediately
import com.soywiz.korio.file.std.resourcesVfs

/////////////////////////////////////////////////////////////////////////////////////////////////////////
                                        /*ESCENA DE MENÚ PRINCIPAL*/
/////////////////////////////////////////////////////////////////////////////////////////////////////////

class MenuPrincipal : Scene() {
    override suspend fun Container.sceneInit() {

        val fondo = sprite(resourcesVfs["fondos/fondoPrincipal.png"].readBitmap()).size(views.virtualWidth,views.virtualHeight)

        val fuente = resourcesVfs["clear_sans.fnt"].readBitmapFont()

        val menu = roundRect(400.0, 300.0, 10.0, fill = Colors["#313335"], stroke = Colors["#1e2022"], strokeThickness = 2.0).centerOnStage()
        val fondoTitulo = roundRect(width = 350.0, height = 80.0, rx = 10.0, fill=Colors["#313335"], stroke=Colors["#1e2022"], strokeThickness = 2.0).centerXOnStage().alignTopToTopOf(menu, -40)
        val titulo = text("Ring of Dread", textSize = 60.0, color = Colors.WHITE, font = fuente, alignment = TextAlignment.MIDDLE_CENTER).centerOn(fondoTitulo)

        val button1 =  this.uiButton(width= 200.0, height= 60.0, "Nueva Partida").centerXOnStage().alignBottomToBottomOf(menu,175).onPress {
        launchImmediately { sceneContainer.changeTo<Presentacion>(transition = AlphaTransition) }
        }
        val button2 = this.uiButton(width= 200.0, height= 60.0, "Opciones").centerXOnStage().alignBottomToBottomOf(menu, 105)
        val button3 = this.uiButton(width= 200.0, height= 60.0, "Opciones").centerXOnStage().alignBottomToBottomOf(menu, 35)

        val spriteMapPersonaje = resourcesVfs["sprites/fantasmaIdle.png"].readBitmap()
        val animacionPersonaje = SpriteAnimation(
            spriteMap = spriteMapPersonaje,
            spriteHeight = 128,
            spriteWidth = 128,
            marginTop = 0,
            marginLeft = 0,
            columns = 13,
            rows = 1,
            offsetBetweenColumns = 0,
            offsetBetweenRows = 0
        )

        val personaje = sprite(animacionPersonaje).scale(0.6,0.6).alignBottomToBottomOf(menu,20).alignLeftToLeftOf(menu, 10)
        personaje.playAnimationLooped()
} }

/* TODO:
-Falta incluir música de fondo.
-Falta añadir más botones, los correspondientes a todas las funciones del juego.
-Obviamente necesita un resideño una vez todos los elementos sean funcionales.
 */