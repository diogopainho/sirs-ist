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

    public class ViewHolder extends RecyclerView.ViewHolder {
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

    public AdapterSmsList(ArrayList<Message_Model> myDataset, Context appContext) {
        mDataset = myDataset;
        this.appContext = appContext;
    }

    @Override
    public AdapterSmsList.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.line_list, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

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

    @Override
    public int getItemCount() {
        return mDataset.size();
    }


    public void setDataset(ArrayList<Message_Model> messages){
        this.mDataset=messages;
    }
}
