/*
 * Andres Trujillo
 * 04/20/2025
 * SNHU
 * Final Project
 */
package com.student.snhu.inventorytracker.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.student.snhu.inventorytracker.R;
import com.student.snhu.inventorytracker.model.InventoryItem;

import java.util.List;

public class InventoryAdapter extends RecyclerView.Adapter<InventoryAdapter.InventoryViewHolder> {

    private List<InventoryItem> inventoryItems;
    private Context context;
    private OnItemClickListener listener;
    private OnDeleteClickListener deleteListener;

    public InventoryAdapter(Context context, List<InventoryItem> inventoryItems, OnItemClickListener listener, OnDeleteClickListener itemDeleted) {
        this.context = context;
        this.inventoryItems = inventoryItems;
        this.listener = listener;
        this.deleteListener = itemDeleted;
    }


    public interface OnItemClickListener{
        void onItemClick(int position);
    }
    public interface OnDeleteClickListener {
        void onDeleteClick(int position);
    }


    public List<InventoryItem> getItemList() {
        return inventoryItems;
    }


    public void addItem(InventoryItem item){
        inventoryItems.add(item);
        notifyItemInserted(inventoryItems.size() - 1);
    }

    // Create and return a ViewHolder object for each item in the RecyclerView
    @NonNull
    @Override
    public InventoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_view, parent, false);
        return new InventoryViewHolder(view);
    }



    // Bind the data (InventoryItem) to the ViewHolder (UI elements)
    @Override
    public void onBindViewHolder(InventoryViewHolder holder, int position) {
        InventoryItem item = inventoryItems.get(position);
        holder.itemName.setText(item.getName());
        holder.itemDescription.setText(item.getItemDescription());
        holder.itemQuantity.setText(String.valueOf(item.getQuantity()));
        holder.btnDeleteItem.setOnClickListener(v -> {
            int pos = holder.getAdapterPosition();
            if (pos != RecyclerView.NO_POSITION) {
                showDeleteDialog(pos); // Only show the dialog here
            }
        });
    }

    private void showDeleteDialog(int pos) {
        new AlertDialog.Builder(context)
                .setTitle("Delete Item")
                .setMessage("Are you sure you want to delete this item?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    deleteListener.onDeleteClick(pos); // Now we delete only if the user confirms
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    public void deleteItem(int position) {
        inventoryItems.remove(position);
        notifyItemRemoved(position);
        Log.d("InventoryAdapter", "Item deleted.");
    }

    // Return the size of the dataset
    @Override
    public int getItemCount() {
        return inventoryItems.size();
    }

    // ViewHolder class holds references to the views for each item
    public class InventoryViewHolder extends RecyclerView.ViewHolder {
        TextView itemName;
        TextView itemDescription;
        TextView itemQuantity;
        ImageButton btnDeleteItem;
        LinearLayout ll;


        public InventoryViewHolder(View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.item_name);  // Reference to item_name TextView in list_item_dummy.xml
            itemDescription = itemView.findViewById(R.id.item_description);  // Reference to item_description TextView
            itemQuantity = itemView.findViewById(R.id.item_quantity);
            btnDeleteItem = itemView.findViewById(R.id.btnDeleteItem);

            ll = itemView.findViewById(R.id.list_item_layout);
            ll.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    if (listener != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            listener.onItemClick(position);
                        }
                    }
                }
            });

        }
    }
}
