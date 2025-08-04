import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.boostbonk.viewmodel.BoostBonkViewModel
import com.example.boostbonk.R
import com.example.boostbonk.ui.components.CreatePostModal
import com.example.boostbonk.ui.components.skeletons.PostCardSkeleton
import com.example.boostbonk.ui.theme.BonkOrange
import com.example.boostbonk.ui.theme.BonkWhite
import com.example.boostbonk.ui.theme.BonkYellow
import com.solana.mobilewalletadapter.clientlib.ActivityResultSender

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedScreen(
    modifier: Modifier = Modifier,
    viewModel: BoostBonkViewModel,
    navController: NavController,
    sender: ActivityResultSender
) {
    val coroutineScope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val context = LocalContext.current

    val (showCreateSheet, setShowCreateSheet) = remember { mutableStateOf(false) }
    val (description, setDescription) = remember { mutableStateOf("") }
    val (imageUri, setImageUri) = remember { mutableStateOf<Uri?>(null) }
    val (isLoading, setIsLoading) = remember { mutableStateOf(false) }
    val posts = viewModel.posts.collectAsState()
    val isLoadingPosts = viewModel.isLoadingPosts.collectAsState().value

    LaunchedEffect(Unit) {
        viewModel.loadAllPosts()
    }

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
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = stringResource(R.string.community_feed),
                        style = MaterialTheme.typography.displayMedium,
                        color = BonkWhite,
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentWidth(Alignment.CenterHorizontally)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = stringResource(R.string.feed_description),
                        style = MaterialTheme.typography.bodyLarge,
                        color = BonkWhite,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentWidth(Alignment.CenterHorizontally)
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                }

                if (isLoadingPosts && posts.value.isEmpty()) {
                    items(5) {
                        PostCardSkeleton()
                    }
                } else {
                    items(posts.value) { post ->
                        PostCard(
                            post = post,
                            navController = navController,
                            viewModel = viewModel,
                            sender = sender,
                            onReload = { viewModel.loadAllPosts() }
                        )
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(32.dp)) // bottom padding
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
                            setIsLoading(true)
                            viewModel.submitPost(
                                description = description,
                                imageUri = imageUri,
                                userId = viewModel.userId.value ?: "",
                                context = context
                            ) { success ->
                                setIsLoading(false)
                                if (success) {
                                    setShowCreateSheet(false)
                                    setDescription("")
                                    setImageUri(null)
                                    viewModel.loadAllPosts()
                                }
                            }
                        },
                    )
                }
            }
        }
    }
}