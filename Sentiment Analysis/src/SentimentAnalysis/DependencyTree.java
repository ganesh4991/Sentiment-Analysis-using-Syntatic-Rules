package SentimentAnalysis;


import java.util.List;
import java.io.StringReader;

import edu.stanford.nlp.process.Tokenizer;
import edu.stanford.nlp.process.TokenizerFactory;
import edu.stanford.nlp.process.CoreLabelTokenFactory;

import edu.stanford.nlp.process.PTBTokenizer;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.trees.*;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;


class DependencyTree {
	
	LexicalizedParser lp;
	public DependencyTree() {
		String parserModel = "edu/stanford/nlp/models/lexparser/englishPCFG.ser.gz";    
		lp = LexicalizedParser.loadModel(parserModel);
	    	
	}
	public List<TypedDependency> generateTree( String sentence) {
	
	
    // This option shows loading and using an explicit tokenizer
    TokenizerFactory<CoreLabel> tokenizerFactory =
    PTBTokenizer.factory(new CoreLabelTokenFactory(), "");
    Tokenizer<CoreLabel> tok =
    tokenizerFactory.getTokenizer(new StringReader(sentence));
    List<CoreLabel> rawWords2 = tok.tokenize();
    Tree parse = lp.apply(rawWords2);
    TreebankLanguagePack tlp = lp.treebankLanguagePack(); // PennTreebankLanguagePack for English
    GrammaticalStructureFactory gsf = tlp.grammaticalStructureFactory();
    GrammaticalStructure gs = gsf.newGrammaticalStructure(parse);
    List<TypedDependency> tdl = gs.typedDependenciesCCprocessed();
    return tdl;
  }

}
