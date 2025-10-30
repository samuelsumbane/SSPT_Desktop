//package com.samuelsumbane.ssptdesktop.ui.components
//
//import androidx.compose.foundation.layout.*
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.remember
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.unit.dp
//
//@Composable
//fun CChart() {
//
//    ColumnChart(
//        modifier = Modifier.fillMaxSize().padding(horizontal = 22.dp),
//        data = remember {
//            listOf(
//                Bars(
//                    label = "Jan",
//                    values = listOf(
//                        Bars.Data(label = "Linux", value = 50.0, color = Brush.verticalGradient(...),
//                        Bars.Data(label = "Windows", value = 70.0, color = SolidColor(Color.Red))
//                    ),
//                ),
//                Bars(
//                    label = "Feb",
//                    values = listOf(
//                        Bars.Data(label = "Linux", value = 80.0, color = Brush.verticalGradient(...),
//                        Bars.Data(label = "Windows", value = 60.0, color = SolidColor(Color.Red))
//                    ),
//                )
//            )
//        },
//        barProperties = BarProperties(
//            radius = Bars.Data.Radius.Rectangle(topRight = 6.dp, topLeft = 6.dp),
//            spacing = 3.dp,
//            strokeWidth = 20.dp
//        ),
//        animationSpec = spring(
//            dampingRatio = Spring.DampingRatioMediumBouncy,
//            stiffness = Spring.StiffnessLow
//        ),
//    )
//}