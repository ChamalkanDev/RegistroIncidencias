package aplicacion.registroincidencias

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import aplicacion.registroincidencias.ui.theme.RegistroIncidenciasTheme

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

    // Variables para guardar lo que escribe el usuario
    var titulo by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }

    // Mensaje que se mostrará después de presionar el botón
    var mensaje by remember {
        mutableStateOf("Aún no hay reporte creado")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),

        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        // Logo de la aplicación
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Logo de la aplicación",
            modifier = Modifier.size(120.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Título
        Text(
            text = "Registro de Incidencias",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Instrucción
        Text(
            text = "Complete los datos para crear un reporte."
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Campo para el título
        OutlinedTextField(
            value = titulo,
            onValueChange = {
                titulo = it
            },
            label = {
                Text("Título de la incidencia")
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(15.dp))

        // Campo para la descripción
        OutlinedTextField(
            value = descripcion,
            onValueChange = {
                descripcion = it
            },
            label = {
                Text("Descripción breve")
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Botón principal
        Button(
            onClick = {

                if (titulo.isNotEmpty() && descripcion.isNotEmpty()) {

                    mensaje = "Reporte preparado: $titulo"

                } else {

                    mensaje = "Complete todos los campos"
                }
            }
        ) {
            Text("Crear reporte")
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Mensaje de retroalimentación
        Text(
            text = mensaje
        )
    }
}