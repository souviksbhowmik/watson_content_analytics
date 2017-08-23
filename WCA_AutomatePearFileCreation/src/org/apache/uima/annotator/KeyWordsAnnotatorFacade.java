package org.apache.uima.annotator;

import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.jcas.JCas;



public class KeyWordsAnnotatorFacade extends JCasAnnotator_ImplBase {

	@Override
	public void process(JCas aJCas) throws AnalysisEngineProcessException {
		// TODO Auto-generated method stub
		
		KeyWordsAnnotator keyWordsAnnotator =  new KeyWordsAnnotator();
		keyWordsAnnotator.annotateKeyWords(aJCas);

		
	}

}
