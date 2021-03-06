package br.com.sigmaonline.camera

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import java.io.File

val EXTENSION_WHITELIST = arrayOf("JPG")
typealias LumaListener = (luma: Double) -> Unit

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    companion object {

        /** Use external media if it is available, our app's file directory otherwise */
        fun getOutputDirectory(context: Context): File {
            val appContext = context.applicationContext
            val mediaDir = context.getExternalFilesDir(null).let {
                File(it, appContext.resources.getString(R.string.app_name)).apply { mkdirs() }
            }
            return if (mediaDir.exists())
                mediaDir else appContext.filesDir
        }
    }
}