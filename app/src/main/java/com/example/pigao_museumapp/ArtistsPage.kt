package com.example.pigao_museumapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class ArtistsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { ArtistsPage() }
    }
}

data class Artist(
    val name: String,
    val bornAt: String,
    val diedAt: String,
    val avatar: Int,
    val artworks: List<Int>
)

enum class TabItem {
    ARTISTS, ARTWORKS
}

@Composable
fun ArtistsPage() {
    val searchQuery = remember { mutableStateOf(TextFieldValue("")) }
    val selectedTab = remember { mutableStateOf(TabItem.ARTISTS) }

    val artists = listOf(
        Artist(
            name = "Leonardo da Vinci",
            bornAt = "1452",
            diedAt = "1519",
            avatar = R.drawable.leonardo_da_vinci,
            artworks = listOf(
                R.drawable.mona_lisa,
                R.drawable.lady_ermine,
                R.drawable.litta_madonna
            )
        ),
        Artist(
            name = "Michelangelo",
            bornAt = "1475",
            diedAt = "1564",
            avatar = R.drawable.michelangelo,
            artworks = listOf(
                R.drawable.david,
                R.drawable.torment_of_saint_anthony,
                R.drawable.delphic_sibyl
            )
        ),
        Artist(
            name = "Gustav Klimt",
            bornAt = "1862",
            diedAt = "1918",
            avatar = R.drawable.gustav_klimt,
            artworks = listOf(
                R.drawable.adele_bloch_bauer,
                R.drawable.lady_with_fan,
                R.drawable.the_kiss
            )
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        // Background Image - Covers entire screen
        Image(
            painter = painterResource(id = R.drawable.background),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // Semi-transparent overlay
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0x80F5F0E1))
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 25.dp)
        ) {
            // Title Texts
            Text(
                text = "Explore the art of",
                fontSize = 22.sp,
                fontFamily = playfairdisplayregular,
                color = Color(0xFF5C5042)
            )
            Text(
                text = "Renaissance",
                fontFamily = playfairdisplayregular,
                fontSize = 34.sp,
                color = Color(0xFFD4AF37),
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Search Bar
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .border(
                        width = 1.dp,
                        color = Color.Gray
                    )
                    .padding(horizontal = 15.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null,
                    tint = Color.Gray,
                    modifier = Modifier.size(22.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "Type to search...",
                    color = Color.Gray,
                    fontSize = 14.sp
                )

                Spacer(modifier = Modifier.weight(1f))

                Image(
                    painter = painterResource(id = R.drawable.scanning),
                    contentDescription = "Scan",
                    modifier = Modifier
                        .size(22.dp)
                        .clickable { }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Tabs Row - Interactive Tab Selection
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                TabItem.values().forEach { tab ->
                    val isSelected = selectedTab.value == tab
                    Text(
                        text = tab.name.lowercase().replaceFirstChar { it.uppercase() },
                        color = if (isSelected) Color(0xFFD4AF37) else Color.Gray,
                        fontSize = 16.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        modifier = Modifier.clickable { selectedTab.value = tab }
                    )
                }
            }

            Divider(color = Color.Gray, thickness = 1.dp)

            Spacer(modifier = Modifier.height(16.dp))

            // Content based on selected tab
            when (selectedTab.value) {
                TabItem.ARTISTS -> {
                    // Artists list with new layout design
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(32.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(artists) { artist ->
                            ArtistItemWithNavigation(artist = artist)
                        }
                    }
                }
                TabItem.ARTWORKS -> {
                    // Placeholder for Artworks tab
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Artworks view coming soon",
                            color = Color(0xFF5C5042),
                            fontSize = 16.sp
                        )
                    }
                }
            }
        }
    }
}

// Helper function to map drawable IDs to artwork titles
fun getArtworkTitleFromDrawable(drawableId: Int): String? {
    return when (drawableId) {
        R.drawable.mona_lisa -> "Mona Lisa"
        R.drawable.lady_ermine -> "Lady Ermine"
        R.drawable.litta_madonna -> "Litta Madonna"
        R.drawable.david -> "David"
        R.drawable.torment_of_saint_anthony -> "Torment of Saint Anthony"
        R.drawable.delphic_sibyl -> "Delphic Sibyl"
        R.drawable.adele_bloch_bauer -> "Adele Bloch-Bauer"
        R.drawable.lady_with_fan -> "Lady with Fan"
        R.drawable.the_kiss -> "The Kiss"
        else -> null
    }
}

@Composable
fun ArtistItemWithNavigation(artist: Artist) {
    val context = LocalContext.current
    
    ArtistItem(
        artist = artist,
        onClick = {
            // Navigate to ExhibitActivity with artist's artworks
            val intent = Intent(context, ExhibitActivity::class.java)
            // Pass artwork titles as intent extra
            val artworkTitles = artist.artworks.map { drawableId ->
                getArtworkTitleFromDrawable(drawableId)
            }.filterNotNull()
            intent.putStringArrayListExtra("artwork_titles", ArrayList(artworkTitles))
            context.startActivity(intent)
        }
    )
}

@Composable
fun ArtistItem(
    artist: Artist,
    onClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        // Top: Large artist avatar with name and dates - clickable
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Image(
                painter = painterResource(id = artist.avatar),
                contentDescription = artist.name,
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )

            Column {
                Text(
                    text = artist.name,
                    fontFamily = optima,
                    color = Color(0xFF3E2C1C),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "${artist.bornAt} - ${artist.diedAt}",
                    fontFamily = optima,
                    color = Color(0xFF8C7C6B),
                    fontSize = 13.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Bottom: Artworks with rounded corners - horizontally scrollable
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(artist.artworks) { art ->
                Image(
                    painter = painterResource(id = art),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(width = 80.dp, height = 100.dp)
                        .clip(RoundedCornerShape(16.dp))
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ArtistsPagePreview() {
    ArtistsPage()
}