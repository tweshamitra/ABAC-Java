import java.util.*;
public class User{
    private String user;
    private String department;
    private String position;
    private String project;
    private static HashMap<String, String> attributes = new HashMap<>();
    public User(String user, String department, String position, String project){
        this.user = user;
        this.department = department;
        this.position = position;
        this.project = project;
        populateHashmap();
       // printMap();
    }

    private void populateHashmap(){
        attributes.put("user", this.user);
        attributes.put("department",this.department);
        attributes.put("position", this.position);
        attributes.put("project", this.project);
    }
    public HashMap getUserAttributes(){
        return this.attributes;
    }
    public static void printMap(){
        System.out.println(attributes);
    }
    public String getName(){
        return this.user;
    }
}