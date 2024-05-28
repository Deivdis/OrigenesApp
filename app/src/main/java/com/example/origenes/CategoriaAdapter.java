package com.example.origenes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
    }

    @Override
    public int getItemCount() {
        return categoriasList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtNombreCategoria;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNombreCategoria = itemView.findViewById(R.id.txtNombreCategoria);
        }
    }
}
