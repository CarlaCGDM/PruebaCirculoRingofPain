import com.soywiz.korge.scene.AlphaTransition
import com.soywiz.korge.scene.Scene
import com.soywiz.korge.ui.uiButton
import com.soywiz.korge.view.*
import com.soywiz.korim.color.Colors
import com.soywiz.korio.async.launchImmediately

/////////////////////////////////////////////////////////////////////////////////////////////////////////
                                        /*ESCENA DE MENÚ PRINCIPAL*/
/////////////////////////////////////////////////////////////////////////////////////////////////////////

class MenuPrincipal : Scene() {
    override suspend fun Container.sceneInit() {
        val menu = solidRect(400, 300, Colors["#ff587f"]).centerOnStage()
        val logo = text("Ring of Dread", textSize = 60.0).centerXOnStage().alignBottomToTopOf(menu, 50)
        val button1 =  this.uiButton(width= 300.0, height= 80.0, "Nueva Partida").centerXOnStage().alignTopToTopOf(menu,50).onPress {
        launchImmediately { sceneContainer.changeTo<Presentacion>(transition = AlphaTransition) }
        }
        val button2 = this.uiButton(width= 300.0, height= 80.0, "Opciones").centerXOnStage().alignBottomToBottomOf(menu, 50)
} }

/* TODO:
-Falta incluir música de fondo.
-Falta añadir más botones, los correspondientes a todas las funciones del juego.
-Obviamente necesita un resideño una vez todos los elementos sean funcionales.
 */