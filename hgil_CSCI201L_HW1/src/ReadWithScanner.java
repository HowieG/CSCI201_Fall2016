//adapted from http://www.javapractices.com/topic/TopicAction.do?Id=87

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class ReadWithScanner
{
	  public String[] categories;
	  public int[] pointValues;
	  public static ReadWithScanner parser;
	  
	  public Category[] categoryObjects = new Category[Category.CATEGORIES_SIZE];
	
  public static void parse(String fileToParse) throws IOException
  {
	 parser = new ReadWithScanner(fileToParse);
	  
	  parser.processLineByLine();
  }
  
  public ReadWithScanner(String aFileName)
  {
    fFilePath = Paths.get(aFileName);
    categories = new String[Category.CATEGORIES_SIZE]; 
    pointValues = new int[Category.CATEGORIES_SIZE]; 
  }
  
  public final void processLineByLine() throws IOException
  {
    try (Scanner scanner =  new Scanner(fFilePath, ENCODING.name()))
    {
    	//parses the first (category) line
      if(scanner.hasNextLine())
      {
    	  processCategoryLine(scanner.nextLine());
      }
      else
      {
    	  //terminate
      }
      //parses the second (point value) line
      if(scanner.hasNextLine())
      {
    	  processPointValLine(scanner.nextLine());
      }
      else
      {
    	  //terminate
      }
      
      //tracks number of questions to be sure there are 25 + jeopardy
      int numQuestions = 0;
      
      //parses the questions
      while(scanner.hasNextLine())
      {
    	  
    	  //check for FJ
    	  
    	  
    	  processQuestionLine(scanner.nextLine());
    	  
    	  numQuestions++;
      }
      
      if(numQuestions > 26)
      {
    	  //terminate;
      }
    }
  }
  
  protected String[] processCategoryLine(String aLine)
  {
	    //use a second Scanner to parse the content of each line 
	    Scanner lineScanner = new Scanner(aLine);
	    lineScanner.useDelimiter("::");
	    
	    int currentCategory = 0;
	    
	    while (lineScanner.hasNext())
	    {
	    	String token = lineScanner.next();
	    	
	    	//terminates if (any lower/uppercase version of) the category already exists in the array
	    	for(int i = 0; i < currentCategory; i++)
	    	{
	    		if(categories[i].toLowerCase().equals(token.toLowerCase()))
	    		{
	    			//terminate
	    		}
	    	}
	    	
	    	categories[currentCategory] = token;
	    	
	    	//new instance of Category object
	    	categoryObjects[currentCategory] = new Category(token);
	    	
	    	currentCategory++;
	    }
	    
	    if(currentCategory != 5)
	    {
	    	//terminate
	    }
	    lineScanner.close();
	    
	    return categories;
  }
  
  protected int[] processPointValLine(String aLine)
  {
    //use a second Scanner to parse the content of each line 
    Scanner lineScanner = new Scanner(aLine);
    lineScanner.useDelimiter("::");
    
    int currentPointValue = 0;
    
    while (lineScanner.hasNext())
    {
    	String token = lineScanner.next();
    	
    	int tokenAsInt;
    	
    	//parses the string to an int iff it is an int 
    	try
    	{
    		tokenAsInt = Integer.parseInt(token);
    		
    		//terminates if the point value already exists in the array
        	for(int i = 0; i < currentPointValue; i++)
        	{
        		if(pointValues[i] == tokenAsInt)
        		{
        			//terminate
        		}
        	}
        	
        	pointValues[currentPointValue] = tokenAsInt;
        	currentPointValue++;
    	}
    	catch (NumberFormatException e)
    	{
    		//terminate
    	}
    }
    
    if(currentPointValue != 5)
    {
    	//terminate
    }
    lineScanner.close();
    
    return pointValues;
  }
  
  protected void processQuestionLine(String aLine) //should return Category[]
  {
	  //check for FJ!
	  
	  
	    //use a second Scanner to parse the content of each line 
	    Scanner lineScanner = new Scanner(aLine);
	    lineScanner.useDelimiter("::");
	    
	    String category = "";
	    int pointVal = -1;
	    String question = "";
	    String answer = "";
	    
	    int currentToken = 0;
	    
	    while (lineScanner.hasNext())
	    {
	    	String token = lineScanner.next();
	    	currentToken++;
	    	
	    	if(token.equals("FJ"))
	    	{
	    		category = token;
	    		question = lineScanner.next();
	    		answer = lineScanner.next();
	    		
	    		//if there are more than 3 sets of ::, terminate
	    		if(lineScanner.hasNext())
	    		{
	    			//terminate
	    		}
	    		
	    		break;
	    	}
	    	else
	    	{
	    		//process question
		    	switch(currentToken)
		    	{
		    	case 1:
		    		category = token;
			    	if(!checkCategoryValidity(category))
			    	{
			    		//terminate
			    	}
		    		break;
		    	case 2:
		        	//parses the string to an int iff it is an int 
		        	try
		        	{
		        		int tokenAsInt = Integer.parseInt(token);
		        		pointVal = tokenAsInt;
		        		
				    	if(!checkPointValidity(pointVal))
				    	{
				    		//terminate
				    	}
		        	}
		        	catch (NumberFormatException e)
		        	{
		        		//terminate
		        	}
		    		break;
		    	case 3:
		    		question = token;
		    		break;
		    	case 4:
		    		answer = token;
		    		break;
		    	default:
		    		//terminate
		    		break;
		    	}
	    	}
	    }
	    
    	QA qa = new QA(question, answer);
    	
    	int indexOfCategory = java.util.Arrays.asList(categories).indexOf(category);
    	
    	if(indexOfCategory != -1) //if it's not the FJ
    	{
    		categoryObjects[indexOfCategory].addQuestion(pointVal, qa);
        	//log(categoryObjects[indexOfCategory].getName());
        	
        	//int currIndex = categoryObjects[indexOfCategory].size - 1;
        	
        	//log(categoryObjects[indexOfCategory].getPointVals()[currIndex]);
    		//log(categoryObjects[indexOfCategory].getQAs()[currIndex].getQuestion());
    		//log(categoryObjects[indexOfCategory].getQAs()[currIndex].getAnswer());
    	}
    	
	    //initialized tokens as empty string, check that they are not empty later!
	    
	    //might be unnecessary if default is break
	    if(currentToken != 4)
	    {
	    	//terminate
	    }
	    lineScanner.close();
	    
	    //return categories;
  }
  
  public String[] getCategories()
  {
	  return categories;
  }
  
  public int[] getPointValues()
  {
	  return pointValues;
  }
  
  //ensures the category token is in the category array
  protected Boolean checkCategoryValidity(String catToCheck)
  {
	  for(int i = 0; i < categories.length; i++)
	  {
		  if(catToCheck.toLowerCase().equals(categories[i].toLowerCase()))
		  {
			  return true;
		  }
	  }
	  
	  return false;
  }
  
  //ensures the point value token is in the point value array
  protected Boolean checkPointValidity(int pointsToCheck)
  {
	  for(int i = 0; i < pointValues.length; i++)
	  {
		  if(pointsToCheck == pointValues[i])
		  {
			  return true;
		  }
	  }
	  
	  return false;
  }
  
  
  // PRIVATE 
  private final Path fFilePath;
  private final static Charset ENCODING = StandardCharsets.UTF_8;  
  
  private static void log(Object aObject)
  {
    System.out.println(String.valueOf(aObject));
  }
  
} 