package activity.example.com.recyclerviewclick;

import android.view.View;

public abstract class OnItemClickListener<T> {

    public abstract void onClick(int index, View source, T data);

    public final static OnItemClickListener DUMMY = new OnItemClickListener<Object>() {
        @Override
        public void onClick(int index, View source, Object data) {
        }
    };
}
