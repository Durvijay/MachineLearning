import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class RocchioClassifier {
	private HashMap<Integer, Double> docWeightLd = new HashMap<>();
	private HashMap<String, Double> centroidCollection = new HashMap<>();
	private HashMap<Integer, Double> termDocFrequency = new HashMap<>();

	public RocchioClassifier(PositionalInvertedIndex index) {
		buildDocumentVector(index);
	}

	public void buildDocumentVector(PositionalInvertedIndex index) {

		for (String token : index.getIndexMap().keySet()) {

			for (TokenDetails docId1 : index.getIndexMap().get(token)) {

				if (docWeightLd.containsKey(docId1.getDocId())) {
					double summationResult = docWeightLd.get(docId1.getDocId());
					double summationSingleTerm = termDocFrequency.get(docId1.getDocId());
					docWeightLd.put(docId1.getDocId(), summationResult + Math.pow(docId1.getPosition().size(), 2));
					termDocFrequency.put(docId1.getDocId(), summationSingleTerm + docId1.getPosition().size());
				} else {
					docWeightLd.put(docId1.getDocId(), Math.pow(docId1.getPosition().size(), 2));
					termDocFrequency.put(docId1.getDocId(), (double) docId1.getPosition().size());
				}
			}
		}

		NaiveIndex.getFilesMapping().forEach((k, v) -> calculateCentroid(v, k));
	}

	private void calculateCentroid(List<Integer> docIds, String name) {

		double finalResult = docIds.stream().mapToDouble(i -> termDocFrequency.get(i) / Math.sqrt(docWeightLd.get(i)))
				.sum();

		System.out.println(name + " : " + finalResult / docIds.size());

		centroidCollection.put(name, finalResult / docIds.size());
	}

	public void calculateEuclidean(String testFolderName) {

		List<Integer> docIds = NaiveIndex.getFilesMapping().get(testFolderName);

		for (int i = 0; i < docIds.size(); i++) {
			double finalResult = termDocFrequency.get(docIds.get(i)) / Math.sqrt(docWeightLd.get(docIds.get(i)));

			Iterator<Map.Entry<String, Double>> itr = centroidCollection.entrySet().iterator();
			String categoryName = "";
			double euclideanFinalValue = 100;

			while (itr.hasNext()) {
				Map.Entry<String, Double> pairs = itr.next();
				double euclideanResult = Math.sqrt(Math.pow(finalResult, 2) + Math.pow(pairs.getValue(), 2));
				if (euclideanResult <= euclideanFinalValue) {
					euclideanFinalValue = euclideanResult;
					categoryName = pairs.getKey();
				}

			}
			System.out.println(docIds.get(i) + " : " + categoryName + " " + euclideanFinalValue);
		}
	}
}
