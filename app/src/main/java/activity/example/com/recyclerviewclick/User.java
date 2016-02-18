package activity.example.com.recyclerviewclick;

public class User {

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
