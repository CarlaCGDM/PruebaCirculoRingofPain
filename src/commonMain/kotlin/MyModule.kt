import com.soywiz.korge.scene.Module
import com.soywiz.korinject.AsyncInjector

object MyModule : Module() {
    //punto de entrada del juego (escena menu principal)
    override val mainScene = MenuPrincipal::class

    //aqui van todas las escenas que va a utilizar el juego
    //si no están en esta lista no se pueden usar
    override suspend fun AsyncInjector.configure() {
        mapPrototype { MenuPrincipal() }
        mapPrototype { Presentacion() }
    }
}