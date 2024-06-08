package com.example.origenes;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CarritoAdapter extends RecyclerView.Adapter<CarritoAdapter.CarritoViewHolder> {

    private List<Producto> productosEnCarrito;
    private OrigenesBD db;

    public CarritoAdapter(List<Producto> productosEnCarrito, OrigenesBD db, CarritoActivity carritoActivity) {
        this.productosEnCarrito = productosEnCarrito;
        this.db = db;
    }

    @NonNull
    @Override
    public CarritoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflater.inflate returns a View, make sure you import the correct package.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_carrito, parent, false);
        return new CarritoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CarritoViewHolder holder, int position) {
        Producto producto = productosEnCarrito.get(position);
        holder.nombreTextView.setText(producto.getNombre());
        holder.precioTextView.setText(String.format("$%.2f", producto.getPrecio()));
        holder.cantidadTextView.setText(String.valueOf(producto.getCantidad()));
        holder.imagenImageView.setImageResource(producto.getImageResourceId());

        holder.decreaseQuantityButton.setOnClickListener(v -> {
            if (producto.getCantidad() > 1) {
                producto.setCantidad(producto.getCantidad() - 1);
                db.actualizarCantidadProductoEnCarrito(producto.getId(), producto.getCantidad());
                notifyItemChanged(position);
            }
        });

        holder.increaseQuantityButton.setOnClickListener(v -> {
            producto.setCantidad(producto.getCantidad() + 1);
            db.actualizarCantidadProductoEnCarrito(producto.getId(), producto.getCantidad());
            notifyItemChanged(position);
        });

        holder.eliminarProductoButton.setOnClickListener(v -> {
            db.eliminarProductoDelCarrito(producto.getId());
            productosEnCarrito.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, productosEnCarrito.size());
        });
    }

    @Override
    public int getItemCount() {
        return productosEnCarrito.size();
    }

    public interface OnItemChangeListener {
        void onItemChanged();
    }

    static class CarritoViewHolder extends RecyclerView.ViewHolder {
        TextView nombreTextView;
        TextView precioTextView;
        TextView cantidadTextView;
        ImageView imagenImageView;
        ImageButton decreaseQuantityButton;
        ImageButton increaseQuantityButton;
        ImageView eliminarProductoButton;

        public CarritoViewHolder(@NonNull View itemView) {
            super(itemView);
            nombreTextView = itemView.findViewById(R.id.nombreProductoTextView);
            precioTextView = itemView.findViewById(R.id.precioProductoTextView);
            cantidadTextView = itemView.findViewById(R.id.cantidadProductoTextView);
            imagenImageView = itemView.findViewById(R.id.imagenProductoImageView);
            decreaseQuantityButton = itemView.findViewById(R.id.decreaseQuantityButton);
            increaseQuantityButton = itemView.findViewById(R.id.increaseQuantityButton);
            eliminarProductoButton = itemView.findViewById(R.id.eliminarProductoButton);
        }
    }
}
