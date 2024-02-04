import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material.Card
import androidx.compose.material.Checkbox
import androidx.compose.material.IconToggleButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ddaypunk.aperture.MainScreenState
import com.ddaypunk.aperture.MainScreenViewModel
import com.ddaypunk.aperture.R
import com.ddaypunk.aperture.data.Award
import org.jetbrains.compose.resources.ExperimentalResourceApi

@Composable
fun MainScreen(
    viewModel: MainScreenViewModel
) {
    val state = viewModel.uiState.collectAsState()

    when(state.value) {
        is MainScreenState.Ready -> MainScreenReady(state.value as MainScreenState.Ready)
        is MainScreenState.Loading -> MainScreenLoading()
    }
}

@Composable
fun MainScreenLoading() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Loading...")
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MainScreenReady(state: MainScreenState.Ready) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(all = 16.dp),
        verticalArrangement = Arrangement
            .spacedBy(8.dp)
    ) {
        with(state.uiState) {
            seasons?.let { nonNullSeasons ->
                nonNullSeasons.forEach { season ->
                    stickyHeader {
                        SeasonHeader(
                            state = SeasonHeaderState(
                                title = "${season.year}"
                            )
                        )
                    }
                    items(season.awards) { award: Award ->
                        AwardCard(
                            state = AwardCardState(
                                award = award
                            ),
                            accessibility = AwardCardAccessibilityState(
                                seasonYear = season.year
                            )
                        )
                    }
                }
            }
        }
    }
}

data class SeasonHeaderState(
    val title: String
)

@Composable
fun SeasonHeader(
    state: SeasonHeaderState
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.DarkGray)
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            text = state.title,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            textDecoration = TextDecoration.Underline,
            color = Color.White

        )
    }
}

data class AwardCardState(
    val award: Award
)

data class AwardCardAccessibilityState(
    val seasonYear: Int
)

@Composable
fun AwardCard(
    state: AwardCardState,
    accessibility: AwardCardAccessibilityState
) {
    var showNominations by remember { mutableStateOf(false) }
    Card {
        Column {
            Row(
                modifier = Modifier
                    .padding(start = 16.dp)
                    .toggleable(
                        value = showNominations,
                        enabled = true,
                        role = Role.DropdownList
                    ) { rowExpanded ->
                        showNominations = rowExpanded
                    },
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    modifier = Modifier.weight(1f),
                    text = state.award.category,
                    fontSize = 20.sp
                )
                IconToggleButton(
                    checked = showNominations,
                    onCheckedChange = { showNominations = !showNominations },
                    enabled = true
                ) {
                    if (showNominations) {
                        Image(
                            painter = painterResource(R.drawable.ic_expand_less_24),
                            contentDescription = "${accessibility.seasonYear} ${state.award.category} show less"
                        )
                    } else {
                        Image(
                            painter = painterResource(R.drawable.ic_expand_more_24),
                            contentDescription = "${accessibility.seasonYear} ${state.award.category} show more"
                        )
                    }
                }
            }

            AnimatedVisibility(
                visible = showNominations
            ) {
                Column {
                    state.award.nominations.forEach { nominee ->
                        Nominee(
                            state = NomineeState(
                                title = nominee.name,
                                isWinner = nominee.won
                            )
                        )
                    }
                }
            }
        }
    }
}
data class NomineeState(
    val title: String,
    val isWinner: Boolean
)

@OptIn(ExperimentalResourceApi::class)
@Composable
fun Nominee(
    state: NomineeState
) {
    // This will need to be in the ViewModel later
    val checked = remember { mutableStateOf(false) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.toggleable(
            value = checked.value,
            enabled = true,
            role = Role.Checkbox
        ) { isChecked ->
            checked.value = isChecked
        }
    ) {

        Checkbox(
            checked = checked.value,
            enabled = true,
            onCheckedChange = { isChecked ->
                checked.value = isChecked
            }
        )
        Text(
            modifier = Modifier.weight(1f),
            text = state.title, fontSize = 18.sp
        )
        if (state.isWinner) {
            Image(
                modifier = Modifier.padding(end = 16.dp),
                painter = painterResource(R.drawable.ic_trophy_24),
                contentDescription = "category winner",
                colorFilter = ColorFilter.tint(Color.Yellow)
            )
        }
    }
}