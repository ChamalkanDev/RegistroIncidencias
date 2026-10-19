package aplicacion.registroincidencias

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.hardware.TriggerEvent
import android.hardware.TriggerEventListener
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.compose.BackHandler
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import aplicacion.registroincidencias.ui.theme.RegistroIncidenciasTheme
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.findViewTreeLifecycleOwner
import kotlin.math.sqrt
import kotlin.math.abs

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            RegistroIncidenciasTheme {
                RegistroIncidenciasApp()
            }
        }
    }
}

@Composable
fun RegistroIncidenciasApp() {

    // -----------------------------------------
    // Variables de estado
    // -----------------------------------------

    var titulo by rememberSaveable {
        mutableStateOf("")
    }

    var descripcion by rememberSaveable {
        mutableStateOf("")
    }

    var tipoProblema by rememberSaveable {
        mutableStateOf("Hardware")
    }

    var prioridad by rememberSaveable {
        mutableStateOf("Media")
    }

    var mensaje by rememberSaveable {
        mutableStateOf("Aún no hay reporte creado")
    }

    // Cambiamos de pantalla usando una variable sencilla.
    var mostrarDiagnostico by rememberSaveable { mutableStateOf(false) }

    if (mostrarDiagnostico) {
        PantallaDiagnostico(onVolver = { mostrarDiagnostico = false })
        return
    }

    // Control del teclado y del foco
    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE0E0E0))
            .safeDrawingPadding()
            .imePadding()
    ) {

        // -----------------------------------------
        // Contenido del formulario
        // -----------------------------------------

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),

            horizontalAlignment = Alignment.CenterHorizontally,

            verticalArrangement = Arrangement.Center
        ) {

            // -----------------------------------------
            // Logo
            // -----------------------------------------

            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo de la aplicación",
                modifier = Modifier.size(100.dp)
            )

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            // -----------------------------------------
            // Título principal
            // -----------------------------------------

            Text(
                text = "Registro de Incidencias",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            // -----------------------------------------
            // Instrucción
            // -----------------------------------------

            Text(
                text = "Complete los datos para registrar el problema."
            )

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            // -----------------------------------------
            // Campo: título del problema
            // -----------------------------------------

            OutlinedTextField(
                value = titulo,

                onValueChange = {
                    titulo = it
                },

                label = {
                    Text("Título del problema")
                },

                placeholder = {
                    Text("Ejemplo: Computadora no enciende")
                },

                modifier = Modifier.fillMaxWidth(),

                singleLine = true,

                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    capitalization = KeyboardCapitalization.Sentences,
                    imeAction = ImeAction.Next
                ),

                keyboardActions = KeyboardActions(
                    onNext = {
                        focusManager.moveFocus(
                            FocusDirection.Down
                        )
                    }
                )
            )

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            // -----------------------------------------
            // Tipo de problema
            // -----------------------------------------

            Text(
                text = "Tipo de problema",
                fontWeight = FontWeight.Bold
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),

                horizontalArrangement =
                    Arrangement.spacedBy(8.dp)
            ) {

                OpcionSeleccion(
                    texto = "Hardware",
                    seleccionada = tipoProblema == "Hardware",
                    modifier = Modifier.weight(1f)
                ) {
                    tipoProblema = "Hardware"

                    mensaje =
                        "Tipo seleccionado: Hardware"
                }

                OpcionSeleccion(
                    texto = "Software",
                    seleccionada = tipoProblema == "Software",
                    modifier = Modifier.weight(1f)
                ) {
                    tipoProblema = "Software"

                    mensaje =
                        "Tipo seleccionado: Software"
                }

                OpcionSeleccion(
                    texto = "Red",
                    seleccionada = tipoProblema == "Red",
                    modifier = Modifier.weight(1f)
                ) {
                    tipoProblema = "Red"

                    mensaje =
                        "Tipo seleccionado: Red"
                }
            }

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            // -----------------------------------------
            // Campo descripción
            // -----------------------------------------

            OutlinedTextField(
                value = descripcion,

                onValueChange = {
                    descripcion = it
                },

                label = {
                    Text("Descripción del problema")
                },

                placeholder = {
                    Text(
                        "Ejemplo: El equipo enciende pero no muestra imagen"
                    )
                },

                modifier = Modifier.fillMaxWidth(),

                minLines = 2,
                maxLines = 4,

                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    capitalization = KeyboardCapitalization.Sentences,
                    imeAction = ImeAction.Done
                ),

                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                    }
                )
            )

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            // -----------------------------------------
            // Prioridad
            // -----------------------------------------

            Text(
                text = "Prioridad",
                fontWeight = FontWeight.Bold
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),

                horizontalArrangement =
                    Arrangement.spacedBy(8.dp)
            ) {

                OpcionSeleccion(
                    texto = "Baja",
                    seleccionada = prioridad == "Baja",
                    modifier = Modifier.weight(1f)
                ) {
                    prioridad = "Baja"

                    mensaje =
                        "Prioridad seleccionada: Baja"
                }

                OpcionSeleccion(
                    texto = "Media",
                    seleccionada = prioridad == "Media",
                    modifier = Modifier.weight(1f)
                ) {
                    prioridad = "Media"

                    mensaje =
                        "Prioridad seleccionada: Media"
                }

                OpcionSeleccion(
                    texto = "Alta",
                    seleccionada = prioridad == "Alta",
                    modifier = Modifier.weight(1f)
                ) {
                    prioridad = "Alta"

                    mensaje =
                        "Prioridad seleccionada: Alta"
                }
            }

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            // -----------------------------------------
            // Estado de las selecciones
            // -----------------------------------------

            Text(
                text = "Tipo: $tipoProblema"
            )

            Text(
                text = "Prioridad: $prioridad"
            )

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            // -----------------------------------------
            // Botón principal
            // -----------------------------------------

            Button(
                onClick = {

                    focusManager.clearFocus()

                    if (
                        titulo.isNotBlank() &&
                        descripcion.isNotBlank()
                    ) {

                        mensaje =
                            "Reporte creado correctamente\n" +
                                    "Problema: $titulo\n" +
                                    "Tipo: $tipoProblema\n" +
                                    "Prioridad: $prioridad"

                    } else {

                        mensaje =
                            "Complete todos los campos"
                    }
                },

                modifier = Modifier.fillMaxWidth()
            ) {

                Text(
                    text = "Registrar problema"
                )
            }

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            // -----------------------------------------
            // Mensaje de retroalimentación
            // -----------------------------------------

            Text(
                text = mensaje
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Botón para abrir la lista de sensores.
            Button(
                onClick = {
                    focusManager.clearFocus()
                    mostrarDiagnostico = true
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Diagnóstico")
            }
        }
    }
}

// -----------------------------------------
// Componente táctil reutilizable
// -----------------------------------------

@Composable
fun OpcionSeleccion(
    texto: String,
    seleccionada: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {

    Card(
        modifier = modifier
            .clickable {
                onClick()
            },

        elevation = CardDefaults.cardElevation(
            defaultElevation =
                if (seleccionada) {
                    8.dp
                } else {
                    2.dp
                }
        )
    ) {

        Text(
            text =
                if (seleccionada) {
                    "✓ $texto"
                } else {
                    texto
                },

            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    vertical = 14.dp,
                    horizontal = 8.dp
                )
        )
    }
}

// -----------------------------------------
// Panel con la posición del teléfono
// -----------------------------------------

@Composable
fun PanelPosicion(
    x: Float,
    y: Float,
    z: Float,
    posicion: String
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),

        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),

            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "Posición del teléfono",
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = posicion,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "X: %.2f   Y: %.2f   Z: %.2f".format(x, y, z)
            )
        }
    }
}

// Cambiar entre la lista y la pantalla de prueba.
@Composable
fun PantallaDiagnostico(onVolver: () -> Unit) {
    val context = LocalContext.current
    val sensorManager = remember(context) {
        context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    }
    val sensores = remember(sensorManager) {
        // Máximo cinco tipos de sensores. Solo mostramos los disponibles.
        listOf(
            Sensor.TYPE_ACCELEROMETER,
            Sensor.TYPE_LIGHT,
            Sensor.TYPE_PROXIMITY,
            Sensor.TYPE_GYROSCOPE,
            Sensor.TYPE_MAGNETIC_FIELD
        ).mapNotNull { tipo -> sensorManager.getDefaultSensor(tipo) }
            .take(5)
    }
    var sensorSeleccionado by remember { mutableStateOf<Sensor?>(null) }

    val seleccionado = sensorSeleccionado
    if (seleccionado == null) {
        PantallaListaSensores(
            sensores = sensores,
            onVolver = onVolver,
            onSeleccionar = { sensorSeleccionado = it }
        )
    } else {
        PantallaPruebaSensor(
            sensor = seleccionado,
            onVolver = { sensorSeleccionado = null }
        )
    }
}

// Segunda pantalla: únicamente la lista de sensores y sus botones.
@Composable
fun PantallaListaSensores(
    sensores: List<Sensor>,
    onVolver: () -> Unit,
    onSeleccionar: (Sensor) -> Unit
) {
    BackHandler(onBack = onVolver)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE0E0E0))
            .safeDrawingPadding()
            .padding(16.dp)
    ) {
        Button(onClick = onVolver) { Text("Volver al inicio") }
        Text(
            text = "Sensores disponibles",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Text("Sensores para probar: ${sensores.size} (máximo 5)")
        Text("Pulsa un botón para abrir la pantalla de prueba de ese sensor.")
        Spacer(modifier = Modifier.height(12.dp))

        // En esta pantalla no se registra ningún listener.
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (sensores.isEmpty()) {
                Text("No hay sensores disponibles en este dispositivo.")
            }
            sensores.forEach { sensor ->
                Button(
                    onClick = { onSeleccionar(sensor) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(nombreSensor(sensor))
                }
            }
        }
    }
}

// Tercera pantalla: datos y mensajes del sensor que se seleccionó.
@Composable
fun PantallaPruebaSensor(sensor: Sensor, onVolver: () -> Unit) {
    var intento by remember { mutableIntStateOf(0) }
    BackHandler(onBack = onVolver)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE0E0E0))
            .safeDrawingPadding()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Button(onClick = onVolver) { Text("Volver a sensores") }
        Text(
            text = "Prueba de sensor",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Text("La prueba dura hasta 5 segundos y después deja de escuchar.")
        PruebaSensor(sensor = sensor, intento = intento)
        Button(
            onClick = { intento++ },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Repetir prueba")
        }
    }
}

@Composable
fun PruebaSensor(sensor: Sensor, intento: Int) {
    val context = LocalContext.current
    val sensorManager = remember(context) {
        context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    }
    val cicloDeVida = LocalView.current.findViewTreeLifecycleOwner()?.lifecycle
    var mensajePrueba by remember(sensor, intento) {
        mutableStateOf("Esperando lectura...")
    }
    var datos by remember(sensor, intento) { mutableStateOf("") }

    // Los ejes y la posición ahora pertenecen a la prueba del acelerómetro.
    var ejeX by remember(sensor, intento) { mutableFloatStateOf(0f) }
    var ejeY by remember(sensor, intento) { mutableFloatStateOf(0f) }
    var ejeZ by remember(sensor, intento) { mutableFloatStateOf(0f) }
    var hayLectura by remember(sensor, intento) { mutableStateOf(false) }

    val posicion = when {
        !hayLectura -> "Sin lectura disponible"
        abs(ejeZ) > 7f && ejeZ > 0 -> "Plano, pantalla hacia arriba"
        abs(ejeZ) > 7f && ejeZ < 0 -> "Plano, pantalla hacia abajo"
        abs(ejeY) >= abs(ejeX) && ejeY > 0 -> "Vertical"
        abs(ejeY) >= abs(ejeX) && ejeY < 0 -> "Vertical invertido"
        ejeX > 0 -> "Horizontal (lado izquierdo abajo)"
        else -> "Horizontal (lado derecho abajo)"
    }

    DisposableEffect(sensor, intento, cicloDeVida) {
        val temporizador = Handler(Looper.getMainLooper())
        var escuchando = false
        var terminada = false
        datos = ""
        hayLectura = false
        mensajePrueba = "Esperando lectura..."

        fun recibirLectura(valores: FloatArray) {
            // Ignorar eventos que ya estaban en la cola cuando se detuvo la prueba.
            if (!escuchando || valores.isEmpty()) return
            if (sensor.type == Sensor.TYPE_ACCELEROMETER) {
                if (valores.size < 3) return
                ejeX = valores[0]
                ejeY = valores[1]
                ejeZ = valores[2]
                hayLectura = true
            }
            datos = valores.mapIndexed { indice, valor ->
                "Valor ${indice + 1}: %.2f".format(valor)
            }.joinToString("   ")
            mensajePrueba = interpretarSensor(sensor, valores)
        }

        val listener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent) {
                recibirLectura(event.values)
            }
            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }

        val trigger = object : TriggerEventListener() {
            override fun onTrigger(event: TriggerEvent) {
                if (!escuchando) return
                recibirLectura(event.values)
                // Android cancela automáticamente los sensores de un solo evento.
                escuchando = false
                terminada = true
                temporizador.removeCallbacksAndMessages(null)
                mensajePrueba = "Evento detectado. Prueba finalizada."
            }
        }

        fun detener() {
            escuchando = false
            terminada = true
            sensorManager.unregisterListener(listener)
            if (sensor.reportingMode == Sensor.REPORTING_MODE_ONE_SHOT) {
                sensorManager.cancelTriggerSensor(trigger, sensor)
            }
            temporizador.removeCallbacksAndMessages(null)
        }

        fun iniciar() {
            if (terminada || escuchando) return
            try {
                escuchando = if (sensor.reportingMode == Sensor.REPORTING_MODE_ONE_SHOT) {
                    sensorManager.requestTriggerSensor(trigger, sensor)
                } else {
                    sensorManager.registerListener(
                        listener, sensor, SensorManager.SENSOR_DELAY_NORMAL
                    )
                }
                if (escuchando) {
                    // El temporizador no se reinicia cuando llegan nuevas lecturas.
                    temporizador.postDelayed({
                        detener()
                        mensajePrueba = if (datos.isEmpty()) {
                            "Sin lectura disponible. Prueba finalizada."
                        } else {
                            "$mensajePrueba\nPrueba finalizada (5 segundos)."
                        }
                    }, 5000L)
                } else {
                    detener()
                    mensajePrueba = "Sin lectura disponible"
                }
            } catch (_: SecurityException) {
                detener()
                mensajePrueba = "Sin lectura disponible: este sensor requiere permiso."
            }
        }

        val observador = LifecycleEventObserver { _, evento ->
            if (evento == Lifecycle.Event.ON_RESUME) iniciar()
            if (evento == Lifecycle.Event.ON_PAUSE) {
                if (!terminada) mensajePrueba = "Prueba detenida al pausar la app."
                detener()
            }
        }
        if (cicloDeVida != null) {
            cicloDeVida.addObserver(observador)
        } else {
            terminada = true
            mensajePrueba = "Sin lectura disponible"
        }

        // Al cambiar de sensor o salir se cancelan la escucha y el temporizador.
        onDispose {
            cicloDeVida?.removeObserver(observador)
            detener()
        }
    }

    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(nombreSensor(sensor), fontWeight = FontWeight.Bold)
            Text(sensor.name)
            Spacer(modifier = Modifier.height(8.dp))
            Text(mensajePrueba, fontWeight = FontWeight.Bold)
            if (sensor.type != Sensor.TYPE_ACCELEROMETER && datos.isNotEmpty()) {
                Text(datos)
            }
            Text(ayudaSensor(sensor))
        }
    }

    // Se usa la misma lectura de la prueba, sin crear otro listener.
    if (sensor.type == Sensor.TYPE_ACCELEROMETER && hayLectura) {
        PanelPosicion(
            x = ejeX,
            y = ejeY,
            z = ejeZ,
            posicion = posicion
        )
    }
}

fun nombreSensor(sensor: Sensor): String {
    return when (sensor.type) {
        Sensor.TYPE_ACCELEROMETER -> "Acelerómetro"
        Sensor.TYPE_LIGHT -> "Sensor de luz"
        Sensor.TYPE_PROXIMITY -> "Sensor de proximidad"
        Sensor.TYPE_GYROSCOPE -> "Giroscopio"
        Sensor.TYPE_MAGNETIC_FIELD -> "Campo magnético"
        Sensor.TYPE_PRESSURE -> "Presión atmosférica"
        Sensor.TYPE_AMBIENT_TEMPERATURE -> "Temperatura ambiente"
        Sensor.TYPE_RELATIVE_HUMIDITY -> "Humedad relativa"
        Sensor.TYPE_GRAVITY -> "Gravedad"
        Sensor.TYPE_LINEAR_ACCELERATION -> "Aceleración lineal"
        Sensor.TYPE_ROTATION_VECTOR -> "Vector de rotación"
        Sensor.TYPE_STEP_COUNTER -> "Contador de pasos"
        Sensor.TYPE_STEP_DETECTOR -> "Detector de pasos"
        else -> sensor.name
    }
}

fun interpretarSensor(sensor: Sensor, valores: FloatArray): String {
    val valor = valores.firstOrNull() ?: return "Sin lectura disponible"
    val fuerza = sqrt(valores.take(3).sumOf { (it * it).toDouble() }).toFloat()

    // Son límites sencillos para esta práctica.
    return when (sensor.type) {
        Sensor.TYPE_ACCELEROMETER -> {
            if (abs(fuerza - SensorManager.GRAVITY_EARTH) > 1.5f) {
                "Movimiento detectado"
            } else {
                "Sin movimiento importante"
            }
        }
        Sensor.TYPE_LINEAR_ACCELERATION -> {
            if (fuerza > 1f) "Movimiento detectado" else "Sin movimiento importante"
        }
        Sensor.TYPE_GYROSCOPE -> {
            if (fuerza > 0.2f) "Giro detectado" else "Sin giro importante"
        }
        Sensor.TYPE_LIGHT -> when {
            valor >= 1000f -> "Ambiente con mucha luz: %.2f lux".format(valor)
            valor < 10f -> "Ambiente con poca luz: %.2f lux".format(valor)
            else -> "Ambiente con luz moderada: %.2f lux".format(valor)
        }
        Sensor.TYPE_PROXIMITY -> {
            if (valor < sensor.maximumRange && valor < 5f) {
                "Objeto cercano: %.2f cm".format(valor)
            } else {
                "Sin objeto cercano"
            }
        }
        Sensor.TYPE_MAGNETIC_FIELD -> "Campo magnético detectado (µT)"
        Sensor.TYPE_PRESSURE -> "Presión: %.2f hPa".format(valor)
        Sensor.TYPE_AMBIENT_TEMPERATURE -> "Temperatura ambiente: %.2f °C".format(valor)
        Sensor.TYPE_RELATIVE_HUMIDITY -> "Humedad relativa: %.2f %%".format(valor)
        Sensor.TYPE_STEP_COUNTER -> "Pasos desde el reinicio: ${valor.toInt()}"
        Sensor.TYPE_STEP_DETECTOR -> "Paso detectado"
        else -> "Lectura recibida"
    }
}

fun ayudaSensor(sensor: Sensor): String {
    return when (sensor.type) {
        Sensor.TYPE_ACCELEROMETER, Sensor.TYPE_LINEAR_ACCELERATION ->
            "Mueve el teléfono suavemente. Los valores están en m/s²."
        Sensor.TYPE_GYROSCOPE -> "Gira el teléfono. Los valores están en rad/s."
        Sensor.TYPE_LIGHT -> "Cubre el sensor o acércalo a una luz."
        Sensor.TYPE_PROXIMITY -> "Acerca la mano a la parte superior del teléfono."
        Sensor.TYPE_STEP_COUNTER, Sensor.TYPE_STEP_DETECTOR ->
            "Camina unos pasos. Este sensor puede requerir permiso de actividad física."
        else -> "Observa si los valores cambian al realizar la prueba."
    }
}
