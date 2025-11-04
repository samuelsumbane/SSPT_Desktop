package com.samuelsumbane.ssptdesktop.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEvent
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.samuelsumbane.ssptdesktop.core.utils.cut

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun <T> DataTable(
    headers: List<String>,
    rows: List<T>,
    modifier: Modifier = Modifier,
    cellContent: @Composable (T) -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme
    val datatableHeaderColor = colorScheme.background.copy(red = 0.60f, green = 0.60f, blue = 0.60f)
    val datatableEvenColor = colorScheme.background.copy(red = 0.75f, green = 0.75f, blue = 0.75f)
    val datatableOddColor = colorScheme.background.copy(red = 0.80f, green = 0.80f, blue = 0.80f)

    var searchValue by remember { mutableStateOf("") }
    var filteredData by remember { mutableStateOf(emptyList<List<String>>()) }

    Column(
        modifier = modifier
            .padding(start = 5.dp, top = 50.dp, end = 10.dp)
            .border(1.dp, Color.LightGray, RoundedCornerShape(14.dp))
            .background(datatableHeaderColor, RoundedCornerShape(14.dp))
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Transparent),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            headers.forEach { header ->
                Text(
                    text = header,
                    color = colorScheme.background,
                    modifier = Modifier.padding(6.dp),
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        HorizontalDivider()
        var isHovered by remember { mutableStateOf(false) }
        var hoveredIndex by remember { mutableStateOf(-1) }

        Column(
            modifier = Modifier
                .background(Color.Transparent, RoundedCornerShape(14.dp))
                .padding(bottom = 5.dp)
        ) {
            LazyColumn(
            ) {
                items(rows.size) { index ->
                    val item = rows[index]
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 3.dp, end = 3.dp)
                            .background(
                                if (isHovered && hoveredIndex == index) datatableHeaderColor else if (index % 2 == 0) datatableOddColor
                                else datatableEvenColor
                            )
                            .onPointerEvent(PointerEventType.Enter) {
                                isHovered = true
                                hoveredIndex = index
                            }
                            .onPointerEvent(PointerEventType.Exit) { isHovered = false },
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            cellContent(item)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DatatableText(text: String) {
    Text(text = text.cut(), color = MaterialTheme.colorScheme.background)
}