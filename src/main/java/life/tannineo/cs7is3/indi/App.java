package life.tannineo.cs7is3.indi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;

import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriter;

import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;

import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;

import org.apache.lucene.search.similarities.Similarity;
import org.apache.lucene.search.similarities.BM25Similarity;
import org.apache.lucene.search.similarities.ClassicSimilarity;
import org.apache.lucene.search.similarities.LMDirichletSimilarity;

import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

/**
 * Hello world!
 *
 */
public class App {

  static String INDEX_PATH = "genIndex";
  static String RESULT_PATH_PREFIX = "result_";
  static int HITS_PER_PAGE = 25;

  public static void main(String[] args) throws IOException {

    // -------- 1. read cran.all.1400 --------
    System.out.println("Reading & parsing cran/cran.all.1400 ...");

    InputStream inputStream = App.class.getResourceAsStream("/cran/cran.all.1400");
    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));

    String str = null;
    EnumTag tag = null;
    Document tempDoc = null;
    String bufferString = "";
    List<Document> docList1400 = new ArrayList<>();
    int cur = -1;
    while ((str = bufferedReader.readLine()) != null) {
      if (str.startsWith(EnumTag.ID.getTag())) {
        if (cur >= 0) {
          int cnt = addTextFieldByTag(docList1400.get(cur), bufferString, tag);
          System.out.println("Document ID= " + String.valueOf(cur) + " added " + String.valueOf(cnt) + " "
              + tag.getFieldName() + " tag(s)");
        }
        // from old document
        cur++; // move to the next slot
        // new document
        docList1400.add(new Document());

        // set ID
        tag = EnumTag.ID;
        String[] rowI = str.split(" ");
        docList1400.get(cur).add(new StringField(EnumTag.ID.getFieldName(), rowI[1], Field.Store.YES));
        bufferString = "";
        System.out.println("Document ID= " + rowI[1] + " added 1 " + tag.getFieldName() + " tag(s)");
      } else if (str.startsWith(EnumTag.TITLE.getTag())) {
        int cnt = addTextFieldByTag(docList1400.get(cur), bufferString, tag);
        System.out.println("Document ID= " + String.valueOf(cur + 1) + " added " + String.valueOf(cnt) + " "
            + tag.getFieldName() + " tag(s)");
        bufferString = "";
        tag = EnumTag.TITLE;
      } else if (str.startsWith(EnumTag.AUTHORS.getTag())) {
        int cnt = addTextFieldByTag(docList1400.get(cur), bufferString, tag);
        System.out.println("Document ID= " + String.valueOf(cur + 1) + " added " + String.valueOf(cnt) + " "
            + tag.getFieldName() + " tag(s)");
        bufferString = "";
        tag = EnumTag.AUTHORS;
      } else if (str.startsWith(EnumTag.BIBLIOGRAPHY.getTag())) {
        int cnt = addTextFieldByTag(docList1400.get(cur), bufferString, tag);
        System.out.println("Document ID= " + String.valueOf(cur + 1) + " added " + String.valueOf(cnt) + " "
            + tag.getFieldName() + " tag(s)");
        bufferString = "";
        tag = EnumTag.BIBLIOGRAPHY;
      } else if (str.startsWith(EnumTag.WORDS.getTag())) {
        int cnt = addTextFieldByTag(docList1400.get(cur), bufferString, tag);
        System.out.println("Document ID= " + String.valueOf(cur + 1) + " added " + String.valueOf(cnt) + " "
            + tag.getFieldName() + " tag(s)");
        bufferString = "";
        tag = EnumTag.WORDS;
      } else {
        bufferString += " " + str;
      }
    }
    int lastCnt = addTextFieldByTag(docList1400.get(cur), bufferString, tag);
    System.out.println("Document ID= " + String.valueOf(cur + 1) + " added " + String.valueOf(lastCnt) + " "
        + tag.getFieldName() + " tag(s)");

    inputStream.close();
    bufferedReader.close();

    System.out.println("Reading & parsing cran/cran.all.1400 complete with " + String.valueOf(docList1400.size()));

    // -------- 1.5 parse queries --------
    System.out.println("Reading & parsing cran/cran.qry ...");

    InputStream inputStreamQ = App.class.getResourceAsStream("/cran/cran.qry");
    BufferedReader bufferedReaderQ = new BufferedReader(new InputStreamReader(inputStreamQ, StandardCharsets.UTF_8));

    str = null;
    tag = EnumTag.ID;
    tempDoc = null;
    bufferString = "";
    Map<Integer, String> qryMap225 = new LinkedHashMap<>();
    cur = -1;
    String idStr = "";
    while ((str = bufferedReaderQ.readLine()) != null) {
      if (str.startsWith(EnumTag.ID.getTag())) {
        if (cur >= 0 && tag.equals(EnumTag.WORDS)) {
          qryMap225.put(Integer.parseInt(idStr), bufferString);
          System.out.println("Query ID= " + idStr + " added:\n" + bufferString);

        }
        cur++;
        tag = EnumTag.ID;
        bufferString = "";
        String[] rowQ = str.split(" ");
        idStr = rowQ[1];
      } else if (str.startsWith(EnumTag.WORDS.getTag())) {
        System.out.println("Query ID= " + idStr + " ...");
        tag = EnumTag.WORDS;
      } else {
        bufferString += " " + str;
      }
    }
    qryMap225.put(Integer.parseInt(idStr), bufferString);
    System.out.println("Query ID= " + idStr + " added:\n" + bufferString);

    inputStreamQ.close();
    bufferedReaderQ.close();

    System.out.println("Reading & parsing cran/cran.all.1400 complete with " + String.valueOf(docList1400.size()));

    // -------- 2. create index using different analyzers & similarity measures
    Map<String, Analyzer> analyzMap = new HashMap<>();
    analyzMap.put("SimpleAnalyzer", new SimpleAnalyzer());
    analyzMap.put("WhitespaceAnalyzer", new WhitespaceAnalyzer());
    analyzMap.put("EnglishAnalyzer", new EnglishAnalyzer());

    Map<String, Similarity> simMap = new HashMap<>();
    simMap.put("ClassicSimilarity", new ClassicSimilarity());
    simMap.put("BM25Similarity", new BM25Similarity());
    simMap.put("LMDirichletSimilarity", new LMDirichletSimilarity());

    Path indexPath = Paths.get(App.INDEX_PATH);

    for (Map.Entry<String, Analyzer> analyzEntry : analyzMap.entrySet()) {

      for (Map.Entry<String, Similarity> simEntry : simMap.entrySet()) {
        IndexWriterConfig iwconfig = new IndexWriterConfig(analyzEntry.getValue());
        iwconfig.setSimilarity(simEntry.getValue());

        Directory indexDir = FSDirectory.open(indexPath);
        iwconfig.setOpenMode(IndexWriterConfig.OpenMode.CREATE); // overwrite multiple times
        IndexWriter indexWriter = new IndexWriter(indexDir, iwconfig);

        // add document to the index
        indexWriter.addDocuments(docList1400);

        indexWriter.close();
        indexDir.close(); // close index before next use

        // -------- 3. search query --------
        Directory dirr = FSDirectory.open(indexPath);
        DirectoryReader dirReader = DirectoryReader.open(dirr);
        IndexSearcher searcher = new IndexSearcher(dirReader);
        searcher.setSimilarity(simEntry.getValue());

        List<String> resultArr = new ArrayList<>();
        MultiFieldQueryParser queryParser = new MultiFieldQueryParser(EnumTag.getAllFields(), analyzEntry.getValue());
        queryParser.setAllowLeadingWildcard(true);
        try {
          int curQryCnt = 1;
          for (Map.Entry<Integer, String> queryEntry : qryMap225.entrySet()) {
            System.out.println("Querying: " + queryEntry.getValue());

            Query qry = queryParser.parse(queryEntry.getValue());

            TopDocs topDocs = searcher.search(qry, HITS_PER_PAGE);
            ScoreDoc[] topHits = topDocs.scoreDocs;

            for (ScoreDoc hit : topHits) {
              Document doc = searcher.doc(hit.doc);
              String result = String.format("%03d", curQryCnt) + " 0 " + doc.get(EnumTag.ID.getFieldName()) + " 0 "
                  + hit.score + " STANDARD";
              System.out.println(result);
              // add the result
              resultArr.add(result);
            }
            curQryCnt++;
          }
        } catch (Exception e) {
          e.printStackTrace();
          // TODO: do sth?
        }

        dirReader.close();
        dirr.close();

        // -------- 4. generate search result --------
        String filename = RESULT_PATH_PREFIX + analyzEntry.getKey() + "_" + simEntry.getKey();
        Path resultPath = Paths.get(filename);
        Files.write(resultPath, resultArr, StandardCharsets.UTF_8);

        System.out.println(filename + " complete!");
      }

    }

  }

  // process different textFields according to the tag type
  public static int addTextFieldByTag(Document doc, String bufStr, EnumTag tag) {
    String modStr = String.valueOf(bufStr);

    if (modStr.equals("")) {
      return 0;
    }

    int cnt = 0;

    if (tag.equals(EnumTag.TITLE)) {
      doc.add(new TextField(EnumTag.TITLE.getFieldName(), modStr, Field.Store.YES));
      System.out.println("TITLE->" + modStr);
      cnt++;
    } else if (tag.equals(EnumTag.AUTHORS)) {
      String[] modStrs = modStr.split(" ");
      for (String str : modStrs) {
        if (!str.equals("and") && !str.equals("")) {
          doc.add(new StringField(EnumTag.AUTHORS.getFieldName(), modStr, Field.Store.YES));
          System.out.println("AUTHOR->" + str);
          cnt++;
        }
      }
    } else if (tag.equals(EnumTag.BIBLIOGRAPHY)) {
      doc.add(new StringField(EnumTag.BIBLIOGRAPHY.getFieldName(), modStr, Field.Store.YES));
      System.out.println("BIBLIOGRAPHY->" + modStr);
      cnt++;
    } else if (tag.equals(EnumTag.WORDS)) {
      doc.add(new TextField(EnumTag.WORDS.getFieldName(), modStr, Field.Store.YES));
      cnt++;
    } else {
      // TODO: sth wrong
    }

    return cnt;
  }
}
