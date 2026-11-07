package com.student.snhu.nutriscan360;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

public class ImagePickerActivity extends AppCompatActivity {

    private ActivityResultLauncher<Intent> imagePickerLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Uri imageUri = result.getData().getData();
                        if (imageUri != null) {
                            processBarcodeImage(imageUri);
                        }
                    } else {
                        finish();
                    }
                }
        );

        openImagePicker();
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        imagePickerLauncher.launch(Intent.createChooser(intent, "Select Barcode Image"));
    }

    private void processBarcodeImage(Uri uri) {
        BarcodeProcessor.processImageFromUri(this, uri, new BarcodeProcessor.BarcodeCallback() {
            @Override
            public void onBarcodeDetected(String upc) {
                Log.d("Barcode", "Detected UPC: " + upc);
                Toast.makeText(ImagePickerActivity.this, "UPC: " + upc, Toast.LENGTH_SHORT).show();

                runOnUiThread(() -> {
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(android.R.id.content, NutritionFragment.newInstance(upc))
                            .commit();
                });
            }

            @Override
            public void onBarcodeNotFound() {
                Toast.makeText(ImagePickerActivity.this, "No barcode found", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onError(Exception e) {
                Toast.makeText(ImagePickerActivity.this, "Barcode scan failed", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
                finish();
            }
        });
    }
}
