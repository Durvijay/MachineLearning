
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.*;
import java.util.*;

/**
 Represents a naive index over a set of documents in a single directory.
 */
public class NaiveIndex {

   private String[] mFileArray;
   private int docIdTemporary=0;
   private static HashMap<String,List<Integer>> filesMapping = new HashMap<>();
   private HashMap<Integer,String> files = new HashMap<>();

   



public static HashMap<String, List<Integer>> getFilesMapping() {
	return filesMapping;
}

public static void setFilesMapping(HashMap<String, List<Integer>> filesMapping) {
	NaiveIndex.filesMapping = filesMapping;
}

/**
    Indexes all .txt files in the specified directory. First builds a dictionary
    of all terms in those files, then builds a boolean term-document matrix as
    the index.

    @param directory the Path of the directory to index.
 * @param pIndex 
 * @param docId 
 * @return 
    */
   public int indexDirectory(final Path directory, PositionalInvertedIndex pIndex, int docId) {
      // will need a data structure to store all the terms in the document
      // HashSet: a hashtable structure with constant-time insertion; does not
      // allow duplicate entries; stores entries in unsorted order.



      
	   final List<Integer> docIds=new ArrayList<>();
      try {
         // go through each .txt file in the working directory

         System.out.println(directory.toString());

         Files.walkFileTree(directory, new SimpleFileVisitor<Path>() {
        	 int docId1=docId;
        	 
            public FileVisitResult preVisitDirectory(Path dir,
             BasicFileAttributes attrs) {
               // make sure we only process the current working directory
               if (directory.equals(dir)) {
                  return FileVisitResult.CONTINUE;
               }
               return FileVisitResult.SKIP_SUBTREE;
            }

            public FileVisitResult visitFile(Path file,
             BasicFileAttributes attrs) {
               // only process .txt files
               if (file.toString().endsWith(".txt")) {
            	   buildIndex(file, pIndex,docId1);
                  files.put(docId,file.getFileName().toString());
                  docIds.add(docId1);
                  docId1++;
                  
               }
               return FileVisitResult.CONTINUE;
            }

            // don't throw exceptions if files are locked/other errors occur
            public FileVisitResult visitFileFailed(Path file,
             IOException e) {

               return FileVisitResult.CONTINUE;
            }
         });

         // convert the dictionaries to sorted arrays, so we can use binary
         // search for finding indices.

        // mFileArray = files.toArray(new String[0]);

        // Arrays.sort(mFileArray);


       filesMapping.put(directory.getFileName().toString(), docIds);
         return ++docIdTemporary;
      }
      catch (Exception ex) {
      }
	return docIdTemporary;

   }

   // reads the file given by Path; adds each term from file to the dictionary
   private void buildIndex(Path file, PositionalInvertedIndex pIndex, int docId) {
      try {
    	  int i=0;
         try (Scanner scan = new Scanner(file)) {
            while (scan.hasNext()) {
               // read one word at a time; process and add it to dictionary.

               String word = processWord(scan.next());
               if (word.length() > 0) {
                  pIndex.addTerm(word, docId, i);
                  ++i;
               }
            }
         }
         docIdTemporary=docId;
      }
      catch (IOException ex) {
      }

   }
   private static String processWord(String next) {
      return next.replaceAll("\\W", "").toLowerCase();
   }

}
