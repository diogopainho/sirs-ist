package pt.ulisboa.tecnico.meic.sirs.securesms;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class AdapterSmsList extends RecyclerView.Adapter<AdapterSmsList.ViewHolder> {
    private ArrayList<Message_Model> mDataset;
    private Context appContext;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView txtHeader;
        public TextView txtFooter;

        public ViewHolder(View v) {
            super(v);
            txtHeader = (TextView) v.findViewById(R.id.firstLine);
            txtFooter = (TextView) v.findViewById(R.id.secondLine);
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
    public AdapterSmsList(ArrayList<Message_Model> myDataset, Context appContext) {
        mDataset = myDataset;
        this.appContext = appContext;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public AdapterSmsList.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.line_list, parent, false);
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

        holder.txtHeader.setText(message.getPhoneNumber());
        holder.txtFooter.setText(message.getMessage().substring(0, Math.min(10, message.getMessage().length())));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(appContext, ConversationActivity.class);
                intent.putExtra("PHONE_NUMBER", message.getPhoneNumber());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                appContext.startActivity(intent);
            }
        });

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
