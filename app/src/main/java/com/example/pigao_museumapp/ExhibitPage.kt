package com.example.pigao_museumapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import java.io.BufferedReader
import java.io.InputStreamReader
import kotlin.math.abs

class ExhibitActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var artworks by remember { mutableStateOf<List<Artwork>>(emptyList()) }

            // Get artwork titles from intent if provided (for artist filtering)
            val artworkTitles = intent.getStringArrayListExtra("artwork_titles")

            LaunchedEffect(Unit) {
                val allArtworks = loadAllArtworksFromJson()
                // Filter artworks if titles are provided, otherwise show all
                artworks = if (artworkTitles != null && artworkTitles.isNotEmpty()) {
                    allArtworks.filter { it.title in artworkTitles }
                } else {
                    allArtworks
                }
            }

            if (artworks.isNotEmpty()) {
                ExhibitScreen(artworks = artworks)
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color(0xFFFFC107))
                }
            }
        }
    }

    private suspend fun loadAllArtworksFromJson(): List<Artwork> = withContext(Dispatchers.IO) {
        try {
            val inputStream = assets.open("artworks.json")
            val reader = BufferedReader(InputStreamReader(inputStream))
            val jsonText = reader.use { it.readText() }
            val jsonArray = JSONArray(jsonText)

            val artworkList = mutableListOf<Artwork>()
            for (i in 0 until jsonArray.length()) {
                val obj = jsonArray.getJSONObject(i)
                artworkList.add(
                    Artwork(
                        title = obj.getString("title"),
                        years = obj.getString("years"),
                        bornAt = obj.getString("born_at"),
                        comment = obj.getString("comment")
                    )
                )
            }
            artworkList
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
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
fun ExhibitScreen(artworks: List<Artwork>) {
    val pagerState = rememberPagerState(pageCount = { artworks.size })
    val currentPage = pagerState.currentPage
    val currentArtwork = artworks[currentPage]

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

            // Single centered artwork display
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.65f)
                    .padding(horizontal = 32.dp)
            ) {
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier.fillMaxSize()
                ) { page ->
                    val artwork = artworks[page]

                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        ArtworkCard(
                            artwork = artwork,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Yellow info card
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
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = currentArtwork.title,
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = PlayfairDisplay,
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "${currentArtwork.years}, ${currentArtwork.bornAt}",
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

            // Quote section - smoothly updates as user swipes
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

                // Crossfade between comments as pages change
                Crossfade(
                    targetState = currentArtwork.comment,
                    animationSpec = tween(durationMillis = 300),
                    label = "comment_crossfade"
                ) { comment ->
                    Text(
                        text = comment,
                        color = Color.White,
                        fontSize = 16.sp,
                        fontFamily = Optima,
                        lineHeight = 24.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.weight(1f, fill = false)
                    )
                }
            }
        }
    }
}

@Composable
fun ArtworkCard(
    artwork: Artwork,
    modifier: Modifier = Modifier
) {
    val imgRes = when (artwork.title) {
        "Mona Lisa" -> R.drawable.mona_lisa
        "Lady Ermine" -> R.drawable.lady_ermine
        "Litta Madonna" -> R.drawable.litta_madonna
        else -> R.drawable.mona_lisa
    }

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = imgRes),
            contentDescription = artwork.title,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(topStart = 150.dp, topEnd = 150.dp))
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ExhibitPagePreview() {
    val sampleArtworks = listOf(
        Artwork(
            title = "Lady Ermine",
            years = "c. 1489–91",
            bornAt = "Milan, Italy",
            comment = "It is a captivating image of exquisite elegance and reveals the artistic genius of Leonardo's incomparable creative mind."
        )
    )
    ExhibitScreen(artworks = sampleArtworks)
}