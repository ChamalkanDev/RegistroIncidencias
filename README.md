# Registro de Incidencias

Este proyecto es una aplicación Android desarrollada en Kotlin utilizando Jetpack Compose, con el objetivo de registrar problemas relacionados con computadoras y equipos informáticos.

La aplicación permite ingresar información sobre una falla, seleccionar el tipo de problema, establecer su prioridad y generar un reporte con un mensaje de confirmación.

Como parte de una nueva práctica, se agregó el uso del **acelerómetro del teléfono Android**, que permite detectar los movimientos del dispositivo e identificar su orientación aproximada.

Con esta mejora se busca conocer cómo funcionan los sensores de Android y cómo se pueden integrar en una aplicación.

## Funciones

La aplicación permite:

- Ingresar el título de un problema.
- Escribir una descripción de la falla.
- Clasificar el problema como Hardware, Software o Red.
- Seleccionar la prioridad: Baja, Media o Alta.
- Mostrar visualmente las opciones seleccionadas.
- Registrar un problema mediante un botón.
- Mostrar un mensaje de confirmación al crear el reporte.
- Avisar cuando alguno de los campos obligatorios está vacío.
- Mostrar un logo en la pantalla principal.
- Utilizar un fondo gris claro en la interfaz.
- Detectar movimientos e inclinaciones del teléfono utilizando el acelerómetro.
- Mostrar los valores de aceleración de los ejes X, Y y Z.
- Identificar aproximadamente la orientación del dispositivo.
- Mostrar la orientación del teléfono en un panel inferior.

## Entrada de texto

Los campos de entrada fueron configurados utilizando `KeyboardOptions` para mejorar la experiencia del usuario al escribir.

En el campo **Título del problema** se utiliza:

- `KeyboardType.Text`
- `KeyboardCapitalization.Sentences`
- `ImeAction.Next`

Estas opciones permiten ingresar texto, iniciar las oraciones con mayúscula y avanzar al siguiente campo mediante el teclado.

En el campo **Descripción del problema** se utiliza:

- `KeyboardType.Text`
- `KeyboardCapitalization.Sentences`
- `ImeAction.Done`

La opción `Done` permite finalizar la escritura y retirar el foco del campo.

También se utilizaron `KeyboardActions` y `LocalFocusManager` para controlar el comportamiento del teclado.

## Interacción táctil

La aplicación incorpora interacción táctil para seleccionar el tipo de problema y su prioridad.

Las opciones se implementaron utilizando componentes `Card` y el modificador:

`Modifier.clickable`

Este modificador permite detectar cuando el usuario toca una opción.

Cuando se selecciona una categoría o prioridad, el estado cambia y se muestra una marca de selección.

Por ejemplo:

- `✓ Hardware`
- `✓ Alta`

Para estas opciones no fue necesario utilizar `pointerInput`, porque solamente se requiere detectar un toque simple.

## Uso del sensor acelerómetro

Durante esta práctica se incorporó el acelerómetro del teléfono Android para detectar sus movimientos e identificar su orientación aproximada.

El acelerómetro es un sensor que mide la aceleración en tres ejes, incluyendo el efecto de la gravedad.

Los ejes utilizados son:

- **Eje X:** mide la aceleración hacia los lados del teléfono.
- **Eje Y:** mide la aceleración a lo largo del teléfono.
- **Eje Z:** mide la aceleración perpendicular a la pantalla.

Los valores recibidos permiten estimar la inclinación y orientación del dispositivo.

### Implementación del acelerómetro

Para implementar el sensor se utilizaron las siguientes clases:

- `SensorManager`
- `Sensor`
- `SensorEvent`
- `SensorEventListener`

Se utilizó `Sensor.TYPE_ACCELEROMETER` para obtener el acelerómetro disponible en el dispositivo.

El sensor se obtiene mediante el siguiente código:

```kotlin
val sensorManager = remember {
    context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
}

val acelerometro = remember {
    sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
}
```

Para recibir los datos se implementó `SensorEventListener`, que permite detectar los cambios de aceleración.

Los valores se obtienen de la siguiente manera:

```kotlin
override fun onSensorChanged(event: SensorEvent) {
    ejeX = event.values[0]
    ejeY = event.values[1]
    ejeZ = event.values[2]
}
```

Estos valores se guardan en variables de estado de Jetpack Compose, permitiendo que la interfaz se actualice cuando cambian.

También se utilizó `DisposableEffect` para registrar el sensor y eliminar el listener cuando el efecto deja de utilizarse.

### Posiciones que identifica la aplicación

Además de mostrar los valores X, Y y Z, la aplicación analiza estos datos para identificar aproximadamente la posición del teléfono.

Las posiciones que reconoce son:

1. **Plano, pantalla hacia arriba:** cuando el teléfono está colocado horizontalmente con la pantalla hacia arriba.
2. **Plano, pantalla hacia abajo:** cuando el teléfono está colocado horizontalmente con la pantalla hacia abajo.
3. **Vertical:** cuando el teléfono se encuentra en posición vertical normal.
4. **Vertical invertido:** cuando el teléfono está verticalmente colocado al revés.
5. **Horizontal (lado izquierdo abajo):** cuando el teléfono se gira hacia uno de sus lados.
6. **Horizontal (lado derecho abajo):** cuando el teléfono se gira hacia el lado contrario.

Para determinar estas posiciones se utilizan comparaciones entre los valores de los ejes X, Y y Z.

También se utiliza la función `abs()` para obtener valores absolutos y comparar las aceleraciones.

La aplicación muestra la posición detectada en un panel ubicado en la parte inferior de la pantalla.

### Panel de posición del teléfono

Se creó un componente llamado `PanelPosicion`, que se encarga de mostrar la información del acelerómetro.

Este panel contiene:

- Título **Posición del teléfono**.
- Texto con la orientación aproximada detectada.
- Valor del eje X.
- Valor del eje Y.
- Valor del eje Z.

Los valores se presentan con dos cifras decimales para facilitar su lectura.

Ejemplo de visualización:

```text
Posición del teléfono

Plano, pantalla hacia arriba

X: 0.12   Y: 0.35   Z: 9.75
```

Los valores anteriores son solamente un ejemplo. Los resultados reales cambian según el movimiento y la inclinación del teléfono.

### Manejo del sensor

Para evitar problemas con el registro del sensor se utilizó `DisposableEffect`.

Dentro de este bloque se registra el listener mediante `registerListener` y se elimina utilizando `unregisterListener`.

También se verifica que el dispositivo tenga un acelerómetro disponible.

Si el teléfono no cuenta con este sensor, la aplicación muestra el mensaje:

`Sensor no disponible`

De esta manera se evita intentar registrar un sensor inexistente.

## Pruebas del acelerómetro

Para verificar el funcionamiento del acelerómetro se debe ejecutar la aplicación en un teléfono Android que tenga este sensor.

El procedimiento de prueba es el siguiente:

1. Abrir la aplicación Registro de Incidencias.
2. Observar el panel de posición ubicado en la parte inferior.
3. Revisar los valores de los ejes X, Y y Z.
4. Colocar el teléfono sobre una superficie plana con la pantalla hacia arriba.
5. Girarlo para colocar la pantalla hacia abajo.
6. Sostener el teléfono verticalmente.
7. Girarlo en posición vertical invertida.
8. Inclinarlo hacia el lado izquierdo.
9. Inclinarlo hacia el lado derecho.
10. Verificar que los valores y el texto de posición respondan a los cambios de orientación.

Estas pruebas permiten comprobar la lectura del sensor y verificar si la orientación mostrada corresponde aproximadamente con la posición del dispositivo.

## Propósito del acelerómetro

El propósito de esta práctica es aprender a utilizar uno de los sensores físicos disponibles en los dispositivos Android.

Con esta actividad se busca comprender cómo obtener información del acelerómetro, interpretar sus valores y mostrar los resultados en una interfaz desarrollada con Jetpack Compose.

También permite conocer nuevas funciones que pueden incorporarse en aplicaciones móviles utilizando sensores.

## Limitaciones del acelerómetro

El acelerómetro permite medir aceleración y estimar la inclinación del dispositivo tomando como referencia la gravedad.

Sin embargo, tiene algunas limitaciones:

- No permite conocer la ubicación geográfica del teléfono.
- No proporciona directamente una orientación exacta.
- Los valores pueden cambiar rápidamente cuando el dispositivo se mueve.
- La orientación mostrada puede ser menos precisa en posiciones intermedias.
- Se necesita un dispositivo con acelerómetro o un emulador que permita simularlo.

La aplicación utiliza comparaciones sencillas de los valores del sensor, por lo que la posición identificada es aproximada.

## Próxima mejora

Como mejora futura se podría incorporar un indicador gráfico que represente la inclinación del teléfono.

También se podría combinar el acelerómetro con otros sensores, como el giroscopio, para mejorar la precisión de la orientación.

Otra mejora sería optimizar el manejo del sensor para que su lectura se suspenda cuando la aplicación pase a segundo plano.

## Estado de la aplicación

La aplicación utiliza variables de estado para almacenar temporalmente la información ingresada y las opciones seleccionadas.

Se utilizan principalmente:

- `remember`
- `mutableStateOf`

Las variables almacenan:

- Título del problema.
- Descripción de la falla.
- Tipo de problema.
- Prioridad.
- Mensaje de retroalimentación.
- Valores del acelerómetro en los ejes X, Y y Z.

La aplicación también calcula la orientación aproximada utilizando los valores recibidos del sensor.

Por el momento, los reportes no se guardan en una base de datos.

## Diseño de la aplicación

La pantalla principal contiene:

- Logo de la aplicación.
- Título **Registro de Incidencias**.
- Instrucción para el usuario.
- Campo para el título del problema.
- Selección del tipo de problema.
- Campo para la descripción.
- Selección de prioridad.
- Indicador del tipo de problema seleccionado.
- Indicador de la prioridad seleccionada.
- Botón **Registrar problema**.
- Mensaje de retroalimentación.
- Panel inferior con la posición del teléfono.
- Valores X, Y y Z del acelerómetro.
- Fondo gris claro.

Se utilizaron componentes de Material Design 3 para mantener una interfaz sencilla y organizada.

## Flujo de uso

1. Abrir la aplicación.
2. Escribir el título del problema.
3. Presionar **Next** en el teclado para continuar.
4. Seleccionar Hardware, Software o Red.
5. Escribir la descripción de la falla.
6. Presionar **Done** al finalizar la escritura.
7. Seleccionar la prioridad: Baja, Media o Alta.
8. Presionar el botón **Registrar problema**.
9. Observar el mensaje de confirmación.
10. Revisar el panel del acelerómetro.
11. Mover o inclinar el teléfono para observar los cambios de orientación.

Ejemplo de reporte:

```text
Reporte creado correctamente
Problema: Computadora no enciende
Tipo: Hardware
Prioridad: Alta
```

## Avances del proyecto

Durante el desarrollo se realizaron las siguientes mejoras:

- Se configuró el teclado contextual de los campos.
- Se agregó `KeyboardCapitalization.Sentences`.
- Se incorporó `ImeAction.Next`.
- Se incorporó `ImeAction.Done`.
- Se agregó manejo del foco entre los campos.
- Se incorporó selección táctil del tipo de problema.
- Se agregó selección táctil de prioridad.
- Se utilizó `Modifier.clickable`.
- Se agregó retroalimentación visual mediante cambios de estado.
- Se adaptó la aplicación para registrar problemas de computadoras.
- Se incorporó el acelerómetro del teléfono Android.
- Se implementó la lectura de los ejes X, Y y Z.
- Se agregó el reconocimiento aproximado de seis orientaciones del dispositivo.
- Se creó un panel inferior para mostrar los datos del sensor.
- Se utilizó `DisposableEffect` para controlar el registro y la eliminación del listener.
- Se agregó la verificación de disponibilidad del acelerómetro.

## Dificultades encontradas y cómo fueron abordadas

Durante el desarrollo se presentaron algunas dificultades relacionadas con el manejo del teclado, la interacción táctil y la integración del acelerómetro.

### Manejo del teclado

Una de las primeras dificultades fue controlar el cambio de foco entre los campos del formulario.

Esto se resolvió utilizando:

- `KeyboardActions`
- `LocalFocusManager`
- `FocusDirection.Down`

Estas herramientas permitieron mejorar la navegación mediante el teclado.

### Interacción táctil

También fue necesario implementar una forma sencilla de seleccionar las categorías y prioridades.

Para resolverlo se utilizó `Modifier.clickable`, que permite detectar los toques del usuario y actualizar las opciones seleccionadas.

### Integración del acelerómetro

Una de las principales dificultades de la nueva práctica fue comprender cómo obtener los valores del acelerómetro y utilizarlos para identificar la posición del teléfono.

Para abordar esta dificultad se investigó el funcionamiento de `SensorManager` y `SensorEventListener`.

Posteriormente se implementó la lectura de los tres ejes mediante `onSensorChanged`.

También se utilizaron variables de estado para mostrar los cambios en la pantalla.

Otra parte importante fue interpretar los valores del sensor para mostrar una orientación comprensible para el usuario.

Esto se resolvió utilizando condiciones que comparan las aceleraciones de los diferentes ejes.

Finalmente, se implementó `DisposableEffect` para administrar el listener del sensor dentro de Jetpack Compose.

## Tecnologías utilizadas

- Kotlin.
- Android Studio.
- Jetpack Compose.
- Material Design 3.
- Android Sensor Framework.
- SensorManager.
- SensorEventListener.
- Git.
- GitHub.

## Cómo ejecutar el proyecto

1. Abrir el proyecto en Android Studio.
2. Esperar que Gradle termine de cargar.
3. Verificar que el proyecto compile correctamente.
4. Conectar un teléfono Android con acelerómetro o configurar un emulador compatible.
5. Seleccionar el dispositivo de ejecución.
6. Presionar **Run**.
7. Probar los campos del formulario.
8. Seleccionar el tipo de problema y la prioridad.
9. Registrar un problema.
10. Revisar el panel de posición del teléfono.
11. Mover el dispositivo y observar los valores del acelerómetro.
12. Comprobar que el texto de orientación cambie según la posición.

## Control de versiones

Para mantener actualizado el proyecto en GitHub se utiliza Git.

Antes de comenzar a trabajar desde otro equipo se recomienda ejecutar:

```bash
git pull
```

Después de modificar el código y actualizar el README se utilizan los siguientes comandos:

```bash
git add .
git commit -m "Agregar acelerometro y deteccion de posicion del telefono"
git push
```

Estos comandos permiten registrar los cambios realizados y subirlos al repositorio de GitHub.

La actualización incluye la integración del acelerómetro, la lectura de los ejes X, Y y Z y la detección aproximada de la orientación del teléfono.

## Conclusión

Con esta práctica se logró ampliar las funciones de la aplicación Registro de Incidencias mediante la integración del acelerómetro de un teléfono Android.

Durante el desarrollo se aprendió a obtener los valores de aceleración de los ejes X, Y y Z utilizando `SensorManager` y `SensorEventListener`.

También se implementó una función que interpreta estos valores para identificar aproximadamente si el teléfono está plano, vertical o en posición horizontal.

Esta actividad permitió comprender mejor el funcionamiento de los sensores Android y cómo se pueden utilizar junto con Jetpack Compose para agregar nuevas funciones a una aplicación móvil.

## Autor

**Juan Carlos Hernández**

Proyecto realizado con fines académicos.