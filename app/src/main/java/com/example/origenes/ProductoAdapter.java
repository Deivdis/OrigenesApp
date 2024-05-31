package com.example.origenes;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ProductoAdapter extends RecyclerView.Adapter<ProductoAdapter.ProductoViewHolder> {

    private List<Producto> productos;
    private Context context;

    public ProductoAdapter(List<Producto> productos, Context context) {
        this.productos = productos;
        this.context = context;
    }

    @Override
    public ProductoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_producto, parent, false);
        return new ProductoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductoViewHolder holder, int position) {
        Producto producto = productos.get(position);
        holder.bind(producto);
    }

    @Override
    public int getItemCount() {
        return productos.size();
    }

    public void setProductos(List<Producto> productos) {
        this.productos = productos;
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
                        Producto producto = productos.get(position);

                        Log.d("ProductoAdapter", "Producto seleccionado: " + producto.getNombre());
                        Intent intent = new Intent(context, vistaProducto.class);
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
            txtDescripcionProducto.setText(producto.getDescripcion());
            txtPrecioProducto.setText(producto.getPrecio());
            imgProducto.setImageResource(producto.getImageResourceId());
        }
    }
}
