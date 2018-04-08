import java.util.*;
@SuppressWarnings("unchecked")

public class Enforce{

    public static void main(String[] args){
        long startTime = System.nanoTime();
        Policy policy = new Policy(args[0]);
        long endTime = System.nanoTime();
        ArrayList<User> users = policy.getUsers();
        ArrayList<Resource> resources = policy.getResources();
        ArrayList<Rule> rules = policy.getRules();
        
        Scanner sc = new Scanner(System.in);
        int choice = 0;
        int flag = 1;
        while(flag == 1){
            System.out.println("\nChoose an option below (-1 to quit):\n1.Make access queries\n2.Time processing and queries\n");
            try{
                choice = sc.nextInt();
            }catch(InputMismatchException e){
                System.out.println("Please enter only one of the options provided above...system exiting");
                System.exit(0);
            }
            switch(choice){
                case 1:
                    accessQuery(users, resources, rules);
                    break;
                case 2:
                    time();
                    break;
                case -1:
                    flag = 0;
            }
        }
        System.out.println("\n");
    }    
    /**
        This method times how long processing the policy file takes, 
        then, given a set of hardcoded queries, it makes the queries
        on a given file. It prints out the time it took for both operations separately. 
        The time for a query does not take into consideration the processing
        of the policy file. 
     */
    public static void time(){
        System.out.println("Timing policy file preprocessing...");
        String[] files = {"policy1.json", "policy2.json", "policy3.json", "policy4.json","policy5.json"};
        ArrayList<String[]> queries = new ArrayList<String[]>();
        String[] arr1= {"user1 file1 write","user8 file4 read","user8 file1 write"};
        String[] arr2 = {"user1 file1 write","user8 file4 read","user8 file1 write"};
        String[] arr3 = {"user1 file1 write","user8 file4 read","user8 file1 write"};
        String[] arr4 = {"user1 file1 write","user8 file4 read","user8 file1 write"};
        String[] arr5 = {"user1 file1 write","user8 file4 read","user8 file1 write"};
        queries.add(arr1);
        queries.add(arr2);
        queries.add(arr3);
        queries.add(arr4);
        queries.add(arr5);
        ArrayList<Policy> policies = new ArrayList<Policy>();
        for(int i = 0; i < files.length; i++){
            long startTime = System.nanoTime();
            Policy policy = new Policy(files[i]);
            long endTime = System.nanoTime();
            policies.add(policy);
            System.out.println("Time spent pre-processing policy file " + files[i] + ": " + (endTime - startTime) + " ns");
        }

        System.out.println("\nTiming queries...\n");
        for(int i = 0; i < files.length-1; i++){
            Policy policy = new Policy(files[i]);
            ArrayList<User> _users_ = policy.getUsers();
            ArrayList<Rule> _rules_ = policy.getRules();
            ArrayList<Resource> _resources_ = policy.getResources();
            String[] q = queries.get(i);
            System.out.println(files[i]);
            long startTime,endTime;
            for(int x = 0; x < q.length; x++){
                startTime = System.nanoTime();
                accessQuery(q[x], _users_,_resources_, _rules_);
                endTime = System.nanoTime();
                System.out.println("Time: " + (endTime - startTime) + " ns");
            }
            System.out.println("\n");
        }
       
    }

    /**
        This method tells you if access is permitted or denied based on the specified string. 
        This method is used by the time function and has the query string hard coded into it
        because it is not interactive.
     */
    public static void accessQuery(String access,ArrayList<User> users, ArrayList<Resource> resources, ArrayList<Rule> rules ){
        String query = access;
        String[] line = query.split(" ");     
        String user = line[0];   
        HashMap<String, String> _user = null;
        for(User u : users){
            if(user.equals(u.getName())){
                _user = u.getUserAttributes();
                break;
            }
        }
        if(line.length == 3){
            String resource = line[1];
            String accessType = line[2];
            checkResourceAccess(_user, rules, resource, accessType);
        }
        else if(line.length==2){
            String project = line[1];
            checkProject(_user, rules, user, project);
        }
    }

    /**
        This method is interactive and asks the user to enter in a query string. 
        Then, based on the type of query, it calls methods that check resource access  
        or whether a user is working on a specified project or not.
     */
    public static void accessQuery(ArrayList<User> users, ArrayList<Resource> resources, ArrayList<Rule> rules){
        Scanner sc = new Scanner(System.in);
        System.out.println("\nYou can query this system to ask things like:\n1.Can user <u> access resource <r> with <read/write> privilege?\n2.Is user <u> working on project <p>?");
        System.out.println("\nTo check access, you must type in your query in one of the following format:\n<user> <resource> <read/write>\n<user> <project>\n");
        String query = sc.nextLine();
        String[] line = query.split(" ");     
        String user = line[0];   
        HashMap<String, String> _user = null;
        for(User u : users){
            if(user.equals(u.getName())){
                _user = u.getUserAttributes();
                break;
            }
        }
        if(line.length == 3){
            String resource = line[1];
            String accessType = line[2];
            checkResourceAccess(_user, rules, resource, accessType);
        }
        else if(line.length==2){
            String project = line[1];
            checkProject(_user, rules, user, project);
        }
    }

    /**
        This checks whether a user can access a resource and with what privilege level
        by checking the rules against the attributes of the user. 
     */
    public static void checkResourceAccess(HashMap<String,String> user, ArrayList<Rule> rules, String resource, String accessType){
        boolean found = false;
        Rule r = null;
        for(int i = 0; i < rules.size(); i++){
            if(rules.get(i).getResource().equals(resource) && rules.get(i).getPosition().equals(user.get("position")) && rules.get(i).getProject().equals(user.get("project"))){
            r = rules.get(i);
            found = true;
                break;
            }
        }
        if(found){
            System.out.println("User " + user.get("user") + " can access resource " + resource + " with " + r.getAction() + " access" );
        }
        else{
            System.out.println("User " + user.get("user") + " does not have permission to access resource " + resource);

        }
    }

    /**
        This checks whether a user is working on a specific project
        by checking the rules against the attributes of the user. 
     */
    public static void checkProject(HashMap<String,String> _user, ArrayList<Rule>rules,String user, String project){
        if(_user.get("project").equals(project)){
            System.out.println("User " + _user.get("user") + " is working on " + project);
        }
        else{
            System.out.println("User " + _user.get("user") + " is not working on " + project);
        }
    }

}