import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.boostbonk.BoostBonkViewModel
import com.example.boostbonk.R
import com.example.boostbonk.data.model.PostWithUser
import com.example.boostbonk.ui.components.ColumnValue
import com.example.boostbonk.ui.theme.BonkGray
import com.example.boostbonk.ui.theme.BonkOrange
import com.example.boostbonk.ui.theme.BonkWhite
import com.example.boostbonk.utils.formatBonkEarned
import com.example.boostbonk.utils.formatRelativeTime

@Composable
fun PostCard(
    modifier: Modifier = Modifier,
    post: PostWithUser,
    navController: NavController,
    viewModel: BoostBonkViewModel,
) {
    val coroutineScope = rememberCoroutineScope()

    val (showBoostModal, setShowBoostModal) = remember { mutableStateOf(false) }
    val (isLoading, setIsLoading) = remember { mutableStateOf(false) }

    val username = viewModel.username.collectAsState().value ?: ""
    val isOwnProfile = post.username == username
    val userId = viewModel.userId.collectAsState().value ?: ""

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(BonkWhite)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(12.dp)
                .clickable {
                    navController.navigate("profile/${post.username}")
                }
        ) {
            Image(
                painter = rememberAsyncImagePainter(post.avatarUrl),
                contentDescription = null,
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(8.dp))

            Column {
                Text(
                    text = "${post.fullName}",
                    style = MaterialTheme.typography.displaySmall,
                    color = BonkGray
                )
                Text(
                    text = "@${post.username}",
                    style = MaterialTheme.typography.labelLarge,
                    color = BonkOrange
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = formatRelativeTime(post.createdAt),
                    style = MaterialTheme.typography.bodyMedium,
                    color = BonkGray
                )
            }
        }

        Text(
            text = post.description,
            modifier = Modifier.padding(horizontal = 12.dp),
            style = MaterialTheme.typography.bodyMedium,
            color = BonkGray
        )

        Spacer(modifier = Modifier.height(8.dp))

        post.image?.takeIf { it.isNotBlank() }?.let { imageUrl ->
            Image(
                painter = rememberAsyncImagePainter(imageUrl),
                contentDescription = "Post image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .padding(horizontal = 12.dp)
                    .clip(RoundedCornerShape(10.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(12.dp))
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp)
                .background(BonkGray, shape = RoundedCornerShape(10.dp))
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                ColumnValue(
                    name = stringResource(R.string.boosts),
                    value = "%,d".format(post.boosts)
                )
            }
            Spacer(modifier = Modifier.width(4.dp))
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                ColumnValue(
                    name = stringResource(R.string.bonk_earned),
                    value = formatBonkEarned(post.bonkEarned ?: 0.0)
                )
            }

            if (!isOwnProfile) {
                Spacer(modifier = Modifier.width(20.dp))
                BoostButton(
                    modifier = Modifier.weight(1f),
                    onClick = { setShowBoostModal(true) }
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        if (showBoostModal) {
            BoostBonkModal(
                onDismiss = { setShowBoostModal(false) },
                coroutineScope = coroutineScope,
                isLoading = isLoading,
                onSubmit = { amount ->
                    setIsLoading(true)
                    viewModel.submitBoost(
                        bonks = amount,
                        postId = post.id,
                        fromUserId = userId,
                        toUserId = post.userId
                    ) { success ->
                        setIsLoading(false)
                        setShowBoostModal(false)
                        if (success) {
                            viewModel.getAllPosts()
                        }
                    }
                }
            )
        }

    }
}