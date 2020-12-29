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
		String text = "The 25-year-old lives in Rockford, Illinois, sharing a home with his parents about a half mile from the Don Carter Lanes, where he was a regular at the upstairs tavern, Shooter Bar and Grill. He was there as usual this past Saturday, but left earlier than he normally does.";
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
			System.out.print(tokens.get(i).getWord()+ " ");
		}
	}
	
	@Test
	public void TestupdatePosTagPrepositionWithVerb() {
		List<CustomToken> tokens = null;
		String text = "Carrera said there was some confusion on Monday, as some manamko' who weren't registered to come to the clinic showed up. Public Health is working with the island's mayors to register their manamko' to schedule when they should come to Okkodo.";
		tokens = nlpAnalyzer.getTokens(text);
		
		CustomToken tokenunit = null;
		String n = "cut";
		tokenunit = (CustomToken) nlpAnalyzer.getTokens(n).get(0);
		
		PosTagImprover testtags = new PosTagImprover();
		
		tokens = testtags.updatePosTagPrepositionWithVerb(tokens,tokenunit);
		
		for(int i = 0; i < tokens.size();i++) {
			System.out.print(tokens.get(i).getWord()+" ");
		}
	}
	@Test
	public void	TestupdatePosTagVerbWithAdjective() {
		List<CustomToken> tokens = null;
		String text = "They had complained that Maxwell was being mistreated by guards who wake her every 15 minutes at night and who subject her to repeated unnecessary searches while failing to adequately protect her from an outbreak of the coronavirus at the jail.";
		tokens = nlpAnalyzer.getTokens(text);
		
		CustomToken tokenunit = null;
		String n = "pretty";
		tokenunit = (CustomToken) nlpAnalyzer.getTokens(n).get(0);
		
		PosTagImprover testtags = new PosTagImprover();
		
		tokens = testtags.updatePosTagVerbWithAdjective(tokens,tokenunit);
		
		for(int i = 0; i < tokens.size();i++) {
			System.out.print(tokens.get(i).getWord()+" ");
		}
	}
	
	@Test
	public void TestadjustNounPosTags(){
		List<CustomToken> tokens = null;
		String text = "For substantially the same reasons as the Court determined that detention was warranted in the initial bail hearing, the Court again concludes that no conditions of release can reasonably assure the Defendant’s appearance at future proceedings.";
		tokens = nlpAnalyzer.getTokens(text);
		
		PosTagImprover testtags = new PosTagImprover();
		
		
		tokens = testtags.adjustNounPosTags(tokens);
		
		for(int i = 0; i < tokens.size();i++) {
			System.out.print(tokens.get(i).getWord()+" ");
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
		String text = " Ever since our five children got their cell phones and started texting, Susan and I have used this technology to lift them up and encourage them";
		tokens = nlpAnalyzer.getTokens(text);
		
		PosTagImprover testtags = new PosTagImprover();
		
		CustomToken tokenunit = null;
		String n = "Object";
		tokenunit = (CustomToken) nlpAnalyzer.getTokens(n).get(0);
		
		
		tokens = testtags.updatePosTagWithNoun(tokens,tokenunit);
		
		for(int i = 0; i < tokens.size();i++) {
			System.out.print(tokens.get(i).getWord()+" ");
		}
	}
	

	@Test
	public void TestadjustPrePositionPosTags(){
		List<CustomToken> tokens = null;
		String text = "to give the kids a bit of controlled freedom as they communicate with family members and friends that we agree upon";
		tokens = nlpAnalyzer.getTokens(text);
		
		PosTagImprover testtags = new PosTagImprover();
		
		
		tokens = testtags.adjustPrePositionPosTags(tokens);
		
		for(int i = 0; i < tokens.size();i++) {
			System.out.print(tokens.get(i).getWord() + " ");
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
		String text = "have a rule that the phone stays in the house and on our main living area, not downstairs or in bedrooms";
		tokens = nlpAnalyzer.getTokens(text);
		
		PosTagImprover testtags = new PosTagImprover();
		
		
		tokens = testtags.adjustUseCaseKeywords(tokens);
		
		for(int i = 0; i < tokens.size();i++) {
			System.out.print(tokens.get(i).getWord()+ " ");
		}
	}
	
	
	
	
	@Test
	public void getVerbBaseFromThirdPerson(){
		List<CustomToken> tokens = null;
		String text = " Are our kids young for this? Probably. But it’s a different day and age, folks, and we’re doing the best we can";
		tokens = nlpAnalyzer.getTokens(text);
		
		PosTagImprover testtags = new PosTagImprover();
		
		
		System.out.println(testtags.getVerbBaseFromThirdPerson(text));
		
	}
	
	
	
	

	
	
	
	
	
	
	
}
