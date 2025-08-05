import android.net.Uri
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.boostbonk.R
import com.example.boostbonk.data.model.UserInfo
import com.example.boostbonk.ui.components.CreatePostModal
import com.example.boostbonk.ui.components.skeletons.PostCardSkeleton
import com.example.boostbonk.ui.components.skeletons.ProfileCardSkeleton
import com.example.boostbonk.ui.theme.BonkOrange
import com.example.boostbonk.ui.theme.BonkWhite
import com.example.boostbonk.viewmodel.BoostBonkViewModel
import com.solana.mobilewalletadapter.clientlib.ActivityResultSender

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    viewModel: BoostBonkViewModel,
    userInfo: UserInfo? = null,
    navController: NavController,
    sender: ActivityResultSender
) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    val toUserId = userInfo?.id ?: ""
    val isLoadingUserPosts = viewModel.isLoadingUserPosts.collectAsState().value
    val isLoadingStats = viewModel.isLoadingUserStats.collectAsState().value
    val userStats = viewModel.userStats.collectAsState().value

    val username = userInfo?.username ?: viewModel.username.collectAsState().value ?: ""
    val displayName = userInfo?.fullName ?: viewModel.fullName.collectAsState().value ?: ""
    val avatarUrl = userInfo?.avatarUrl ?: viewModel.avatarUrl.collectAsState().value ?: ""
    val userWalletAddress = userInfo?.walletAddress ?: viewModel.userWalletAddress.collectAsState().value ?: ""

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val (showCreateSheet, setShowCreateSheet) = remember { mutableStateOf(false) }
    val (description, setDescription) = remember { mutableStateOf("") }
    val (imageUri, setImageUri) = remember { mutableStateOf<Uri?>(null) }
    val (isLoading, setIsLoading) = remember { mutableStateOf(false) }

    val posts = viewModel.userPosts.collectAsState().value
    val isOwnProfile = userInfo?.username == viewModel.username.collectAsState().value

    LaunchedEffect(username) {
        viewModel.loadPostsByUsername(username)
        viewModel.loadUserStatsByUsername(username)
    }

    Scaffold(
        containerColor = Color.Transparent,
        floatingActionButton = {
            if (isOwnProfile && !isLoadingUserPosts && !isLoadingStats && !userWalletAddress.isEmpty()) {
                FloatingActionButton(
                    onClick = { setShowCreateSheet(true) },
                    containerColor = BonkOrange
                ) {
                    Icon(Icons.Default.Add, contentDescription = stringResource(R.string.create_post))
                }
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
                    if (isLoadingUserPosts || isLoadingStats) {
                        ProfileCardSkeleton()
                    } else {
                        ProfileCard(
                            displayName = displayName,
                            avatarUrl = avatarUrl,
                            username = "@${username}",
                            isOwnProfile = isOwnProfile,
                            viewModel = viewModel,
                            toUserId = toUserId,
                            totalBoosts = userStats?.totalBoosts ?: 0,
                            totalBonkEarned = userStats?.totalBonkEarned ?: 0.0,
                            sender = sender,
                            userInfo = userInfo,
                            userWalletAddress = userWalletAddress
                        )
                    }
                }

                item {
                    Card(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        colors = CardDefaults.cardColors(BonkWhite)
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                            if (isLoadingUserPosts || isLoadingStats) {
                                repeat(3) {
                                    PostCardSkeleton(modifier = Modifier.padding(horizontal = 16.dp))
                                }
                            } else {
                                posts.forEach { post ->
                                    PostCard(
                                        post = post,
                                        navController = navController,
                                        viewModel = viewModel,
                                        sender = sender,
                                        onReload = {
                                            viewModel.loadPostsByUsername(username)
                                            viewModel.loadUserStatsByUsername(username)
                                            viewModel.loadWeeklyStats()
                                            viewModel.loadAllTimeStats()
                                        }
                                    )
                                }
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
                        setIsLoading(true)
                        viewModel.submitPost(
                            description = description,
                            imageUri = imageUri,
                            context = context
                        ) { success ->
                            setIsLoading(false)
                            if (success) {
                                setShowCreateSheet(false)
                                setDescription("")
                                setImageUri(null)
                                viewModel.loadPostsByUsername(username)
                            }
                        }
                    },
                )
            }
        }
    }
}