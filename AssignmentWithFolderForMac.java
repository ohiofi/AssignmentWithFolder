import java.util.Scanner;
import java.awt.*;
import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class AssignmentWithFolderForMac{
  private String code;
  private String subname;
  public static void main(String[] args) throws FileNotFoundException{
	System.out.println("Assignment Checker 2 with a foler input.  By: Corinne Dixon");
    String string;
    String line;
    File folder;
    ArrayList<AssignmentWithFolder> assignments = new ArrayList<AssignmentWithFolder>();
    boolean removeS = false;
    boolean removeC = false;
    char comment = 'N';
    Scanner scanner = new Scanner(System.in);
    JFileChooser chooser = new JFileChooser();
    System.out.print("Do you want spaces removed? y or n? ");
    if(scanner.nextLine().equals("y")){
      removeS = true;
    }
    System.out.print("Do you want comments removed? y or n? ");
    if(scanner.nextLine().equals("y")){
      System.out.print("What is the first character of a comment? ");
      comment = scanner.next().charAt(0);
      removeC = true;
      scanner.nextLine();
    }
    System.out.print("Use GUI folder selection? y or n? ");
    if(scanner.nextLine().equals("y")){
      chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
      chooser.setAcceptAllFileFilterUsed(false);
      while(true){
        if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            folder = chooser.getSelectedFile();
            break;
        } else {
          System.out.println("No Selection ");
        }
      }
    } else {
      //folder = new File("/usr/local/bin/geeks");
      folder = new File("/Users/teacher/Downloads/submissions (5)/");
    }
		File[] listOfFiles = folder.listFiles();
		for(File file : listOfFiles) {
			string = "";
			line = "";
	  		if (file.isFile()) {
				System.out.print(file.getName() + "   ");
				Scanner doc = new Scanner(file);
				while(doc.hasNextLine()){
					line = doc.nextLine();
				    int index = 0;
				    while(line.length()>index && line.charAt(index) == ' '){
				    	index++;
				    }
				    if(removeC && line.length() > index && line.charAt(index) == comment){
				    	continue;
				    }
				    string = string + line;
				}

				if(removeS){
					string = replaceSpaces(string);
        		}
        		assignments.add(new AssignmentWithFolder(string, file.getName()));
			}
		}
    while(true){
      System.out.print("\nHow many characters do you want to compare at a time? ");
      int words = scanner.nextInt();
      System.out.print("What is the minimum threshold you want to display? ");
      int min = scanner.nextInt();
      //now you just need to compare all of the files
      if(assignments.size() > 1){
        for(int i = 0; i < assignments.size(); i++){
          for(int j = 0; j < assignments.size(); j++){
            String compare = compareAssignments(assignments.get(i), assignments.get(j), words);
            int num = Integer.parseInt(compare.substring(0,compare.length()-1));
            if(i != j && num>=min){
              System.out.println(assignments.get(i).getName() + " compared to File " + assignments.get(j).getName() + "= " + compare);
            }
          }
        }
      }
      scanner.nextLine();
      System.out.println("Re-run compare? y or n? ");
      if(scanner.nextLine().equals("n")){
        break;
      }
    }
  }

  public AssignmentWithFolder(String fileText, String fileName){
    code = fileText;
    subname = fileName.substring(0,13);
  }
  public static String replaceSpaces(String code){
    String string = code.replaceAll(" ", "");
    return string;
  }

  public String getName(){
    return subname;
  }


  public static String compareAssignments(AssignmentWithFolder project1, AssignmentWithFolder project2, int wordsToCompare){
    int similar = 0;
    double total = 0;
    for(int count=0; (count+1)*wordsToCompare < ((project2.code).length()); count++){
      if((project1.code).contains((project2.code).substring(count*wordsToCompare,wordsToCompare*(1+count)))){
        similar ++;
      }
      total++;
    }
    return Math.round((similar/total)*100.0) + "%";
  }
}