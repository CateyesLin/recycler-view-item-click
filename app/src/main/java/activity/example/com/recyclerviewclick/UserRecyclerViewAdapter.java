package activity.example.com.recyclerviewclick;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class UserRecyclerViewAdapter extends ClickableRecyclerViewAdapter<User, UserRecyclerViewAdapter.ViewHolder> {

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final View row = inflater.inflate(ViewHolder.LAYOUT_ID, parent, false);
        return new ViewHolder(row);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final User user = getItem(position);
        holder.rowNumber.setText("" + position);
        holder.name.setText(user.name);
        holder.age.setText("" + user.age);
        holder.job.setText(user.jobTitle);
        holder.gender.setText(user.gender.strResId);

        enableClickOnView(position, holder.itemView, user);

        enableClickOnView(position, holder.editButton, user);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public final static int LAYOUT_ID = R.layout.row_user;

        public final TextView rowNumber;

        public final TextView name;

        public final TextView age;

        public final TextView gender;

        public final TextView job;

        public final View editButton;

        public ViewHolder(View itemView) {
            super(itemView);

            rowNumber = (TextView) itemView.findViewById(R.id.row_number);
            name = (TextView) itemView.findViewById(R.id.name);
            age = (TextView) itemView.findViewById(R.id.age);
            gender = (TextView) itemView.findViewById(R.id.gender);
            job = (TextView) itemView.findViewById(R.id.job);
            editButton = itemView.findViewById(R.id.edit);
        }
    }
}
