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

    public class ViewHolder extends RecyclerView.ViewHolder {
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

    public AdapterConversationList(ArrayList<Message_Model> myDataset, Context context) {
        mDataset = myDataset;
        appcontext = context;
    }

    @Override
    public AdapterConversationList.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                        int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_line, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final Message_Model message = mDataset.get(position);

        holder.txtHeader.setText(message.getMessage());

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

    @Override
    public int getItemCount() {
        return mDataset.size();
    }


    public void setDataset(ArrayList<Message_Model> messages){
        this.mDataset=messages;
    }

}
