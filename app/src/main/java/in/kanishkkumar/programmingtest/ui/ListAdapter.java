package in.kanishkkumar.programmingtest.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;

import in.kanishkkumar.programmingtest.R;
import in.kanishkkumar.programmingtest.model.ResponseItem;

public class ListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<ResponseItem> itemList;
    private Context mContext;
    OnItemClickListener mListener;

    public ListAdapter(Context context, List<ResponseItem> dataList, OnItemClickListener listener) {
        this.mListener = listener;
        this.mContext = context;
        itemList = new ArrayList<>(dataList);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_layout, parent, false);
        return new ListItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final ListItemHolder itemHolder = (ListItemHolder) holder;
        itemHolder.textView.setText(itemList.get(position).getName());
        final ImageView imageView = itemHolder.imageView;
        Glide.with(mContext).load(itemList.get(position).getPicture())
                .thumbnail(0.5f)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
        itemHolder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener!=null){
                    mListener.onItemClicked(itemList.get(itemHolder.getAdapterPosition()));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    static class ListItemHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;
        View container;
        public ListItemHolder(View itemView) {
            super(itemView);
            container = itemView;
            imageView = itemView.findViewById(R.id.imageView);
            textView = itemView.findViewById(R.id.textView);
        }
    }

    public interface OnItemClickListener {
        void onItemClicked(ResponseItem item);
    }
}
