package com.shetty.heyalle

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.shetty.heyalle.ui.AlleNavGraph
import com.shetty.heyalle.ui.AppDestinations
import com.shetty.heyalle.ui.Screen
import com.shetty.heyalle.ui.theme.HeyAlleTheme
import com.shetty.heyalle.ui.viewmodel.ImagesViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HeyAlleTheme(darkTheme = false) {
                val navController = rememberNavController()
                val viewModel = hiltViewModel<ImagesViewModel>()
                Scaffold(
                    bottomBar = {
                        BottomAppBarWithNavigation(navController = navController, viewModel)
                    }
                ) {
                    Surface(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(it),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        AlleNavGraph(navController, AppDestinations.HOME_ROUTE, viewModel)
                    }
                }
            }
        }

    }

    @Composable
    @OptIn(ExperimentalMaterial3Api::class)
    private fun AppBar() {
        CenterAlignedTopAppBar(
            colors = TopAppBarDefaults.smallTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                titleContentColor = MaterialTheme.colorScheme.primary,
            ),
            title = {
                Text("Hey alle")
            }
        )
    }
}


@Composable
fun BottomAppBarWithNavigation(
    navController: NavHostController,
    viewModel: ImagesViewModel,
) {
    Surface(
        color = Color.White,
        contentColor = contentColorFor(BottomAppBarDefaults.containerColor),
        tonalElevation = BottomAppBarDefaults.ContainerElevation,
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .windowInsetsPadding(BottomAppBarDefaults.windowInsets)
                .background(Color.White)
                .defaultMinSize(minHeight = 80.dp)
                .padding(BottomAppBarDefaults.ContentPadding),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            NavigationItem(navController, Screen.Home, Icons.Default.Share, "Home")
            NavigationItem(navController, Screen.Search, Icons.Default.Info, "Info")
            ActionItem(viewModel, Icons.Default.Delete, "Delete")
        }
    }
}

@Composable
fun NavigationItem(
    navController: NavHostController,
    screen: Screen,
    icon: ImageVector,
    label: String,
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val isSelected = currentRoute == screen.route

    Column(
        Modifier.clickable {
            navController.navigate(screen.route)
        },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(imageVector = icon, contentDescription = label)
        Text(
            text = label, style = TextStyle(
                color = if (isSelected) Color.Black else Color.LightGray,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
            )
        )
    }
}

@Composable
fun ActionItem(
    viewModel: ImagesViewModel,
    icon: ImageVector,
    label: String
) {
    Column(
        modifier = Modifier.clickable {
            viewModel.deleteImage(viewModel.selectedImageUri.value)
        },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(imageVector = icon, contentDescription = label)
        Text(
            text = label, style = TextStyle(
                color = Color.LightGray,
                fontWeight = FontWeight.Medium
            )
        )
    }
}
