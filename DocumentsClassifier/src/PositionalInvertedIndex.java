
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * @author Durvijay Sharma
 */
public class PositionalInvertedIndex {
	private Map<String, List<TokenDetails>> indexMap = new HashMap<>();

	public PositionalInvertedIndex() {
		indexMap.clear();
	}

	public Map<String, List<TokenDetails>> getIndexMap() {
		return indexMap;
	}

	public void setIndexMap(Map<String, List<TokenDetails>> indexMap) {
		this.indexMap = indexMap;
	}

	/**
	 * maps Positon of the token and documentId to the tokenDetail Object and
	 * stores it in hashmap
	 * 
	 * @param term
	 * @param docID
	 * @param wordPosition
	 */
	public void addTerm(String term, int docID, int wordPosition) {
		List<TokenDetails> list = new ArrayList<>();
		TokenDetails docList = new TokenDetails(docID, wordPosition);

		try {
			if (indexMap.containsKey(term)) {
				list = indexMap.get(term);
				docList = list.get(list.size() - 1);
				if (docList.getDocId() == docID) {
					docList.setPosition(wordPosition);
				} else {

					docList = new TokenDetails(docID, wordPosition);
					list.add(docList);
					indexMap.put(term, list);
				}
			} else {
				list.add(docList);
				indexMap.put(term, list);
			}

		} catch (Exception e) {
			Logger.getLogger(PositionalInvertedIndex.class.getName()).log(java.util.logging.Level.SEVERE, null, e);
		}

	}

	/**
	 * returns list of documents id containing the term for PI index
	 * 
	 * @param term
	 * @return
	 */
	public List<TokenDetails> getPostings(String term) {
		return indexMap.get(term);
	}

	/**
	 * returns size of the PI index
	 * 
	 * @return
	 */
	public int getTermCount() {
		return indexMap.size();
	}

	/**
	 * returns complete list of the vocabulary term of the PI index
	 * 
	 * @return
	 */
	public String[] getDictionary() {
		return indexMap.keySet().stream().sorted().toArray(String[]::new);
	}

}
