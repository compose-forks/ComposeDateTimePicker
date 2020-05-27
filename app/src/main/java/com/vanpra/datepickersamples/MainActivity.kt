package com.vanpra.datepickersamples

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.state
import androidx.ui.animation.animate
import androidx.ui.core.*
import androidx.ui.foundation.Text
import androidx.ui.foundation.drawBackground
import androidx.ui.graphics.Color
import androidx.ui.layout.*
import androidx.ui.material.*
import androidx.ui.unit.dp
import com.vanpra.datetimepicker.*

class MainActivity : AppCompatActivity() {
    private val lightTheme = lightColorPalette(onPrimary = Color.White, primary = Color.Blue)
    private val darkTheme = darkColorPalette(onPrimary = Color.White, primary = Color.Blue)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val currentTheme = state { lightTheme }

            MaterialTheme(currentTheme.value) {
                val showingDateTime = state { false }
                val showingDate = state { false }
                val showingTime = state { false }

                val checked = state { false }
                val selectedDate = state { "" }

                DateTimePicker(
                    showing = showingDateTime,
                    colors = currentTheme.value,
                    onComplete = { selectedDate.value = it.toString() })

                DatePicker(
                    showing = showingDate,
                    colors = currentTheme.value,
                    onComplete = { selectedDate.value = it.toString() })

                TimePicker(
                    showing = showingTime,
                    colors = currentTheme.value,
                    onComplete = { selectedDate.value = it.toString() })

                Column(Modifier.drawBackground(currentTheme.value.background).fillMaxSize()) {
                    Row(Modifier.padding(8.dp)) {
                        Text("Dark Mode", color = currentTheme.value.onBackground)
                        Switch(checked = checked.value, onCheckedChange = {
                            checked.value = it
                            if (checked.value) {
                                currentTheme.value = darkTheme
                            } else {
                                currentTheme.value = lightTheme
                            }
                        }, modifier = Modifier.padding(start = 8.dp))
                    }

                    Button(
                        onClick = { showingDateTime.value = true },
                        modifier = Modifier.fillMaxWidth().padding(8.dp)
                    ) {
                        Text(
                            "Show Date and Time dialog",
                            modifier = Modifier.wrapContentWidth(Alignment.CenterHorizontally)
                        )
                    }

                    Button(
                        onClick = { showingDate.value = true },
                        modifier = Modifier.fillMaxWidth().padding(8.dp)
                    ) {
                        Text(
                            "Show Date dialog",
                            modifier = Modifier.wrapContentWidth(Alignment.CenterHorizontally)
                        )
                    }

                    Button(
                        onClick = { showingTime.value = true },
                        modifier = Modifier.fillMaxWidth().padding(8.dp)
                    ) {
                        Text(
                            "Show Time dialog",
                            modifier = Modifier.wrapContentWidth(Alignment.CenterHorizontally)
                        )
                    }

                    Snackbar(text = {
                        Text("You selected: " + selectedDate.value)
                    }, modifier = Modifier.padding(8.dp))
                }
            }
        }
    }
}