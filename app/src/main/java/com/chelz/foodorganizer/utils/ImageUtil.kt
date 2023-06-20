package com.chelz.foodorganizer.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import androidx.exifinterface.media.ExifInterface
import java.io.ByteArrayOutputStream

fun getOrientation(exif: ExifInterface?) =
	exif?.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED)

fun decodeByteArray(byteArray: ByteArray?, orientation: Int): ByteArray? {

	val bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray?.size ?: 0)
	val matrix = Matrix()
	when (orientation) {
		ExifInterface.ORIENTATION_ROTATE_90  -> matrix.postRotate(90F)
		ExifInterface.ORIENTATION_ROTATE_180 -> matrix.postRotate(180F)
		ExifInterface.ORIENTATION_ROTATE_270 -> matrix.postRotate(270F)
	}
	val rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)

	val maxSizeBytes = 1024 * 1024 // 1MB
	val outputStream = ByteArrayOutputStream()
	var quality = 100
	rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
	while (outputStream.toByteArray().size > maxSizeBytes && quality > 10) {
		outputStream.reset()
		quality -= 10
		rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
	}
	return outputStream.toByteArray()
}