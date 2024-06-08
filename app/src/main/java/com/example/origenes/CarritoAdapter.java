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
    private OrigenesBD db; // Añadir referencia a la base de datos

    public CarritoAdapter(List<Producto> productosEnCarrito, OrigenesBD db) {
        this.productosEnCarrito = productosEnCarrito;
        this.db = db; // Inicializar la base de datos
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
        holder.cantidadTextView.setText(String.format("%d", producto.getCantidad()));
        holder.imagenImageView.setImageResource(producto.getImageResourceId());

        holder.increaseQuantityButton.setOnClickListener(v -> {
            int newQuantity = producto.getCantidad() + 1;
            producto.setCantidad(newQuantity);
            db.actualizarCantidadProductoEnCarrito(1, producto.getId(), newQuantity); // Asume userId=1
            notifyItemChanged(position);
        });

        holder.decreaseQuantityButton.setOnClickListener(v -> {
            int newQuantity = producto.getCantidad() - 1;
            if (newQuantity > 0) {
                producto.setCantidad(newQuantity);
                db.actualizarCantidadProductoEnCarrito(1, producto.getId(), newQuantity); // Asume userId=1
                notifyItemChanged(position);
            }
        });

        holder.eliminarProductoButton.setOnClickListener(v -> {
            db.eliminarProductoDelCarrito(1, producto.getId()); // Asume userId=1
            productosEnCarrito.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, productosEnCarrito.size());
        });
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
        ImageView increaseQuantityButton;
        ImageView decreaseQuantityButton;
        ImageView eliminarProductoButton;

        public CarritoViewHolder(@NonNull View itemView) {
            super(itemView);
            nombreTextView = itemView.findViewById(R.id.nombreProductoTextView);
            precioTextView = itemView.findViewById(R.id.precioProductoTextView);
            cantidadTextView = itemView.findViewById(R.id.cantidadProductoTextView);
            imagenImageView = itemView.findViewById(R.id.imagenProductoImageView);
            increaseQuantityButton = itemView.findViewById(R.id.increaseQuantityButton);
            decreaseQuantityButton = itemView.findViewById(R.id.decreaseQuantityButton);
            eliminarProductoButton = itemView.findViewById(R.id.eliminarProductoButton);
        }
    }
}
