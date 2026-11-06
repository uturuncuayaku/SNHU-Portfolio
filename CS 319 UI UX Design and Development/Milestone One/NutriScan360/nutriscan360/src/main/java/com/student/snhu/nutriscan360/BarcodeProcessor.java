package com.student.snhu.nutriscan360;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.common.InputImage;

import java.io.IOException;
import java.util.List;

public class BarcodeProcessor {

    public interface BarcodeCallback {
        void onBarcodeDetected(String upc);
        void onBarcodeNotFound();
        void onError(Exception e);
    }

    public static void processImageFromUri(@NonNull Context context, @NonNull Uri uri, @NonNull BarcodeCallback callback) {
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
            InputImage image = InputImage.fromBitmap(bitmap, 0);
            BarcodeScanner scanner = BarcodeScanning.getClient();

            scanner.process(image)
                    .addOnSuccessListener(barcodes -> {
                        if (barcodes.isEmpty()) {
                            callback.onBarcodeNotFound();
                        } else {
                            String upc = barcodes.get(0).getRawValue();
                            callback.onBarcodeDetected(upc);
                        }
                    })
                    .addOnFailureListener(e -> {
                        callback.onError(e);
                    });

        } catch (IOException e) {
            callback.onError(e);
        }
    }
}
