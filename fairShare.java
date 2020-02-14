import java.util.*;
import java.io.*;
import java.lang.*;
import java.text.*;

public class fairShare
{
    /*String[] inputCommands --> for storing the 3 user commands
    1. registering of new roommates
    2. storing expenses of each event
    3. generating report for a particular individual
    */
    public String[] inputCommands; 
    public String[] Roommates;
    public int noOfEvents;
    public Map<String, Double> fairShare;
    
    public void initializeFairShareList()
    {
        fairShare = new HashMap<String,Double>();
        
        for(int i=0; i<Roommates.length; i++)
        {
            fairShare.put(Roommates[i],0.0);
        }
    }

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

    public void displayExpenses()
    {
        /*for(int j = 0; j<fairShare.size(); j++)
        {
            System.out.println(fairShare.get(j));
        }*/
        System.out.println(fairShare);
    }

    public void Register(String toRegister)
    {  
        final String[] r = toRegister.split(" ");
        
        //excluding "register" while storing, hence starting with index "1"
        Roommates = Arrays.copyOfRange(r,1, r.length);

        /*for(String roommate : Roommates)
        {
            System.out.println(roommate);
        }*/
    }

    public void addExpense(String toAddExpense)
    {
        String[] e = toAddExpense.split(" ");

        /*for(final String i : e)
        {
            System.out.println(i);
        }*/

        int indexOfRoommate = getIndexOfRoommate(e[1]);
        
        if(indexOfRoommate!=-1) //if roommate is registered
        {
            final double perHeadShare = Double.valueOf(e[2])/Roommates.length;
            //System.out.println(perHeadShare);
            
            for(int i=0; i<Roommates.length; i++)
            {
                if(i==indexOfRoommate)
                {
                    //System.out.println("Hi");
                    double old = fairShare.get(Roommates[i]);
                    fairShare.put(Roommates[i], old+(Double.valueOf(e[2])-perHeadShare));
                }
                else
                {
                    //System.out.println("Hello");
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

    public void generateReport(String toGenerateReport)
    {
        String roommate = toGenerateReport.split(" ")[1];

        //System.out.println(fairShare.get(roommate));
        //System.out.println("Rounded off : "+ Round(fairShare.get(roommate)));
        System.out.println(Round(fairShare.get(roommate)));
    }

    public void decideMode(String input)
    {
        //initializeFairShareList();

        //splitting commands by tab and storing them in a String[]
        inputCommands = input.split("   ");
        
        for(final String command : inputCommands)
        {   
            //splitting every individual command by space, getting first element (which is the mode of operation)
            final String mode = command.split(" ")[0];
            
            if(mode.equalsIgnoreCase("register"))
            {
                //System.out.println("register mode");
                Register(command);
                initializeFairShareList();
            }
            else if(mode.equalsIgnoreCase("expense"))
            {
                noOfEvents++;
                addExpense(command);
                //System.out.println(noOfEvents);
                //System.out.println("expense mode");
            }
            else if(mode.equalsIgnoreCase("report"))
            {
                generateReport(command);
                //System.out.println("report mode");
            }
        }
        
    }

    public double Round(double a)
    {
        //double time = 200.3456;
        DecimalFormat df = new DecimalFormat("#.##");      
        a = Double.valueOf(df.format(a)); 
        return a;  
    }

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
            input += line + "   "; //tab added as a separator (for splitting the commands)

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
