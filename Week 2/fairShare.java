import java.util.*;
import java.io.*;
import java.lang.*;
import java.text.*;

public class fairShare
{
    /*String[] inputCommands --> for storing the 3 types of user commands
    1. registering of new roommates
    2. storing expenses of each event
    3. generating report for a particular individual
    */
    public String[] inputCommands;
    public String[] Roommates; //for registering roommates
    public int noOfEvents; //for counting the number of events (if required, for debugging)
    public Map<String, Double> fairShare; //for storing fair share of each roommate (updated after each event)
    
    //Initialization of map that will contain updated fair share of each roomate
    public void initializeFairShareList()
    {
        fairShare = new HashMap<String,Double>();
        
        for(int i=0; i<Roommates.length; i++)
        {
            fairShare.put(Roommates[i],0.00);
        }
    }

    /*if roommate name is known, to get index of the roommate.
    if roommate is not registered, returns back -1
    */
    public int getIndexOfRoommate(String name)
    {
        for(int i=0; i<Roommates.length; i++)
        {
            if(Roommates[i].equalsIgnoreCase(name))
            {
                return i;
            }
        }
        return -1;
    }

    /*for debugging
    used to display the updated fair share of each roommate
    gets updated after every event
    */
    public void displayExpenses()
    {
        System.out.println(fairShare);
    }

    //used to register new roommates
    public void Register(String toRegister)
    {  
        String reg = toRegister.replaceAll(" +", " ");
        String[] r = reg.split(" ");
        
        //excluding "register" while storing, hence starting with index "1"
        Roommates = Arrays.copyOfRange(r,1, r.length);

        /*for(String roommate : Roommates)
        {
            System.out.println(roommate);
        }*/
    }

    //used to add new event and update the fair share of each roommate
    public void addExpense(String toAddExpense)
    {   
        String reg = toAddExpense.replaceAll(" +", " ");
        String[] e = reg.split(" ");
        
        /*for(String i : e)
        {
            System.out.println(i);
        }*/

        int indexOfRoommate = getIndexOfRoommate(e[1]);
        
        if(indexOfRoommate!=-1) //if roommate is registered
        {
            double perHeadShare = Double.valueOf(e[2])/Roommates.length;
            //System.out.println(perHeadShare);
            
            for(int i=0; i<Roommates.length; i++)
            {
                if(i==indexOfRoommate)
                {
                    double old = fairShare.get(Roommates[i]);
                    fairShare.put(Roommates[i], old+(Double.valueOf(e[2])-perHeadShare));
                }
                else
                {
                    double old = fairShare.get(Roommates[i]);
                    fairShare.put(Roommates[i], old+(-perHeadShare));
                }
            }
        }
        else
        {
            //if roommate is not registered
            System.out.println("Error");
        }

        //displayExpenses();
    
    }

    //used to generate report (final fair share) of a particular roommate
    public void generateReport(String toGenerateReport)
    {
        String reg = toGenerateReport.replaceAll(" +", " ");
        String roommate = reg.split(" ")[1];

        //System.out.println(fairShare.get(roommate));
        //System.out.println("Rounded off : "+ Round(fairShare.get(roommate)));
        if(getIndexOfRoommate(roommate)!=-1)
        {
            //System.out.println(Round(fairShare.get(roommate)));
            System.out.printf("%.2f%n", fairShare.get(roommate));
            //"%,.2d%n" if we want commas like 30,066,253,287.00 instead of 30066253287.00

        }
        else
        {
            System.out.println("Error");
        }
    }

    public void decideMode(String input)
    {
        //splitting commands by comma and storing them in a String[]
        inputCommands = input.split(",");
        
        for(String command : inputCommands)
        {   
            //replacing multiple spaces with a single space
            String reg = command.replaceAll(" +", " ");

            //splitting every individual command by space, getting first element (which is the mode of operation)
            String mode = reg.split(" ")[0];

            if(mode.equalsIgnoreCase("register"))
            {
                Register(command);
                initializeFairShareList();
            }
            else if(mode.equalsIgnoreCase("expense"))
            {
                noOfEvents++;
                addExpense(command);
            }
            else if(mode.equalsIgnoreCase("report"))
            {
                generateReport(command);
            }
        }
        
    }

    /*public Double Round(double a)
    {
        DecimalFormat df = new DecimalFormat("#,##0.00");      
        a = Double.valueOf(df.format(a)); 
        return a;  
    }*/

    public void execute(String input)
    {
        decideMode(input);
        //displayExpenses();
    }

    public static void main(String args[])
    {
        final Scanner scanner = new Scanner(System.in);
        String line = "";
        String input = "";
        
        while(scanner.hasNextLine())
        {
            line = scanner.nextLine();
            input += line + ","; //comma added as a separator (for splitting the commands)

            if(line.equalsIgnoreCase("end"))
            {
                break;
            }
        }

        scanner.close();
        //System.out.println(input);

        final fairShare obj = new fairShare();
        //obj.decideMode(input);
        obj.execute(input);
    }
}

/*
OUTPUT :
soha@soha-Inspiron-5559:~/SwayamCourse$ cd /home/soha/SwayamCourse ; /usr/lib/jvm/java-8-openjdk-amd64/bin/java -Dfile.encoding=UTF-8 -cp /home/soha/.config/Code/User/workspaceStorage/7c876ab31e2762bede43822d98ec53cc/redhat.java/jdt_ws/jdt.ls-java-project/bin fairShare 
register f1 f2 f3 f4 f5
expense f3 100
expense f2 150
expense f5 600
expense f1 225
expense f3 117
report  f5
end
361.60
soha@soha-Inspiron-5559:~/SwayamCourse$ cd /home/soha/SwayamCourse ; /usr/lib/jvm/java-8-openjdk-amd64/bin/java -Dfile.encoding=UTF-8 -cp /home/soha/.config/Code/User/workspaceStorage/7c876ab31e2762bede43822d98ec53cc/redhat.java/jdt_ws/jdt.ls-java-project/bin fairShare 
register f1 f2 f4 f5
expense f4 127
expense f1 89
expense f4 529
expense f3 327
expense f2 208
report  f3
report  f5
end
Error
Error
-238.25
soha@soha-Inspiron-5559:~/SwayamCourse$ cd /home/soha/SwayamCourse ; /usr/lib/jvm/java-8-openjdk-amd64/bin/java -Dfile.encoding=UTF-8 -cp /home/soha/.config/Code/User/workspaceStorage/7c876ab31e2762bede43822d98ec53cc/redhat.java/jdt_ws/jdt.ls-java-project/bin fairShare 
register f1 f2 f3 f4 f5
expense f3 100
expense f2 150
expense f5 600
expense f1 225
expense f3 117
report  f5
end
361.60
soha@soha-Inspiron-5559:~/SwayamCourse$ cd /home/soha/SwayamCourse ; /usr/lib/jvm/java-8-openjdk-amd64/bin/java -Dfile.encoding=UTF-8 -cp /home/soha/.config/Code/User/workspaceStorage/7c876ab31e2762bede43822d98ec53cc/redhat.java/jdt_ws/jdt.ls-java-project/bin fairShare 
register f1 f2 f3 f4 f5
expense f4  700
expense f1   50
expense f4  500
expense f3  225
expense f2 1050
report  f3
report  f2
report  f4
end
-280.00
545.00
695.00
soha@soha-Inspiron-5559:~/SwayamCourse$ cd /home/soha/SwayamCourse ; /usr/lib/jvm/java-8-openjdk-amd64/bin/java -Dfile.encoding=UTF-8 -cp /home/soha/.config/Code/User/workspaceStorage/7c876ab31e2762bede43822d98ec53cc/redhat.java/jdt_ws/jdt.ls-java-project/bin fairShare 
register f1 f2
expense f1  700
expense f1   50
expense f2  500
expense f1  225
expense f2 1050
report  f1
report  f2
end
-287.50
287.50
soha@soha-Inspiron-5559:~/SwayamCourse$ cd /home/soha/SwayamCourse ; /usr/lib/jvm/java-8-openjdk-amd64/bin/java -Dfile.encoding=UTF-8 -cp /home/soha/.config/Code/User/workspaceStorage/7c876ab31e2762bede43822d98ec53cc/redhat.java/jdt_ws/jdt.ls-java-project/bin fairShare 
register f9 f8 f7 f6 f5
expense f8 -100
expense f9   50
expense f7  500
expense f6  225
expense f8  355
report  f8
report  f9
end
49.00
-156.00
soha@soha-Inspiron-5559:~/SwayamCourse$ cd /home/soha/SwayamCourse ; /usr/lib/jvm/java-8-openjdk-amd64/bin/java -Dfile.encoding=UTF-8 -cp /home/soha/.config/Code/User/workspaceStorage/7c876ab31e2762bede43822d98ec53cc/redhat.java/jdt_ws/jdt.ls-java-project/bin fairShare 
register f9 f8 f7 f6 f5
expense f8  100
expense f9   50
expense f7  500
expense f6  225
expense f8  355
report  f8
report  f9
report  f4
end
209.00
-196.00
Error
soha@soha-Inspiron-5559:~/SwayamCourse$ cd /home/soha/SwayamCourse ; /usr/lib/jvm/java-8-openjdk-amd64/bin/java -Dfile.encoding=UTF-8 -cp /home/soha/.config/Code/User/workspaceStorage/7c876ab31e2762bede43822d98ec53cc/redhat.java/jdt_ws/jdt.ls-java-project/bin fairShare 
register f9 f8 f7 f6 f5
expense  f8  50000010025
expense  f9   1078839070
expense  f7  44294967295
expense  f6   4294967300
report   f8
report   f9
end
30,066,253,287.00
-18,854,917,668.00
soha@soha-Inspiron-5559:~/SwayamCourse$ cd /home/soha/SwayamCourse ; /usr/lib/jvm/java-8-openjdk-amd64/bin/java -Dfile.encoding=UTF-8 -cp /home/soha/.config/Code/User/workspaceStorage/7c876ab31e2762bede43822d98ec53cc/redhat.java/jdt_ws/jdt.ls-java-project/bin fairShare 
register f9 f8 f7 f6 f5
expense  f8  50000010025
expense  f9   1078839070
expense  f7  44294967295
expense  f6   4294967300
report   f8
report   f9
end
30066253287.00
-18854917668.00
soha@soha-Inspiron-5559:~/SwayamCourse$ cd /home/soha/SwayamCourse ; /usr/lib/jvm/java-8-openjdk-amd64/bin/java -Dfile.encoding=UTF-8 -cp /home/soha/.config/Code/User/workspaceStorage/7c876ab31e2762bede43822d98ec53cc/redhat.java/jdt_ws/jdt.ls-java-project/bin fairShare 
register f1 f2 f3 f4 f5
expense  f4  127
expense  f1   89
expense  f4  529
expense  f3  327
expense  f2  208
report   f3
report   f1
report   f5
end
71.00
-167.00
-256.00
soha@soha-Inspiron-5559:~/SwayamCourse$ cd /home/soha/SwayamCourse ; /usr/lib/jvm/java-8-openjdk-amd64/bin/java -Dfile.encoding=UTF-8 -cp /home/soha/.config/Code/User/workspaceStorage/7c876ab31e2762bede43822d98ec53cc/redhat.java/jdt_ws/jdt.ls-java-project/bin fairShare 
register f1 f2 f3 f4 f5 f6
expense  f4  127
expense  f1   89
expense  f4  529
expense  f3  327
expense  f2  208
report   f3
report   f6
report   f5
end
113.67
-213.33
-213.33
soha@soha-Inspiron-5559:~/SwayamCourse$ cd /home/soha/SwayamCourse ; /usr/lib/jvm/java-8-openjdk-amd64/bin/java -Dfile.encoding=UTF-8 -cp /home/soha/.config/Code/User/workspaceStorage/7c876ab31e2762bede43822d98ec53cc/redhat.java/jdt_ws/jdt.ls-java-project/bin fairShare 
register f1 f2 f4 f5
expense  f4  127
expense  f1   89
expense  f4  529
expense  f3  327
expense  f2  208
report   f3
report   f5
end
Error
Error
-238.25
soha@soha-Inspiron-5559:~/SwayamCourse$ cd /home/soha/SwayamCourse ; /usr/lib/jvm/java-8-openjdk-amd64/bin/java -Dfile.encoding=UTF-8 -cp /home/soha/.config/Code/User/workspaceStorage/7c876ab31e2762bede43822d98ec53cc/redhat.java/jdt_ws/jdt.ls-java-project/bin fairShare 
register f1 f2 f3 f4 f5
expense  f3  927
expense  f2  989
expense  f4  529
expense  f3  627
expense  f1  248
report   f3
report   f1
report   f5
end
890.00
-416.00
-664.00
soha@soha-Inspiron-5559:~/SwayamCourse$ 
*/
