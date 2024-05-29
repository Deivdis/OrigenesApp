package com.example.origenes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class CategoriaAdapter extends RecyclerView.Adapter<CategoriaAdapter.ViewHolder> {

    private List<Categoria> categoriasList;
    private Context context;

    public CategoriaAdapter(List<Categoria> categoriasList, Context context) {
        this.categoriasList = categoriasList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_categoria, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Categoria categoria = categoriasList.get(position);
        holder.txtNombreCategoria.setText(categoria.getNombre());

        // Cargar imagen desde recursos drawables
        int imageResourceId = categoria.getImageResourceId();
        if (imageResourceId != 0) {
            holder.imgCategoria.setImageResource(imageResourceId);
        }
    }

    @Override
    public int getItemCount() {
        return categoriasList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtNombreCategoria;
        ImageView imgCategoria; // Añadido campo de imagen

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNombreCategoria = itemView.findViewById(R.id.txtNombreCategoria);
            imgCategoria = itemView.findViewById(R.id.imgCategoria); // Enlazar la imagen
        }
    }
}
