package com.example.productmanager;

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
import com.google.firebase.firestore.FirebaseFirestore;

public class ProductCreateFragment extends Fragment {

    private EditText editTextName, editTextDescription, editTextPrice, editTextStock;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_create, container, false);


        editTextName = view.findViewById(R.id.editTextName);
        editTextDescription = view.findViewById(R.id.editTextDescription);
        editTextPrice = view.findViewById(R.id.editTextPrice);
        editTextStock = view.findViewById(R.id.editTextStock);
        MaterialButton buttonCreate = view.findViewById(R.id.buttonCreate);


        buttonCreate.setOnClickListener(v -> addProductToFirestore());

        return view;
    }

    // AGREGAR PRODUCTO DB FB
    private void addProductToFirestore() {
        String name = editTextName.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();
        String priceStr = editTextPrice.getText().toString().trim();
        String stockStr = editTextStock.getText().toString().trim();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(priceStr) || TextUtils.isEmpty(stockStr)) {
            Toast.makeText(getContext(), "Por favor completa todos los campos requeridos", Toast.LENGTH_SHORT).show();
            return;
        }

        double price = Double.parseDouble(priceStr);
        int stock = Integer.parseInt(stockStr);

        // CREAR NUEVO PRODUCTO
        Product product = new Product(name, description, price, stock);

        // AGREGA PRODUCTO EN COLECCION "PRODUCTS"
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("products").add(product)
                .addOnSuccessListener(documentReference -> {
                    product.setId(documentReference.getId()); // ID
                    Toast.makeText(getContext(), "Producto agregado con éxito: " + product.getName(), Toast.LENGTH_SHORT).show();
                    clearFields();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Error al agregar producto: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }


    // LIMPIAR CAMPOS DSP QUE AGREGA EL PRODUC
    private void clearFields() {
        editTextName.setText("");
        editTextDescription.setText("");
        editTextPrice.setText("");
        editTextStock.setText("");
    }
}
