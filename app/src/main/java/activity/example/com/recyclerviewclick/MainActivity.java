package activity.example.com.recyclerviewclick;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static activity.example.com.recyclerviewclick.User.Gender.MALE;
import static activity.example.com.recyclerviewclick.User.Gender.FEMALE;

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
