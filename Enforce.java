import java.util.*;

public class Enforce{
    public static void main(String[] args){
        Parser parser = new Parser(args[0]);
        ArrayList<User> users = parser.getUsers();
        ArrayList<Resource> resources = parser.getResources();
        Scanner sc = new Scanner(System.in);
        int choice = 0;
        int flag = 1;
        while(flag == 1){
            System.out.println("\nChoose an action below (-1 to quit):\n1.Make an access query\n2.Indirection\n3.Delegation\n4.Attribute Intersection\n5.Role Hierarchy");
            choice = sc.nextInt();
            switch(choice){
                case 1:
                    accessQuery(users, resources);
                    break;
                // case 2:
                //     indirection();
                //     break;
                // case 3:
                //     delegation();
                //     break;
                // case 4:
                //     intersection();
                //     break;
                // case 5:
                //     hierarchy();
                //     break;
                case -1:
                    flag = 0;
            }
        }
    }    

    public static void accessQuery(ArrayList<User> users, ArrayList<Resource> resources){
        Scanner sc = new Scanner(System.in);
        System.out.println("\nTo check access, you must type in your query in the following format:\nCan user <user> access resource <resource> with <read/write> privilege?");
        String query = sc.nextLine();
        String[] line = query.split(" ");
        String user = line[2];
        String resource = line[5];
        String accessType = line[7];
        HashMap<String, String> _user = null;
        HashMap<String,String> _resource = null;
        for(User u : users){
            if(user.equals(u.getName())){
                _user = u.getUserAttributes();
                break;
            }
        }
        for(Resource r: resources){
            if(resource.equals(r.getName())){
                _resource = r.getResourceAttributes();
                break;
            }
        }
        if(_user.get("project").equals(_resource.get("project"))){
            System.out.println("User " + user + " can access resource " + resource);
        }
        else{
            System.out.println("User " + user + " does not have permission to access resource " + resource);
        }
    }
}