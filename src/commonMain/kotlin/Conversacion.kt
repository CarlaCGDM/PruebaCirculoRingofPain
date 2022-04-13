class Conversacion(
    val linea:String,
    val opcion1:Pair<String,Conversacion>? = null,
    val opcion2:Pair<String,Conversacion>? = null,
    val opcion3:Pair<String,Conversacion>? = null,
) {
    var opciones = if (opcion1!=null && opcion2 !=null && opcion3 != null) true else false
}

/* Sistema alternativo en el que las decisiones afectan al progreso del jugador:

-El objeto Jugador tiene una serie de métodos que permiten realizar estos efectos.
-Las opciones son Triples en lugar de Pairs, siendo el tercer elemento una llamada al método correspondiente del objeto Jugador.

 */