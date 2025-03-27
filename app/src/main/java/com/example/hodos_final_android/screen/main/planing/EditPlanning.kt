package com.example.hodos_final_android.screen.main.planing

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DragIndicator
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.hodos_final_android.LocalNavController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

data class PlanEvent(
    val id: String,
    val title: String,
    val subtitle: String,
    val imageUrl: String,
    val dayNumber: Int,
    val isNew: Boolean = false,
    val isDeleting: Boolean = false
)

data class PlanDay(
    val date: LocalDate,
    val events: List<PlanEvent>
)

@SuppressLint("NewApi")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditPlanning() {
    val navController = LocalNavController.current

    val initialDays = listOf(
        PlanDay(
            date = LocalDate.of(2025, 4, 12),
            events = listOf(
                PlanEvent("1", "CAMPING DALAT", "CAMPING DALAT", "https://images.unsplash.com/photo-1504280390367-361c6d9f38f4", 1),
                PlanEvent("2", "CAMPING DALAT", "CAMPING DALAT", "https://imagetravel.vn/wp-content/uploads/2023/09/camping-bbq-2.gif", 1),
                PlanEvent("3", "CAMPING DALAT", "CAMPING DALAT", "https://chuyentactical.com/wp-content/uploads/2020/12/Camping-la-gi.jpg", 1),
                PlanEvent("4", "CAMPING DALAT", "CAMPING DALAT", "https://metroparkstoledo.com/media/9350/camping-1400x700-a.jpg", 1),
                PlanEvent("5", "CAMPING DALAT", "CAMPING DALAT", "https://lh4.googleusercontent.com/3as9VlEsYHN9vtetBvPJVhiUq9U_utmoqCw4m3dmNYtyOH64yDUVK0ZmJYCPd3u8iBKgDgUs9vU7-3CAFzgs2iE5DIKXLAQmti6n56hrfSTUf6Smykz0SYQ6xK1A4uv2aAAko6r8X-ZfnW9cWPEwjAM", 1)
            )
        ),
        PlanDay(
            date = LocalDate.of(2025, 4, 13),
            events = emptyList()
        )
    )

    var planDays by remember { mutableStateOf(initialDays) }
    var draggedItemInfo by remember { mutableStateOf<Pair<Int, Int>?>(null) }
    var dragOffset by remember { mutableStateOf(Offset.Zero) }
    val lazyListState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val hapticFeedback = LocalHapticFeedback.current
    val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    var lastAddedItemId by remember { mutableStateOf<String?>(null) }
    var eventsBeingDeleted by remember { mutableStateOf(listOf<String>()) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("EDIT PLAN") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            LazyColumn(
                state = lazyListState,
                modifier = Modifier.weight(1f),
            ) {
                planDays.forEachIndexed { dayIndex, day ->
                    item(key = "day_header_$dayIndex") {
                        Text(
                            text = "DAY ${dayIndex + 1}: ${day.date.format(dateFormatter)}",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(vertical = 16.dp, horizontal = 10.dp)
                        )
                    }

                    itemsIndexed(
                        items = day.events,
                        key = { _, event -> event.id }
                    ) { eventIndex, event ->
                        val isDragging = draggedItemInfo?.let {
                            it.first == dayIndex && it.second == eventIndex
                        } ?: false

                        val isNewlyAdded = event.id == lastAddedItemId
                        val isBeingDeleted = eventsBeingDeleted.contains(event.id)

                        AnimatedVisibility(
                            visible = !isBeingDeleted,
                            enter = expandVertically(tween(300)) + fadeIn(tween(300)),
                            exit = shrinkVertically(tween(300)) + fadeOut(tween(300))
                        ) {
                            val dismissState = rememberSwipeToDismissBoxState(
                                confirmValueChange = { value ->
                                    if (value == SwipeToDismissBoxValue.EndToStart) {
                                        eventsBeingDeleted = eventsBeingDeleted + event.id
                                        coroutineScope.launch {
                                            delay(300)
                                            val updatedDays = planDays.toMutableList()
                                            val updatedEvents = updatedDays[dayIndex].events.toMutableList()
                                            updatedEvents.removeIf { it.id == event.id }
                                            updatedDays[dayIndex] = updatedDays[dayIndex].copy(events = updatedEvents)
                                            planDays = updatedDays
                                            eventsBeingDeleted = eventsBeingDeleted.filter { it != event.id }
                                        }
                                        true
                                    } else false
                                }
                            )

                            SwipeToDismissBox(
                                state = dismissState,
                                backgroundContent = {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .background(Color.Red)
                                            .padding(horizontal = 20.dp),
                                        contentAlignment = Alignment.CenterEnd
                                    ) {
                                        Icon(
                                            Icons.Default.Delete,
                                            contentDescription = "Delete",
                                            tint = Color.White
                                        )
                                    }
                                },
                                content = {
                                    EventCardDraggable(
                                        event = event,
                                        isDragging = isDragging,
                                        isNewlyAdded = isNewlyAdded,
                                        dragOffset = if (isDragging) dragOffset else Offset.Zero,
                                        onDragStart = {
                                            hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                                            draggedItemInfo = Pair(dayIndex, eventIndex)
                                        },
                                        onDrag = { offset ->
                                            dragOffset = offset
                                            handleDrag(
                                                dayIndex = dayIndex,
                                                eventIndex = eventIndex,
                                                offset = offset,
                                                planDays = planDays,
                                                setPlanDays = { planDays = it },
                                                draggedItemInfo = draggedItemInfo,
                                                setDraggedItemInfo = { draggedItemInfo = it },
                                                hapticFeedback = hapticFeedback
                                            )
                                        },
                                        onDragEnd = {
                                            draggedItemInfo = null
                                            dragOffset = Offset.Zero
                                        }
                                    )
                                }
                            )
                        }

                        LaunchedEffect(isNewlyAdded) {
                            if (isNewlyAdded) {
                                delay(500)
                                lastAddedItemId = null
                            }
                        }
                    }

                    item(key = "add_event_$dayIndex") {
                        AddMoreEventButton(
                            onClick = {
                                val newEventId = "day${dayIndex + 1}_event_${System.currentTimeMillis()}"
                                val newEvent = PlanEvent(
                                    id = newEventId,
                                    title = "CAMPING DALAT",
                                    subtitle = "CAMPING DALAT",
                                    imageUrl = "https://images.unsplash.com/photo-1504280390367-361c6d9f38f4",
                                    dayNumber = dayIndex + 1,
                                    isNew = true
                                )

                                val updatedDays = planDays.toMutableList()
                                val updatedEvents = updatedDays[dayIndex].events.toMutableList()
                                updatedEvents.add(newEvent)
                                updatedDays[dayIndex] = updatedDays[dayIndex].copy(events = updatedEvents)
                                planDays = updatedDays
                                lastAddedItemId = newEventId

                                coroutineScope.launch {
                                    lazyListState.animateScrollToItem(
                                        index = lazyListState.layoutInfo.totalItemsCount - 1
                                    )
                                }
                            }
                        )
                        if (dayIndex < planDays.size - 1) {
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                    }
                }

                item(key = "add_day") {
                    AnimatedVisibility(
                        visible = true,
                        enter = expandVertically() + fadeIn()
                    ) {
                        Button(
                            onClick = {
                                val lastDay = planDays.last().date
                                val newDay = PlanDay(
                                    date = lastDay.plusDays(1),
                                    events = emptyList()
                                )
                                planDays = planDays + newDay
                                coroutineScope.launch {
                                    lazyListState.animateScrollToItem(
                                        index = lazyListState.layoutInfo.totalItemsCount
                                    )
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 16.dp, horizontal = 10.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFE3F2FD),
                                contentColor = Color(0xFF2196F3)
                            ),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                "ADD NEW DAY",
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                        }
                    }
                }
            }

            Button(
                onClick = { navController.popBackStack() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3)),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    "SAVE CHANGES",
                    modifier = Modifier.padding(vertical = 8.dp),
                    color = Color.White
                )
            }
        }
    }
}

@Composable
fun EventCardDraggable(
    event: PlanEvent,
    isDragging: Boolean,
    isNewlyAdded: Boolean,
    dragOffset: Offset,
    onDragStart: () -> Unit,
    onDrag: (Offset) -> Unit,
    onDragEnd: () -> Unit
) {
    val scale by animateFloatAsState(
        targetValue = if (isDragging) 1.05f else 1f,
        animationSpec = tween(100, easing = FastOutSlowInEasing)
    )

    val elevation by animateFloatAsState(
        targetValue = if (isDragging) 8f else 2f,
        animationSpec = tween(100, easing = FastOutSlowInEasing)
    )

    val alpha by animateFloatAsState(
        targetValue = if (isDragging) 0.9f else 1f,
        animationSpec = tween(100, easing = FastOutSlowInEasing)
    )

    val scaleAnimation = animateFloatAsState(
        targetValue = if (isNewlyAdded) 1.1f else 1f,
        animationSpec = tween(300, easing = FastOutSlowInEasing)
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .graphicsLayer {
                translationY = if (isDragging) dragOffset.y else 0f
                scaleX = if (isNewlyAdded) scaleAnimation.value else scale
                scaleY = if (isNewlyAdded) scaleAnimation.value else scale
                this.alpha = alpha
                this.shadowElevation = elevation
            }
            .zIndex(if (isDragging) 1f else 0f)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(0.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (isDragging) MaterialTheme.colorScheme.secondary else Color.White
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 15.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .pointerInput(Unit) {
                            detectDragGesturesAfterLongPress(
                                onDragStart = { onDragStart() },
                                onDrag = { change, dragAmount ->
                                    change.consume()
                                    onDrag(Offset(0f, dragAmount.y))
                                },
                                onDragEnd = { onDragEnd() },
                                onDragCancel = { onDragEnd() }
                            )
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.DragIndicator,
                        contentDescription = "Reorder",
                        tint = MaterialTheme.colorScheme.tertiary,
                        modifier = Modifier.size(24.dp)
                    )
                }

                AsyncImage(
                    model = event.imageUrl,
                    contentDescription = event.title,
                    modifier = Modifier
                        .size(60.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    contentScale = ContentScale.Crop
                )

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 12.dp)
                ) {
                    Text(
                        text = event.title,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.outline
                    )
                    Text(
                        text = event.subtitle,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.tertiary
                    )
                }
            }
        }
    }
}

fun handleDrag(
    dayIndex: Int,
    eventIndex: Int,
    offset: Offset,
    planDays: List<PlanDay>,
    setPlanDays: (List<PlanDay>) -> Unit,
    draggedItemInfo: Pair<Int, Int>?,
    setDraggedItemInfo: (Pair<Int, Int>?) -> Unit,
    hapticFeedback: HapticFeedback
) {
    if (draggedItemInfo == null) return

    val draggedDayIndex = draggedItemInfo.first
    val draggedEventIndex = draggedItemInfo.second

    if (draggedDayIndex != dayIndex) return

    val targetIndex = when {
        offset.y > 50 && draggedEventIndex < planDays[dayIndex].events.size - 1 -> draggedEventIndex + 1
        offset.y < -50 && draggedEventIndex > 0 -> draggedEventIndex - 1
        else -> draggedEventIndex
    }

    if (targetIndex != draggedEventIndex) {
        hapticFeedback.performHapticFeedback(HapticFeedbackType.TextHandleMove)
        val updatedDays = planDays.toMutableList()
        val updatedEvents = updatedDays[dayIndex].events.toMutableList()
        val item = updatedEvents.removeAt(draggedEventIndex)
        updatedEvents.add(targetIndex, item)
        updatedDays[dayIndex] = updatedDays[dayIndex].copy(events = updatedEvents)
        setPlanDays(updatedDays)
        setDraggedItemInfo(Pair(draggedDayIndex, targetIndex))
    }
}

@Composable
fun AddMoreEventButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 10.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFFE3F2FD),
            contentColor = Color(0xFF2196F3)
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(
            "ADD MORE EVENT",
            modifier = Modifier.padding(vertical = 8.dp)
        )
    }
}