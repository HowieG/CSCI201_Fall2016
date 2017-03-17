//holds values array which is directly related to the Question and Answer arrays
public class Category
{
	public String categoryName;
	public int[] pointValsArray;
	public QA[] QAArray;
	
	public static final int CATEGORIES_SIZE = 5;
	
	public int size;
	
	//default constructor
	public Category()
	{
		categoryName = "";
	}
	
	 public Category(String categoryNameToSet)
	 { 
		  setName(categoryNameToSet); 
		  pointValsArray = new int[CATEGORIES_SIZE]; 
		  QAArray = new QA[CATEGORIES_SIZE]; 
		  size = 0;
	}
	 
	 public void addQuestion(int pointVal, QA qa)
	 {
		 pointValsArray[size] = pointVal;
		 QAArray[size] = qa;
		 size++;
	 }
	
	//set category name
	public void setName(String categoryNameToSet)
	{
		categoryName = categoryNameToSet;
	}
	
	//get category name
	public String getName()
	{
		return categoryName;
	}
	
	//get QAs
	 public QA[] getQAs()
	 { 
	  return QAArray; 
	 }
	 
	 //get point vals
	 public int[] getPointVals()
	 {
		 return pointValsArray;
	 }

	 //get QA of corresponding point val
	 public QA getQA(int pointVal)
	 {
		 for(int i = 0; i < pointValsArray.length; i++)
		 {
			 if(pointValsArray[i] == pointVal)
			 {
				 return QAArray[i];
			 }
		 }
		 
		 return null;
	 }
}