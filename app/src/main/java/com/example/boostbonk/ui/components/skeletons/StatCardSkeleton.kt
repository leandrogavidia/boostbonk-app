package com.example.boostbonk.ui.components.skeletons

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.boostbonk.ui.theme.BonkWhite
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material3.placeholder
import com.google.accompanist.placeholder.material3.shimmer


@Composable
fun StatCardSkeleton(
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .background(color = BonkWhite, shape = RoundedCornerShape(8.dp))
            .padding(12.dp)
    ) {
        Box(
            modifier = Modifier
                .height(24.dp)
                .width(60.dp)
                .background(Color.LightGray, shape = RoundedCornerShape(4.dp))
                .placeholder(
                    visible = true,
                    highlight = PlaceholderHighlight.shimmer(),
                    color = Color.LightGray
                )
        )

        Spacer(modifier = Modifier.height(8.dp))

        Box(
            modifier = Modifier
                .height(14.dp)
                .width(40.dp)
                .placeholder(
                    visible = true,
                    highlight = PlaceholderHighlight.shimmer(),
                    color = Color.LightGray
                )
                .background(Color.LightGray, shape = RoundedCornerShape(4.dp))
        )
    }
}

@Preview(showBackground = true)
@Composable
fun StatCardSkeletonPreview() {
    StatCardSkeleton()
}