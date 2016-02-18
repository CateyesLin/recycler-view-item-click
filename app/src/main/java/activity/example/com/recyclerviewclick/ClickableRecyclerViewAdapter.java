package activity.example.com.recyclerviewclick;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.Collections;
import java.util.List;

public abstract class ClickableRecyclerViewAdapter<T, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

    OnItemClickListener itemClickListener = OnItemClickListener.DUMMY;

    public final int DATA_INDEX = R.id.tag_row_data;
    public final int POSITION_INDEX = R.id.tag_row_position;

    final View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            itemClickListener.onClick((Integer) v.getTag(POSITION_INDEX), v, (T) v.getTag(DATA_INDEX));
        }
    };

    protected List<T> data = Collections.emptyList();

    public ClickableRecyclerViewAdapter setData(@NonNull List<T> data) {
        this.data = data;
        notifyDataSetChanged();
        return this;
    }

    public T getItem(int position) {
        return data.get(position);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public ClickableRecyclerViewAdapter setOnItemClickListener(OnItemClickListener<T> listener) {
        this.itemClickListener = null == listener ? OnItemClickListener.DUMMY : listener;
        return this;
    }

    protected void enableClickOnView(int position, View view, T data) {
        view.setTag(POSITION_INDEX, position);
        view.setTag(DATA_INDEX, data);
        view.setOnClickListener(clickListener);
    }
}
