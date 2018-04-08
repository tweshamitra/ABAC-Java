import java.util.*;
public class Rule{
    private String name;
    private String position;
    private String action;
    private String resource;
    private String project;
    private HashMap<String, String> ruleAttributes = new HashMap<String, String>();
    public Rule(String position, String project, String resource, String action){
        this.position = position;
        this.action = action;
        this.resource = resource;
        this.project = project;
        populateHashmaps();
    }
    private void populateHashmaps(){
        ruleAttributes.put("position", this.position);
        ruleAttributes.put("project", this.project);
        ruleAttributes.put("resource", this.resource);
        ruleAttributes.put("action", this.action);
    }
    public String getPosition(){
        return this.position;
    }
    public String getAction(){
        return this.action;
    }
    public String getResource(){
        return this.resource;
    }
    public String getProject(){
        return this.project;
    }
    public void updateRule(String action){
        this.action = action;
    }
}
