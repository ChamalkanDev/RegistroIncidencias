# Registro de Incidencias

Este proyecto es una aplicación Android desarrollada en Kotlin utilizando Jetpack Compose, con el objetivo de registrar problemas relacionados con computadoras y equipos informáticos.

La aplicación permite ingresar información sobre una falla, seleccionar el tipo de problema, establecer su prioridad y generar un reporte con un mensaje de confirmación.

Como parte de una nueva práctica, se agregó una sección de **Diagnóstico** con una lista de hasta cinco sensores disponibles. Cada sensor tiene una pantalla de prueba donde se muestran sus datos y un mensaje que ayuda a entender la lectura.

Con esta mejora se busca conocer cómo funcionan los sensores de Android, cómo mostrar sus lecturas y cómo detener la escucha cuando ya no se necesita. Las pruebas duran como máximo **cinco segundos**.

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
- Mostrar la orientación y los ejes del acelerómetro dentro de su pantalla de prueba.
- Acceder al diagnóstico mediante un botón en la pantalla principal.
- Mostrar un botón por cada sensor disponible, con un máximo de cinco.
- Abrir una pantalla independiente para probar el sensor seleccionado.
- Consultar el sensor de luz, proximidad, giroscopio y campo magnético, si están disponibles.
- Mostrar mensajes como “Movimiento detectado”, “Objeto cercano” o “Ambiente con mucha luz”.
- Detener la escucha después de cinco segundos, al salir de la prueba o al pausar la aplicación.
- Repetir la prueba y regresar a la lista de sensores.

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

## Diagnóstico del dispositivo

La sección de diagnóstico se organizó en tres pantallas:

1. **Pantalla principal:** contiene el formulario y el botón **Diagnóstico**. El acelerómetro ya no se muestra ni se escucha desde esta pantalla.
2. **Lista de sensores:** muestra hasta cinco botones, uno por cada sensor disponible entre los seleccionados para la práctica. En esta pantalla todavía no se reciben lecturas.
3. **Pantalla de prueba:** se abre al tocar un sensor. Muestra su nombre, sus datos, un mensaje de interpretación y los botones **Repetir prueba** y **Volver a sensores**.

Desde la lista se puede pulsar **Volver al inicio**. El botón Atrás del teléfono también permite regresar de la prueba a la lista y de la lista al formulario.

Se utilizaron funciones `@Composable` y variables de estado para cambiar de pantalla de una forma sencilla:

- `PantallaDiagnostico`: controla si se muestra la lista o la prueba.
- `PantallaListaSensores`: presenta los botones de los sensores disponibles.
- `PantallaPruebaSensor`: muestra la pantalla del sensor seleccionado.
- `PruebaSensor`: recibe e interpreta las lecturas y controla cuándo se detienen.

### Sensores incluidos

| Sensor | Para qué se utiliza | Información que muestra |
| --- | --- | --- |
| Acelerómetro | Detectar movimiento y estimar la orientación del teléfono. | Ejes X, Y y Z en m/s², orientación aproximada y mensajes de movimiento. |
| Luz | Reconocer la iluminación del ambiente. | Lectura en lux y mensajes de poca luz, luz moderada o mucha luz. |
| Proximidad | Detectar objetos cercanos al dispositivo. | Mensajes como “Objeto cercano” o “Sin objeto cercano”. |
| Giroscopio | Detectar giros del teléfono. | Valores de los tres ejes en rad/s y mensajes de giro. |
| Campo magnético | Medir el campo magnético alrededor del teléfono. | Valores de los tres ejes en microteslas y el mensaje “Campo magnético detectado”. |

La aplicación revisa cada tipo mediante `getDefaultSensor`. Si el equipo no tiene alguno, ese sensor no aparece en la lista. Por eso pueden mostrarse menos de cinco botones. Si no se encuentra ninguno de los cinco tipos, se muestra un mensaje indicando que no hay sensores disponibles.

### Cómo entrar a una prueba

1. Abrir la aplicación y pulsar **Diagnóstico**.
2. Elegir uno de los sensores que aparecen en la lista.
3. Realizar la acción correspondiente, como mover el teléfono o cubrir el sensor de luz.
4. Observar los datos y el mensaje durante los cinco segundos de la prueba.
5. Pulsar **Repetir prueba** si se necesita otra lectura.
6. Pulsar **Volver a sensores** para elegir otro sensor.

## Uso del sensor acelerómetro

El acelerómetro se utiliza dentro de **Diagnóstico → Acelerómetro** para detectar movimientos e identificar la orientación aproximada del teléfono.

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

La aplicación muestra la orientación detectada en el panel `PanelPosicion`, dentro de la pantalla de prueba del acelerómetro. Este panel no aparece en el formulario principal.

### Panel de posición del teléfono

El componente `PanelPosicion` muestra la información del acelerómetro usando las mismas lecturas de la prueba, sin registrar un segundo listener.

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

## Registro y liberación de los sensores

Para evitar que los sensores se queden escuchando indefinidamente, la aplicación registra solamente el sensor seleccionado cuando se abre su pantalla de prueba.

El proceso es el siguiente:

1. Obtener el sensor disponible con `SensorManager`.
2. Registrar un `SensorEventListener` con `registerListener` y `SENSOR_DELAY_NORMAL`.
3. Recibir los datos en `onSensorChanged` y actualizar la pantalla.
4. Programar con un `Handler` la función `detener` después de **5000 milisegundos**.
5. Llamar a `unregisterListener(listener)` para dejar de recibir eventos y cancelar las tareas pendientes del temporizador.

El temporizador no se reinicia cada vez que llega una lectura. Al finalizar, el último resultado permanece visible, pero la aplicación deja de recibir datos nuevos.

También se libera el recurso en estos casos:

- Al pausar o minimizar la aplicación, mediante `Lifecycle.Event.ON_PAUSE`.
- Al salir de la pantalla de prueba, mediante `onDispose` de `DisposableEffect`.
- Al repetir la prueba, liberando primero la escucha anterior.

Después de una pausa, la lectura no se reactiva automáticamente: el usuario puede pulsar **Repetir prueba**. Esto evita mantener escuchas innecesarias o acumular varios listeners.

Si el sensor no entrega datos durante la prueba, aparece **Sin lectura disponible**. Si el registro requiere un permiso que no se tiene, la aplicación también informa que no puede obtener la lectura.

## Pruebas del acelerómetro

Para comprobar el acelerómetro se puede usar un teléfono compatible o un emulador que permita simularlo.

El procedimiento propuesto es el siguiente:

1. Abrir la aplicación y pulsar **Diagnóstico**.
2. Seleccionar **Acelerómetro**.
3. Revisar los valores X, Y y Z en la pantalla de prueba.
4. Colocar el teléfono sobre una superficie plana, primero con la pantalla hacia arriba y después hacia abajo.
5. Sostenerlo verticalmente y luego en posición vertical invertida.
6. Girarlo hacia el lado izquierdo y hacia el derecho.
7. Moverlo suavemente y observar los mensajes de movimiento.
8. Verificar que los datos dejen de cambiar al finalizar los cinco segundos.
9. Pulsar **Repetir prueba** para comprobar otra orientación o movimiento.

Estas acciones permiten comparar la orientación mostrada con la forma en que está colocado el dispositivo. Los valores recibidos incluyen la gravedad, por lo que pueden ser distintos de cero aunque el teléfono esté quieto.

## Pruebas de los otros sensores

- **Luz:** cubrir y descubrir el sensor o cambiar la iluminación; observar la lectura en lux y su mensaje.
- **Proximidad:** acercar y retirar la mano de la parte superior del teléfono; revisar si cambia entre cerca y lejos.
- **Giroscopio:** girar el teléfono suavemente; observar los valores y el mensaje de giro.
- **Campo magnético:** cambiar la orientación del teléfono y comparar las lecturas en lugares con distintas interferencias.

Para todos los sensores se debe comprobar que la prueba se detenga al terminar los cinco segundos, volver a la lista o minimizar la aplicación. También conviene repetirla varias veces para revisar que no se acumulen escuchas.

Las acciones anteriores son una guía de verificación. Para documentar una prueba realizada, se debe indicar el dispositivo utilizado, el sensor, la acción, el resultado observado y una captura de pantalla.

## Uso del emulador y sus limitaciones

El emulador de Android permite simular algunos sensores desde **Extended controls → Virtual sensors**. Los controles disponibles dependen del dispositivo virtual y su configuración.

Sus principales limitaciones para esta práctica son:

- Los valores son simulados y no representan directamente el ambiente de la computadora.
- Acercar la mano al monitor o mover el mouse no equivale a activar los sensores de un teléfono.
- Algunos sensores pueden no estar disponibles en el dispositivo virtual.
- La simulación no confirma por sí sola la precisión ni las interferencias de un sensor físico.

En un teléfono real probaría movimientos, giros, cambios de iluminación y acercamiento de objetos. También comprobaría la liberación del recurso al salir de la prueba y al pausar la aplicación.

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

Otra mejora sería guardar un pequeño historial de las pruebas para comparar sus resultados. La suspensión de la lectura al pasar a segundo plano ya está implementada.

## Estado de la aplicación

La aplicación utiliza variables de estado para almacenar temporalmente la información ingresada y las opciones seleccionadas.

Se utilizan principalmente:

- `remember`
- `rememberSaveable`
- `mutableStateOf`
- `mutableFloatStateOf`
- `mutableIntStateOf`

Las variables almacenan:

- Título del problema.
- Descripción de la falla.
- Tipo de problema.
- Prioridad.
- Mensaje de retroalimentación.
- Pantalla de diagnóstico y sensor seleccionado.
- Intento de prueba y mensaje del sensor.
- Valores del acelerómetro en los ejes X, Y y Z.

La aplicación también calcula la orientación aproximada utilizando los valores del acelerómetro. Los campos del formulario usan `rememberSaveable` para conservar la información al volver de diagnóstico y ante recreaciones de la actividad.

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
- Botón **Diagnóstico** para entrar a la lista de sensores.
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
10. Pulsar **Diagnóstico** para abrir la lista de sensores.
11. Seleccionar un sensor para abrir su pantalla de prueba.
12. Realizar la prueba y observar los datos y mensajes.
13. Pulsar **Repetir prueba** o **Volver a sensores** según se necesite.

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
- Se creó un panel para mostrar los datos del acelerómetro y se trasladó a su pantalla de prueba.
- Se utilizó `DisposableEffect` para controlar el registro y la eliminación del listener.
- Se agregó la verificación de disponibilidad de los cinco tipos de sensores.
- Se agregó el botón **Diagnóstico** a la pantalla principal.
- Se separaron la lista de sensores y la pantalla de prueba individual.
- Se incorporaron luz, proximidad, giroscopio y campo magnético.
- Se agregaron mensajes para interpretar las lecturas.
- Se limitaron las pruebas a cinco segundos.
- Se agregó la liberación del recurso al pausar la aplicación o salir de la prueba.
- Se incorporaron los botones para repetir las pruebas y regresar entre pantallas.

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

### Pantallas de diagnóstico y tiempo de escucha

Fue necesario separar la lista de sensores de la pantalla donde se realiza cada prueba. Para hacerlo se utilizaron variables de estado y funciones `@Composable`.

También se agregó un límite de cinco segundos con `Handler` y una función `detener` que libera el listener. El ciclo de vida y `onDispose` permiten detener la lectura cuando la pantalla deja de usarse.

## Tecnologías utilizadas

- Kotlin.
- Android Studio.
- Jetpack Compose.
- Material Design 3.
- Android Sensor Framework.
- SensorManager.
- SensorEventListener.
- LifecycleEventObserver.
- DisposableEffect.
- Handler.
- Git.
- GitHub.

## Cómo ejecutar el proyecto

1. Abrir el proyecto en Android Studio.
2. Esperar que Gradle termine de cargar.
3. Verificar que el proyecto compile correctamente.
4. Conectar un teléfono Android o configurar un emulador compatible con sensores virtuales.
5. Seleccionar el dispositivo de ejecución.
6. Presionar **Run**.
7. Probar los campos del formulario.
8. Seleccionar el tipo de problema y la prioridad.
9. Registrar un problema.
10. Pulsar **Diagnóstico** y revisar los sensores disponibles.
11. Seleccionar un sensor para abrir su pantalla de prueba.
12. Realizar la acción correspondiente y observar sus datos.
13. Comprobar el límite de cinco segundos y los botones para repetir y volver.

## Control de versiones

Para mantener actualizado el proyecto en GitHub se utiliza Git.

Antes de comenzar a trabajar desde otro equipo se recomienda ejecutar:

```bash
git pull
```

Después de modificar el código y actualizar el README se utilizan los siguientes comandos:

```bash
git add .
git commit -m "Agregar pantallas de diagnostico y pruebas de sensores"
git push
```

Estos comandos permiten registrar los cambios realizados y subirlos al repositorio de GitHub.

Esta actualización incluye las pantallas de diagnóstico, la lista de hasta cinco sensores, las pruebas individuales y el control del tiempo de escucha. Los comandos anteriores se ejecutan desde la carpeta del repositorio cuando los cambios estén listos para subir.

## Conclusión

Con esta práctica se amplió la aplicación Registro de Incidencias mediante una sección de diagnóstico con hasta cinco sensores disponibles.

Se organizaron tres pantallas: el formulario principal, la lista de sensores y la prueba del sensor seleccionado. Las lecturas se acompañan de mensajes sencillos para entender mejor los datos.

El acelerómetro permite observar los ejes X, Y y Z y la orientación aproximada del teléfono desde su pantalla de prueba.

También se incorporó un límite de cinco segundos y la liberación del recurso al pausar la aplicación o salir de la pantalla. Esto permitió comprender la importancia de registrar un sensor solamente cuando se necesita y detener su escucha al terminar.

## Autor

**Juan Carlos Hernández**

Proyecto realizado con fines académicos.
