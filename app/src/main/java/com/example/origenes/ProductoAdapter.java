package com.example.origenes;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;

public class ProductoAdapter extends RecyclerView.Adapter<ProductoAdapter.ProductoViewHolder> {

    private List<Producto> productoList;
    private List<Producto> productoListFull;
    private Context context;

    public ProductoAdapter(List<Producto> productos, Context context) {
        this.productoList = productos;
        this.productoListFull = new ArrayList<>(productos);
        this.context = context;
    }

    @Override
    public ProductoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_producto, parent, false);
        return new ProductoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductoViewHolder holder, int position) {
        Producto producto = productoList.get(position);
        holder.bind(producto);
    }

    @Override
    public int getItemCount() {
        return productoList.size();
    }

    public void setProductos(List<Producto> productos) {
        this.productoList = productos;
        this.productoListFull = new ArrayList<>(productos);
        actualizarvista();
    }

    public void filter(String text) {
        productoList.clear();
        if (text.isEmpty()) {
            productoList.addAll(productoListFull);
        } else {
            String filterPattern = text.toLowerCase().trim();
            for (Producto producto : productoListFull) {
                if (producto.getNombre().toLowerCase().contains(filterPattern) ||
                        producto.getDescripcion().toLowerCase().contains(filterPattern)) {
                    productoList.add(producto);
                }
            }
        }
        actualizarvista();
    }

    public void actualizarvista(){
        notifyDataSetChanged();
    }

    class ProductoViewHolder extends RecyclerView.ViewHolder {
        TextView txtNombreProducto;
        TextView txtDescripcionProducto;
        TextView txtPrecioProducto;
        ImageView imgProducto;

        public ProductoViewHolder(View itemView) {
            super(itemView);
            txtNombreProducto = itemView.findViewById(R.id.txtNombreProducto);
            txtDescripcionProducto = itemView.findViewById(R.id.txtDescripcionProducto);
            txtPrecioProducto = itemView.findViewById(R.id.txtPrecioProducto);
            imgProducto = itemView.findViewById(R.id.imgProducto);

            // Añadir el OnClickListener al itemView
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Producto producto = productoList.get(position);

                        Log.d("ProductoAdapter", "Producto seleccionado: " + producto.getNombre());
                        Intent intent = new Intent(context, vistaProducto.class);
                        intent.putExtra("idProducto", producto.getId());
                        intent.putExtra("imagenProducto", producto.getImageResourceId());
                        intent.putExtra("nombreProducto", producto.getNombre());
                        intent.putExtra("descripcionProducto", producto.getDescripcion());
                        intent.putExtra("precioProducto", producto.getPrecio());
                        context.startActivity(intent);
                    }
                }
            });
        }

        public void bind(Producto producto) {
            txtNombreProducto.setText(producto.getNombre());
            txtPrecioProducto.setText(String.valueOf(producto.getPrecio()));
            imgProducto.setImageResource(producto.getImageResourceId());
        }
    }
}