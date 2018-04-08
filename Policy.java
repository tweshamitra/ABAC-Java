import java.io.*;
import java.util.*;
import org.json.simple.JSONObject;
import java.lang.*;
import org.json.simple.JSONArray;
import org.json.simple.parser.*;

public class Policy{
    private ArrayList<User> users = new ArrayList<User>();  
    private ArrayList<Resource> resources = new ArrayList<Resource>();
    private ArrayList<Rule> rules = new ArrayList<Rule>();
    private String filename;
    public Policy(String filename){
        this.filename = filename;
        users = new ArrayList<User>();
        resources = new ArrayList<Resource>();
        parse(filename);
    }

    public void parse(String filename){
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
           if((entity.get("user")!=null)){
               createUser(entity);
           }
           
           if((entity.get("delegateAuthAttr")!=null)){
               delegate(entity);
           }
           if((entity.get("access")!=null)){
               attributeIntersection(entity);
           }
       }
    }
   
    private void createUser(JSONObject obj){
        User u = new User((String)obj.get("user"), (String)obj.get("department"), (String)obj.get("position"), (String) obj.get("project"));
        users.add(u);
    }

    private void createResource(JSONObject obj){
        Resource r = new Resource((String)obj.get("project"),(String)obj.get("resource"), (String)obj.get("owner"));
        resources.add(r);
    }


    public ArrayList getUsers(){
        return this.users;
    }

    public ArrayList getResources(){
        return this.resources;
    }

    public ArrayList getRules(){
        return this.rules;
    }

    public void printList(){
        for(User u : this.users){
            System.out.println(u.getUserAttributes());
        }
    }

    private void delegate(JSONObject obj){
        String line = (String)obj.get("delegateAuthAttr");
        String[] arr = line.split("<-");
        String delegatorPos = (arr[1].split("/"))[1];
        String delegateePos = (arr[0].split("/"))[1];
        String project = (arr[0].split("/"))[0];
        Rule delegatorR;
        Rule delegateeR;
        String action = null;
        //find access to give to delegatee
        for(int i = 0; i<rules.size(); i++){
            if(rules.get(i).getPosition().equals(delegatorPos) && rules.get(i).getProject().equals(project)){
                action = rules.get(i).getAction();
                break;
            }
        }
        //find delegatee and update access
        for(int i = 0; i < rules.size(); i++){
            if(rules.get(i).getPosition().equals(delegateePos) && rules.get(i).getProject().equals(project)){
                rules.get(i).updateRule(action);
                break;
            }
        }
       
    }
    private void attributeIntersection(JSONObject obj){
        String[] line = ((String)obj.get("access")).split("<-");
        String resource = (line[0].split("/"))[0];
        String action = (line[0].split("/"))[1];
        String position = (((line[1].split("&&"))[0]).split("/"))[1];
        String project = (((line[1].split("&&"))[1]).split("/"))[1];
        Rule newRule = new Rule(position, project, resource, action);
        if(rules.size()==0){
            rules.add(newRule);
        }
        else{
            for(int i = 0; i < rules.size(); i++){
                if(!(rules.get(i).getAction().equals(action)) || !(rules.get(i).getPosition().equals(position)) || !(rules.get(i).getProject().equals(project) || !(rules.get(i).getResource().equals(resource))) ){
                    rules.add(newRule);
                    break;
                }
            }
        }
    }   
    public String getName(){
        return this.filename;
    }
}