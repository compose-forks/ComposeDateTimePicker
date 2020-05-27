package com.vanpra.datetimepicker

import androidx.compose.Composable
import androidx.ui.core.Alignment
import androidx.ui.core.Modifier
import androidx.ui.foundation.Text
import androidx.ui.geometry.Offset
import androidx.ui.graphics.Color
import androidx.ui.layout.*
import androidx.ui.material.ColorPalette
import androidx.ui.material.TextButton
import androidx.ui.material.ripple.ripple
import androidx.ui.text.TextStyle
import androidx.ui.text.font.FontWeight
import androidx.ui.unit.dp
import androidx.ui.unit.sp
import java.time.*
import java.util.*
import kotlin.math.cos
import kotlin.math.sin

@Composable
internal fun ButtonLayout(
    modifier: Modifier = Modifier, confirmText: String = "Ok", onConfirm: () -> Unit = {},
    onCancel: () -> Unit = {}
) {
    Row(horizontalArrangement = Arrangement.End, modifier = modifier.fillMaxWidth().padding(8.dp)) {
        TextButton(onClick = { onCancel() }, modifier = Modifier.ripple(color = Color.White)) {
            Text("Cancel")
        }
        TextButton(onClick = { onConfirm() }, modifier = Modifier.ripple(color = Color.White)) {
            Text(confirmText)
        }
    }
}

@Composable
internal fun DialogTitle(text: String, colors: ColorPalette, modifier: Modifier = Modifier) {
    Text(
        text,
        modifier = modifier.fillMaxWidth()
            .wrapContentWidth(Alignment.CenterHorizontally),
        color = colors.onBackground,
        fontSize = 20.sp,
        style = TextStyle(fontWeight = FontWeight.W600)
    )
}


internal fun Float.getOffset(angle: Double): Offset =
    Offset((this * cos(angle)).toFloat(), (this * sin(angle)).toFloat())

internal val LocalDate.yearMonth: YearMonth
    get() = YearMonth.of(this.year, this.month)

internal val Month.fullLocalName: String
    get() = this.getDisplayName(java.time.format.TextStyle.FULL, Locale.getDefault())

internal val Month.shortLocalName: String
    get() = this.getDisplayName(java.time.format.TextStyle.SHORT, Locale.getDefault())

internal val DayOfWeek.shortLocalName: String
    get() = this.getDisplayName(java.time.format.TextStyle.SHORT, Locale.getDefault())