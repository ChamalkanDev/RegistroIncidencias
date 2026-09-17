# Registro de Incidencias

Este proyecto es una aplicación Android creada para registrar incidencias o mantenimientos de equipos.

## Funciones

La aplicación permite:

- Ingresar el título de una incidencia.
- Escribir una descripción breve.
- Mostrar un logo en la pantalla principal.
- Crear un reporte de forma básica.
- Mostrar un mensaje cuando se presiona el botón.
- Avisar si algún campo está vacío.
- Utilizar un fondo gris en la pantalla principal.

## Estado de la aplicación

La aplicación utiliza variables de estado para guardar temporalmente lo que escribe el usuario.

Se utilizan:

- `remember`
- `mutableStateOf`

Por el momento, la información no se guarda en una base de datos.

## Diseño

La pantalla principal contiene:

- Logo de la aplicación.
- Título de la aplicación.
- Instrucción para el usuario.
- Campo para el título de la incidencia.
- Campo para la descripción.
- Botón **Crear reporte**.
- Mensaje de estado.
- Fondo gris claro.

## Tecnologías usadas

- Kotlin
- Android Studio
- Jetpack Compose
- Material Design 3
- Git
- GitHub

## Cómo usar la aplicación

1. Abrir la aplicación.
2. Escribir el título de la incidencia.
3. Escribir una descripción breve.
4. Presionar el botón **Crear reporte**.
5. La aplicación mostrará un mensaje con el resultado.

## Cómo ejecutar el proyecto

1. Abrir el proyecto en Android Studio.
2. Esperar que Gradle termine de cargar.
3. Seleccionar un emulador o dispositivo Android.
4. Presionar **Run**.

## Autor

Juan Carlos Hernández

Proyecto realizado con fines académicos.