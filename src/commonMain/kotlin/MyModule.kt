import com.soywiz.korge.scene.Module
import com.soywiz.korinject.AsyncInjector

object MyModule : Module() {
    override val mainScene = MenuPrincipal::class

    override suspend fun AsyncInjector.configure() {
        mapPrototype { MenuPrincipal() }
        mapPrototype { Presentacion() }
    }
}