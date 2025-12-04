// MainActivity.kt
package com.example.pigao_museumapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pigao_museumapp.ui.theme.Pigao_MuseumAppTheme
import kotlinx.coroutines.delay


// Define custom fonts
val playfairdisplayregular = FontFamily(
    Font(resId = R.font.playfairdisplayregular, weight = FontWeight.Normal)
)
val optima = FontFamily(
    Font(resId = R.font.optima, weight = FontWeight.Normal)
)

@Composable
fun TypingText(
    text: String,
    typingSpeed: Long = 50,
    modifier: Modifier = Modifier,
    style: TextStyle = TextStyle.Default
) {
    var displayedText by remember { mutableStateOf("") }

    LaunchedEffect(text) {
        displayedText = ""
        text.forEach { char ->
            displayedText += char
            delay(typingSpeed)
        }
    }

    Text(
        text = displayedText,
        modifier = modifier,
        style = style
    )
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Pigao_MuseumAppTheme {
                Homepage()
            }
        }
    }
}

@Composable
fun Homepage(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    
    // Animation states
    var museumVisible by remember { mutableStateOf(false) }
    var titleVisible by remember { mutableStateOf(false) }
    var introVisible by remember { mutableStateOf(false) }
    var buttonVisible by remember { mutableStateOf(false) }
    
    // Control animation sequence
    LaunchedEffect(Unit) {
        // Start museum animation
        museumVisible = true
        delay(800) // Museum animation duration
        
        // Start title animation after museum finishes
        titleVisible = true
        delay(750) // Time for title typing animation (14 chars * 50ms + buffer)
        
        // Start introduction animation after title finishes
        introVisible = true
        // Introduction text: ~120 characters * 30ms = ~3600ms
        delay(3700) // Wait for introduction text typing to complete
        
        // Show button after introduction text finishes
        buttonVisible = true
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        // Background image with slide-in animation from top
        AnimatedVisibility(
            visible = museumVisible,
            enter = slideInVertically(
                initialOffsetY = { -it },
                animationSpec = tween(
                    durationMillis = 800,
                    easing = FastOutSlowInEasing
                )
            ) + fadeIn(
                animationSpec = tween(
                    durationMillis = 800,
                    easing = FastOutSlowInEasing
                )
            )
        ) {
            Image(
                painter = painterResource(id = R.drawable.louvre),
                contentDescription = "Louvre Museum",
                modifier = Modifier
                    .align(Alignment.Center)
                    .offset(y = 50.dp)
                    .size(500.dp),
                contentScale = ContentScale.Fit
            )
        }

        // Dark overlay for better text readability
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.4f))
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Logo at the top
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Gallery Logo",
                modifier = Modifier
                    .size(140.dp)
                    .padding(top = 40.dp)
            )

            Spacer(modifier = Modifier.weight(1f))

            // Main title with typing animation
            AnimatedVisibility(
                visible = titleVisible,
                enter = fadeIn(animationSpec = tween(300))
            ) {
                TypingText(
                    text = "Experience Art",
                    typingSpeed = 50,
                    modifier = Modifier.padding(bottom = 16.dp),
                    style = TextStyle(
                        fontFamily = playfairdisplayregular,
                        fontSize = 32.sp,
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )
                )
            }

            // Description text with typing animation
            AnimatedVisibility(
                visible = introVisible,
                enter = fadeIn(animationSpec = tween(300))
            ) {
                TypingText(
                    text = "We are thrilled to invite you to join us for\nan extraordinary event that will immerse\nyou in the world of art.",
                    typingSpeed = 30,
                    modifier = Modifier.padding(bottom = 40.dp),
                    style = TextStyle(
                        fontFamily = optima,
                        fontSize = 16.sp,
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        lineHeight = 24.sp
                    )
                )
            }

            // Explore Now button - appears after introduction text finishes
            AnimatedVisibility(
                visible = buttonVisible,
                enter = fadeIn(animationSpec = tween(500)) + slideInVertically(
                    initialOffsetY = { 20 },
                    animationSpec = tween(500, easing = FastOutSlowInEasing)
                )
            ) {
                Button(
                    onClick = {
                        val intent = Intent(context, ExploreActivity::class.java)
                        context.startActivity(intent)
                    },
                    modifier = Modifier.padding(vertical = 10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFD4AF37)
                    ),
                    shape = RoundedCornerShape(25.dp)
                ) {
                    Text(
                        text = "Explore Now",
                        fontFamily = optima,
                        fontSize = 18.sp,
                        color = Color.Black,
                        modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomepagePreview() {
    Pigao_MuseumAppTheme {
        Homepage()
    }
}