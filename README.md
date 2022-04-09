# Creando el anillo del Ring of Pain (Kotlin/Korge)
Vamos a ver paso a paso como llegar a tener una función que genere un anillo de cartas distribuídas de manera similar al formato visto en el juego Ring of Pain a partir de una lista. Aunque en el producto final usaremos una lista de objetos tipo carta, aquí usaremos una lista de enteros para ver las cosas con mayor claridad.

![gif](https://i.ibb.co/7bqzWLp/rotacion.gif)
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
  var contenedorPrincipal:Container = generarAnillo(cartas) //en el siguiente paso crearemos esta función
	var anillo:Container = Container()
	var botones:Container = Container()
	contenedorPrincipal.addChildAt(anillo,0)
	contenedorPrincipal.addChildAt(botones,1)
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
	val centro = Pair(views.virtualWidth/2,views.virtualHeight/2)
	val angulo = 360/cartas.size * PI/180
	var anillo = container { }
	var x: Int
	var y: Int
	//averiguar la posición de cada carta
	for (i in cartas.size.downTo(1) {
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

 <img src="https://i.imgur.com/2AvSwEA.gif" width="40%" height="40%"/>

**Resultado**: 
Este sistema distribuye las cartas a partir del punto (1,0) en el círculo unitario, por lo que debemos rotar el círculo -90 grados para que la primera carta se coloque en la posición más cercana al jugador (-1,0). Para esto creamos una nueva constante que almacene los grados de rotación en radianes (PI/2) y se la restamos al ángulo con el que estamos trabajando antes de utilizarlo para calcular la posición.

```kotlin
val rot = PI/2
//...
x = centro.first + radio*cos(angulo*i + rot)
y = centro.second + radio*sin(angulo*i + rot)
```
<img src="https://user-images.githubusercontent.com/92323990/162573006-bd37fa88-a615-4c7f-9354-7700bb9d4ea3.png" width="30%" height="30%"/> <img src="https://user-images.githubusercontent.com/92323990/162573048-2286a940-8860-4a28-9ca3-31666cf43874.png" width="30%" height="30%"/>

### A PARTIR DE AQUI NO ESTA TERMINADO. NO LEER MAS ALLA DE ESTE PUNTO.

### Anillo 1.1: Puntos en un arco
Aunque pueda parecer que el sistema ya funciona, tenemos que tener en cuenta que lo que buscamos no es realmente un círculo con todas las cartas de nuestra lista, sino las dos primeras cartas presentadas frontalmente al jugador y el resto de cartas distribuídas equitativamente a lo largo de un arco que se posicione detrás. Para conseguirlo, vamos a colocar manualmente los dos primeros elementos y cambiar el total de 360º del círculo por una fracción del mismo. 

![image](https://user-images.githubusercontent.com/92323990/162575697-85da7a31-336b-4836-ba9d-18701c55c924.png)

El problema es que ahora nuestro anillo ha perdido su orientación. Además, tenemos otro problema, que podemos ver claramente si añadimos una constante de perspectiva (simplemente un número por el cual dividiremos todos nuestros valores de Y, para achatar el círculo verticalmente). ¡Las cartas no se dibujan en el orden correcto, por lo que no se solapan como queremos que lo hagan! 


