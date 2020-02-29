import java.util.*;
import java.io.*;
import java.lang.*;
import java.text.*;

public class fairShareHTML
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
    public static String outputMessage = "";
    public int flagToPrintOutputMessage = 0;


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
    public void Register(String toRegister, FileWriter fw_html) throws Exception
    {  
        String registerBlock = "";

        String reg = toRegister.replaceAll(" +", " ");
        String[] r = reg.split(" ");
        
        //excluding "register" while storing, hence starting with index "1"
        Roommates = Arrays.copyOfRange(r,1, r.length);

        registerBlock += "<p>Registered Roommates are : \n</p>\n<ol>";

        for(String roommate : Roommates)
        {
            registerBlock += "<li>" + roommate +"</li>\n";
        }

        registerBlock += "</ol>\n"; 

        registerBlock += "Number of roommates : " + Roommates.length + "\n</div>\n";

        registerBlock += "<div class=\"expensesBlock\">\n";
        registerBlock += "<h2>Expenses</h2>\n";
        registerBlock += "<ol>\n";

        fw_html.write(registerBlock);
    }

    //used to add new event and update the fair share of each roommate
    public void addExpense(String toAddExpense, FileWriter fw_html) throws Exception
    {   
        String expensesBlock = "";

        String reg = toAddExpense.replaceAll(" +", " ");
        String[] e = reg.split(" ");
        
        /*for(String i : e)
        {
            System.out.println(i);
        }*/

        if(getIndexOfRoommate(e[1])!=-1)
        {
            expensesBlock += "<li>Adding expense to "+e[1]+" : "+e[2]+"</li>\n";
        }

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
            expensesBlock += "<li>Error</li>\n";
        }

        fw_html.write(expensesBlock);
        //displayExpenses();
    }


    //used to generate report (final fair share) of a particular roommate
    public void generateReport(String toGenerateReport, FileWriter fw_html) throws Exception
    { 
        String reg = toGenerateReport.replaceAll(" +", " ");
        String roommate = reg.split(" ")[1];

        if(flagToPrintOutputMessage!=1)
        {
            outputMessage += "</ol>\n</div>\n";
            outputMessage += "<div class=\"reportBlock\">\n";
            outputMessage += "<h2>Report</h2>\n";
            outputMessage += "<ol>\n";
            fw_html.write(outputMessage);
            flagToPrintOutputMessage = 1;
        }

        String r = "<li>"+roommate+" : ";
        fw_html.write(r);

        if(getIndexOfRoommate(roommate)!=-1)
        {
            //System.out.printf("%.2f%n", fairShare.get(roommate));
            //"%,.2d%n" if we want commas like 30,066,253,287.00 instead of 30066253287.00
        
            //String value = String.format("%.2f",String.valueOf(fairShare.get(roommate)));
            String value = String.valueOf(Round(fairShare.get(roommate)));
            fw_html.write(value);
            fw_html.write("</li>");
        }
        else
        {
            fw_html.write("Error</li>");
        }

    }

    public void decideMode(String input, FileWriter fw_html) throws Exception
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
                fw_html.write("<div class=\"registerBlock\">\n");
                fw_html.write("<h2>Register</h2>\n");

                Register(command, fw_html);
                initializeFairShareList();
            }
            else if(mode.equalsIgnoreCase("expense"))
            {
                noOfEvents++;
                addExpense(command, fw_html);
            }
            else if(mode.equalsIgnoreCase("report"))
            {
                generateReport(command, fw_html);
            }
        }
        
    }

    public Double Round(double a)
    {
        DecimalFormat df = new DecimalFormat("#.##");      
        a = Double.valueOf(df.format(a)); 
        return a;  
    }

    public void execute(String input, FileWriter fw_html) throws Exception
    {
        decideMode(input, fw_html);
        //displayExpenses();
        fw_html.write("</ol>\n</div>");
    }

    public void createHTMLHeader(FileWriter fw_html) throws Exception
    {
        String htmlHeader = "";
        htmlHeader += "<!DOCTYPE html>\n";
        htmlHeader += "<html>";
        htmlHeader += "<!-- HTML Header Start -->\n";
        htmlHeader += "<head>\n";
        htmlHeader += "<title>The Fairshare System.</title>\n";
        htmlHeader += "<link rel='stylesheet' href=\"CSSoutput.css\" type=\"text/css\"/>\n";
        htmlHeader += "</head>\n<body>\n";
        htmlHeader += "<div class=\"welcomeBlock\">\n";
        htmlHeader += "<h1>FairShare System.</h1>\n";
        htmlHeader += "<p>Welcome. This is the command line version of our FairShare application with HTML+CSS output.</p>\n";
        htmlHeader += "</div>\n";

        fw_html.write(htmlHeader);
    }

    public void createCSSFile(FileWriter fw_css) throws Exception
    {
        String cssString = "";

        cssString += ".welcomeBlock{\n";
        cssString += "\tbackground-color: darkolivegreen;\n";
        cssString += "}\n";

        cssString += ".registerBlock{\n";
        cssString += "\tbackground-color: burlywood;\n";
        cssString += "}\n";

        cssString += ".expensesBlock{\n";
        cssString += "\tbackground-color: darkkhaki;\n";
        cssString += "}\n";

        cssString += ".reportBlock{\n";
        cssString += "\tbackground-color: darksalmon;\n";
        cssString += "}\n";
    
        fw_css.write(cssString);
    }

    public static void main(String args[]) throws Exception
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
        
        final fairShareHTML obj = new fairShareHTML();

        //File f = new File("HTMLoutput.html");
        FileWriter fw_html = new FileWriter("HTMLoutput.html");
        
        obj.createHTMLHeader(fw_html);
        obj.execute(input, fw_html);

        System.out.println("Calculating...");
        Thread.sleep(2000);
        System.out.println("HTML file generated in current working directory : HTMLoutput.html");

        FileWriter fw_css = new FileWriter("CSSoutput.css");
        obj.createCSSFile(fw_css);
        System.out.println("CSS file generated in current working directory : CSSoutput.css");

        fw_html.close();
        fw_css.close();
        
    }
}

/*
OUTPUT :

soha@soha-Inspiron-5559:~/SwayamCourse$ cd /home/soha/SwayamCourse ; /usr/lib/jvm/java-8-openjdk-amd64/bin/java -Dfile.encoding=UTF-8 -cp /home/soha/.config/Code/User/workspaceStorage/7c876ab31e2762bede43822d98ec53cc/redhat.java/jdt_ws/SwayamCourse_3588825c/bin fairShareHTML 
register f1 f2 f3 f4 f5
expense f3 365
expense f2 148
expense f4 172
expense f3 453
expense f1 366
report f3
report f1
report f5
end
Calculating...
HTML file generated in current working directory : HTMLoutput.html
CSS file generated in current working directory : CSSoutput.css
soha@soha-Inspiron-5559:~/SwayamCourse$
*/
