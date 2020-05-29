package com.vanpra.datetimepicker


import androidx.compose.Composable
import androidx.compose.MutableState
import androidx.compose.remember
import androidx.compose.state
import androidx.ui.core.Alignment
import androidx.ui.core.Modifier
import androidx.ui.core.clip
import androidx.ui.core.drawOpacity
import androidx.ui.foundation.*
import androidx.ui.foundation.shape.corner.CircleShape
import androidx.ui.graphics.Color
import androidx.ui.graphics.ColorFilter
import androidx.ui.layout.*
import androidx.ui.layout.ColumnScope.gravity
import androidx.ui.material.ColorPalette
import androidx.ui.material.MaterialTheme
import androidx.ui.material.icons.Icons
import androidx.ui.material.icons.filled.ChevronLeft
import androidx.ui.material.icons.filled.ChevronRight
import androidx.ui.material.ripple.ripple
import androidx.ui.text.TextStyle
import androidx.ui.text.font.FontWeight.Companion.W400
import androidx.ui.text.font.FontWeight.Companion.W500
import androidx.ui.text.font.FontWeight.Companion.W600
import androidx.ui.text.font.FontWeight.Companion.W700
import androidx.ui.unit.dp
import androidx.ui.unit.sp
import androidx.ui.util.fastForEach
import java.time.LocalDate
import java.time.YearMonth

@Composable
fun DatePicker(
        showing: MutableState<Boolean>,
        onComplete: (LocalDate) -> Unit
) {
    val currentDate = remember { LocalDate.now() }
    val selectedDate = state { currentDate }

    if (showing.value) {
        ThemedDialog(onCloseRequest = { showing.value = false }) {
            Column(Modifier.drawBackground(MaterialTheme.colors.background)) {
                DialogTitle("Select a date", Modifier.padding(top = 16.dp, bottom = 16.dp))
                DatePickerLayout(currentDate = currentDate, selectedDate = selectedDate)
                ButtonLayout(
                        confirmText = "Ok",
                        onConfirm = {
                            showing.value = false
                            onComplete(selectedDate.value)
                        })
            }
        }
    }
}

@Composable
internal fun DatePickerLayout(
        modifier: Modifier = Modifier,
        selectedDate: MutableState<LocalDate>,
        currentDate: LocalDate
) {
    Column(modifier.drawBackground(MaterialTheme.colors.background)) {
        DateTitle(selectedDate)
        ViewPager(Modifier.drawBackground(Color.Transparent), useAlpha = true) {
            val newDate = remember(index) {
                currentDate.plusMonths(index.toLong())
            }
            val dates = remember(newDate) { getDates(newDate) }
            val yearMonth = remember(newDate) { newDate.yearMonth }

            Column {
                MonthTitle(this@ViewPager, newDate.month.fullLocalName, newDate.year.toString())
                DaysTitle()
                DateLayout(dates, yearMonth, selectedDate)
            }
        }
    }
}

//TODO Replace for loops with Table API when it returns
@Composable
private fun DateLayout(month: List<List<String>>, yearMonth: YearMonth, selected: MutableState<LocalDate>) {
    val check = remember(selected.value, yearMonth) {
        selected.value.monthValue == yearMonth.monthValue
                && selected.value.year == yearMonth.year
    }

    val textStyle = TextStyle(fontSize = 12.sp, fontWeight = W400)
    month.fastForEach {
        Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.padding(top = 8.dp, bottom = 8.dp, start = 10.dp, end = 10.dp)
                        .gravity(Alignment.CenterHorizontally)
                        .fillMaxWidth()
        ) {
            it.fastForEach { date ->
                var selectedModifier: Modifier = Modifier
                var textColor: Color = MaterialTheme.colors.onBackground
                val enabled = date != " "

                if (check && enabled && selected.value.dayOfMonth == date.toInt()) {
                    selectedModifier = Modifier.drawBackground(
                            MaterialTheme.colors.primary.copy(0.7f), CircleShape
                    )
                    textColor = Color.White
                }

                Box(
                        modifier = selectedModifier.preferredSize(40.dp).clip(CircleShape)
                                .clickable(onClick = {
                                    selected.value =
                                            LocalDate.of(yearMonth.year, yearMonth.month, date.toInt())
                                }, enabled = enabled)
                ) {
                    if (enabled) {
                        Text(
                                date,
                                modifier = Modifier.fillMaxSize()
                                        .wrapContentSize(Alignment.Center),
                                style = textStyle,
                                color = textColor
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun DaysTitle() {
    Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.padding(top = 16.dp, bottom = 12.dp)
                    .fillMaxWidth()
    ) {
        listOf("M", "T", "W", "T", "F", "S", "S").fastForEach {
            Text(
                    it,
                    modifier = Modifier.drawOpacity(0.8f),
                    style = TextStyle(fontSize = 14.sp, fontWeight = W600),
                    color = MaterialTheme.colors.onBackground
            )
        }
    }
}

@Composable
private fun MonthTitle(scope: ViewPagerScope, month: String, year: String) {
    Row(modifier = Modifier.fillMaxWidth().padding(top = 16.dp)) {
        Box(
                Modifier.clip(CircleShape).ripple(color = Color.White)
                        .clickable(
                                onClick = { scope.previous() },
                                enabled = true
                        )
        ) {
            Image(
                    Icons.Default.ChevronLeft,
                    modifier = Modifier.padding(start = 24.dp).wrapContentWidth(Alignment.Start),
                    colorFilter = ColorFilter.tint(MaterialTheme.colors.onBackground)
            )
        }

        Text(
                "$month $year",
                modifier = Modifier.weight(3f).wrapContentSize(Alignment.Center),
                style = TextStyle(fontSize = 16.sp, fontWeight = W500),
                color = MaterialTheme.colors.onBackground
        )

        Box(
                Modifier.clip(CircleShape).ripple(color = Color.White)
                        .clickable(
                                onClick = { scope.next() },
                                enabled = true
                        )
        ) {
            Image(
                    Icons.Default.ChevronRight,
                    modifier = Modifier.padding(end = 24.dp).wrapContentWidth(Alignment.End),
                    colorFilter = ColorFilter.tint(MaterialTheme.colors.onBackground)
            )
        }
    }
}

@Composable
private fun DateTitle(selected: MutableState<LocalDate>) {
    val month = selected.value.month.shortLocalName
    val day = selected.value.dayOfWeek.shortLocalName

    Box(backgroundColor = MaterialTheme.colors.primary, modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                    text = selected.value.year.toString(), color = MaterialTheme.colors.onPrimary,
                    modifier = Modifier.drawOpacity(0.8f).padding(bottom = 2.dp),
                    style = TextStyle(fontSize = 18.sp, fontWeight = W700)
            )
            Text(
                    text = "$day, $month ${selected.value.dayOfMonth}",
                    color = MaterialTheme.colors.onPrimary,
                    style = TextStyle(fontSize = 26.sp, fontWeight = W700)
            )
        }
    }
}

private fun getDates(date: LocalDate): List<List<String>> {
    val dates = mutableListOf<List<String>>()

    val firstDate = LocalDate.of(date.year, date.monthValue, 1)
    val firstDay = firstDate.dayOfWeek.value - 1
    val numDays = date.month.length(firstDate.isLeapYear)

    val firstRow = mutableListOf<String>()
    for (x in 0 until firstDay) {
        firstRow.add(" ")
    }

    for (x in 1..(7 - firstDay)) {
        firstRow.add(x.toString())
    }

    dates.add(firstRow)


    for (x in 0 until 3) {
        val week = mutableListOf<String>()
        for (y in 1..7) {
            week.add(((7 - firstDay) + 7 * x + y).toString())
        }
        dates.add(week)
    }

    if ((7 - firstDay) + 21 != numDays) {
        val lastRow = mutableListOf<String>()

        for (x in ((7 - firstDay) + 22)..numDays) {
            lastRow.add(x.toString())
        }
        for (x in (numDays % 7 + firstDay) until 7) {
            lastRow.add(" ")
        }

        dates.add(lastRow)
    }

    return dates
}