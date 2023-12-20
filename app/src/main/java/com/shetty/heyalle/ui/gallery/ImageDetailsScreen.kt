@file:OptIn(ExperimentalLayoutApi::class)

package com.shetty.heyalle.ui.gallery

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.staggeredgrid.LazyHorizontalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.shetty.heyalle.R
import com.shetty.heyalle.ui.viewmodel.ImagesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InfoScreen(viewModel: ImagesViewModel) {
    val focusManager = LocalFocusManager.current
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    focusManager.clearFocus()
                })
            }
            .padding(horizontal = 16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.Start

    ) {
        val selectedImageUri: String by viewModel.selectedImageUri.collectAsState()
        val collections by viewModel.collectionLabels.collectAsState()
        val description by viewModel.imageDescription.collectAsState()
        viewModel.fetchCollectionsForImage(selectedImageUri)
        viewModel.fetchDescriptionForImage(selectedImageUri)
        AsyncImage(
            contentScale = ContentScale.Crop,
            model = ImageRequest.Builder(LocalContext.current)
                .data(selectedImageUri).build(),
            contentDescription = "preview",
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .height(500.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        var text by rememberSaveable { mutableStateOf("Text") }
        OutlinedTextField(
            value = text, onValueChange = {
                text = it
            },
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally)
        )
        CollectionsWidget(collections)
        DescriptionWidget(description = description)
    }
}

@Composable
private fun DescriptionWidget(description: String) {
    Spacer(modifier = Modifier.height(16.dp))
    SectionTitle(title = stringResource(id = R.string.title_description))
    Text(
        text = description, style = TextStyle(
            color = Color.LightGray,
            fontWeight = FontWeight.Medium, fontSize = 16.sp
        )
    )
}

@Composable
private fun CollectionsWidget(collections: List<String>) {
    Spacer(modifier = Modifier.height(16.dp))
    SectionTitle(title = stringResource(id = R.string.title_collections_label))
    if (collections.isEmpty().not()) {
        FlowRow {
            for (element in collections) {
                Box(modifier = Modifier.padding(4.dp)) {
                    Text(
                        text = element,
                        style = TextStyle(
                            color = Color.DarkGray,
                            fontWeight = FontWeight.Bold, fontSize = 14.sp
                        ),
                        modifier = Modifier
                            .background(
                                color = colorResource(id = R.color.label_background),
                                shape = RoundedCornerShape(4.dp)
                            )
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun SectionTitle(title: String) {
    Text(
        text = title,
        style = TextStyle(
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        )
    )
}