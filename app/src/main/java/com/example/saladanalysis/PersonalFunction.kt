package com.example.saladanalysis

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.task.vision.detector.ObjectDetector
import java.io.IOException

fun loadImageFromUri(context: Context, uri: Uri?): ImageBitmap? {
    return try {
        val contentResolver = context.contentResolver
        val inputStream = contentResolver.openInputStream(uri!!)
        val bitmap = BitmapFactory.decodeStream(inputStream)
        bitmap.asImageBitmap()
    } catch (e: IOException) {
        e.printStackTrace()
        null
    }
}

fun runObjectDetection(context: Context, bitmap: ImageBitmap?): List<DetectionResult> {
    val image = TensorImage.fromBitmap(bitmap?.asAndroidBitmap())
    val option = ObjectDetector.ObjectDetectorOptions.builder()
        .setMaxResults(5)
        .setScoreThreshold(0.5f)
        .build()
    val detector = ObjectDetector.createFromFileAndOptions(
        context,
        "salad.tflite",
        option
    )
    val results = detector.detect(image)
    val resultToDisplay = results.map {
        val category = it.categories.first()
        val text = "${category.label}, ${category.score.times(100).toInt()}"
        DetectionResult(it.boundingBox, text)
    }
    return resultToDisplay.ifEmpty { emptyList() }
}