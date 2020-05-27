package com.vanpra.datetimepicker

import androidx.compose.*
import androidx.ui.core.*
import androidx.ui.foundation.*
import androidx.ui.foundation.shape.corner.CircleShape
import androidx.ui.geometry.Offset
import androidx.ui.graphics.ColorFilter
import androidx.ui.layout.*
import androidx.ui.material.*
import androidx.ui.material.icons.Icons
import androidx.ui.material.icons.filled.ArrowBack
import androidx.ui.material.ripple.ripple
import androidx.ui.unit.dp
import java.time.*
import java.time.temporal.ChronoUnit

//TODO Remove colors when dialog material theme is fixed
@Composable
fun DateTimePicker(
    colors: ColorPalette = MaterialTheme.colors,
    showing: MutableState<Boolean>,
    onComplete: (LocalDateTime) -> Unit
) {
    val currentDate = remember { LocalDate.now() }
    val selectedDate = state { currentDate }

    val currentTime = remember { LocalTime.now().truncatedTo(ChronoUnit.MINUTES) }
    val selectedTime = state { currentTime }

    if (showing.value) {
        Dialog(onCloseRequest = { showing.value = false }) {
            val scrollerPosition by state { ScrollerPosition() }
            val scrollTo = state { 0f }
            val currentScreen = state { 0 }

            Column(Modifier.fillMaxWidth().drawBackground(colors.background)) {
                Stack(Modifier.fillMaxWidth().padding(top = 24.dp, bottom = 24.dp)) {
                    WithConstraints {
                        val ratio = scrollerPosition.value / constraints.maxWidth.value
                        Image(
                            Icons.Default.ArrowBack,
                            colorFilter = ColorFilter.tint(colors.onBackground),
                            modifier = Modifier.padding(start = 16.dp)
                                .clip(CircleShape)
                                .ripple()
                                .clickable(onClick = {
                                    scrollerPosition.smoothScrollTo(0f)
                                    currentScreen.value = 0
                                })
                                .drawOpacity(1f * ratio)
                        )
                    }

                    DialogTitle(
                        "Select Date and Time",
                        colors
                    )
                }

                Row(
                    Modifier.fillMaxWidth().wrapContentWidth(Alignment.CenterHorizontally)
                ) {
                    WithConstraints {
                        val ratio = scrollerPosition.value / constraints.maxWidth.value
                        val color = colors.onBackground
                        Canvas(modifier = Modifier) {
                            val offset = Offset(30f, 0f)
                            drawCircle(
                                color.copy(0.7f + 0.3f * (1 - ratio)),
                                radius = 8f + 7f * (1 - ratio),
                                center = center - offset
                            )
                            drawCircle(
                                color.copy(0.7f + 0.3f * ratio),
                                radius = 8f + 7f * ratio,
                                center = center + offset
                            )
                        }
                    }
                }

                WithConstraints {
                    scrollTo.value = constraints.maxWidth.value.toFloat()
                    HorizontalScroller(
                        isScrollable = false,
                        scrollerPosition = scrollerPosition
                    ) {
                        DatePickerLayout(
                            Modifier.padding(top = 16.dp).preferredWidth(maxWidth),
                            colors,
                            selectedDate,
                            currentDate
                        )
                        TimePickerLayout(
                            Modifier.padding(top = 16.dp).preferredWidth(maxWidth),
                            colors,
                            selectedTime
                        )
                    }

                }

                ButtonLayout(
                    confirmText = if (currentScreen.value == 0) {
                        "Next"
                    } else {
                        "Ok"
                    }, onConfirm = {
                        if (currentScreen.value == 0) {
                            scrollerPosition.smoothScrollTo(scrollTo.value)
                            currentScreen.value = 1
                        } else {
                            showing.value = false
                            onComplete(LocalDateTime.of(selectedDate.value, selectedTime.value))
                        }
                    }, onCancel = {
                        showing.value = false
                    })

            }
        }
    }
}