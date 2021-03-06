package stackoverflow.tag.recommender.clustering

import java.io.StringReader
import custom.analyzer.StackOverflowAnalyzer
import org.apache.lucene.util.Version
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute
import scala.collection.mutable
//import org.apache.lucene.analysis.tokenattributes.CharTermAttributport stack.overflow.Document

object Tokenizer {
  val LuceneVersion = Version.LUCENE_46

  def tokenizeAll(docs: Iterable[Document]) = docs.map(tokenize)

  def tokenize(doc: Document): TermDoc = TermDoc(Set(doc.docId), doc.label, doc.integerLabel, tokenize(doc.body))

  def tokenize(content: String): Seq[String] = {
    val tReader = new StringReader(content)
    val analyzer = new StackOverflowAnalyzer()
    val tStream = analyzer.tokenStream("contents", tReader)
    val term = tStream.addAttribute(classOf[CharTermAttribute])
    tStream.reset()

    val result = mutable.ArrayBuffer.empty[String]
    while(tStream.incrementToken()) {
      val termValue = term.toString
      if (!(termValue matches ".*[\\d\\.].*")) {
        result += term.toString
      }
    }
    result
  }
}

case class TermDoc(doc: Set[String], label: Set[String], integerLabel: Int, terms: Seq[String])
