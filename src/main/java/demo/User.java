package demo;

public class User {
    public Integer id;
    public String username;
    public String email;
    public Integer age;

    public static User build(Integer id, String username, String email, Integer age) {
        User user = new User();
        user.id = id;
        user.username = username;
        user.email = email;
        user.age = age;
        return user;
    }
}
