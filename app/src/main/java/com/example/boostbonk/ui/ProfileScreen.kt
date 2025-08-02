import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.boostbonk.R
import com.example.boostbonk.data.mock.mockPosts
import com.example.boostbonk.ui.components.CreatePostModal
import com.example.boostbonk.ui.theme.BonkOrange
import com.example.boostbonk.ui.theme.BonkWhite
import com.example.boostbonk.ui.theme.BonkYellow
import com.example.boostbonk.ui.theme.BoostBonkTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(modifier: Modifier = Modifier) {
    val coroutineScope = rememberCoroutineScope()

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val (showCreateSheet, setShowCreateSheet) = remember { mutableStateOf(false) }
    val (description, setDescription) = remember { mutableStateOf("") }
    val (imageUri, setImageUri) = remember { mutableStateOf<Uri?>(null) }
    val (isLoading, setIsLoading) = remember { mutableStateOf(false) }

    Scaffold(
        containerColor = Color.Transparent,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { setShowCreateSheet(true) },
                containerColor = BonkOrange
            ) {
                Icon(Icons.Default.Add, contentDescription = stringResource(R.string.create_post))
            }
        }
    ) { innerPadding ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 24.dp, horizontal = 4.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    ProfileCard(
                        username = "@cryptodev_mike",
                        description = "Building the future of DeFi on Solana. Passionate about creating tools that empower builders and foster community growth. Always learning, always building!"
                    )
                }

                item {
                    Card(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        colors = CardDefaults.cardColors(BonkWhite)
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                            mockPosts.forEach { post ->
                                PostCard(post = post)
                            }
                        }
                    }
                }
            }

            if (showCreateSheet) {
                CreatePostModal(
                    sheetState = sheetState,
                    coroutineScope = coroutineScope,
                    description = description,
                    imageUri = imageUri,
                    isLoading = isLoading,
                    onDescriptionChange = setDescription,
                    onImageChange = setImageUri,
                    onCancel = {
                        setShowCreateSheet(false)
                        setDescription("")
                        setImageUri(null)
                    },
                    onSubmitSuccess = {
                        setIsLoading(false)
                        setShowCreateSheet(false)
                    },
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    BoostBonkTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(BonkOrange, BonkYellow),
                        start = Offset(0f, 0f),
                        end = Offset.Infinite
                    )
                )
        ) {
            ProfileScreen()
        }
    }
}