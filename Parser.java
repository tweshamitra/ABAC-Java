import java.io.*;
import java.util.*;
import org.json.simple.JSONObject;
import java.lang.*;
import org.json.simple.JSONArray;
import org.json.simple.parser.*;

public class Parser{
    private static ArrayList<User> users = new ArrayList<User>();
    private static ArrayList<Resource> resources = new ArrayList<Resource>();

    public Parser(String filename){
        parse(filename);
    }

    public static void parse(String filename){
        Object obj = null;
        JSONParser parser = new JSONParser();
        JSONArray arr;
        try{
            obj = parser.parse(new FileReader(filename));
        }catch(Exception e){
            e.printStackTrace();
        }
       arr = (JSONArray) obj;
       for(Object o : arr){
           JSONObject entity = (JSONObject) o;
           String user = (String) entity.get("user");
           if((entity.get("resource")!=null)){
               createResource(entity);
           }
           else{
               createUser(entity);
           }
       }
    }
   
    private static void createUser(JSONObject obj){
        User u = new User((String)obj.get("user"), (String)obj.get("department"), (String)obj.get("position"), (String) obj.get("project"));
        users.add(u);
    }

    private static void createResource(JSONObject obj){
        Resource r = new Resource((String)obj.get("project"),(String)obj.get("resource"));
        resources.add(r);
    }

    public  ArrayList getUsers(){
        return this.users;
    }
    public  ArrayList getResources(){
        return this.resources;
    }
}