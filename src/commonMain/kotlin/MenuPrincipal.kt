import com.soywiz.korge.scene.Scene
import com.soywiz.korge.ui.uiButton
import com.soywiz.korge.view.*
import com.soywiz.korim.color.Colors
import com.soywiz.korio.async.launchImmediately


class MenuPrincipal() : Scene() {
    override suspend fun Container.sceneInit() {
        var menu = solidRect(400, 300, Colors["#ff587f"]).centerOnStage()
        var logo = text("Ring of Dread", textSize = 60.0).centerXOnStage().alignBottomToTopOf(menu, 50)
        var button1 =  this.uiButton(width= 300.0, height= 80.0, "Nueva Partida").centerXOnStage().alignTopToTopOf(menu,50).onPress { launchImmediately { sceneContainer.changeTo<Presentacion>() } }
        var button2 = this.uiButton(width= 300.0, height= 80.0, "Opciones").centerXOnStage().alignBottomToBottomOf(menu, 50)
} }
