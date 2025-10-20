package com.example.pigao_museumapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.QrCodeScanner
import androidx.compose.material.icons.rounded.Search
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pigao_museumapp.ui.theme.Pigao_MuseumAppTheme

class ArtistsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Pigao_MuseumAppTheme {
                ArtistsScreen()
            }
        }
    }
}

@Composable
fun ArtistsScreen() {
    val context = LocalContext.current
    var selectedTabIndex by remember { mutableStateOf(0) }
    var query by remember { mutableStateOf("") }

    Box(modifier = Modifier.fillMaxSize()) {
        // Background covers entire screen
        Image(
            painter = painterResource(id = R.drawable.background),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // Dark overlay
        Box(modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.55f)))

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "Explore the art of Renaissance",
                fontFamily = playfairdisplayregular,
                fontSize = 28.sp,
                color = Color(0xFFFFD43F),
                fontWeight = FontWeight.Normal,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            // Search box
            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFF1F1F1F)),
                placeholder = {
                    Text("Type to search...", fontFamily = optima, color = Color.Gray)
                },
                leadingIcon = {
                    Icon(imageVector = Icons.Rounded.Search, contentDescription = "Search", tint = Color.Gray)
                },
                trailingIcon = {
                    Icon(imageVector = Icons.Outlined.QrCodeScanner, contentDescription = "Scan", tint = Color.Gray)
                },
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    cursorColor = Color(0xFFFFD43F),
                    focusedContainerColor = Color(0xFF1F1F1F),
                    unfocusedContainerColor = Color(0xFF1F1F1F)
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Tabs
            TabRow(
                selectedTabIndex = selectedTabIndex,
                containerColor = Color.Transparent,
                contentColor = Color(0xFFFFD43F),
                divider = {}
            ) {
                Tab(
                    selected = selectedTabIndex == 0,
                    onClick = { selectedTabIndex = 0 },
                    selectedContentColor = Color(0xFFFFD43F),
                    unselectedContentColor = Color.White
                ) {
                    Text("Artists", modifier = Modifier.padding(vertical = 12.dp), fontFamily = optima)
                }
                Tab(
                    selected = selectedTabIndex == 1,
                    onClick = { selectedTabIndex = 1 },
                    selectedContentColor = Color(0xFFFFD43F),
                    unselectedContentColor = Color.White
                ) {
                    Text("Artworks", modifier = Modifier.padding(vertical = 12.dp), fontFamily = optima)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            if (selectedTabIndex == 0) {
                ArtistsList(
                    onArtworkClick = {
                        val intent = Intent(context, ExhibitActivity::class.java)
                        context.startActivity(intent)
                    }
                )
            } else {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Browse artworks", color = Color.White, fontFamily = optima)
                }
            }
        }
    }
}

@Composable
private fun ArtistsList(onArtworkClick: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize()) {
        ArtistCard(
            avatarRes = R.drawable.logo,
            name = "Leonardo da Vinci",
            years = "1452 – 1519",
            artworkResIds = listOf(R.drawable.renaissance, R.drawable.louvre, R.drawable.beautiful),
            onArtworkClick = onArtworkClick
        )
        Spacer(modifier = Modifier.height(14.dp))
        ArtistCard(
            avatarRes = R.drawable.logo,
            name = "Sandro Botticelli",
            years = "1445 – 1510",
            artworkResIds = listOf(R.drawable.renaissance, R.drawable.renaissance, R.drawable.louvre),
            onArtworkClick = onArtworkClick
        )
        Spacer(modifier = Modifier.height(14.dp))
        ArtistCard(
            avatarRes = R.drawable.logo,
            name = "Raphael",
            years = "1483 – 1520",
            artworkResIds = listOf(R.drawable.beautiful, R.drawable.louvre, R.drawable.renaissance),
            onArtworkClick = onArtworkClick
        )
    }
}

@Composable
private fun ArtistCard(
    avatarRes: Int,
    name: String,
    years: String,
    artworkResIds: List<Int>,
    onArtworkClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xCC141414))
            .padding(14.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = avatarRes),
                contentDescription = name,
                modifier = Modifier
                    .size(46.dp)
                    .clip(CircleShape)
                    .border(1.dp, Color(0xFFFFD43F), CircleShape),
                contentScale = ContentScale.Crop
            )
            Column(modifier = Modifier.padding(start = 12.dp)) {
                Text(name, color = Color.White, fontFamily = playfairdisplayregular, fontSize = 18.sp)
                Text(years, color = Color.Gray, fontFamily = optima, fontSize = 14.sp)
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            artworkResIds.forEachIndexed { index, resId ->
                Image(
                    painter = painterResource(id = resId),
                    contentDescription = "Artwork",
                    modifier = Modifier
                        .size(width = 160.dp, height = 100.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .clickable(enabled = index == 0) { if (index == 0) onArtworkClick() },
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ArtistsPreview() {
    Pigao_MuseumAppTheme {
        ArtistsScreen()
    }
}
