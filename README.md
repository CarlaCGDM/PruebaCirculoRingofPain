# Creando el anillo del Ring of Pain (Kotlin/Korge)
Vamos a ver paso a paso como llegar a tener una función que genere un anillo de cartas distribuídas de manera similar al formato visto en el juego Ring of Pain a partir de una lista. Aunque en el producto final usaremos una lista de objetos tipo carta, aquí usaremos una lista de enteros para ver las cosas con mayor claridad.

![ringOfPain](https://user-images.githubusercontent.com/92323990/162646124-e0b10f35-2eb6-4d87-a901-25fa86d2a7ff.gif)

## Vistas en Korge 
Lo primero que tenemos que saber de Korge es que los distintos elementos que queramos mostrar por pantalla (imágenes, texto, etc.) 
se llaman *vistas*, ya que heredan de la clase abstracta "View". Estas vistas se agrupan en *contenedores* (clase "Container") para poder organizarlas y trabajar con ellas.

Por eso lo primero que vamos a colocar en nuestra escena va a ser un contenedor que contenga un contenedor para las cartas, y un contenedor 
para los botones que nos permitirán hacerlas girar. La forma de meter contenedores dentro de otros es mediante las funciones "addChild()" y "addChildAt()". 

La segunda nos permite especificar el lugar exacto en el cual queremos añadir el contenedor: los elementos de un contenedor añadido en el índice 0 se dibujarán 
detrás de todos los demás elementos, mientras que los de un contenedor añadido en el mayor índice de la lista se dibujarán delante de todos los demás. Como queremos que
los botones se dibujen delante, los añadimos en el índice "1", mientras que el anillo quedará en el índice "0". 

```kotlin
suspend fun main() = Korge(
	width = 480,
	height = 640,
	title = "Anillo",
	bgcolor =  Colors.ANTIQUEWHITE
) {

var cartas = (0..10).toMutableList()
//es importante usar una copia de la lista original, ya que a lo largo de la función la iremos destruyendo
var contenedorPrincipal:Container = generarAnillo(ArrayList(cartas)) //en el siguiente paso crearemos esta función
var anillo:Container = Container()
var botones:Container = Container()
contenedorPrincipal.addChild(anillo)
contenedorPrincipal.addChild(botones)

}
```

## Función generarAnillo()
A grandes rasgos, lo que buscamos es una función que distribuya equitativamente un número de puntos dados a lo largo de una circunferencia. Vamos a ver cómo podemos
lograr eso, y luego iremos viendo cómo podemos ir logrando todo lo demás. 

### Anillo 1.0: Puntos en una circunferencia

```kotlin
fun generarAnillo(cartas:MutableList<Int>):Container {
	//preparar todo lo necesario
	val radio = views.virtualWidth/3
	val centro = Pair(views.virtualWidth/2.0,views.virtualHeight/2.0)
	val angulo = 360.0/cartas.size * PI/180
	var anillo = container { }
	var x: Double
	var y: Double
	//averiguar la posición de cada carta
	for (i in cartas.size.downTo(1)) {
		x = centro.first + radio*cos(angulo*i)
		y = centro.second + radio*sin(angulo*i)
		//añadir los elementos visuales de la carta al contenedor en esas posiciones
		anillo.addChild(
			RoundRect(
				width = 40.0,
				height = 60.0,
				rx = 2.0,
				fill=Colors.CORAL,
				stroke=Colors.BLACK,
				strokeThickness = 2.0
			).center().position(x,y)
		)
		anillo.addChild(
			Text(
				text = cartas.removeFirst().toString(),
				alignment = TextAlignment.MIDDLE_CENTER,
				color = Colors.BLACK
			).position(x, y)
		)
	}
	//devolver el contenedor con los elementos
	return anillo
}
```

**Radio**: Para hacer que el anillo ocupe horizontalmente siempre 2/3 de la pantalla usaremos el atributo de la vista principal "virtualWidth", que nos da el ancho de la pantalla.

**Centro**: De forma similar al radio, encontramos el centro de la pantalla tomando la mitad del valor de virtualWidth y la mitad del valor de virtualHeight.

**Ángulo**: Dividiendo los 360º de la circunferencia entre el número de cartas sabremos cada cuántos grados debemos colocar una carta nueva.

**Anillo**: La función devuelve un objeto del tipo Container. Lo escribimos en minúsculas para añadirlo automáticamente a la vista principal.

**X e Y**: Las coordenadas de la posición exacta donde debemos colocar esa carta nos vienen dadas por el seno y el coseno del ángulo que le corresponde, como puede verse
en el siguiente diagrama. Multiplicamos estos valores por el radio y los desplazamos al centro de la pantalla sumándoles los valores correspondientes.

Este sistema distribuye las cartas a partir del punto (1,0) en el círculo unitario, por lo que debemos rotar el círculo -90 grados para que la primera carta se coloque en la posición más cercana al jugador (-1,0). Para esto creamos una nueva constante que almacene los grados de rotación en radianes (PI/2) y se la restamos al ángulo con el que estamos trabajando antes de utilizarlo para calcular la posición.

```kotlin
val rot = PI/2
//...
x = centro.first + radio*cos(angulo*i + rot)
y = centro.second + radio*sin(angulo*i + rot)
```
<img src="https://i.imgur.com/2AvSwEA.gif" width="30%" height="30%"/> <img src="https://user-images.githubusercontent.com/92323990/162573006-bd37fa88-a615-4c7f-9354-7700bb9d4ea3.png" width="30%" height="30%"/> <img src="https://user-images.githubusercontent.com/92323990/162573048-2286a940-8860-4a28-9ca3-31666cf43874.png" width="30%" height="30%"/>

### Anillo 1.1: Puntos en un arco
La base funciona, pero lo que buscamos no es realmente un círculo, sino una o dos cartas frontales con el resto distribuídas a lo largo de un arco detrás. La primera parte es fácil: si sólo tenemos una carta, vamos a colocarla en la posición (centro.x, centro.y-radio). Si tenemos dos o más, vamos a colocar las dos primeras en las posiciones (centro.x-separacion, centro.y-radio) y (centro.x+separacion, centro.y-radio). 

La separación tendremos que determinarla según el tamaño de nuestras cartas, para asegurarnos de que no se toquen debe ser mayor a la mitad del ancho de la carta, ya que la distancia entre sus centros cuando se colocan una junto a la otra es del total del ancho de la carta (la mitad de cada una). Podemos aprovechar para crear variables que se encarguen del ancho y el alto de la carta, de este modo será más fácil hacer que las dos cartas frontales sean un poco más grandes multiplicando el valor por 1.1 (añadir un 10%).

```kotlin
val ancho = 40.0
val alto = 60.0
val sep = ancho/2 + 5
//si solo hay una carta, la colocamos en la posición central
if (cartas.size == 1) {
	anillo.addChildren(listOf(
		RoundRect(width = ancho*1.1, height = alto*1.1, /*...*/).center().position(centro.first, centro.second+radio),
		Text(text = cartas.removeFirst().toString(), /*...*/).position(centro.first, centro.second+radio)
	))
	return anillo
}
//si hay dos o más, colocamos las dos primeras en la posición central con una pequeña separación
//el bucle se encargará del resto
anillo.addChildren(listOf(
	RoundRect(width = ancho*1.1, height = alto*1.1, /*...*/).center().position(centro.first-sep, centro.second+radio),
	RoundRect(width = ancho*1.1, height = alto*1.1, /*...*/).center().position(centro.first+sep, centro.second+radio),
	Text(text = cartas.removeFirst().toString(), /*...*/).position(centro.first-sep, centro.second+radio),
	Text(text = cartas.removeFirst().toString(), /*...*/).position(centro.first+sep, centro.second+radio),
	))
```

Para recuperar el ángulo correcto, sólo tenemos que añadir una fracción de la parte que hemos "eliminado" del círculo al sacar los primeros dos elementos al valor de la rotación.

```Kotlin
val rot = PI/2 + angulo/2
```

<img src="https://user-images.githubusercontent.com/92323990/162577490-b9398514-5e4e-4aed-9a1e-a5cd7000b89b.png" width="30%" height="30%"/> <img src="https://user-images.githubusercontent.com/92323990/162577390-cbebe4bd-5f01-4d00-a1ad-45b6c9585c61.png" width="30%" height="30%"/> <img src="https://user-images.githubusercontent.com/92323990/162591049-e174e190-b202-4822-ba26-7d17d08ee6e3.png" width="30%" height="30%"/> 

Si fueramos a visualizar el anillo en el plano, habríamos terminado. Pero el anillo que buscamos crear cuenta con cierta inclinación. Al incluir un valor de perspectiva y usarlo para dividir todos los valores del eje vertical en los que colocamos los elementos, se hace evidente el fallo de este método: no nos permite controlar el orden en el que se dibujan las cartas.

```Kotlin
val persp = 3
```

<img src="https://user-images.githubusercontent.com/92323990/162591394-9208b091-4938-438c-bfb4-f46585138d92.png" width="30%" height="30%"/>

### Anillo 1.2: Orden de las Cartas

Hemos visto que podemos indicar a Korge dónde queremos colocar las cartas en la lista de objetos hijos de un contenedor de vistas. Si nos fijamos en lo que tenemos hasta ahora, cada carta nueva se dibuja por defecto delante de la anterior, lo cual funciona para la mitad izquierda del círculo pero no para la mitad derecha, dónde necesitamos que las cartas nuevas se dibujen detrás de la anterior. Para corregir esto, vamos a comprobar en cada iteracion del bucle si la carta se encuentra en la primera o en la segunda mitad de la lista para determinar si debemos dibujarla delante o detrás de las demás.

```Kotlin
val total = cartas.size
val mitad = total/2 - 1 
//...
for (i in cartas.size.downTo(1)) {
	x = centro.first + radio*cos(angulo*i + rot)
	y = centro.second + radio*sin(angulo*i + rot)/3
	//añadir los elementos visuales de la carta al contenedor en esas posiciones
	if (i <= mitad) {
		anillo.addChildAt(RoundRect(width = ancho, height = alto, /*...*/).center().position(x,y), anillo.numChildren)
		anillo.addChildAt(Text(text = cartas.removeFirst().toString(), /*...*/).position(x, y), anillo.numChildren)
	} else {
		anillo.addChildAt(Text(text = cartas.removeFirst().toString(), /*...*/).position(x, y), 0)
		anillo.addChildAt(RoundRect(width = ancho, height = alto, /*...*/).center().position(x,y), 0)
	}
}
```
Ahora que el anillo ya se dibuja como debería, sólo tenemos que esperar a que el anillo se haya terminado de dibujar para añadir las dos cartas frontales. Para esto, las definimos como contenido de variables y las añadimos con addChild() al final de la función. Podemos también cambiar su color para ver el resultado con más claridad.

```Kotlin
val primeraCarta = RoundRect(color = Colors.GOLD, /*...*/).center().position(centro.first-sep, centro.second+radio/3)
val segundaCarta = RoundRect(color = Colors.GOLD, /*...*/).center().position(centro.first+sep, centro.second+radio/3)
val primerTexto = Text(/*...*/).position(centro.first-sep, centro.second+radio/3)
val segundoTexto = Text(/*...*/).position(centro.first+sep, centro.second+radio/3)
//bucle...
anillo.addChildren(listOf(primeraCarta,segundaCarta,primerTexto,segundoTexto))
``` 

<img src="https://user-images.githubusercontent.com/92323990/162639239-e10eb6d4-39f4-453c-aa5c-3c958544af62.png" width="30%" height="30%"/> <img src="https://user-images.githubusercontent.com/92323990/162639120-3c96ed53-aa45-41dc-ada0-78d4efcb99d6.png" width="30%" height="30%"/>

### Anillo 1.3: Degradado de Color:

Para que el anillo adquiera una sensación de tres dimensiones, vamos a hacer que las cartas que se encuentren en la parte trasera del anillo se vean un poco más oscuras que las que se encuentran en la parte frontal. Para esto añadiremos una copia en color negro de la carta que dibujaremos por encima de ella, alternando el valor de su canal alpha para variar su transparencia. Necesitamos que la primera carta negra que dibujamos (la que está más cerca del jugador) tenga un valor de alpha 0 (transparencia completa) y que la última tenga un valor de alpha alrededor de 0.5 (semi-transparente, ya que el valor 1 indica opacidad completa, y es el valor por defecto.)

Para obtener este valor vamos a usar la posición de la carta. Para la primera mitad del círculo, que toma los valores desde el 3 hasta la mitad de lo que queda de la lista, le daremos al alpha de la carta un valor de i/total de cartas. De este modo, en una lista de 10 cartas, la carta negra que se encuentre en la posición 3 tendrá un 0.3 (30%) de opacidad, la que se encuentre en la posición 4 un 0.4 (40%), etc. No debemos olvidar operar con números decimales para poder obtener el valor alpha correctamente (toFloat() o toDouble() en alguno de los valores).

```Kotlin
if (i <= mitad) {
	//carta
	//texto
	anillo.addChildAt(RoundRect(fill=Colors.BLACK, /*...*/).center().position(x,y).alpha(i/total.toDouble()), 0)
} else {
	anillo.addChildAt(RoundRect(fill=Colors.BLACK, /*...*/).center().position(x,y).alpha(i/total.toDouble()), 0)
	//texto
	//carta
}
```

Para la mitad derecha tenemos que invertir la direccion en la que progresa el "oscurecimiento" de la carta. Si siguieramos usando la misma formula, acabaríamos con un degradado que se oscureciera hasta que la ultima carta se viera completamente negra como vemos en la primera imagen. Sin embargo, si invertimos el valor usando 1 - i/total, obtenemos el resultado que buscamos. 

```Kotlin
anillo.addChildAt(RoundRect(fill=Colors.BLACK, /*...*/).center().position(x,y).alpha(1-i/total.toDouble()), 0)
```

<img src="https://user-images.githubusercontent.com/92323990/162644054-972fe84c-0fcc-4232-bdaa-25982e551a7d.png" width="30%" height="30%"/> <img src="https://user-images.githubusercontent.com/92323990/162644087-0326c4f8-2d99-4ccb-bebc-7e9a29bcbeb6.png" width="30%" height="30%"/>

## Botones:

Queremos poder rotar la posición de las cartas según pulsamos dos botones, así que vamos a añadirlos al contenedor que creamos originalmente para ellos y añadirles un método ".onClick{ }" que primero haga girar la lista de cartas y luego vuelva a llamar a la función que genera el anillo. Por suerte, un método para hacer girar una lista hacia la derecha o la izquierda ya viene incluido en Kotlin (rotated) así que lo usaremos.

```Kotlin
var botonDerecho = Circle(/*...*/).center().position(/*x e y*/).onClick{
	cartas = cartas.rotated(-1).toMutableList()
	contenedorPrincipal=generarAnillo(ArrayList(cartas))
	}
var botonIzquierdo = Circle(/*...*/).center().position(/*...*/).onClick{
	cartas = cartas.rotated(1).toMutableList()
	contenedorPrincipal=generarAnillo(ArrayList(cartas))
	}
var textoDerecho = Text(text = ">", alignment = TextAlignment.MIDDLE_CENTER, /*...*/).position(/*...*/)
var textoIzquierdo = Text(text = "<", alignment = TextAlignment.MIDDLE_CENTER, /*...*/).position(/*...*/)
botones.addChild(botonDerecho!!)
botones.addChild(textoDerecho)
botones.addChild(botonIzquierdo!!)
botones.addChild(textoIzquierdo)
```

<img src="https://user-images.githubusercontent.com/92323990/162646124-e0b10f35-2eb6-4d87-a901-25fa86d2a7ff.gif" width="30%" height="30%"/>

(Ya mejoraré este tutorial con una forma más elegante de colocar los botones en pantalla y organizar todo el asunto. Si has llegado hasta aquí... ¡enhorabuena! Y gracias por leer^^)

