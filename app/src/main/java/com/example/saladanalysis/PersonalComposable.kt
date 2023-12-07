import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.saladanalysis.R

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

@Composable
fun ShowImage(image: ImageBitmap?, selectImage: MutableState<Boolean>) {
    Box(modifier = Modifier
        .size(width = 400.dp, height = 200.dp)
        .border(BorderStroke(1.dp, Color.DarkGray))
    ) {
        Image(
            bitmap = image!!,
            contentDescription = null,
            modifier = Modifier
                .clickable(
                    indication = null,
                    interactionSource = MutableInteractionSource()
                ) { selectImage.value = !selectImage.value }
                .fillMaxSize()
        )
    }
}