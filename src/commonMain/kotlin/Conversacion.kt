class Conversacion(
    val linea:String,
    val opcion1:Pair<String,Conversacion>? = null,
    val opcion2:Pair<String,Conversacion>? = null,
    val opcion3:Pair<String,Conversacion>? = null,
) {
    var opciones = if (opcion1!=null && opcion2 !=null && opcion3 != null) true else false
}