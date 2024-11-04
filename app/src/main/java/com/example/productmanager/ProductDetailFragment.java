package com.example.productmanager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProductDetailFragment extends Fragment {

    private EditText editTextName, editTextDescription, editTextPrice, editTextStock;
    private Product product;
    private DocumentReference productRef;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_detail, container, false);


        editTextName = view.findViewById(R.id.editTextName);
        editTextDescription = view.findViewById(R.id.editTextDescription);
        editTextPrice = view.findViewById(R.id.editTextPrice);
        editTextStock = view.findViewById(R.id.editTextStock);
        MaterialButton buttonUpdate = view.findViewById(R.id.buttonUpdate);
        MaterialButton buttonDelete = view.findViewById(R.id.buttonDelete);
        FloatingActionButton buttonLogout = view.findViewById(R.id.fab_logout);

        // Recibe los productos
        Bundle args = getArguments();
        if (args != null) {
            product = (Product) args.getSerializable("product");
            if (product != null) {
                loadProductData();
                productRef = FirebaseFirestore.getInstance().collection("products").document(product.getId());
            } else {
                Toast.makeText(getContext(), "No se pudo cargar el producto", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getContext(), "No se recibieron datos del producto", Toast.LENGTH_SHORT).show();
        }


        buttonUpdate.setOnClickListener(v -> updateProduct());
        buttonDelete.setOnClickListener(v -> deleteProduct());


        buttonLogout.setOnClickListener(v -> logout());

        return view;
    }

    private void loadProductData() {
        if (product != null) {
            editTextName.setText(product.getName());
            editTextDescription.setText(product.getDescription());
            editTextPrice.setText(String.valueOf(product.getPrice()));
            editTextStock.setText(String.valueOf(product.getStock()));
        }
    }

    private void updateProduct() {
        String name = editTextName.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();
        String priceStr = editTextPrice.getText().toString().trim();
        String stockStr = editTextStock.getText().toString().trim();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(priceStr) || TextUtils.isEmpty(stockStr)) {
            Toast.makeText(getContext(), "Completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        double price;
        int stock;

        try {
            price = Double.parseDouble(priceStr);
        } catch (NumberFormatException e) {
            Toast.makeText(getContext(), "Precio no válido", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            stock = Integer.parseInt(stockStr);
        } catch (NumberFormatException e) {
            Toast.makeText(getContext(), "Stock no válido", Toast.LENGTH_SHORT).show();
            return;
        }

        productRef.update("name", name, "description", description, "price", price, "stock", stock)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(getContext(), "Producto actualizado", Toast.LENGTH_SHORT).show();
                    requireActivity().getSupportFragmentManager().popBackStack();
                })
                .addOnFailureListener(e -> Toast.makeText(getContext(), "Error al actualizar el producto: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void deleteProduct() {
        productRef.delete()
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(getContext(), "Producto eliminado", Toast.LENGTH_SHORT).show();
                    requireActivity().getSupportFragmentManager().popBackStack();
                })
                .addOnFailureListener(e -> Toast.makeText(getContext(), "Error al eliminar el producto: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    // CERRAR SESION
    private void logout() {

        Toast.makeText(getContext(), "Cerrando sesión...", Toast.LENGTH_SHORT).show();


        Intent intent = new Intent(getActivity(), LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        requireActivity().finish();
    }
}
