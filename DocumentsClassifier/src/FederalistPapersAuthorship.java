import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FederalistPapersAuthorship {
	
	private static NaiveIndex naiveIndex=new NaiveIndex();
	
	public static void main(String[] args) throws IOException {
		
		String workingDir = System.getProperty("user.dir")+"/Federalpapers/";
		
		PositionalInvertedIndex pIndex=new PositionalInvertedIndex();
		int docId=0;
		
		docId=indexDocuments(Paths.get(workingDir+"HAMILTON"),pIndex,docId);
		docId=indexDocuments(Paths.get(workingDir+"HAMILTONANDMADISON"),pIndex,docId);
		docId=indexDocuments(Paths.get(workingDir+"JAY"),pIndex,docId);
		docId=indexDocuments(Paths.get(workingDir+"MADISON"),pIndex,docId);
		indexDocuments(Paths.get(workingDir+"HAMILTONORMADISON"),pIndex,docId);
		
		RocchioClassifier rClassifier=new RocchioClassifier(pIndex);
		
		rClassifier.calculateEuclidean(Paths.get(workingDir+"HAMILTONORMADISON").getFileName().toString());
		
	}
	
	private static int indexDocuments(Path path, PositionalInvertedIndex pIndex, int docId2) {
		return naiveIndex.indexDirectory(path,pIndex,docId2);
	}

}
