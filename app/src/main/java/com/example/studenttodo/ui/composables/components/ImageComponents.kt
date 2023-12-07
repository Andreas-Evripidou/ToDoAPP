package com.example.studenttodo.ui.composables.components

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.provider.MediaStore.Images.Media.getBitmap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Checkbox
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import java.net.URI

@Composable
fun SelectImage(selectedUri: String, updateSelectedUri: (uri: String) -> Unit, edit: Boolean = false){
    var showImage by remember { mutableStateOf(selectedUri != "") }
    val imageText = if (showImage) "Edit Image" else "Add Image"

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.clickable { showImage = !showImage }
    ) {
        Checkbox(
            checked = showImage,
            onCheckedChange = { showImage = it })
        Text(imageText)
    }

    var pickedImageBitmap by remember { mutableStateOf<ImageBitmap?>(null) }


    if (showImage) {
        val context = LocalContext.current
        val imageFromGalleryLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.PickVisualMedia()
        ) { uri: Uri? ->
            if (uri == null) {
                pickedImageBitmap = null
            } else {
                val contentResolver: ContentResolver = context.contentResolver
                pickedImageBitmap = ImageDecoder.decodeBitmap(
                    ImageDecoder.createSource(contentResolver, uri)
                ).asImageBitmap()
                 updateSelectedUri(uri.toString())
            }
        }



        Column {
            pickedImageBitmap?.let { imageBitmap ->
                Image(imageBitmap, null)
            }
        }
        OutlinedButton(onClick = {
            imageFromGalleryLauncher.launch(
                PickVisualMediaRequest(mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
        }
        ) {
            Text("Select From Gallery")
        }
    }
}
