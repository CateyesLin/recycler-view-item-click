由於在 RecyclerView 內，不像之前的 ListView 有 [setOnItemClickListener](http://developer.android.com/intl/zh-tw/reference/android/widget/AdapterView.html#setOnItemClickListener(android.widget.AdapterView.OnItemClickListener)) 可以用，而需要 Item Click 功能的需求很高。

這邊我就放一下我目前在用的寫法，但是請注意這僅是適用在我大部分的情境，你情況不同的話可能會遇到一些限制。
例如：你想要在 RecyclerView 中間插個廣告。這時候就請自己變形了。

先輕鬆一點，看個 `OnItemClickListener`，會傳 View 過來就是為了利用 id 可以知道是點到哪一個元件。如果一個 Row 上有不同的動作就可以善加利用。
``` OnItemClickListener
public abstract class OnItemClickListener<T> {

    public abstract void onClick(int index, View source, T data);

    public final static OnItemClickListener DUMMY = new OnItemClickListener<Object>() {
        @Override
        public void onClick(int index, View source, Object data) {
        }
    };
}
```

最關鍵的是 `ClickableRecyclerViewAdapter` 定義共用的功能，並且利用泛型規定好資料型別，方便在 OnItemViewClick 取出來使用。
``` java ClickableRecyclerViewAdapter.java
public abstract class ClickableRecyclerViewAdapter<T, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

    OnItemClickListener itemClickListener = OnItemClickListener.DUMMY;

    //定義在 ids.xml 內，避免被人手殘弄到重複。
    public final int DATA_INDEX = R.id.tag_row_data;
    public final int POSITION_INDEX = R.id.tag_row_position;

    final View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //利用泛型進行 cast，方便在接的時候直接取用不用再轉一次。
            itemClickListener.onClick((Integer) v.getTag(POSITION_INDEX), v, (T) v.getTag(DATA_INDEX));
        }
    };
    //預先準備好空的 List，讓 getItemCount 不會 NullPointer
    protected List<T> data = Collections.emptyList();

    //用範型限定傳入的型別，加上 @NonNull 警告不要傳 null 進來。
    public ClickableRecyclerViewAdapter setData(@NonNull List<T> data) {
        this.data = data;
        notifyDataSetChanged();
        return this;
    }

    //定義類似 ListView 的 Adapter 使用的 getItem，方便在 onBindViewHolder 使用。
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

    //在 Bind 時只要呼叫這個功能就可以在某個 View 加上 Click 的接收。
    protected void enableClickOnView(int position, View view, T data) {
        view.setTag(POSITION_INDEX, position);
        view.setTag(DATA_INDEX, data);
        view.setOnClickListener(clickListener);
    }
}
```

既然定義好了就只要單純的跟平常使用 RecyclerView 一樣，針對要啟用點擊的 View 去呼叫 enableClickOnView
``` java 
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

        //啟用點整列
        enableClickOnView(position, holder.itemView, user);

        //啟用點旁邊的編輯鈕
        enableClickOnView(position, holder.editButton, user);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        //此 ViewHolder 對應的 Layout
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
```

用起來就跟 ListView 很像，只是變成是在 Adapter 去做 `setOnItemClickListener`。利用 id 去分辨點到哪一個 View 就知道要做什麼處理。
``` java MainActivity.java
public class MainActivity extends AppCompatActivity {

    //DataSet
    private final List<User> users = new ArrayList<>();

    UserRecyclerViewAdapter adapter = new UserRecyclerViewAdapter();
    RecyclerView list;

    protected Toast toast;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        prepareData();
        setupView();
    }

    private void prepareData() {
        users.add(new User(1, "John", "CEO", 23, MALE));
        users.add(new User(2, "Jack", "PM", 45, MALE));
        users.add(new User(3, "Mary", "UI", 21, FEMALE));
        users.add(new User(6, "Bob", "RD", 27, MALE));
        users.add(new User(9, "Alice", "RD", 22, FEMALE));
        users.add(new User(20, "Oak", "RD", 28, MALE));
        users.add(new User(34, "Lora", "PM", 27, FEMALE));
        users.add(new User(35, "Alec", "RD", 22, MALE));
        users.add(new User(39, "Patricia", "RD", 26, FEMALE));
        users.add(new User(41, "Valkry", "CTO", 40, FEMALE));
        users.add(new User(43, "Mike", "RD", 36, MALE));
    }

    private void setupView() {
        list = (RecyclerView) findViewById(R.id.list);
        list.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        list.setAdapter(adapter);
        adapter.setData(users);
        adapter.setOnItemClickListener(new OnItemClickListener<User>() {
            @Override
            public void onClick(int index, View source, User data) {
                switch (source.getId()) {
                    case R.id.edit:
                        toast("Edit " + data.name);
                        break;
                    default:
                        toast("View " + data.name);
                }
            }
        });
    }

    protected void toast(String text) {
        if(null != toast) {
            toast.cancel();
        }
        toast = Toast.makeText(this, text, Toast.LENGTH_SHORT);
        toast.show();
    }
}
```

`User` 就…沒什麼好說的。
public class User {

    //直接綁定 String Resource ID，作多國語言比較方便。
    public enum Gender {
        MALE(R.string.male),
        FEMALE(R.string.female);

        public final int strResId;

        Gender(int strResId) {
            this.strResId = strResId;
        }
    }

    public final int id;

    public final String name;

    public final String jobTitle;

    public final int age;

    public final Gender gender;

    public User(int id, String name, String jobTitle, int age, Gender gender) {
        this.id = id;
        this.name = name;
        this.jobTitle = jobTitle;
        this.age = age;
        this.gender = gender;
    }
}
```
