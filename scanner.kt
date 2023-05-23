import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.zxing.Result
import me.dm7.barcodescanner.zxing.ZXingScannerView

class MainActivity : AppCompatActivity(), ZXingScannerView.ResultHandler {

    private lateinit var scannerView: ZXingScannerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA),
                CAMERA_PERMISSION_REQUEST
            )
        } else {
            startScanner()
        }
    }

    override fun onResume() {
        super.onResume()
        if (::scannerView.isInitialized) {
            scannerView.setResultHandler(this)
            scannerView.startCamera()
        }
    }

    override fun onPause() {
        super.onPause()
        if (::scannerView.isInitialized) {
            scannerView.stopCamera()
        }
    }

    override fun handleResult(result: Result?) {
        if (result != null) {
            val barcodeValue = result.text
            Toast.makeText(this, "Scanned Barcode: $barcodeValue", Toast.LENGTH_SHORT).show()
        }
        scannerView.resumeCameraPreview(this)
    }

    private fun startScanner() {
        scannerView = ZXingScannerView(this)
        setContentView(scannerView)
        scannerView.setResultHandler(this)
        scannerView.startCamera()
    }

    companion object {
        private const val CAMERA_PERMISSION_REQUEST = 123
    }
}
