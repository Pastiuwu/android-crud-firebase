package com.example.productmanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class ProductListFragment extends Fragment {

    private LinearLayout layoutContainer;
    private ListenerRegistration productListener; //

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_list, container, false);
        layoutContainer = view.findViewById(R.id.layoutContainer);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        loadProductsFromFirebase();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (productListener != null) {
            productListener.remove();
        }
    }

    private void loadProductsFromFirebase() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        productListener = db.collection("products")
                .addSnapshotListener((queryDocumentSnapshots, e) -> {
                    if (e != null) {
                        Toast.makeText(getContext(), "Error al cargar productos: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // LIMPIA LA LISTA ANTES DE CARGAR
                    layoutContainer.removeAllViews();

                    // AGREGA LOS PRODUCTOS
                    if (queryDocumentSnapshots != null) {
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            Product product = document.toObject(Product.class);
                            product.setId(document.getId());
                            View card = createCardView(product);
                            layoutContainer.addView(card);
                        }
                    }
                });
    }

    private View createCardView(Product product) {

        View cardView = LayoutInflater.from(getContext()).inflate(R.layout.item_product_card, layoutContainer, false);


        TextView name = cardView.findViewById(R.id.textViewName);
        TextView price = cardView.findViewById(R.id.textViewPrice);
        Button detailsButton = cardView.findViewById(R.id.buttonDetails);


        name.setText(product.getName());
        price.setText(String.format("$%.2f", product.getPrice()));


        detailsButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ProductDetailActivity.class);
            intent.putExtra("product", product);  // Enviar el producto al detalle
            startActivity(intent);
        });

        return cardView;
    }
}
