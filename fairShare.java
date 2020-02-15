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

