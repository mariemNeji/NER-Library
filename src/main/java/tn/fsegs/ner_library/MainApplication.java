package tn.fsegs.ner_library;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Properties;

import com.google.common.io.Files;

import edu.stanford.nlp.ling.CoreAnnotations.LemmaAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.NamedEntityTagAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;

public class MainApplication {

    public static void main(String[] args) throws IOException {

	// creates a StanfordCoreNLP object, with POS tagging, lemmatization, NER,
	// parsing, and coreference resolution
	Properties props = new Properties();

	props.put("annotators", "tokenize, ssplit, pos, lemma, ner");
	StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

	// read some text from the file..
	File inputFile = new File(args[0]);
	String text = Files.toString(inputFile, Charset.forName("UTF-8"));

	// create an empty Annotation just with the given text
	Annotation document = new Annotation(text);

	// run all Annotators on this text
	pipeline.annotate(document);

	// these are all the sentences in this document
	// a CoreMap is essentially a Map that uses class objects as keys and has values
	// with custom types
	List<CoreMap> sentences = document.get(SentencesAnnotation.class);

	System.out.print("[");
	for (int mapIndex = 0; mapIndex < sentences.size(); mapIndex++) {
	    // get the current sentence
	    CoreMap sentence = sentences.get(mapIndex);
	    // traversing the words in the current sentence
	    // a CoreLabel is a CoreMap with additional token-specific methods
	    List<CoreLabel> tokens = sentence.get(TokensAnnotation.class);
	    for (int labelIndex = 0; labelIndex < tokens.size(); labelIndex++) {
		// get the current token
		CoreLabel token = tokens.get(labelIndex);
		// this is the NER label of the token
		String ner = token.get(NamedEntityTagAnnotation.class);
		// this is the lemma of the token
		String lemma = token.get(LemmaAnnotation.class);
		if (labelIndex == tokens.size() - 1 && mapIndex == sentences.size() - 1) {
		    System.out.print(lemma + ", " + ner);
		} else {
		    System.out.print(lemma + ", " + ner + ", ");
		}
	    }
	}
	System.out.print("]");
    }
}
