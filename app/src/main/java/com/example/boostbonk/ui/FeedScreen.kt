import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
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
fun FeedScreen(modifier: Modifier = Modifier) {
    val coroutineScope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val (showCreateSheet, setShowCreateSheet) = remember { mutableStateOf(false) }
    val (description, setDescription) = remember { mutableStateOf("") }
    val (imageUri, setImageUri) = remember { mutableStateOf<Uri?>(null) }
    val (isLoading, setIsLoading) = remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(BonkOrange, BonkYellow),
                    start = Offset(0f, 0f),
                    end = Offset.Infinite
                )
            )
    ) {
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
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(vertical = 24.dp, horizontal = 4.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = stringResource(R.string.community_feed),
                    style = MaterialTheme.typography.displayMedium,
                    color = BonkWhite
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = stringResource(R.string.feed_description),
                    style = MaterialTheme.typography.bodyLarge,
                    color = BonkWhite,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(16.dp))
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(mockPosts) { post ->
                        PostCard(post = post)
                    }
                }
            }

            if (showCreateSheet) {
                ModalBottomSheet(
                    onDismissRequest = { setShowCreateSheet(false) },
                    sheetState = sheetState,
                    containerColor = BonkWhite
                ) {
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
}


@Preview(showBackground = true)
@Composable
fun FeedScreenPreview() {
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
            FeedScreen()
        }
    }
}
