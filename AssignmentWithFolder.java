import java.util.Scanner;
import java.awt.*;
import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class AssignmentWithFolder{
  private String code;
  private String filename;
  public static void main(String[] args) throws FileNotFoundException{
  	System.out.println("Assignment Checker 2 with a folder input.  By: Corinne Dixon");
      String string;
      String line;
      File folder;
      ArrayList<AssignmentWithFolder> assignments = new ArrayList<AssignmentWithFolder>();
      ArrayList<Pair> pairs = new ArrayList<Pair>();
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
      chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
      chooser.setAcceptAllFileFilterUsed(false);
      while(true){
        if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
  	    folder = chooser.getSelectedFile();
  	    break;
  	  } else {
  	  	System.out.println("No Selection ");
  	  	System.out.print("Quit? y or n? :");
      	if(scanner.nextLine().equals("y")){
			return;
		}
  	  }
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
        assignments.add(new AssignmentWithFolder(string,file));
  		}
  	}
    System.out.print("\nHow many characters do you want to compare at a time? ");
    int words = scanner.nextInt();
    //System.out.print("What is the minimum threshold you want to display? ");
    //int min = scanner.nextInt();
    //now you just need to compare all of the files
    if(assignments.size() > 1){
      for(int i = 0; i < assignments.size(); i++){
        for(int j = 0; j < assignments.size(); j++){
          double compare = compareAssignments(assignments.get(i), assignments.get(j), words);
          Pair myPair = new Pair(
            assignments.get(i).getName(),
            assignments.get(j).getName(),
            compare
          );
          if(i != j){
			pairs.add(myPair);
          	//System.out.println(myPair);
		  }
        }
      }
      //Convert to Pair array
      Pair[] myarray = pairs.toArray(new Pair[pairs.size()]);
      // mergesort
      mergeSort(myarray, 0, myarray.length - 1);
      int sum = 0;
      for(Pair p : myarray){
		  System.out.println(p);
		  sum += p.getScore();
	  }
	  double average = (double) sum / (double) myarray.length;
	  System.out.println("\nAverage compare score = "+average+"%");
    }
  }

  public AssignmentWithFolder(String fileText,File file){
    code = fileText;
    filename = file.getName().substring(0,14); //name length
  }
  public static String replaceSpaces(String code){
    String string = code.replaceAll(" ", "");
    return string;
  }
  public static double compareAssignments(AssignmentWithFolder project1, AssignmentWithFolder project2, int wordsToCompare){
    int similar = 0;
    double total = 0;
    for(int count=0; (count+1)*wordsToCompare < ((project2.code).length()); count++){
      if((project1.code).contains((project2.code).substring(count*wordsToCompare,wordsToCompare*(1+count)))){
        similar ++;
      }
      total++;
    }
    return (similar/total)*100.0;
  }
  public String getName(){
	  return filename;
  }
  static void merge(Pair[] arr, int l, int m, int r)
  {
      int i, j, k;
      int n1 = m - l + 1;
      int n2 =  r - m;
      /* create temp arrays */
      Pair[] L = new Pair[n1];
      Pair[] R = new Pair[n2];
      /* Copy data to temp arrays L[] and R[] */
      for (i = 0; i < n1; i++)
          L[i] = arr[l + i];
      for (j = 0; j < n2; j++)
          R[j] = arr[m + 1 + j];
      /* Merge the temp arrays back into arr[l..r]*/
      i = 0; // Initial index of first subarray
      j = 0; // Initial index of second subarray
      k = l; // Initial index of merged subarray
      while (i < n1 && j < n2)
      {
          if (L[i].getScore() <= R[j].getScore())
          {
              arr[k] = L[i];
              i++;
          }
          else
          {
              arr[k] = R[j];
              j++;
          }
          k++;
      }
      /* Copy the remaining elements of L[], if there
         are any */
      while (i < n1)
      {
          arr[k] = L[i];
          i++;
          k++;
      }
      /* Copy the remaining elements of R[], if there
         are any */
      while (j < n2)
      {
          arr[k] = R[j];
          j++;
          k++;
      }
  }
  /* l is for left index and r is right index of the
     sub-array of arr to be sorted */
  static void mergeSort(Pair[] arr, int l, int r)
  {
      if (l < r)
      {
          // Same as (l+r)/2, but avoids overflow for
          // large l and h
          int m = l+(r-l)/2;

          // Sort first and second halves
          mergeSort(arr, l, m);
          mergeSort(arr, m+1, r);

          merge(arr, l, m, r);
      }
  }
  // inner class Pair
  static class Pair{
	  double score;
	  String first;
	  String second;

	  public Pair(String _first, String _second, double _score){
		  first = _first;
		  second = _second;
		  score = _score;
	  }
	  public String toString(){
		  return Math.round(score)+"% = "+first+" compared to "+second;
	  }
	  public double getScore(){
		  return score;
	  }
  }
}