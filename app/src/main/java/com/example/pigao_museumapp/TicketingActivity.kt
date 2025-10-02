package com.example.pigao_museumapp

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pigao_museumapp.ui.theme.Pigao_MuseumAppTheme
import java.time.Instant
import java.time.Duration

class TicketingActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Pigao_MuseumAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Ticketing()
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Ticketing() {
    var generalAdmissionCount by remember { mutableStateOf(0) }
    var freeTicketCount by remember { mutableStateOf(0) }

    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = Instant.now()
            .plus(Duration.ofDays(2)).toEpochMilli(),
        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                return utcTimeMillis >= Instant.now()
                    .plus(Duration.ofDays(1)).toEpochMilli()
            }
        }
    )

    val totalPrice = generalAdmissionCount * 500

    Column(
        modifier = Modifier.background(Color.Black)
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
            // Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(230.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.beautiful),
                    contentDescription = "Museum",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(230.dp),
                    contentScale = ContentScale.Crop
                )
                // Black overlay
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(230.dp)
                        .background(Color.Black.copy(alpha = 0.7f))
                )
                Text(
                    "Official\nTicketing Service",
                    fontSize = 32.sp,
                    fontFamily = playfairdisplayregular,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    lineHeight = 36.sp
                )
            }

            // Inner container for date and ticket types
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                DatePicker(
                    modifier = Modifier
                        .padding(0.dp)
                        .fillMaxWidth(),
                    state = datePickerState,
                    title = null,
                    showModeToggle = false,
                    headline = {
                        Text(
                            "1. Date to Visit",
                            fontSize = 26.sp,
                            fontFamily = playfairdisplayregular
                        )
                    },
                    colors = DatePickerDefaults.colors(
                        titleContentColor = Color(0xFFD29F1B),
                        headlineContentColor = Color(0xFFD29F1B),
                        weekdayContentColor = Color(0xFFD29F1B),
                        containerColor = Color.Transparent,
                        dayContentColor = Color.White,
                        todayContentColor = Color(0xFFD29F1B),
                        todayDateBorderColor = Color(0xFFD29F1B),
                        selectedDayContainerColor = Color(0xFFD29F1B),
                        selectedDayContentColor = Color.Black,
                        disabledDayContentColor = Color.Gray
                    )
                )

                Spacer(modifier = Modifier.height(40.dp))

                Text(
                    "2. Number of Tickets",
                    fontSize = 26.sp,
                    fontFamily = playfairdisplayregular,
                    color = Color(0xFFD29F1B),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(20.dp))

                // General Admission Ticket
                TicketSelector(
                    title = "General Admission",
                    price = "P500",
                    count = generalAdmissionCount,
                    onIncrement = { generalAdmissionCount++ },
                    onDecrement = { if (generalAdmissionCount > 0) generalAdmissionCount-- }
                )

                Spacer(modifier = Modifier.height(30.dp))

                Divider(
                    color = Color(0xFFD29F1B).copy(alpha = 0.3f),
                    thickness = 1.dp,
                    modifier = Modifier.padding(vertical = 10.dp)
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Free Ticket
                TicketSelector(
                    title = "Under 18s, Under 26s\nresidents of the EEA,\nMuseum members,\nProfessionals",
                    price = "FREE",
                    count = freeTicketCount,
                    onIncrement = { freeTicketCount++ },
                    onDecrement = { if (freeTicketCount > 0) freeTicketCount-- },
                    priceColor = Color(0xFFD29F1B)
                )

                Spacer(modifier = Modifier.height(40.dp))
            }
        }

        // Bottom bar for totals
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .background(Color(0xFFD29F1B))
                .padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Total: P$totalPrice",
                fontSize = 26.sp,
                fontFamily = playfairdisplayregular,
                color = Color.Black,
                fontWeight = FontWeight.Normal
            )
            Button(
                modifier = Modifier.padding(5.dp),
                onClick = { /* TODO */ },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
            ) {
                Text(
                    "Checkout",
                    fontSize = 20.sp,
                    fontFamily = playfairdisplayregular,
                    color = Color(0xFFD29F1B),
                    fontWeight = FontWeight.Normal
                )
            }
        }
    }
}

@Composable
fun TicketSelector(
    title: String,
    price: String,
    count: Int,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit,
    priceColor: Color = Color.White
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                title,
                fontSize = 18.sp,
                fontFamily = playfairdisplayregular,
                color = Color.White,
                lineHeight = 22.sp
            )
            Text(
                price,
                fontSize = 22.sp,
                fontFamily = playfairdisplayregular,
                color = priceColor,
                fontWeight = FontWeight.Normal
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            OutlinedButton(
                onClick = onDecrement,
                modifier = Modifier.size(50.dp),
                shape = MaterialTheme.shapes.extraLarge,
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = Color(0xFFD29F1B)
                ),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFD29F1B)),
                contentPadding = PaddingValues(0.dp)
            ) {
                Text(
                    "−",
                    fontSize = 24.sp,
                    color = Color(0xFFD29F1B)
                )
            }

            Text(
                count.toString(),
                fontSize = 23.sp,
                fontFamily = playfairdisplayregular,
                color = Color.White,
                modifier = Modifier.widthIn(min = 30.dp),
                textAlign = TextAlign.Center
            )

            OutlinedButton(
                onClick = onIncrement,
                modifier = Modifier.size(50.dp),
                shape = MaterialTheme.shapes.extraLarge,
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = Color(0xFFD29F1B)
                ),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFD29F1B)),
                contentPadding = PaddingValues(0.dp)
            ) {
                Text(
                    "+",
                    fontSize = 24.sp,
                    color = Color(0xFFD29F1B)
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun TicketingPreview() {
    Pigao_MuseumAppTheme {
        Ticketing()
    }
}