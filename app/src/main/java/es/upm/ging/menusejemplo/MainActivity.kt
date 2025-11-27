package es.upm.ging.menusejemplo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()


        setContent {
            MenusApp()
        }
    }
}

@Composable
fun MenusApp() {
    var navy by rememberSaveable { mutableStateOf(true) }
    val background = if (navy) Color(0xFF0A3D62) else Color(0xFF2ECC71)

    var optionsExpanded by remember { mutableStateOf(false) }
    var contextualExpanded by remember { mutableStateOf(false) }
    var contextualOffset by remember { mutableStateOf(DpOffset(0.dp, 0.dp)) }
    var popupVisible by remember { mutableStateOf(false) }

    val clipboard = LocalClipboardManager.current

    MaterialTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(background)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                // Barra superior personalizada sin TopAppBar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Menús en Compose", style = MaterialTheme.typography.titleLarge)
                    IconButton(onClick = { optionsExpanded = true }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "Menú de opciones")
                    }
                    DropdownMenu(
                        expanded = optionsExpanded,
                        onDismissRequest = { optionsExpanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Cambiar color de fondo") },
                            onClick = {
                                navy = !navy
                                optionsExpanded = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Menú contextual") },
                            onClick = {
                                contextualOffset = DpOffset(0.dp, 0.dp)
                                contextualExpanded = true
                                optionsExpanded = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Menú emergente") },
                            onClick = {
                                popupVisible = true
                                optionsExpanded = false
                            }
                        )
                    }
                }

                // Texto con menú contextual en pulsación larga
                Box {
                    SelectionContainer {
                        Text(
                            text = "Pulsa largo aquí para abrir el menú contextual",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.pointerInput(Unit) {
                                detectTapGestures(
                                    onLongPress = {
                                        contextualOffset = DpOffset(0.dp, 8.dp)
                                        contextualExpanded = true
                                    }
                                )
                            }
                        )
                    }
                    DropdownMenu(
                        expanded = contextualExpanded,
                        onDismissRequest = { contextualExpanded = false },
                        offset = contextualOffset
                    ) {
                        DropdownMenuItem(
                            text = { Text("Copiar texto") },
                            onClick = {
                                clipboard.setText(AnnotatedString("Pulsa largo aquí para abrir el menú contextual"))
                                contextualExpanded = false
                            }
                        )
                        DropdownMenuItem(text = { Text("Compartir (simulado)") }, onClick = { contextualExpanded = false })
                        DropdownMenuItem(text = { Text("Eliminar (simulado)") }, onClick = { contextualExpanded = false })
                    }
                }

                Button(onClick = { popupVisible = true }) {
                    Text("Mostrar menú emergente")
                }
            }

            // Menú emergente centrado
            if (popupVisible) {
                Popup(
                    alignment = Alignment.Center,
                    properties = PopupProperties(focusable = true),
                    onDismissRequest = { popupVisible = false }
                ) {
                    Card(
                        modifier = Modifier.width(280.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text("Menú emergente", style = MaterialTheme.typography.titleMedium)
                            Divider()
                            TextButton(onClick = {
                                navy = !navy
                                popupVisible = false
                            }) { Text("Cambiar color de fondo") }
                            TextButton(onClick = {
                                contextualExpanded = true
                                popupVisible = false
                            }) { Text("Abrir menú contextual") }
                            TextButton(onClick = { popupVisible = false }) { Text("Cerrar") }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MenusApp()
}