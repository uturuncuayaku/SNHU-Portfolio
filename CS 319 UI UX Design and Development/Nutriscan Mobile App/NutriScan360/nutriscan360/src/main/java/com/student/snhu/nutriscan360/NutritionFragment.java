package com.student.snhu.nutriscan360;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class NutritionFragment extends Fragment {

    private static final String ARG_UPC = "upc";
    private String upc;

    public static NutritionFragment newInstance(String upc) {
        NutritionFragment fragment = new NutritionFragment();
        Bundle args = new Bundle();
        args.putString(ARG_UPC, upc);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            upc = getArguments().getString(ARG_UPC);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_nutrition, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        TextView productName = view.findViewById(R.id.product_name);
        TextView calories = view.findViewById(R.id.calories);

        ProductApiClient.fetchProductByUPC(upc, new ProductApiClient.Callback() {
            @Override
            public void onSuccess(String json) {
                ProductData product = ProductParser.parse(json);
                if (product != null && getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        productName.setText(product.product_name);
                        calories.setText("Calories per 100g: " + product.nutriments.energy_kcal_100g);
                    });
                }
            }

            @Override
            public void onFailure(Exception e) {
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() ->
                            Toast.makeText(getActivity(), "Failed to fetch product", Toast.LENGTH_SHORT).show());
                }
            }
        });
    }
}
