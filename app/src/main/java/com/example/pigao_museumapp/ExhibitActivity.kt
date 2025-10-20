package com.example.pigao_museumapp

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FormatQuote
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pigao_museumapp.ui.theme.Pigao_MuseumAppTheme
import org.json.JSONObject

class ExhibitActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Pigao_MuseumAppTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = Color.Black) {
                    ExhibitScreen()
                }
            }
        }
    }
}

data class Artwork(
    val title: String,
    val artist: String,
    val year: String,
    val place: String,
    val imageResId: Int,
    val comment: String
)

private fun loadFirstArtwork(context: Context): Artwork? {
    return try {
        val inputStream = context.resources.openRawResource(R.raw.artworks)
        val jsonText = inputStream.bufferedReader().use { it.readText() }
        val root = JSONObject(jsonText)
        val arr = root.getJSONArray("artworks")
        if (arr.length() == 0) return null
        val obj = arr.getJSONObject(0)
        val imageName = obj.getString("image")
        val imageRes = context.resources.getIdentifier(imageName, "drawable", context.packageName)
        Artwork(
            title = obj.getString("title"),
            artist = obj.getString("artist"),
            year = obj.getString("year"),
            place = obj.getString("place"),
            imageResId = imageRes,
            comment = obj.getString("comment")
        )
    } catch (e: Exception) {
        null
    }
}

@Composable
fun ExhibitScreen() {
    val context = LocalContext.current
    val artwork = remember { loadFirstArtwork(context) }

    if (artwork == null) {
        Box(modifier = Modifier.fillMaxSize().background(Color.Black), contentAlignment = Alignment.Center) {
            Text("No artwork found", color = Color.White, fontFamily = optima)
        }
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A1A))
        ) {
            Image(
                painter = painterResource(id = artwork.imageResId),
                contentDescription = artwork.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp)
                    .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
            )

            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = artwork.title,
                    fontFamily = playfairdisplayregular,
                    fontSize = 24.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Normal,
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = artwork.year + " • " + artwork.place,
                    fontFamily = optima,
                    fontSize = 16.sp,
                    color = Color(0xFFFFD43F)
                )

                Spacer(modifier = Modifier.height(16.dp))

                ElevatedCard(
                    colors = CardDefaults.elevatedCardColors(containerColor = Color(0xFF262626)),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.FormatQuote,
                            contentDescription = "Quote",
                            tint = Color(0xFFFFD43F),
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = artwork.comment,
                            fontFamily = optima,
                            fontSize = 16.sp,
                            color = Color.White,
                            lineHeight = 22.sp
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ExhibitPreview() {
    Pigao_MuseumAppTheme {
        ExhibitScreen()
    }
}
