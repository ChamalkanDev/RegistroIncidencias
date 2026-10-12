package aplicacion.registroincidencias

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
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

    var titulo by remember {
        mutableStateOf("")
    }

    var descripcion by remember {
        mutableStateOf("")
    }

    var tipoProblema by remember {
        mutableStateOf("Hardware")
    }

    var prioridad by remember {
        mutableStateOf("Media")
    }

    var mensaje by remember {
        mutableStateOf("Aún no hay reporte creado")
    }

    // Control del teclado y del foco
    val focusManager = LocalFocusManager.current


    // -----------------------------------------
    // Sensor de posición (acelerómetro)
    // -----------------------------------------

    val context = LocalContext.current

    var ejeX by remember { mutableStateOf(0f) }
    var ejeY by remember { mutableStateOf(0f) }
    var ejeZ by remember { mutableStateOf(0f) }

    val sensorManager = remember {
        context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    }

    val acelerometro = remember {
        sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    }

    DisposableEffect(acelerometro) {

        val listener = object : SensorEventListener {

            override fun onSensorChanged(event: SensorEvent) {
                ejeX = event.values[0]
                ejeY = event.values[1]
                ejeZ = event.values[2]
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }

        if (acelerometro != null) {
            sensorManager.registerListener(
                listener,
                acelerometro,
                SensorManager.SENSOR_DELAY_NORMAL
            )
        }

        onDispose {
            sensorManager.unregisterListener(listener)
        }
    }

    val posicion = when {
        acelerometro == null -> "Sensor no disponible"
        abs(ejeZ) > 7f && ejeZ > 0 -> "Plano, pantalla hacia arriba"
        abs(ejeZ) > 7f && ejeZ < 0 -> "Plano, pantalla hacia abajo"
        abs(ejeY) >= abs(ejeX) && ejeY > 0 -> "Vertical"
        abs(ejeY) >= abs(ejeX) && ejeY < 0 -> "Vertical invertido"
        ejeX > 0 -> "Horizontal (lado izquierdo abajo)"
        else -> "Horizontal (lado derecho abajo)"
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE0E0E0))
    ) {

        // -----------------------------------------
        // Contenido del formulario
        // -----------------------------------------

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
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
        }


        // -----------------------------------------
        // Panel inferior: posición del teléfono
        // -----------------------------------------

        PanelPosicion(
            x = ejeX,
            y = ejeY,
            z = ejeZ,
            posicion = posicion
        )
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