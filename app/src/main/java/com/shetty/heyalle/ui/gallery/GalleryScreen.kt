package com.shetty.heyalle.ui.gallery

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.shetty.heyalle.R
import com.shetty.heyalle.ui.UIState
import com.shetty.heyalle.ui.viewmodel.ImagesViewModel


private const val TAG = "GalleryScreen"

@Composable
fun GalleryScreen(
    viewModel: ImagesViewModel,
    modifier: Modifier = Modifier
) {

    val state: UIState by viewModel.state.collectAsState()

    RequestPermission(viewModel)
    ComposableLifecycle { _, event ->
        if (event == Lifecycle.Event.ON_START) {
            viewModel.getImages()
        }
    }

    when (val data = state) {
        is UIState.Error -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                val context = LocalContext.current
                Text(stringResource(R.string.something_went_wrong))
                OutlinedButton(onClick = {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).also {
                        val uri = Uri.parse("package:${context.packageName}")
                        it.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        it.data = uri
                    }
                    startActivity(context, intent, null)
                }) {
                    Text(text = stringResource(R.string.grant_permission))
                }
            }
        }
        is UIState.Loading -> {

        }
        is UIState.Success -> {
            SuccessWidget(modifier, data, viewModel)
        }

        is UIState.PermissionNotGranted -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                val context = LocalContext.current
                Text(stringResource(R.string.app_needs_storage_permission_to_work))
                OutlinedButton(onClick = {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).also {
                        val uri = Uri.parse("package:${context.packageName}")
                        it.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        it.data = uri
                    }
                    startActivity(context, intent, null)
                }) {
                    Text(text = stringResource(R.string.grant_permission))
                }
            }
        }
    }

}

@Composable
private fun SuccessWidget(
    modifier: Modifier,
    data: UIState.Success,
    viewModel: ImagesViewModel
) {
    var selectedIndex by remember { mutableIntStateOf(-1) }
    val selectedImageUri: String by viewModel.selectedImageUri.collectAsState()
    Column(
        modifier
            .fillMaxHeight()
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(

            contentScale = ContentScale.Crop,
            model = ImageRequest.Builder(LocalContext.current)
                .data(selectedImageUri).build(),
            contentDescription = "preview",
            modifier = modifier
                .padding(horizontal = 8.dp)
                .weight(1f, true)
                .clickable {

                }
        )
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(2.dp)
                .background(Color.White)
        )

        LazyRow {
            items(data.articles.size) {
                val isSelected = it == selectedIndex
                Box(
                    modifier = modifier.background(
                        color = if (isSelected) Color.LightGray else Color.White,
                    ).padding(all = 8.dp)
                ) {
                    AsyncImage(
                        contentScale = ContentScale.Crop,
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(data.articles[it]).build(),
                        contentDescription = "preview",
                        modifier = Modifier
                            .height(100.dp)
                            .width(100.dp)
//                        .padding(horizontal = 8.dp)
                            .clickable {
                                selectedIndex = it
                                viewModel.selectImage(data.articles[selectedIndex])
                            }
                    )
                }
            }
        }
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(Color.LightGray)
        )

    }
}

@Composable
private fun RequestPermission(vm: ImagesViewModel) {

    val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        Manifest.permission.READ_MEDIA_IMAGES
    } else {
        Manifest.permission.READ_EXTERNAL_STORAGE
    }
    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            vm.getImages()
        } else {
            vm.permissionsGranted(false)
        }
    }

    if (isPermissionGranted().not()) {
        SideEffect {
            launcher.launch(permission)
        }
    }

}

@Composable
fun isPermissionGranted(): Boolean {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        return ContextCompat.checkSelfPermission(
            LocalContext.current,
            Manifest.permission.READ_MEDIA_IMAGES
        ) == PackageManager.PERMISSION_GRANTED

    } else {
        return ContextCompat.checkSelfPermission(
            LocalContext.current,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }
}

@Composable
fun ComposableLifecycle(
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    onEvent:  (LifecycleOwner, Lifecycle.Event) -> Unit
) {

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { source, event ->
            onEvent(source, event)
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
}



