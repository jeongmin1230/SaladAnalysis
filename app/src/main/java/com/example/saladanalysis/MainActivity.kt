package com.example.saladanalysis

import PictureTypeRow
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.example.saladanalysis.ui.theme.SaladAnalysisTheme

class MainActivity : ComponentActivity() {
    private var image: Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SaladAnalysisTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen()
                }
            }
        }
    }
}

@Composable
fun MainScreen() {
    var selectImage by remember { mutableStateOf(false) }

    val cameraResult = remember { mutableStateOf<ImageBitmap?>(null) }
    val galleryResult = remember { mutableStateOf<Uri?>(null) }

    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap: Bitmap? ->  cameraResult.value = bitmap?.asImageBitmap() }
    val galleryLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? -> galleryResult.value = uri }
    val uriImage = remember { mutableStateOf<Uri?>(null) }
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = stringResource(id = R.string.main_info),
            style = MaterialTheme.typography.bodyLarge.copy(color = Color.Black, textAlign = TextAlign.Center),
            modifier = Modifier.padding(bottom = 10.dp)
        )
        if(selectImage) {
            AlertDialog(
                onDismissRequest = { selectImage = false },
                containerColor = Color.White,
                confirmButton = {
                    Column(Modifier.fillMaxWidth()) {
                        PictureTypeRow(PaddingValues(bottom = 12.dp), ImageVector.vectorResource(R.drawable.ic_camera), stringResource(R.string.camera)) {
                            cameraResult.value = null
                            galleryResult.value = null
                            selectImage = false
                            cameraLauncher.launch()
                        }
                        PictureTypeRow(PaddingValues(top = 12.dp), ImageVector.vectorResource(R.drawable.ic_gallery), stringResource(R.string.gallery)) {
                            cameraResult.value = null
                            galleryResult.value = null
                            selectImage = false
                            galleryLauncher.launch("image/*")
                        }
                    }
                },
                properties = DialogProperties(usePlatformDefaultWidth = false),
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
            )

        }
        cameraResult.value?.let { camera ->
            LaunchedEffect(camera) {
                cameraResult.value = camera
            }
        }
        galleryResult.value?.let { gallery ->
            LaunchedEffect(gallery) {
                galleryResult.value = gallery
            }
        }
        Column(Modifier.padding(horizontal = 10.dp)) {
            if(cameraResult.value == null && galleryResult.value == null) {
                Box(modifier = Modifier
                    .size(width = 400.dp, height = 200.dp)
                    .border(BorderStroke(1.dp, Color.DarkGray))
                ) {
                    Image(
                        imageVector = ImageVector.vectorResource(R.drawable.question_mark),
                        contentDescription = null,
                        modifier = Modifier
                            .clickable(
                                indication = null,
                                interactionSource = MutableInteractionSource()
                            ) { selectImage = !selectImage }
                            .fillMaxSize()
                    )
                }
            } else if(cameraResult.value != null){
                Box(modifier = Modifier
                    .size(width = 400.dp, height = 200.dp)
                    .border(BorderStroke(1.dp, Color.DarkGray))
                ) {
                    Image(
                        bitmap = cameraResult.value!!,
                        contentDescription = null,
                        modifier = Modifier
                            .clickable(
                                indication = null,
                                interactionSource = MutableInteractionSource()
                            ) { selectImage = !selectImage }
                            .fillMaxSize()
                    )
                }
            }
            else if(galleryResult.value != null) {
                println("in else if ")
                galleryResult.value?.let {
                    Box(modifier = Modifier
                        .size(width = 400.dp, height = 200.dp)
                        .border(BorderStroke(1.dp, Color.DarkGray))
                    ) {
                        Image(
                            bitmap = BitmapFactory.decodeStream(LocalContext.current.contentResolver.openInputStream(it)).asImageBitmap(),
                            contentDescription = null,
                            modifier = Modifier
                                .clickable(
                                    indication = null,
                                    interactionSource = MutableInteractionSource()
                                ) { selectImage = !selectImage }
                                .fillMaxSize()
                        )
                    }
                }

            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun MainScreenPreview(){
    MainScreen()
}