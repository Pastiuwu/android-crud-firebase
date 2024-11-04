package com.example.productmanager;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ProductDetailActivity extends AppCompatActivity {

    private EditText editTextName, editTextDescription, editTextPrice, editTextStock;
    private Product product;
    private DocumentReference productRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        // CONFIG INPUT BOTONES
        editTextName = findViewById(R.id.editTextName);
        editTextDescription = findViewById(R.id.editTextDescription);
        editTextPrice = findViewById(R.id.editTextPrice);
        editTextStock = findViewById(R.id.editTextStock);
        MaterialButton buttonUpdate = findViewById(R.id.buttonUpdate);
        MaterialButton buttonDelete = findViewById(R.id.buttonDelete);
        FloatingActionButton fabLogout = findViewById(R.id.fabLogout); // Agrega el FAB de cerrar sesión

        // CARGA DATOS
        product = (Product) getIntent().getSerializableExtra("product");
        if (product != null) {
            productRef = FirebaseFirestore.getInstance().collection("products").document(product.getId());
            loadProductData();
        }


        buttonUpdate.setOnClickListener(v -> updateProduct());
        buttonDelete.setOnClickListener(v -> deleteProduct());


        fabLogout.setOnClickListener(v -> logout());
    }

    private void loadProductData() {
        editTextName.setText(product.getName());
        editTextDescription.setText(product.getDescription());
        editTextPrice.setText(String.valueOf(product.getPrice()));
        editTextStock.setText(String.valueOf(product.getStock()));
    }

    private void updateProduct() {
        String name = editTextName.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();
        String priceStr = editTextPrice.getText().toString().trim();
        String stockStr = editTextStock.getText().toString().trim();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(priceStr) || TextUtils.isEmpty(stockStr)) {
            Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        double price = Double.parseDouble(priceStr);
        int stock = Integer.parseInt(stockStr);

        productRef.update("name", name, "description", description, "price", price, "stock", stock)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Producto actualizado correctamente", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Error al actualizar el producto", Toast.LENGTH_SHORT).show());
    }

    private void deleteProduct() {
        productRef.delete()
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Producto eliminado correctamente", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Error al eliminar el producto", Toast.LENGTH_SHORT).show());
    }

    // CERRAR SESION
    private void logout() {

        Toast.makeText(this, "Cerrando sesión...", Toast.LENGTH_SHORT).show();


        Intent intent = new Intent(ProductDetailActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish(); //
    }
}
