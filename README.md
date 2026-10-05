# Registro de Incidencias

Este proyecto es una aplicación Android desarrollada para registrar problemas relacionados con computadoras y equipos informáticos.

La aplicación permite ingresar información básica sobre una falla, clasificar el tipo de problema, seleccionar su prioridad y generar un reporte con retroalimentación para el usuario.

## Funciones

La aplicación permite:

- Ingresar el título del problema.
- Escribir una descripción de la falla.
- Clasificar el problema como:
    - Hardware
    - Software
    - Red
- Seleccionar la prioridad del problema:
    - Baja
    - Media
    - Alta
- Mostrar visualmente la opción seleccionada.
- Registrar un problema mediante un botón.
- Mostrar un mensaje de confirmación al crear el reporte.
- Avisar al usuario cuando alguno de los campos obligatorios está vacío.
- Mostrar un logo en la pantalla principal.
- Utilizar un fondo gris claro en la interfaz.

## Entrada de texto

Los campos de entrada fueron configurados utilizando `KeyboardOptions` para mejorar la experiencia del usuario.

En el campo **Título del problema** se utiliza:

- `KeyboardType.Text`
- `KeyboardCapitalization.Sentences`
- `ImeAction.Next`

Esto permite escribir texto, iniciar las frases con mayúscula y avanzar al siguiente campo desde el teclado.

En el campo **Descripción del problema** se utiliza:

- `KeyboardType.Text`
- `KeyboardCapitalization.Sentences`
- `ImeAction.Done`

La acción `Done` permite finalizar la escritura y retirar el foco del campo.

## Interacción táctil

La aplicación incorpora interacción táctil para seleccionar el tipo de problema y su prioridad.

Las opciones se implementaron mediante componentes `Card` y el modificador:

`Modifier.clickable`

Este mecanismo permite detectar un toque simple sobre cada opción.

No fue necesario utilizar `pointerInput`, debido a que la aplicación no requiere gestos personalizados como arrastrar, deslizar o detectar movimientos complejos.

Cuando el usuario toca una opción, el estado cambia inmediatamente y se muestra una marca de selección.

Ejemplo:

- `✓ Hardware`
- `✓ Alta`

## Estado de la aplicación

La aplicación utiliza variables de estado para guardar temporalmente la información ingresada y las opciones seleccionadas.

Se utilizan principalmente:

- `remember`
- `mutableStateOf`

Las variables almacenan temporalmente:

- Título del problema.
- Descripción.
- Tipo de problema.
- Prioridad.
- Mensaje de retroalimentación.

Por el momento, la información no se guarda en una base de datos.

## Diseño

La pantalla principal contiene:

- Logo de la aplicación.
- Título **Registro de Incidencias**
- Instrucción para el usuario.
- Campo para el título del problema.
- Selección del tipo de problema.
- Campo para la descripción.
- Selección de prioridad.
- Indicador del tipo de problema seleccionado.
- Indicador de la prioridad seleccionada.
- Botón **Registrar problema**.
- Mensaje de retroalimentación.
- Fondo gris claro.

## Flujo de uso

1. Abrir la aplicación.
2. Escribir el título del problema.
3. Presionar **Next** en el teclado para continuar.
4. Seleccionar el tipo de problema: Hardware, Software o Red.
5. Escribir la descripción de la falla.
6. Presionar **Done** al finalizar la escritura.
7. Seleccionar la prioridad: Baja, Media o Alta.
8. Presionar el botón **Registrar problema**.
9. La aplicación mostrará un mensaje con el resultado del reporte.

Ejemplo:

```text
Reporte creado correctamente
Problema: Computadora no enciende
Tipo: Hardware
Prioridad: Alta
```

## Avance de la semana

Durante esta semana se realizaron las siguientes mejoras:

- Se configuró el teclado contextual de los campos de texto.
- Se agregó `KeyboardCapitalization.Sentences`.
- Se incorporó `ImeAction.Next`.
- Se incorporó `ImeAction.Done`.
- Se agregó manejo del foco entre los campos.
- Se incorporó selección táctil del tipo de problema.
- Se agregó selección táctil de prioridad.
- Se utilizó `Modifier.clickable` para las interacciones táctiles.
- Se agregó retroalimentación visual mediante cambios de estado.
- Se adaptó la aplicación específicamente para registrar problemas de computadoras.

## Dificultad resuelta

Una de las dificultades fue controlar el comportamiento del teclado y el cambio de foco entre los campos.

Esto se resolvió utilizando:

- `KeyboardActions`
- `LocalFocusManager`
- `FocusDirection.Down`

También fue necesario implementar una forma sencilla de seleccionar opciones mediante toque. Para ello se utilizó `Modifier.clickable`.

## Tecnologías utilizadas

- Kotlin
- Android Studio
- Jetpack Compose
- Material Design 3
- Git
- GitHub

## Cómo ejecutar el proyecto

1. Abrir el proyecto en Android Studio.
2. Esperar que Gradle termine de cargar.
3. Verificar que el proyecto compile correctamente.
4. Seleccionar un emulador o dispositivo Android.
5. Presionar **Run**.
6. Probar los campos de texto y las opciones táctiles.

## Control de versiones

Antes de trabajar desde otro equipo se recomienda ejecutar:

```bash
git pull
```

Después de realizar los cambios:

```bash
git add .
git commit -m "Agregar teclado contextual y selección táctil de prioridad"
git push
```

## Autor

Juan Carlos Hernández

Proyecto realizado con fines académicos.
