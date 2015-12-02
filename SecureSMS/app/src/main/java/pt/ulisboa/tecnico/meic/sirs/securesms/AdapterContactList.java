package pt.ulisboa.tecnico.meic.sirs.securesms;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import pt.ulisboa.tecnico.meic.sirs.securesms.Activities.GenerateQRCodeActivity;
import pt.ulisboa.tecnico.meic.sirs.securesms.Models.ContactModel;

public class AdapterContactList extends RecyclerView.Adapter<AdapterContactList.ViewHolder> {
    private ArrayList<ContactModel> mDataset;
    private Context appContext;


    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txtName;
        public TextView txtNumber;
        public TextView txtKey;

        public ViewHolder(View v) {
            super(v);
            txtName = (TextView) v.findViewById(R.id.firstLine);
            txtNumber = (TextView) v.findViewById(R.id.secondLine);
        }
    }

    public void add(int position, ContactModel item) {
        mDataset.add(position, item);
        notifyItemInserted(position);
    }

    public void remove(ContactModel item) {
        int position = mDataset.indexOf(item);
        mDataset.remove(position);
        notifyItemRemoved(position);
    }

    public AdapterContactList(ArrayList<ContactModel> myDataset, Context appContext) {
        mDataset = myDataset;
        this.appContext = appContext;
    }

    @Override
    public AdapterContactList.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                        int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.line_list, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final ContactModel contact = mDataset.get(position);
        holder.txtName.setText(contact.getName());
        holder.txtNumber.setText(mDataset.get(position).getPhoneNumber());


        holder.itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(appContext, GenerateQRCodeActivity.class);
                intent.putExtra("NAME", contact.getName());
                intent.putExtra("PHONE_NUMBER", contact.getPhoneNumber());
                intent.putExtra("PUB_KEY", contact.getPublicKeyBytes());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                appContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public void setDataset(ArrayList<ContactModel> contact){
        this.mDataset=contact;
    }
}
