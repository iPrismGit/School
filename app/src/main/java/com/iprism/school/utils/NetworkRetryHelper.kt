import android.R
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.view.LayoutInflater
import android.view.ViewGroup
import com.iprism.school.databinding.NoInternetDialogBinding

object NetworkRetryHelper {
    fun <T> checkAndCallWithRetry(
        context: Context,
        request: T,
        apiCall: (T) -> Unit
    ) {
        if (context.isConnected()) {
            apiCall(request)
        } else {
            context.showNoInternetDialog {
                checkAndCallWithRetry(context, request, apiCall)
            }
        }
    }

    private fun Context.showNoInternetDialog(onRetry: () -> Unit) {
        if (this !is Activity || isFinishing || isDestroyed) return
        val dialog = Dialog(this, R.style.ThemeOverlay)
        val binding = NoInternetDialogBinding.inflate(LayoutInflater.from(this))
        dialog.setContentView(binding.root)
        dialog.setCancelable(false)

        binding.refreshBtn.setOnClickListener {
            dialog.dismiss()
            onRetry()
        }

        dialog.window?.apply {
            setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        }

        dialog.show()
    }

    private fun Context.isConnected(): Boolean {
        val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = cm.activeNetwork ?: return false
        val capabilities = cm.getNetworkCapabilities(network) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
                capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
    }
}
