package com.example.studenttodo.ui.composables.components

import android.content.ContentResolver
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

@RequiresApi(Build.VERSION_CODES.P)
@Composable
fun SelectImage(selectedUri: String, updateSelectedUri: (uri: String) -> Unit, edit: Boolean = false){
    val showImage = remember { mutableStateOf(selectedUri != "") }
    val imageText = if (edit && selectedUri != "") "Edit Image" else "Add Image"
    var pickedImageBitmap by remember { mutableStateOf<ImageBitmap?>(null) }
    var hasCameraPermission by remember { mutableStateOf(false) }

    CustomSubMenu(text = imageText, showMore = showImage )

    Spacer(modifier = Modifier.size(4.dp))

    if (edit) {
        if (showImage.value) {

            val context = LocalContext.current
            val uri = Uri.parse(selectedUri)
            var bitmap : Bitmap?= null
            uri?.let { uri ->
                    val source = ImageDecoder.createSource(context.contentResolver,uri)
                    bitmap = ImageDecoder.decodeBitmap(source)
                }
            if (bitmap != null) {
                Row (modifier = Modifier
                    .widthIn(max = 260.dp)
                    .heightIn(max = 400.dp)) {
                    Image(bitmap = bitmap!!.asImageBitmap(), contentDescription = "Selected image")
                }
            }

        }
    }

    if (showImage.value) {
        val context = LocalContext.current
        val imageFromGalleryLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.PickVisualMedia(),
            onResult = { uri: Uri? ->
            if (uri == null) {
                pickedImageBitmap = null
            } else {
                val contentResolver: ContentResolver = context.contentResolver
                contentResolver.takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION)
                pickedImageBitmap = ImageDecoder.decodeBitmap(
                    ImageDecoder.createSource(contentResolver, uri)
                ).asImageBitmap()
                 updateSelectedUri(uri.toString())
            }

        })

        Row (modifier = Modifier
            .widthIn(max = 260.dp)
            .heightIn(max = 400.dp)){
            pickedImageBitmap?.let { imageBitmap ->
                Image(imageBitmap, null)

                Spacer(modifier = Modifier.size(4.dp))
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

