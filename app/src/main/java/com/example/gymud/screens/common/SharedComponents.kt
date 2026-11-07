package com.example.gymud.screens.common

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

val GymudDark = Color(0xFF121212)
val GymudSurface = Color(0xFF1E1E1E)
val GymudYellow = Color(0xFFFFC107)

@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color.White.copy(alpha = 0.05f),
    content: @Composable ColumnScope.() -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(backgroundColor)
            .border(1.dp, Color.White.copy(alpha = 0.1f), RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Column(content = content)
    }
}

@Composable
fun ScreenTitle(title: String, subtitle: String = "") {
    Column(modifier = Modifier.padding(bottom = 24.dp)) {
        Text(text = title, fontSize = 28.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
        if (subtitle.isNotEmpty()) {
            Text(text = subtitle, fontSize = 14.sp, color = Color.Gray)
        }
    }
}