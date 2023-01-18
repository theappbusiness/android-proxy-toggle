@file:Suppress("MagicNumber")

package com.kinandcarta.create.proxytoggle.core.theme

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.kinandcarta.create.proxytoggle.core.R

private val DmSans = FontFamily(
    Font(R.font.dmsans_regular, FontWeight.W400),
    Font(R.font.dmsans_bold, FontWeight.W700)
)

val StatusLabelTextStyle = TextStyle(
    fontSize = 12.sp,
    fontWeight = FontWeight.W700,
    fontFamily = DmSans,
    letterSpacing = 0.2.em
)

val InputTextStyle = TextStyle(
    fontSize = 20.sp,
    fontWeight = FontWeight.W400,
    fontFamily = DmSans
)
