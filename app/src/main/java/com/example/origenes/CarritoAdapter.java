package com.example.origenes;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CarritoAdapter extends RecyclerView.Adapter<CarritoAdapter.CarritoViewHolder> {

    private List<Producto> productosEnCarrito;

    public CarritoAdapter(List<Producto> productosEnCarrito) {
        this.productosEnCarrito = productosEnCarrito;
    }

    @NonNull
    @Override
    public CarritoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_carrito, parent, false);
        return new CarritoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CarritoViewHolder holder, int position) {
        Producto producto = productosEnCarrito.get(position);
        holder.nombreTextView.setText(producto.getNombre());
        holder.precioTextView.setText(String.format("$%.2f", producto.getPrecio()));
        holder.cantidadTextView.setText(String.format("Cantidad: %d", producto.getCantidad()));
        holder.imagenImageView.setImageResource(producto.getImageResourceId());
    }

    @Override
    public int getItemCount() {
        return productosEnCarrito.size();
    }

    static class CarritoViewHolder extends RecyclerView.ViewHolder {
        TextView nombreTextView;
        TextView precioTextView;
        TextView cantidadTextView;
        ImageView imagenImageView;

        public CarritoViewHolder(@NonNull View itemView) {
            super(itemView);
            nombreTextView = itemView.findViewById(R.id.nombreProductoTextView);
            precioTextView = itemView.findViewById(R.id.precioProductoTextView);
            cantidadTextView = itemView.findViewById(R.id.cantidadProductoTextView);
            imagenImageView = itemView.findViewById(R.id.imagenProductoImageView);
        }
    }
}