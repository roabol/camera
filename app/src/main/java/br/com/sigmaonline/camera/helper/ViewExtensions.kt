package br.com.sigmaonline.camera.helper

import android.os.Build
import android.view.DisplayCutout
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog

/** Milissegundos usados para animações de IU */
const val ANIMATION_FAST_MILLIS = 50L
const val ANIMATION_SLOW_MILLIS = 100L

/** Pad this view with the insets provided by the device cutout (i.e. notch) */
@RequiresApi(Build.VERSION_CODES.P)
fun View.padWithDisplayCutout() {

    /** Helper method that applies padding from cutout's safe insets */
    fun doPadding(cutout: DisplayCutout) = setPadding(
        cutout.safeInsetLeft,
        cutout.safeInsetTop,
        cutout.safeInsetRight,
        cutout.safeInsetBottom
    )

    // Apply padding using the display cutout designated "safe area"
    rootWindowInsets?.displayCutout?.let { doPadding(it) }

    // Set a listener for window insets since view.rootWindowInsets may not be ready yet
    setOnApplyWindowInsetsListener { _, insets ->
        insets.displayCutout?.let { doPadding(it) }
        insets
    }
}

/** Same as [AlertDialog.show] but setting immersive mode in the dialog's window */
fun AlertDialog.showImmersive() {
    // Defina a caixa de diálogo para não focalizável
    ownerActivity?.window?.setFlags(
        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
    )

    /** Combinação de todos os sinalizadores necessários para colocar a atividade em modo imersivo */
    // Certifique-se de que a janela da caixa de diálogo esteja em tela inteira
    // https://stackoverflow.com/questions/62764130/immersive-mode-android-app-all-documentation-is-deprecated
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        ownerActivity?.window?.insetsController?.hide(WindowInsets.Type.statusBars())
    } else {
        @Suppress("DEPRECATION")
        ownerActivity?.window?.decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_LOW_PROFILE or
                View.SYSTEM_UI_FLAG_FULLSCREEN or
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
    }

    // Mostra a caixa de diálogo enquanto ainda está no modo imersivo
    show()

    // Defina a caixa de diálogo para focalizar novamente
    ownerActivity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE)
}