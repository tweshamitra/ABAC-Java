import java.util.*;
public class Resource{
    private String project;
    private String name;
    private static HashMap<String, String> attributes = new HashMap<>();
    public Resource(String project, String name){
        this.project = project;
        this.name = name;
        populateHashmap();
        //printHashmap();
    }
    private void populateHashmap(){
        attributes.put("project", this.project);
        attributes.put("resource", this.name);
    }

    public HashMap getResourceAttributes(){
        return this.attributes;
    }
    public void printHashmap(){
        System.out.println(attributes);
    }
    public String getName(){
        return this.name;
    }
}