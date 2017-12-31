import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class RocchioClassifier {
	 private HashMap<Integer, Double> docWeightLd = new HashMap<>();
	 private HashMap<String, Double> centroidCollection = new HashMap<>();
	 private HashMap<Integer, Double> termDocFrequency=new HashMap<>();

	 
	public RocchioClassifier(PositionalInvertedIndex index, int docId) throws FileNotFoundException {
		buildDocumentVector(index, docId);
	}


	public String getRocchioResults(HashMap<String, List<TokenDetails>> indexMap) {
		
		return null;
	}
	
	
	public void buildDocumentVector(PositionalInvertedIndex index, int docId) throws FileNotFoundException {
		Iterator it = index.getIndexMap().entrySet().iterator();
		
		
		while (it.hasNext()) {
		    Map.Entry pair = (Map.Entry)it.next();
//		    System.out.println(pair.getKey() + " = " + pair.getValue());
		    
		    for (TokenDetails docId1 : index.getIndexMap().get(pair.getKey())) {

				if (docWeightLd.containsKey(docId1.getDocId())) {
					double summationResult = docWeightLd.get(docId1.getDocId());
					double summationSingleTerm = termDocFrequency.get(docId1.getDocId());
					docWeightLd.put(docId1.getDocId(), summationResult + Math.pow(docId1.getPosition().size(), 2));
					termDocFrequency.put(docId1.getDocId(),summationSingleTerm+docId1.getPosition().size() );
				} else {
					docWeightLd.put(docId1.getDocId(), Math.pow(docId1.getPosition().size(), 2));
					termDocFrequency.put(docId1.getDocId(),(double)docId1.getPosition().size() );
				}

			
			}


		   
		}
		Iterator itr = NaiveIndex.getFilesMapping().entrySet().iterator();
		
		while (itr.hasNext()) {
		    Map.Entry pairs = (Map.Entry)itr.next();
		    
		    calculateCentroid(pairs.getValue(),pairs.getKey().toString());	 
		}
		
	}


	private void calculateCentroid(Object object, String name) {
		double finalResult=0;
		List<Integer> docIds=(List<Integer>) object;
		for (int i = 0; i <  docIds.size(); i++) {
		//	System.out.println(termDocFrequency.get(docIds.get(i))/Math.sqrt(docWeightLd.get(docIds.get(i))));
			 finalResult=finalResult+termDocFrequency.get(docIds.get(i))/Math.sqrt(docWeightLd.get(docIds.get(i)));
			 
		}
		System.out.println(name+" : "+finalResult/((List<Integer>) object).size());
		centroidCollection.put(name,finalResult/((List<Integer>) object).size());
	}


	public void calculateEuclidean(PositionalInvertedIndex pIndex, int docId, String testFolderName) {
		List<Integer> docIds=NaiveIndex.getFilesMapping().get(testFolderName);
		for (int i = 0; i <  docIds.size(); i++) {
			double finalResult=termDocFrequency.get(docIds.get(i))/Math.sqrt(docWeightLd.get(docIds.get(i)));
			//System.out.println("docIds.get(i) "+docIds.get(i)+" : "+finalResult);
			Iterator itr = centroidCollection.entrySet().iterator();
			String categoryName="";
			double eiclidFinalValue=100;
			while (itr.hasNext()) {
			    Map.Entry pairs = (Map.Entry)itr.next();
			    double eucildeanResult=Math.sqrt(Math.pow(finalResult, 2)+Math.pow((double) pairs.getValue(), 2));
			    if(eucildeanResult<=eiclidFinalValue){
			    	eiclidFinalValue=eucildeanResult;
			    	categoryName=(String) pairs.getKey();
			    }
			    
			}
			System.out.println(docIds.get(i)+" : "+categoryName+" "+eiclidFinalValue);
			 
		}
	}


}
