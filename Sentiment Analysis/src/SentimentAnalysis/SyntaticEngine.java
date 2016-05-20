package SentimentAnalysis;

import java.io.IOException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.math.*;
import java.security.KeyStore.Entry;

import edu.stanford.nlp.ling.IndexedWord;
import edu.stanford.nlp.trees.TypedDependency;

public class SyntaticEngine {

	DependencyTree parseTree;
	WordPolartiy w;
	Map<IndexedWord, Double> map;

	SyntaticEngine() throws IOException {

		map = new HashMap<IndexedWord, Double>();
		w = new WordPolartiy("SentiWordNet.txt");
		System.out.println("Syntactic Rule Progation Engine \nLoading the necessary modules ");
		parseTree = new DependencyTree();
	}

	double identifyRules(List<TypedDependency> list) {
		double total = 0;

		System.out.println("Dependency :-" + list);

		for (TypedDependency a : list) {
			if (a.gov().originalText().trim().equals("ROOT")) {
				map.put(a.gov(), new Double(w.extract(a.gov().toString())));
				System.out.println("Adding governor");
			}
			map.put(a.dep(), new Double(w.extract(a.dep().toString())));

		}

		System.out.println("Before applying rules: " + map);

		for (TypedDependency a : list) {
			if (a.reln().getShortName().equals("neg"))
				map.put(a.gov(), new Double(inverter(a)));

			if (a.reln().getShortName().contains("advmod"))
				map.put(a.gov(), new Double(tuner(a)));
			else if (a.reln().getShortName().contains("mod"))
				map.put(a.gov(), new Double(modifier(a)));

			if (a.reln().getShortName().equals("dobj"))
				map.put(a.dep(), new Double(directObject(a)));

			if (a.reln().getShortName().equals("nsubj"))
				map.put(a.dep(), new Double(nominalSubject(a)));

		}

		System.out.println("After applying the rules: " + map);

		for (Map.Entry<IndexedWord,Double> a : map.entrySet()) {
			IndexedWord word=a.getKey();
			if (word.tag().contains("N"))
				System.out.println(
						"Sentiment value for " + word.originalText() + " : " + a.getValue());
		}

		return total;
	}

	double inverter(TypedDependency a) {

		double value = map.get(a.gov());
		if (value < 0)
			value = 1 - Math.abs(value);
		else
			value = value - 1;

		return value;
	}

	double tuner(TypedDependency a) {

		double value = map.get(a.gov());
		double coefficient = 0.3;
		value = value * (1 + coefficient);

		return value;
	}

	double modifier(TypedDependency a) {

		double coefficient = 0.5; // Coefficient of propagation
		double value = map.get(a.gov());
		value = value + (coefficient * map.get(a.dep()));
		return value;
	}

	double directObject(TypedDependency a) {

		double coefficient = 0.8; // Coefficient of propagation
		double value = map.get(a.dep());
		value = value + (coefficient * map.get(a.gov()));

		return value;
	}

	double nominalSubject(TypedDependency a) {

		double coefficient = 0.9; // Coefficient of propagation
		double value = map.get(a.gov());
		value = value * coefficient;

		return value;
	}

	void run(String s) {
		String input[] = s.split("\\.");
		List<TypedDependency> dependencies = new ArrayList<TypedDependency>();
		for (String a : input) {
			dependencies = parseTree.generateTree(a);
			identifyRules(dependencies);
		}
	}

}
