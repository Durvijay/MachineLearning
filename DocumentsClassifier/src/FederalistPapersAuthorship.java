import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Scanner;

public class FederalistPapersAuthorship {

	public static void main(String[] args) throws IOException {
		Scanner scr=new Scanner(System.in);
		PositionalInvertedIndex pIndex=new PositionalInvertedIndex();
		NaiveIndex naiveIndex=new NaiveIndex();
		
		
	//	System.out.println("Enter the Number of training sets");
//		int trainingSetSize=scr.nextInt();
		int docId=0;
/*		for (int i = 0; i < trainingSetSize; i++) {
			System.out.println("Enter the training path no "+i);
			Path trainingPath=Paths.get(scr.next());
			System.out.println(docId);
			docId=naiveIndex.indexDirectory(trainingPath,pIndex,docId);
		}
*/		
		docId=naiveIndex.indexDirectory(Paths.get("C:/Users/DURVIJAY/Documents/Federalpapers/HAMILTON"),pIndex,docId);
		docId=naiveIndex.indexDirectory(Paths.get("C:/Users/DURVIJAY/Documents/Federalpapers/HAMILTONANDMADISON"),pIndex,docId);
		docId=naiveIndex.indexDirectory(Paths.get("C:/Users/DURVIJAY/Documents/Federalpapers/JAY"),pIndex,docId);
		docId=naiveIndex.indexDirectory(Paths.get("C:/Users/DURVIJAY/Documents/Federalpapers/MADISON"),pIndex,docId);
	//	docId=naiveIndex.indexDirectory(Paths.get("C:/Users/DURVIJAY/Documents/Federalpapers/Test"),pIndex,docId);
		System.out.println("Total Docs :"+docId);
	//	String rocResults=rClassifier.getRocchioResults(pIndex.getIndexMap());
		docId=naiveIndex.indexDirectory(Paths.get("C:/Users/DURVIJAY/Documents/Federalpapers/HAMILTONORMADISON"),pIndex,docId);
		RocchioClassifier rClassifier=new RocchioClassifier(pIndex,docId);
		
		rClassifier.calculateEuclidean(pIndex,docId,Paths.get("C:/Users/DURVIJAY/Documents/Federalpapers/HAMILTONORMADISON").getFileName().toString());

	}

}
