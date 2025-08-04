import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowOutward
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.boostbonk.viewmodel.BoostBonkViewModel
import com.example.boostbonk.R
import com.example.boostbonk.ui.components.skeletons.LeaderboardCardSkeleton
import com.example.boostbonk.ui.components.skeletons.StatCardSkeleton
import com.example.boostbonk.ui.theme.BonkOrange
import com.example.boostbonk.ui.theme.BonkWhite
import com.example.boostbonk.utils.formatBonkEarned

@Composable
fun RankingScreen(
    modifier: Modifier = Modifier,
    viewModel: BoostBonkViewModel,
    navController: NavController
) {
    val scrollState = rememberScrollState()

    val thisWeek = "thisWeek"
    val allTime = "allTime"
    val boosts = "boosts"
    val bonks = "bonks"

    val (selectedTime, setSelectedTime) = remember { mutableStateOf(thisWeek) }
    val (selectedMode, setSelectedMode) = remember { mutableStateOf(boosts) }
    val weeklyStatsSummary = viewModel.weeklyStats.collectAsState().value
    val allTimeStatsSummary = viewModel.allTimeStats.collectAsState().value

    val isLoadingStats = if (selectedTime == thisWeek)
        viewModel.isLoadingWeeklyStats.collectAsState().value
    else
        viewModel.isLoadingAllTimeStats.collectAsState().value

    Log.e("allTimeStatsSummary", allTimeStatsSummary.toString())

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)
        .verticalScroll(scrollState)
    )
    {
        Card(
            modifier = Modifier
                .fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
            colors = CardDefaults.cardColors(containerColor = BonkWhite)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (isLoadingStats) {
                    StatCardSkeleton(modifier = Modifier.weight(1f))
                    StatCardSkeleton(modifier = Modifier.weight(1f))
                } else {
                    StatCard(
                        value = if (selectedTime == thisWeek)
                            weeklyStatsSummary?.weeklyTotalBoosts.toString()
                        else
                            allTimeStatsSummary?.allTimeTotalBoosts.toString(),
                        label = stringResource(R.string.total_boosts),
                        modifier = Modifier.weight(1f)
                    )

                    StatCard(
                        value = if (selectedTime == thisWeek)
                            formatBonkEarned(weeklyStatsSummary?.weeklyTotalBonkEarned?.toDouble() ?: 0.0)
                        else
                            formatBonkEarned(allTimeStatsSummary?.allTimeTotalBonkEarned?.toDouble() ?: 0.0),
                        label = stringResource(R.string.total_bonk_earned),
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
        Spacer(
            modifier = Modifier.height(20.dp)
        )
        Card(
            modifier = Modifier
                .fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
            colors = CardDefaults.cardColors(containerColor = BonkWhite)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),

            ) {
                CustomButton(
                    onClick = { setSelectedTime(thisWeek) },
                    contentColor = if (selectedTime == thisWeek) BonkWhite else BonkOrange,
                    backgroundColor = if (selectedTime == thisWeek) BonkOrange else BonkWhite,
                    text = stringResource(R.string.this_week),
                    icon = Icons.Filled.Schedule,
                    modifier = Modifier
                        .weight(1f)
                        .border(
                            width = 1.dp,
                            color = if (selectedTime == thisWeek) BonkWhite else BonkOrange,
                            shape = RoundedCornerShape(12.dp)
                        )
                )

                CustomButton(
                    onClick = { setSelectedTime(allTime) },
                    contentColor = if (selectedTime == allTime) BonkWhite else BonkOrange,
                    backgroundColor = if (selectedTime == allTime) BonkOrange else BonkWhite,
                    text = stringResource(R.string.all_time),
                    icon = Icons.Filled.CalendarMonth,
                    modifier = Modifier
                        .weight(1f)
                        .border(
                            width = 1.dp,
                            color = if (selectedTime == allTime) BonkWhite else BonkOrange,
                            shape = RoundedCornerShape(12.dp)
                        )
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 0.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),

                ) {
                CustomButton(
                    onClick = { setSelectedMode(boosts) },
                    contentColor = if (selectedMode == boosts) BonkWhite else BonkOrange,
                    backgroundColor = if (selectedMode == boosts) BonkOrange else BonkWhite,
                    text = stringResource(R.string.total_boosts),
                    icon = Icons.Filled.ArrowOutward,
                    modifier = Modifier
                        .weight(1f)
                        .border(
                            width = 1.dp,
                            color = if (selectedMode == boosts) BonkWhite else BonkOrange,
                            shape = RoundedCornerShape(12.dp)
                        )
                )

                CustomButton(
                    onClick = { setSelectedMode(bonks) },
                    contentColor = if (selectedMode == bonks) BonkWhite else BonkOrange,
                    backgroundColor = if (selectedMode == bonks) BonkOrange else BonkWhite,
                    text = stringResource(R.string.total_bonk_earned),
                    icon = Icons.Filled.AttachMoney,
                    modifier = Modifier
                        .weight(1f)
                        .border(
                            width = 1.dp,
                            color = if (selectedMode == bonks) BonkWhite else BonkOrange,
                            shape = RoundedCornerShape(12.dp)
                        )
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            if (selectedTime == thisWeek) {
                WeekCountdown()
            }

            Spacer(modifier = Modifier.height(12.dp))

            if (selectedTime == thisWeek) {
                if (selectedMode == boosts) {
                    WeeklyBoostRankingList(
                        viewModel = viewModel,
                        navController = navController
                    )
                } else {
                    WeeklyBonkEarnedRankingList(
                        viewModel = viewModel,
                        navController = navController
                    )
                }
            } else {
                if (selectedMode == boosts) {
                    AllTimeBoostRankingList(
                        viewModel = viewModel,
                        navController = navController
                    )
                } else {
                    AllTimeBonkEarnedRankingList(
                        viewModel = viewModel,
                        navController = navController
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Composable
fun WeeklyBoostRankingList(
    modifier: Modifier = Modifier,
    viewModel: BoostBonkViewModel,
    navController: NavController
) {
    LaunchedEffect(Unit) {
        viewModel.loadWeeklyBoostRanking()
    }

    val ranking = viewModel.weeklyBoostRanking.collectAsState().value
    val isLoading = viewModel.isLoadingWeeklyBoostRanking.collectAsState().value

    Column(modifier = modifier) {
        if (isLoading && ranking.isEmpty()) {
            repeat(5) {
                LeaderboardCardSkeleton()
            }
        } else {
            ranking.forEachIndexed { index, user ->
                LeaderboardBoostCard(
                    rank = index + 1,
                    avatarUrl = user.avatarUrl,
                    username = user.username,
                    fullName = user.fullName,
                    boostCount = user.boostCount,
                    navController = navController
                )
            }
        }
    }
}
@Composable
fun WeeklyBonkEarnedRankingList(
    modifier: Modifier = Modifier,
    viewModel: BoostBonkViewModel,
    navController: NavController
) {
    LaunchedEffect(Unit) {
        viewModel.loadWeeklyBonkEarnedRanking()
    }

    val ranking = viewModel.weeklyBonkEarnedRanking.collectAsState().value
    val isLoading = viewModel.isLoadingWeeklyBonkEarnedRanking.collectAsState().value

    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        if (isLoading && ranking.isEmpty()) {
            repeat(5) {
                LeaderboardCardSkeleton()
            }
        } else {
            ranking.forEachIndexed { index, user ->
                LeaderboardBonkEarnedCard(
                    avatarUrl = user.avatarUrl,
                    rank = index + 1,
                    username = user.username,
                    totalBonkEarned = formatBonkEarned(user.totalBonkEarned.toDouble()),
                    fullName = user.fullName,
                    navController = navController
                )
            }
        }
    }
}

@Composable
fun AllTimeBoostRankingList(
    modifier: Modifier = Modifier,
    viewModel: BoostBonkViewModel,
    navController: NavController
) {
    LaunchedEffect(Unit) {
        viewModel.loadAllTimeBoostRanking()
    }

    val ranking = viewModel.allTimeBoostRanking.collectAsState().value
    val isLoading = viewModel.isLoadingAllTimeBoostRanking.collectAsState().value

    Column(modifier = modifier) {
        if (isLoading && ranking.isEmpty()) {
            repeat(5) {
                LeaderboardCardSkeleton()
            }
        } else {
            ranking.forEachIndexed { index, user ->
                LeaderboardBoostCard(
                    rank = index + 1,
                    avatarUrl = user.avatarUrl,
                    username = user.username,
                    fullName = user.fullName,
                    boostCount = user.boostCount,
                    navController = navController
                )
            }
        }
    }
}
@Composable
fun AllTimeBonkEarnedRankingList(
    modifier: Modifier = Modifier,
    viewModel: BoostBonkViewModel,
    navController: NavController
) {
    LaunchedEffect(Unit) {
        viewModel.loadAllTimeBonkEarnedRanking()
    }

    val ranking = viewModel.allTimeBonkEarnedRanking.collectAsState().value
    val isLoading = viewModel.isLoadingAllTimeBonkEarnedRanking.collectAsState().value

    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        if (isLoading && ranking.isEmpty()) {
            repeat(5) {
                LeaderboardCardSkeleton()
            }
        } else {
            ranking.forEachIndexed { index, user ->
                LeaderboardBonkEarnedCard(
                    avatarUrl = user.avatarUrl,
                    rank = index + 1,
                    username = user.username,
                    totalBonkEarned = formatBonkEarned(user.totalBonkEarned.toDouble()),
                    fullName = user.fullName,
                    navController = navController
                )
            }
        }
    }
}