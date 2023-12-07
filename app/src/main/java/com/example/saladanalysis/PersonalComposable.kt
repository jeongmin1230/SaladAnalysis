import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.example.saladanalysis.R

@Composable
fun SelectDialog(result: MutableState<ImageBitmap?>, onDismiss: () -> Unit) {
    val cameraResult = remember { mutableStateOf<Bitmap?>(null) }
    val galleryResult = remember { mutableStateOf<Uri?>(null) }

    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap: Bitmap? ->  cameraResult.value = bitmap }
    val galleryLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? -> galleryResult.value = uri }

    AlertDialog(
        onDismissRequest = { onDismiss() },
        containerColor = Color.White,
        confirmButton = {
            Column(Modifier.fillMaxWidth()) {
                PictureTypeRow(PaddingValues(bottom = 12.dp), ImageVector.vectorResource(R.drawable.ic_camera), stringResource(R.string.camera)) {
                    onDismiss()
                    cameraLauncher.launch()
                }
                PictureTypeRow(PaddingValues(top = 12.dp), ImageVector.vectorResource(R.drawable.ic_gallery), stringResource(R.string.gallery)) {
                    onDismiss()
                    galleryLauncher.launch("image/*")
                }
            }
        },
        properties = DialogProperties(usePlatformDefaultWidth = false),
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
    )
    cameraResult.value?.let { camera ->
        LaunchedEffect(camera) {
            result.value = camera.asImageBitmap()
        }
    }
}

@Composable
fun PictureTypeRow(paddingValues: PaddingValues, image: ImageVector, text: String, onClick: () -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(paddingValues)
            .clickable(
                interactionSource = MutableInteractionSource(),
                indication = null
            ) { onClick() }) {
        Image(imageVector = image,
            contentDescription = stringResource(id = R.string.select_image_type),
            modifier = Modifier.padding(end = 24.dp))
        Text(text = text,
            style = MaterialTheme.typography.bodyMedium.copy(Color.Black),
            modifier = Modifier.fillMaxWidth())
    }
}