package pe.edu.unsa.daisi.lis.cel.util.nlp;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import pe.edu.unsa.daisi.lis.cel.util.nlp.*;


public class TestPosTagImprover {
	
	INLPAnalyzer nlpAnalyzer;
	@Before
	public void setUp() {
		System.out.println("TEST UNIT TestPosTagImprover.JAVA");
		nlpAnalyzer = CoreNLPAnalyzer.getInstance();
	}
	
	@Test
	public void TestgetPosTagsAsString() {
		List<CustomToken> tokens = null;		
		String text = "It is well-known that the existing theoretical models for outlier detection make assumptions that may not reflect the true nature of outliers in every real application. With that in mind, this paper describes an empirical study performed on unsupervised outlier detection using 8 algorithms from the state-of-the-art and 8 datasets that refer to a variety of real-world tasks of high impact, like spotting cyberat- tacks, clinical pathologies and abnormalities in nature. We present the\r\n";
		tokens = nlpAnalyzer.getTokens(text);
		
		PosTagImprover testtags = new PosTagImprover();
	
		
		System.out.println(testtags.getPosTagsAsString(tokens,0,tokens.size()));
		assertEquals("PRP VBZ JJ IN DT VBG JJ NNS IN NN NN VBP NNS WDT MD RB VB DT JJ NN IN NNS IN DT JJ NN . IN DT IN NN , DT NN VBZ DT JJ NN VBN IN JJ NN NN VBG CD NNS IN DT JJ CC CD NNS WDT VBP TO DT NN IN JJ NNS IN JJ NN , IN VBG NN : NNS , JJ NNS CC NNS IN NN . PRP VBP DT", testtags.getPosTagsAsString(tokens,0,tokens.size()));
	}

	@Test
	public void TestupdatePosTagWithVerb() {
		List<CustomToken> tokens = null;
		String text = "What is Docker? Docker is a tool designed to make it easier to create, deploy, and run applications by using containers";
		tokens = nlpAnalyzer.getTokens(text);
		
		//List<CustomToken> f = null;
		
		//String textf = "The run teaches excellent";
		
		//f = nlpAnalyzer.getTokens(textf);
		
		//CustomToken tokenunit = f.get(1);
		
		CustomToken tokenunit = null;
		String n = "run";
		tokenunit = (CustomToken) nlpAnalyzer.getTokens(n).get(0);
		
		PosTagImprover testtags = new PosTagImprover();
		
		tokens = testtags.updatePosTagWithVerb(tokens,tokenunit);
		
		for(int i = 0; i < tokens.size();i++) {
			System.out.println(tokens.get(i).getWord());
		}
	}
	
	@Test
	public void TestupdatePosTagPrepositionWithVerb() {
		List<CustomToken> tokens = null;
		String text = "The teacher teaches excellent";
		tokens = nlpAnalyzer.getTokens(text);
		
		CustomToken tokenunit = null;
		String n = "from";
		tokenunit = (CustomToken) nlpAnalyzer.getTokens(n).get(0);
		
		PosTagImprover testtags = new PosTagImprover();
		
		tokens = testtags.updatePosTagPrepositionWithVerb(tokens,tokenunit);
		
		for(int i = 0; i < tokens.size();i++) {
			System.out.println(tokens.get(i).getWord());
		}
	}
	@Test
	public void	TestupdatePosTagVerbWithAdjective() {
		List<CustomToken> tokens = null;
		String text = "The teacher teaches excellent";
		tokens = nlpAnalyzer.getTokens(text);
		
		CustomToken tokenunit = null;
		String n = "be";
		tokenunit = (CustomToken) nlpAnalyzer.getTokens(n).get(0);
		
		PosTagImprover testtags = new PosTagImprover();
		
		tokens = testtags.updatePosTagVerbWithAdjective(tokens,tokenunit);
		
		for(int i = 0; i < tokens.size();i++) {
			System.out.println(tokens.get(i).getWord());
		}
	}
	
	@Test
	public void TestadjustNounPosTags(){
		List<CustomToken> tokens = null;
		String text = "The teacher teaches excellent";
		tokens = nlpAnalyzer.getTokens(text);
		
		PosTagImprover testtags = new PosTagImprover();
		
		
		tokens = testtags.adjustNounPosTags(tokens);
		
		for(int i = 0; i < tokens.size();i++) {
			System.out.println(tokens.get(i).getWord());
		}
	}
	
	@Test
	public void TestadjustVerbPosTags(){
		List<CustomToken> tokens = null;
		String text = "The teacher teaches excellent";
		tokens = nlpAnalyzer.getTokens(text);
		
		PosTagImprover testtags = new PosTagImprover();
		
		
		tokens = testtags.adjustVerbPosTags(tokens);
		
		for(int i = 0; i < tokens.size();i++) {
			System.out.println(tokens.get(i).getWord());
		}
	}
	
	
	@Test
	public void TestupdatePosTagWithNoun(){
		List<CustomToken> tokens = null;
		String text = "The teacher teaches excellent";
		tokens = nlpAnalyzer.getTokens(text);
		
		PosTagImprover testtags = new PosTagImprover();
		
		CustomToken tokenunit = null;
		String n = "Object";
		tokenunit = (CustomToken) nlpAnalyzer.getTokens(n).get(0);
		
		
		tokens = testtags.updatePosTagWithNoun(tokens,tokenunit);
		
		for(int i = 0; i < tokens.size();i++) {
			System.out.println(tokens.get(i).getWord());
		}
	}
	

	@Test
	public void TestadjustPrePositionPosTags(){
		List<CustomToken> tokens = null;
		String text = "The teacher teaches excellent";
		tokens = nlpAnalyzer.getTokens(text);
		
		PosTagImprover testtags = new PosTagImprover();
		
		
		tokens = testtags.adjustPrePositionPosTags(tokens);
		
		for(int i = 0; i < tokens.size();i++) {
			System.out.println(tokens.get(i).getWord());
		}
	}
	
	@Test
	public void TestadjustAdjectivePosTags(){
		List<CustomToken> tokens = null;
		String text = "The teacher teaches excellent";
		tokens = nlpAnalyzer.getTokens(text);
		
		PosTagImprover testtags = new PosTagImprover();
		
		
		tokens = testtags.adjustAdjectivePosTags(tokens);
		
		for(int i = 0; i < tokens.size();i++) {
			System.out.println(tokens.get(i).getWord());
		}
	}
	

	@Test
	public void TestadjustUseCaseKeywords(){
		List<CustomToken> tokens = null;
		String text = "The teacher teaches excellent";
		tokens = nlpAnalyzer.getTokens(text);
		
		PosTagImprover testtags = new PosTagImprover();
		
		
		tokens = testtags.adjustUseCaseKeywords(tokens);
		
		for(int i = 0; i < tokens.size();i++) {
			System.out.println(tokens.get(i).getWord());
		}
	}
	
	
	
	
	@Test
	public void getVerbBaseFromThirdPerson(){
		List<CustomToken> tokens = null;
		String text = "El caso de uso debe describir qué debe hacer el sistema a desarrollar en su interacción con los actores y no cómo debe hacerlo. Es decir, debe describir sólo comportamiento observable externamente, sin entrar en la funcionalidad interna del sistema.";
		tokens = nlpAnalyzer.getTokens(text);
		
		PosTagImprover testtags = new PosTagImprover();
		
		
		System.out.println(testtags.getVerbBaseFromThirdPerson(text));
		
	}
	
	
	
	

	
	
	
	
	
	
	
}
