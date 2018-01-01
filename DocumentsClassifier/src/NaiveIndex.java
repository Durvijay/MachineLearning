
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import funtionalInterfaces.ProcessWord;

/**
 * Represents a naive index over a set of documents in a single directory.
 */
public class NaiveIndex {

	private int docIdTemporary = 0;
	private static Map<String, List<Integer>> filesMapping = new HashMap<>();
	private Map<Integer, String> files = new HashMap<>();
	private static final Logger LOGGER=Logger.getLogger(NaiveIndex.class.getName());
	

	public static Map<String, List<Integer>> getFilesMapping() {
		return filesMapping;
	}

	public static void setFilesMapping(Map<String, List<Integer>> filesMapping) {
		NaiveIndex.filesMapping = filesMapping;
	}

	/**
	 * Indexes all .txt files in the specified directory. First builds a
	 * dictionary of all terms in those files, then builds a boolean
	 * term-document matrix as the index.
	 * 
	 * @param directory
	 *            the Path of the directory to index.
	 * @param pIndex
	 * @param docId
	 * @return
	 */
	public int indexDirectory(final Path directory, PositionalInvertedIndex pIndex, int docId) {
		// will need a data structure to store all the terms in the document
		// HashSet: a hashtable structure with constant-time insertion; does not
		// allow duplicate entries; stores entries in unsorted order.

		final List<Integer> docIds = new ArrayList<>();
		try {
			// go through each .txt file in the working directory

			Files.walkFileTree(directory, new SimpleFileVisitor<Path>() {
				int docId1 = docId;

				@Override
				public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
					// make sure we only process the current working directory
					return directory.equals(dir) ? FileVisitResult.CONTINUE : FileVisitResult.SKIP_SUBTREE;
				}

				@Override
				public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
					// only process .txt files
					if (file.toString().endsWith(".txt")) {
						buildIndex(file, pIndex, docId1);
						files.put(docId, file.getFileName().toString());
						docIds.add(docId1++);
						docId1++;

					}
					return FileVisitResult.CONTINUE;
				}

				// don't throw exceptions if files are locked/other errors occur
				@Override
				public FileVisitResult visitFileFailed(Path file, IOException e) {
					return FileVisitResult.CONTINUE;
				}
			});

			// convert the dictionaries to sorted arrays, so we can use binary
			// search for finding indices.
			filesMapping.put(directory.getFileName().toString(), docIds);
			
			return ++docIdTemporary;
		} catch (Exception ex) {
			LOGGER.log(Level.SEVERE, "Error Occurred during indexing of document No :"+docId, ex);
		}
		return docIdTemporary;

	}

	// reads the file given by Path; adds each term from file to the dictionary
	private void buildIndex(Path file, PositionalInvertedIndex pIndex, int docId) {
		try {
			int i = 0;
			try (Scanner scan = new Scanner(file)) {

				while (scan.hasNext()) {
					// read one word at a time; process and add it to
					// dictionary.

					ProcessWord processWord = word4 -> word4.replaceAll("\\W", "").toLowerCase();
					String word = processWord.processWord(scan.next());

					if (word.length() > 0) {
						pIndex.addTerm(word, docId, i++);
					}
				}
			}
			docIdTemporary = docId;
		} catch (IOException ex) {
			LOGGER.log(Level.SEVERE, "Error Occurred during processing of document No :"+docId, ex);
		}

	}
}
