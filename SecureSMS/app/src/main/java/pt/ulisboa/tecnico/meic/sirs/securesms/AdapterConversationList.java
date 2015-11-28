package pt.ulisboa.tecnico.meic.sirs.securesms;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class AdapterConversationList extends RecyclerView.Adapter<AdapterConversationList.ViewHolder>{
    private ArrayList<Message_Model> mDataset;
    Context appcontext;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView txtHeader;

        public ViewHolder(View v) {
            super(v);
            txtHeader = (TextView) v.findViewById(R.id.message);
        }
    }

    public void add(int position, Message_Model item) {
        mDataset.add(position, item);
        notifyItemInserted(position);
    }

    public void remove(Message_Model item) {
        int position = mDataset.indexOf(item);
        mDataset.remove(position);
        notifyItemRemoved(position);
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public AdapterConversationList(ArrayList<Message_Model> myDataset, Context context) {
        mDataset = myDataset;
        appcontext = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public AdapterConversationList.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                        int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_line, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        final Message_Model message = mDataset.get(position);

        holder.txtHeader.setText(message.getMessage());

        //As mensagens enviadas ficam a verde
        if(message.getType()){
            holder.txtHeader.setBackgroundColor(appcontext.getResources().getColor(R.color.iron));
            holder.txtHeader.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
        }

        //Accao para apagar a mensagem
        /*holder.txtHeader.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                remove(message);
            }
        };*/

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }


    public void setDataset(ArrayList<Message_Model> messages){
        this.mDataset=messages;
    }

}
