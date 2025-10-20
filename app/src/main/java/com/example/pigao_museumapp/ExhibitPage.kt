package com.example.pigao_museumapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import java.io.BufferedReader
import java.io.InputStreamReader

class ExhibitActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var artwork by remember { mutableStateOf<Artwork?>(null) }

            LaunchedEffect(Unit) {
                artwork = loadFirstArtworkFromJson()
            }

            artwork?.let {
                ExhibitScreen(
                    title = it.title,
                    years = it.years,
                    bornAt = it.bornAt,
                    comment = it.comment
                )
            } ?: Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color(0xFFFFC107))
            }
        }
    }

    private suspend fun loadFirstArtworkFromJson(): Artwork? = withContext(Dispatchers.IO) {
        try {
            val inputStream = assets.open("artworks.json")
            val reader = BufferedReader(InputStreamReader(inputStream))
            val jsonText = reader.use { it.readText() }
            val jsonArray = JSONArray(jsonText)

            if (jsonArray.length() > 0) {
                val obj = jsonArray.getJSONObject(0)
                Artwork(
                    title = obj.getString("title"),
                    years = obj.getString("years"),
                    bornAt = obj.getString("born_at"),
                    comment = obj.getString("comment")
                )
            } else null
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}

data class Artwork(
    val title: String,
    val years: String,
    val bornAt: String,
    val comment: String
)

// Custom fonts
val PlayfairDisplay = FontFamily(
    Font(R.font.playfairdisplayregular, FontWeight.Normal)
)

val Optima = FontFamily(
    Font(R.font.optima, FontWeight.Normal)
)

@Composable
fun ExhibitScreen(title: String, years: String, bornAt: String, comment: String) {
    val imgRes = when (title) {
        "Mona Lisa" -> R.drawable.mona_lisa
        "Lady Ermine" -> R.drawable.lady_ermine
        "Litta Madonna" -> R.drawable.litta_madonna
        else -> R.drawable.mona_lisa
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1A1A1A))
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.weight(1f))

            // Painting with rounded top corners - with side padding
            Image(
                painter = painterResource(id = imgRes),
                contentDescription = title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
                    .fillMaxHeight(0.65f)
                    .clip(RoundedCornerShape(topStart = 200.dp, topEnd = 200.dp))
            )

            Spacer(modifier = Modifier.height(0.dp))

            // Yellow info card - same width as image
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp),
                shape = RoundedCornerShape(0.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFC107))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = title,
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = PlayfairDisplay,
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "$years, $bornAt",
                            fontSize = 16.sp,
                            fontFamily = Optima,
                            color = Color(0xFF555555)
                        )
                    }

                    Icon(
                        painter = painterResource(id = R.drawable.arrows),
                        contentDescription = "Arrow icon",
                        tint = Color.White,
                        modifier = Modifier
                            .size(48.dp)
                            .background(
                                color = Color(0xFF1C1C1C),
                                shape = RoundedCornerShape(50)
                            )
                            .padding(12.dp)
                    )
                }
            }

            // Quote section
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF1A1A1A))
                    .padding(horizontal = 48.dp, vertical = 24.dp),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.quote),
                    contentDescription = "Quote icon",
                    tint = Color(0xFF808080),
                    modifier = Modifier
                        .size(56.dp)
                        .padding(end = 12.dp)
                )
                Text(
                    text = comment,
                    color = Color.White,
                    fontSize = 16.sp,
                    fontFamily = Optima,
                    lineHeight = 24.sp,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                    modifier = Modifier.weight(1f, fill = false)
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ExhibitPagePreview() {
    ExhibitScreen(
        title = "Lady Ermine",
        years = "c. 1489–91",
        bornAt = "Milan, Italy",
        comment = "It is a captivating image of exquisite elegance and reveals the artistic genius of Leonardo's incomparable creative mind."
    )
}