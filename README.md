# Jetpack Compose Date and Time Picker

## Download

[![Download](https://api.bintray.com/packages/vanpra/ComposeDateTimePicker/datetimepicker/images/download.svg?version=0.1.0)](https://bintray.com/vanpra/ComposeDateTimePicker/datetimepicker/0.1.0/link)

```gradle
dependencies {
  ...
  implementation 'com.vanpra:datetimepicker:0.1.0'
  ...
}
```

# Date and Time Picker

![](https://raw.githubusercontent.com/vanpra/ComposeDateTimePicker/master/imgs/datetime.jpg)

```kotlin
val showingDateTime = state { false }

DateTimePicker(
    showing = showingDateTime,
    onComplete = { dateTime ->
        // Do stuff with java.time.LocalDateTime object which is passed in
    })
```

# Date Picker

![](https://raw.githubusercontent.com/vanpra/ComposeDateTimePicker/master/imgs/date.jpg)

```kotlin
val showingDate = state { false }

DatePicker(
    showing = showingDate,
    onComplete = { date ->
        // Do stuff with java.time.LocalDate object which is passed in
    })

```

To show the dialog just set the value of  the state `showingDate` to true

# Time Picker

![](https://raw.githubusercontent.com/vanpra/ComposeDateTimePicker/master/imgs/time.jpg)

```kotlin
val showingTime = state { false }

TimePicker(
    showing = showingTime,
    onComplete = { time ->
        // Do stuff with java.time.LocalTime object which is passed in
    })

```

To show the dialog just set the value of  the state `showingTime` to true

# To Do

1. Limit date selection range (ie. min/max date)

2.  Implement Date range selection 

3. Implement year selection

   
