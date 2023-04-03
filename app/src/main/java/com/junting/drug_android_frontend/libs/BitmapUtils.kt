package com.junting.drug_android_frontend.libs

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.util.Base64

class BitmapUtils {
    companion object {
        fun base64ToBitmap(base64Image: String): Bitmap {
            // Convert base64 string to byte array
            val decodedBytes = Base64.decode(base64Image, Base64.DEFAULT)

            // Convert byte array to bitmap
            var bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)

            // Get the rotation angle from image metadata
            val exif = ExifInterface(decodedBytes.inputStream())
            val orientation =
                exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
            val rotationAngle = when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> 90
                ExifInterface.ORIENTATION_ROTATE_180 -> 180
                ExifInterface.ORIENTATION_ROTATE_270 -> 270
                else -> 0
            }

            // Rotate the bitmap if necessary
            if (rotationAngle != 0) {
                val matrix = Matrix()
                matrix.postRotate(rotationAngle.toFloat())
                bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
            }
            return bitmap
        }
    }
}
