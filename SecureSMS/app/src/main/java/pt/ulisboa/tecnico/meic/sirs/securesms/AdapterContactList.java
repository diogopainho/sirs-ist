package pt.ulisboa.tecnico.meic.sirs.securesms;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class AdapterContactList extends RecyclerView.Adapter<AdapterContactList.ViewHolder> {
    private ArrayList<Contact_Model> mDataset;
    private Context appContext;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView txtName;
        public TextView txtNumber;
        public TextView txtKey;

        public ViewHolder(View v) {
            super(v);
            txtName = (TextView) v.findViewById(R.id.firstLine);
            txtNumber = (TextView) v.findViewById(R.id.secondLine);
        }
    }

    public void add(int position, Contact_Model item) {
        mDataset.add(position, item);
        notifyItemInserted(position);
    }

    public void remove(Contact_Model item) {
        int position = mDataset.indexOf(item);
        mDataset.remove(position);
        notifyItemRemoved(position);
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public AdapterContactList(ArrayList<Contact_Model> myDataset, Context appContext) {
        mDataset = myDataset;
        this.appContext = appContext;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public AdapterContactList.ViewHolder onCreateViewHolder(ViewGroup parent,
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

        final Contact_Model contact = mDataset.get(position);
        holder.txtName.setText(contact.getName());
        holder.txtNumber.setText(mDataset.get(position).getPhoneNumber());


        holder.itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(appContext, GenerateQRCodeActivity.class);
                intent.putExtra("NAME", contact.getName());
                intent.putExtra("PHONE_NUMBER", contact.getPhoneNumber());
                intent.putExtra("PUB_KEY", contact.getPublicKey());
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

    public void setDataset(ArrayList<Contact_Model> contact){
        this.mDataset=contact;
    }
}
