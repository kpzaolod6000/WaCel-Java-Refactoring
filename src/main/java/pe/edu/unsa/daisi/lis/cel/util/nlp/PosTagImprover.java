package pe.edu.unsa.daisi.lis.cel.util.nlp;

import java.util.ArrayList;
import java.util.List;

import pe.edu.unsa.daisi.lis.cel.util.RegularExpression;
import pe.edu.unsa.daisi.lis.cel.util.nlp.dictionary.english.SpecialVerb;

/**
 * It is used for adjusting POSTAGGING of NLP tokens (List<CustomToken>) phase and getting subjects, objects and action-verbs of a sentence (CustomSentence)  
 * @pre-condition 
 * @author Edgar
 *
 */
public final class PosTagImprover {
	
	public static final String EMPTY_CHAR = "";
	public static final String WHITE_SPACE = " ";
	public static final String PREPOSITION_OF = "of";
	public static final String VERB_TO_HAVE = "have";
	public static final String VERB_TO_BE = "be";
	public static final String S_CHAR = "s";
	public static final String POSSESSIVE = "'s";

	/**
	 * @param tokens
	 * @param start from index
	 * @param end to index
	 * @return
	 */
	private static String getPosTagsAsString(List<CustomToken> tokens, int start, int end) {
		//Pos tags
		int currentElement = 0;
		StringBuilder tags = new StringBuilder("");
		if(start >= 0 && end <= tokens.size() && start <= end) {
			for(int i = start; i < end; i++) {
	    		CustomToken token = tokens.get(i) ;
	    		if (currentElement == 0)
	    			tags.append(token.getPosTag());
	    		else
	    			tags.append(" " +token.getPosTag());
	    		currentElement++;
	    	}
		} else {
			for(int i = 0; i < tokens.size(); i++) {
	    		CustomToken token = tokens.get(i) ;
	    		if (currentElement == 0)
	    			tags.append(token.getPosTag());
	    		else
	    			tags.append(" " +token.getPosTag());
	    		currentElement++;
	    	}
		} 
	
    	return tags.toString();
	}
	
	/**
	* @Title: Adjust NOUN/VERB POS TAGS
	* @Goal: Return Tokens from a sentence with adjusted NOUNS/VERBS by applying Rules to adjust the accuracy of POS Tagger
	* @Context:
		- Noun POS tags: 
			- NN Noun, sing. or mass      dog
			- NNS Noun, plural            dogs
		- Action-Verb POS tags: 
				- VB verb, base form          	  eat
				- VBP Verb, infinitive verb       eat
				- VBZ Verb, present tense         eats
	* @Actor: C&l 
	* @Resource: tokens, SpecialVerb.NOUNS_AND_VERBS_HASH
	**/
	public static List<CustomToken> adjustPosTags(List<CustomToken> tokens){
		
		tokens = adjustNounPosTags(tokens);
		tokens = adjustVerbPosTags(tokens);
		tokens = adjustPrePositionPosTags(tokens);
		tokens = adjustAdjectivePosTags(tokens);
		tokens = adjustUseCaseKeywords(tokens);
		
		return tokens;
	}
	
	
	
	public static List<CustomToken> updatePosTagWithVerb(List<CustomToken> tokens, CustomToken noun) {
		//Pre-process nouns

		int tokenIndex = noun.getIndex();//Index in Tokens returned by NLP tool
		String posTag = PosTagEnum.VB.name(); //base form
		if (noun.getPosTag().equals(PosTagEnum.NN.name())) {
			if(tokenIndex >  0){
				posTag = PosTagEnum.VBP.name(); //present tense
			}
		} else { //NNS
			posTag = PosTagEnum.VBZ.name(); //present tense
		}
		noun.setPosTag(posTag);
		tokens.set(tokenIndex, noun);
		
		return tokens;
	}
	
	public static List<CustomToken> updatePosTagPrepositionWithVerb(List<CustomToken> tokens, CustomToken preposition) {
		//Pre-process nouns

		int tokenIndex = preposition.getIndex();//Index in Tokens returned by NLP tool
		String posTag = PosTagEnum.VB.name(); //base form
		if(tokenIndex >  0){
				posTag = PosTagEnum.VBP.name(); //present tense
			
		} 
		preposition.setPosTag(posTag);
		tokens.set(tokenIndex, preposition);
		
		return tokens;
	}
	
	public static List<CustomToken> updatePosTagVerbWithAdjective(List<CustomToken> tokens, CustomToken verb) {
		//Pre-process nouns

		int tokenIndex = verb.getIndex();//Index in Tokens returned by NLP tool
		String posTag = PosTagEnum.JJ.name(); //base form
		verb.setPosTag(posTag);
		tokens.set(tokenIndex, verb);
		
		return tokens;
	}
	
	/**
	* 
	* @Title: Adjust NOUN POS TAGS
	* @Goal: Return Tokens from a sentence with adjusted NOUNS by applying Rules to adjust the accuracy of POS Tagger
	* @Context:
		- Noun POS tags: 
			- NN Noun, sing. or mass      dog
			- NNS Noun, plural            dogs
		
	* @Actor: C&l 
	* @Resource: tokens, SpecialVerb.NOUNS_AND_VERBS_HASH
	**/
	public static List<CustomToken> adjustNounPosTags(List<CustomToken> tokens){ 
		
		List<CustomToken> allNouns = new ArrayList<>(); 

		if (tokens != null && !tokens.isEmpty()) {
			//Get all nouns
			for(int i = 0; i < tokens.size(); i++ ){
				if (tokens.get(i).getPosTag().equals(PosTagEnum.NN.name()) || tokens.get(i).getPosTag().equals(PosTagEnum.NNS.name())){
					
					CustomToken tmpNoun = new CustomToken(tokens.get(i).getWord(), tokens.get(i).getPosTag(), tokens.get(i).getStem(), i) ;
					allNouns.add(tmpNoun);
					
				}
			}
			//PTR1: Check that a 'Noun' is effectively a 'Noun'. Prepositions are most commonly followed by a 'Noun' phrase or 'Pronoun'
			for(int i = 0; i < allNouns.size(); i++ ){
				CustomToken noun = allNouns.get(i);
				//GIVEN (Antecedent)
				if(SpecialVerb.NOUNS_AND_VERBS_HASH.containsKey(noun.getStem())) {
					//WHEN
					//Previous Tokens (contains POS tags:)
					String rtags = ".*((DT|PDT|IN|POS|PRP\\$|JJ.?)|(IN(\\s+DT)?\\s+NN.?))$";
					String prevPOSs = getPosTagsAsString(tokens, 0, noun.getIndex());
					//Next Tokens (contains POS tags:)
					if(prevPOSs.matches(rtags)) {
						//THEN (Adjust Token)
						tokens.get(allNouns.get(i).getIndex()).setConfirmedNoun(true);
						
					}	
				}
			}

			
			//PTR2: Check that a 'Noun' is effectively a 'Noun'. The 'Noun' position is the first or after a coordinating conjunction (CC)  
			for(int i = 0; i < allNouns.size(); i++ ){
				CustomToken noun = allNouns.get(i);
				//GIVEN (Antecedent)
				if(SpecialVerb.NOUNS_AND_VERBS_HASH.containsKey(noun.getStem()) && !tokens.get(allNouns.get(i).getIndex()).isConfirmedNoun()) {
					//WHEN
					//Previous Tokens (contains POS tags:)
					
					String regexprevpostags = "(^|.*CC)$";
					String prevPOSs = getPosTagsAsString(tokens, 0, noun.getIndex());
					//Next Tokens (contains POS tags:)
					String regexnextpostags = "^(NN.?\\s+(NN.?|VB.?)).*";
					String nextPOSs = getPosTagsAsString(tokens, noun.getIndex()+1, tokens.size());
					if(prevPOSs.matches(regexprevpostags) && nextPOSs.matches(regexnextpostags)) {
						//THEN (Adjust Token)
						if(noun.getIndex() + 2 < tokens.size() && tokens.get(noun.getIndex() + 2) != null) {
							CustomToken nextToken =  tokens.get(allNouns.get(i).getIndex() + 2);
							String singularNounNext = nextToken.getStem();
							if(nextToken.getPosTag().contains(PosTagEnum.VB.name()) || SpecialVerb.NOUNS_AND_VERBS_HASH.containsKey(singularNounNext)) {
								tokens.get(allNouns.get(i).getIndex()).setConfirmedNoun(true);
								
							}	
						} 											
					}
				}
			}
			

			
			for(int i = 0; i < allNouns.size(); i++ ){
				CustomToken noun = allNouns.get(i);
			
				if(SpecialVerb.NOUNS_AND_VERBS_HASH.containsKey(noun.getStem())) {
					String regexprevpostags = ".*((VB|VBP|VBZ|VBD|VBN)(\\s+(DT|PDT|IN|NN.?|RB))+)$";
					String prevPOSs =  getPosTagsAsString(tokens, 0, noun.getIndex());
					prevPOSs = prevPOSs.trim();
					String regexnextpostags = "^(TO\\s+(DT|PDT|IN|NN.?|PRP|JJ.?)).*";
					String nextPOSs =  getPosTagsAsString(tokens, noun.getIndex()+1, tokens.size());
					nextPOSs  = nextPOSs.trim();
					if(prevPOSs.matches(regexprevpostagsS) && nextPOSs.matches(regexnextpostags)) {
			
						tokens.get(allNouns.get(i).getIndex()).setConfirmedNoun(true);
									
					}												
				}
			}
			
			
			allNouns = new ArrayList<>(); 
			for(int i = 0; i < tokens.size(); i++ ){
				if (tokens.get(i).getPosTag().contains(PosTagEnum.NN.name())){
			
					CustomToken tmpNoun = new CustomToken(tokens.get(i).getWord(), tokens.get(i).getPosTag(), tokens.get(i).getStem(), i) ;
					allNouns.add(tmpNoun);
					
				}
			}
			
			for(int i = 0; i < allNouns.size(); i++ ){
				CustomToken noun = allNouns.get(i);
			
				if(SpecialVerb.NOUNS_AND_VERBS_HASH.containsKey(noun.getStem())) {
					//WHEN
					//Previous Tokens (contains POS tags:)
					
					
					//Next Tokens (contains POS tags:)
					String regexnextpostags = "^(VBG).*";
					String nextPOSs =   getPosTagsAsString(tokens, noun.getIndex()+1, tokens.size());
					if(nextPOSs.matches(regexnextpostags)) {
						//THEN (Adjust Token)
						tokens.get(allNouns.get(i).getIndex()).setConfirmedNoun(true);
						
					}												
				}
			}
			
			
			//PTR5:	Check that a 'Noun' is effectively a 'Noun'. The 'Noun' is preceded by a Verb + Determiner + Noun or Adjective
			for(int i = 0; i < allNouns.size(); i++ ){
				CustomToken noun = allNouns.get(i);
				//GIVEN (Antecedent)
				if(SpecialVerb.NOUNS_AND_VERBS_HASH.containsKey(noun.getStem()) && !tokens.get(allNouns.get(i).getIndex()).isConfirmedNoun()) {
					//WHEN
					//Previous Tokens (contains POS tags:)
					String regexprevpostags = ".*((VB|VBZ|VBP|VBD|VBN)(\\s+(PDT|DT))?(\\s+(NN.?|JJ.?))*)$";
					String prevPOSs =   getPosTagsAsString(tokens, 0, noun.getIndex());
					//Next Tokens (contains POS tags:)
					if(prevPOSs.matches(regexprevpostags)) {
						//THEN (Adjust Token)
						if(noun.getIndex() -1 > 0 && tokens.get(noun.getIndex() - 1) != null) {
							String singularNounPrev = tokens.get(allNouns.get(i).getIndex() - 1).getStem();
							if(!SpecialVerb.NOUNS_AND_VERBS_HASH.containsKey(singularNounPrev)) {
								tokens.get(allNouns.get(i).getIndex()).setConfirmedNoun(true); 
								
							}	
						} 
					}												
				}				
			}
			
			//PTR6: Check that a 'Noun' is a 'Verb'. The 'Noun' position is after a token, which is not a coordinating conjunction (CC) 
			for(int i = 0; i < allNouns.size(); i++ ){
				CustomToken noun = allNouns.get(i);
				//GIVEN (Antecedent)
				if(SpecialVerb.NOUNS_AND_VERBS_HASH.containsKey(noun.getStem()) && !tokens.get(allNouns.get(i).getIndex()).isConfirmedNoun()
						&& noun.getIndex() > 0) {
					//WHEN
					//Previous Tokens (contains POS tags:)
					String regexprevpostags = ".*([^(CC)])$";
					String prevPOSs = getPosTagsAsString(tokens, 0, noun.getIndex());
					String regexnextpostags = "^(DT|PDT|IN|NN.?|PRP.?|RB.?|JJ.?|VB.?|CD).*";
					String nextPOSs = getPosTagsAsString(tokens, noun.getIndex()+1, tokens.size());
					if(prevPOSs.matches(regexprevpostags) && nextPOSs.matches(regexnextpostags)) {
						//THEN (Adjust Token)
						String singularNounPrev = tokens.get(all.get(i).getIndex()-1).getStem();
						if (!SpecialVerb._AND_VERBS_HASH.containsKey(singularNounPrev) 
								&& !tokens.get(all.get(i).getIndex()-1).getPosTag().contains(PosTagEnum.VB.name())){
							tokens = updatePosTagWithVerb(tokens, noun);
							
							//System.out.println("PTR6 Token: " + noun.getStem());
							//System.out.println("PTR6 Token is Verb!");//FIxed in PTR14: System presents a registration data form/VBP and asks to enter the registration data
						}						
					}
				}
			}

			//Update all nouns
			allNouns = new ArrayList<>(); 
			for(int i = 0; i < tokens.size(); i++ ){
				if (tokens.get(i).getPosTag().contains(PosTagEnum.NN.name())){
					
					CustomToken tmpNoun = new CustomToken(tokens.get(i).getWord(), tokens.get(i).getPosTag(), tokens.get(i).getStem(), i) ;
					allNouns.add(tmpNoun);
				}
			}
			
			//PTR7: Check that a 'Noun' is a 'Verb'. The 'Noun' position is the first or after a coordinating conjunction (CC)  
			for(int i = 0; i < allNouns.size(); i++ ){
				CustomToken noun = allNouns.get(i);
				//GIVEN (Antecedent)
				if(SpecialVerb.NOUNS_AND_VERBS_HASH.containsKey(noun.getStem()) && !tokens.get(allNouns.get(i).getIndex()).isConfirmedNoun()) {
					//WHEN
					//Previous Tokens (contains POS tags:)
					String regexprevpostags = "(^|.*CC)$";
					String prevPOSs =  getPosTagsAsString(tokens, 0, noun.getIndex());
					//Next Tokens (contains POS tags:)
					
					String regexnextpostags = "^(((IN|RP)\\s+)?(NN.?|DT|PDT|JJ.?|VBD|VBN|RB.?|PRP.?)).*";
										
					
					
					String nextPOSs =  getPosTagsAsString(tokens, noun.getIndex()+1, tokens.size());
					if(prevPOSs.matches(regexprevpostags) && nextPOSs.matches(regexnextpostags)) {
						//THEN (Adjust Token)
						tokens = updatePosTagWithVerb(tokens, noun);												
					}
				}
			}
			
			//Update all nouns
			allNouns = new ArrayList<>(); 
			for(int i = 0; i < tokens.size(); i++ ){
				if (tokens.get(i).getPosTag().contains(PosTagEnum.NN.name())){
					
					CustomToken tmpNoun = new CustomToken(tokens.get(i).getWord(), tokens.get(i).getPosTag(), tokens.get(i).getStem(), i) ;
					allNouns.add(tmpNoun);
				}
			}
					
			
			//PTR8: Check that a 'Noun' is a 'Verb'. The 'Noun' position is the first followed by a preposition or particle  
			for(int i = 0; i < allNouns.size(); i++ ){
				CustomToken noun = allNouns.get(i);
				//GIVEN (Antecedent)
				if(SpecialVerb.NOUNS_AND_VERBS_HASH.containsKey(noun.getStem()) && !tokens.get(allNouns.get(i).getIndex()).isConfirmedNoun()) {
					//WHEN
					//Previous Tokens (contains POS tags:)
					String regexprevpostags = "^";
					String prevPOSs =  getPosTagsAsString(tokens, 0, noun.getIndex());
					//Next Tokens (contains POS tags:)
					String regexnextpostags = "(((IN|RP)?$)|(((IN|RP)\\s+)?TO.*))";
					//String REGEX_NEXT_POS_TAGS = "^(IN|RP)?(\\s+[^(VB|VBP|VBZ)])*";
					String nextPOSs =  getPosTagsAsString(tokens, noun.getIndex()+1, tokens.size());
					if(prevPOSs.matches(regexprevpostags) && nextPOSs.matches(regexnextpostags)) {
						//THEN (Adjust Token)
						tokens = updatePosTagWithVerb(tokens, noun);
							
					}
				}
			}
			
			//Update all nouns
			allNouns = new ArrayList<>(); 
			for(int i = 0; i < tokens.size(); i++ ){
				if (tokens.get(i).getPosTag().contains(PosTagEnum.NN.name())){
					
					CustomToken tmpNoun = new CustomToken(tokens.get(i).getWord(), tokens.get(i).getPosTag(), tokens.get(i).getStem(), i) ;
					allNouns.add(tmpNoun);
					
				}
			}
			
			//PTR9:	Check that a 'Noun' is a 'Verb'. The 'Noun' position is the last or before a coordinating conjunction (CC)
			for(int i = 0; i < allNouns.size(); i++ ){
				CustomToken noun = allNouns.get(i);
				//GIVEN (Antecedent)
				if(SpecialVerb.NOUNS_AND_VERBS_HASH.containsKey(noun.getStem()) && !tokens.get(allNouns.get(i).getIndex()).isConfirmedNoun()) {
					//WHEN
					//Previous Tokens (contains POS tags:)
					String regexprevpostags = ".*(((PDT|DT)\\s+)?((NN.?|JJ.?|VBD|VBN)\\s+)*(NN.?))$";
					String prevPOSs =    getPosTagsAsString(tokens, 0, noun.getIndex());
					//Next Tokens (contains POS tags:)
					String regexnextpostags = "^($|CC\\s+(VB|VBP|VBZ).*)"; //include , ; ex. user register, update or delete transactions
					String nextPOSs =    getPosTagsAsString(tokens, noun.getIndex()+1, tokens.size());
					if(prevPOSs.matches(regexprevpostags) && nextPOSs.matches(regexnextpostags) ) {
						//THEN (Adjust Token)
						if(noun.getIndex() - 1 >= 0 && tokens.get(noun.getIndex() - 1) != null) {
							String singularNounPrev = tokens.get(allNouns.get(i).getIndex() - 1).getStem();
							if(!SpecialVerb.NOUNS_AND_VERBS_HASH.containsKey(singularNounPrev)) {
								tokens = updatePosTagWithVerb(tokens, noun);
								
							}	
						} 
					}												
				}				
			}
			

		}

		return tokens;
	}
	
	
	
	/**
	* 
	* @Title: Adjust VERB POS TAGS
	* @Goal: Return Tokens from a sentence with adjusted VERBS by applying Rules to adjust the accuracy of POS Tagger
	* @Context:
		- Action-Verb POS tags: 
				- VB verb, base form          	  eat
				- VBP Verb, infinitive verb       eat
				- VBZ Verb, present tense         eats
	* @Actor: C&l 
	* @Resource: tokens, NOUNS_AND_VERBS_HASH
	**/
	public static List<CustomToken> adjustVerbPosTags(List<CustomToken> tokens){ 
		List<CustomToken> allVerbs = new ArrayList<>();

		if (tokens != null && !tokens.isEmpty()) {
			//Get all verbs 
			for(int i = 0; i < tokens.size(); i++ ){
				if (tokens.get(i).getPosTag().equals(PosTagEnum.VB.name()) || tokens.get(i).getPosTag().equals(PosTagEnum.VBP.name()) || tokens.get(i).getPosTag().equals(PosTagEnum.VBZ.name())){
					//i: position in the analysis tokens array;
					CustomToken tmpVerb = new CustomToken(tokens.get(i).getWord(), tokens.get(i).getPosTag(), tokens.get(i).getStem(), i) ;
					if(tokens.get(i).getWord().length() > 1)//???
						allVerbs.add(tmpVerb);

				}
			}

			//PTR10: Check that a 'Verb' is effectively a 'Verb'. Prepositions are most commonly followed by a 'Noun' phrase or 'Pronoun'   
			for(int i = 0; i < allVerbs.size(); i++ ){
				CustomToken verb = allVerbs.get(i);
				//GIVEN (Antecedent)
				if(SpecialVerb.NOUNS_AND_VERBS_HASH.containsKey(verb.getStem())) {
					//WHEN
					//Previous Tokens (contains POS tags:)
					String regexprevpostags = ".*(DT|PDT|IN|POS|PRP\\$|JJ.?)$";
					String prevPOSs = getPosTagsAsString(tokens, 0, verb.getIndex());
					//Next Tokens (contains POS tags:)
				
					if(prevPOSs.matches(regexprevpostags) ) {
						//THEN (Adjust Token)
						tokens.get(verb.getIndex()).setConfirmedNoun(true);
						tokens = updatePosTagWithNoun(tokens, verb);
													
					}

					
				}
			}
			
			//PTR11: Check that a 'Verb' is effectively a 'Verb'. The 'Verb' position is after a token, which is not a coordinating conjunction (CC)    
			for(int i = 0; i < allVerbs.size(); i++ ){
				CustomToken verb = allVerbs.get(i);
				//GIVEN (Antecedent)
				if(SpecialVerb.NOUNS_AND_VERBS_HASH.containsKey(verb.getStem())) {
					//WHEN
					String regexprevpostags = ".*([^(CC)])$";
					String prevPOSs = getPosTagsAsString(tokens, 0, verb.getIndex());
					//Next Tokens (contains POS tags:)
					String regexnextpostags = "^(DT|PDT|IN|NN.?|PRP.?|RB.?|JJ.?|VB.?).*";
					String nextPOSs =  getPosTagsAsString(tokens, verb.getIndex()+1, tokens.size());
					if(prevPOSs.matches(REGEX_PREV_POS_TAGS) && nextPOSs.matches(REGEX_NEXT_POS_TAGS) ) {
						//THEN (Adjust Token)
						String singularNounPrev = tokens.get(allVerbs.get(i).getIndex()-1).getStem();
						if (!SpecialVerb.NOUNS_AND_VERBS_HASH.containsKey(singularNounPrev) ){
							tokens.get(allVerbs.get(i).getIndex()).setConfirmedVerb(true);
						
						}						
					}

					
				}
			}
			
			//PTR12: Check that a 'Verb' is effectively a 'Verb'. The 'Verb' is preceded by a Noun and followed by 'TO' + Verb    
			for(int i = 0; i < allVerbs.size(); i++ ){
				CustomToken verb = allVerbs.get(i);
				//GIVEN (Antecedent)
				if(SpecialVerb.NOUNS_AND_VERBS_HASH.containsKey(verb.getStem())) {
					//WHEN
					String regexprevpostags = ".*(PRP|NN.?)$";
					String prevPOSs = getPosTagsAsString(tokens, 0, verb.getIndex());
					//Next Tokens (contains POS tags:)
					String regexnextpostags = "^((TO\\s+)(VB.?|NN.?)).*"; 
					String nextPOSs =  getPosTagsAsString(tokens, verb.getIndex()+1, tokens.size());
					if(prevPOSs.matches(regexprevpostags) && nextPOSs.matches(regexnextpostags)) {
						//THEN (Adjust Token)
						String singularNounPrev = tokens.get(allVerbs.get(i).getIndex()-1).getStem();
						String singularVerbNext = tokens.get(allVerbs.get(i).getIndex()+2).getStem();
						if (!SpecialVerb.NOUNS_AND_VERBS_HASH.containsKey(singularNounPrev) && SpecialVerb.NOUNS_AND_VERBS_HASH.containsKey(singularVerbNext)){
							tokens.get(allVerbs.get(i).getIndex()).setConfirmedVerb(true);
							
						}						
					}

					
				}
			}
			
						
			//PTR13: Check that a 'Verb' followed by "OF" or TO_BE or TO_HAVE is a 'Noun'  
			for(int i = 0; i < allVerbs.size(); i++ ){
				CustomToken verb = allVerbs.get(i);
				//GIVEN (Antecedent)
				if(SpecialVerb.NOUNS_AND_VERBS_HASH.containsKey(verb.getStem())) {
					//WHEN
					//Previous Tokens (contains POS tags:)
				
					//Next Tokens (contains POS tags:)
					String regexnextpostags = "^(IN|VB.?).*"; 
					String nextPOSs =  getPosTagsAsString(tokens, verb.getIndex()+1, tokens.size());
					if(nextPOSs.matches(regexnextpostags)) {
						//THEN (Adjust Token)
						String nextWord = tokens.get(allVerbs.get(i).getIndex() + 1).getStem();
						if (nextWord.equals(PREPOSITION_OF) || nextWord.equals(VERB_TO_BE) || nextWord.equals(VERB_TO_HAVE)){ 
							tokens = updatePosTagWithNoun(tokens, verb);
							logger.log("PTR13 Token: " + verb.getStem());
							logger.log("PTR13 Token is Noun!");
							/**
							 * Exception
							 * I don't approve of hunting animals for their fur.
								Our dog died of old age.
								This shampoo smells of bananas.
								https://learnenglish.britishcouncil.org/grammar/intermediate-to-upper-intermediate/verbs-and-prepositions
								https://7esl.com/verb-preposition-combinations/

							 */
						}						
					}

				}
			}
			
			//PTR14: Check that a 'Verb' is a 'Noun'. The 'Verb' position is the last or before a coordinating conjunction (CC)  
			for(int i = 0; i < allVerbs.size(); i++ ){
				CustomToken verb = allVerbs.get(i);
				//GIVEN (Antecedent)
				if(SpecialVerb.NOUNS_AND_VERBS_HASH.containsKey(verb.getStem())) {
					//WHEN
					//Previous Tokens (contains POS tags:)
					
					String regexprevpostags = ".*((VB|VBZ|VBP)\\s+((PDT|DT)\\s+)?((DT|IN|TO|NN.?|JJ.?|VBD|VBN)\\s+)*NN.?)$";//PDT?
					String prevPOSs = getPosTagsAsString(tokens, 0, verb.getIndex());
					//Next Tokens (contains POS tags:)
					String regexnextpostags = "^($|(CC\\s+[^(VB|VBP|VBZ)]))"; 
					String nextPOSs =  getPosTagsAsString(tokens, verb.getIndex()+1, tokens.size());
					if(prevPOSs.matches(regexprevpostags) && nextPOSs.matches(regexnextpostags)) {
						//THEN (Adjust Token)
						tokens = updatePosTagWithNoun(tokens, verb);
																	
					}

				}
			}
		}

		return tokens;
	}
	public static List<CustomToken> updatePosTagWithNoun(List<CustomToken> tokens, CustomToken verb) {
		//Pre-process verbs

		int tokenIndex = verb.getIndex();//Index in Tokens returned by NLP tool
		String posTag = PosTagEnum.NN.name(); //singular
		if (verb.getPosTag().equals(PosTagEnum.VBZ.name())) {
			posTag = PosTagEnum.NNS.name(); //plural
		}
		verb.setPosTag(posTag);
		tokens.set(tokenIndex, verb);
		
		return tokens;
	}
	
	/**
	* Check that  a PREPOSITION is a VERB
	* @Title: Adjust VERB POS TAGS
	* @Goal: Return Tokens from a sentence with adjusted VERBS by applying Rules to adjust the accuracy of POS Tagger
	* @Context:
		- Action-Verb POS tags: 
				- VB verb, base form          	  eat
				- VBP Verb, infinitive verb       eat
				- VBZ Verb, present tense         eats
	* @Actor: C&l 
	* @Resource: tokens
	**/
	public static List<CustomToken> adjustPrePositionPosTags(List<CustomToken> tokens){ 
		if (tokens != null && !tokens.isEmpty()) {

			//Check PREPOSITIONS that are VERBS
			//Ex. user really like it -> NN RB IN (NN& | PRP& W&) -> IN is VB
			//FIX: Complex verbs? -> carry out, move up? 		
			//Get all Prepositions 
			List<CustomToken> allPrepositions = new ArrayList<>();
			for(int i = 0; i < tokens.size(); i++ ){
				if (tokens.get(i).getPosTag().equals(PosTagEnum.IN.name())){
					
					CustomToken tmpPrep = new CustomToken(tokens.get(i).getWord(), tokens.get(i).getPosTag(), tokens.get(i).getStem(), i) ;
					if(tokens.get(i).getWord().length() > 1)//???
						allPrepositions.add(tmpPrep);

				}
			}
			
			//PTR15: Check that a 'Preposition' is a 'Verb'. The 'Preposition' position is the first or after a coordinating conjunction (CC)  
			for(int i = 0; i < allPrepositions.size(); i++ ){
				CustomToken preposition = allPrepositions.get(i);
				//GIVEN (Antecedent)
				if(SpecialVerb.NOUNS_AND_VERBS_HASH.containsKey(preposition.getStem())) {
					//WHEN
					//Previous Tokens (contains POS tags:)
					String regexprevpostags = "(^|.*CC)$";
					String prevPOSs = getPosTagsAsString(tokens, 0, preposition.getIndex());
					//Next Tokens (contains POS tags:)
					String regexnextpostags = "^(NN.?|DT|PDT|JJ.?).*"; 
					String nextPOSs =  getPosTagsAsString(tokens, preposition.getIndex()+1, tokens.size());
					if(prevPOSs.matches(regexprevpostags) && nextPOSs.matches(regexnextpostags)) {
						//THEN (Adjust Token)
						tokens = updatePosTagPrepositionWithVerb(tokens, preposition);
											
					}

				}
			}
			
			//PTR16: Check that a 'Preposition' is a 'Verb'. The 'Preposition' position is after a token, which is not a coordinating conjunction (CC)  
			for(int i = 0; i < allPrepositions.size(); i++ ){
				CustomToken preposition = allPrepositions.get(i);
				//GIVEN (Antecedent)
				if(SpecialVerb.NOUNS_AND_VERBS_HASH.containsKey(preposition.getStem())) {
					//WHEN
					//Previous Tokens (contains POS tags:)
					String regexprevpostags = ".*(PRP|NN.?|RB.?)$";
					String prevPOSs = getPosTagsAsString(tokens, 0, preposition.getIndex());
					//Next Tokens (contains POS tags:)
					 
					String regexnextpostags = "^(NN.?|PRP.?|WDT|WP.?|WRB).*";
					String nextPOSs =  getPosTagsAsString(tokens, preposition.getIndex()+1, tokens.size());
					if(prevPOSs.matches(regexprevpostags) && nextPOSs.matches(regexnextpostags) ) {
						//THEN (Adjust Token)
						tokens = updatePosTagPrepositionWithVerb(tokens, preposition);
											
					}

				}
			}
		}
		
		return tokens;
	}
	
	/**
	* 
	* Check that an ADJECTIVE is a VERBS (user/NN select/JJ option/NN for/IN editing/VBG -> user/NN select/VB option/NN for/IN editing/VBG) 
	* <br>We use the -ing and -ed forms of regular and irregular verbs as adjectives
	* <br> https://dictionary.cambridge.org/grammar/british-grammar/about-adjectives-and-adverbs/adjectives-forms
	* @Title: Adjust ADJECTIVE POS TAGS
	* @Goal: Return Tokens from a sentence with adjusted NOUNS by applying Rules to adjust the accuracy of POS Tagger
	* @Context:
		- JJ POS tags: 
			- JJ adjective 	interesting, interested
		
	* @Actor: C&l 
	* @Resource: tokens
	**/
	public static List<CustomToken> adjustAdjectivePosTags(List<CustomToken> tokens){ 
		if (tokens != null && !tokens.isEmpty()) {
			List<CustomToken> allAdjectives = new ArrayList<>();
			List<CustomToken> allVerbs = new ArrayList<>();//verbs with -ing and -ed
			for(int i = 0; i < tokens.size(); i++ ) {
				//Check that a VERB is an ADJECTIVE
				//Get all verbs with -ing and -ed - filter VBN (have + verb)
				if (tokens.get(i).getPosTag().equals(PosTagEnum.VBG.name()) || tokens.get(i).getPosTag().equals(PosTagEnum.VBD.name()) || tokens.get(i).getPosTag().equals(PosTagEnum.VBN.name())){ //VBN : error from CoreNLP => all/DT required/VBN ?????
					
					CustomToken tmpVerb = new CustomToken(tokens.get(i).getWord(), tokens.get(i).getPosTag(), tokens.get(i).getStem(), i) ;
					if(tokens.get(i).getWord().length() > 1)//???
						allVerbs.add(tmpVerb);
				}
								
				//Check that an ADJECTIVE is a VERB (user/NN select/JJ option/NN for/IN editing/VBG -> user/NN select/VB option/NN for/IN editing/VBG)
				//STANFORD ERROR: For Example "User select option for editing" -> "User/VB select/JJ option/NN for/IN editing/VBG "
				else if (tokens.get(i).getPosTag().equals(PosTagEnum.JJ.name())){//Error CoreNLP
					
					CustomToken tmpAdjective = new CustomToken(tokens.get(i).getWord(), tokens.get(i).getPosTag(), tokens.get(i).getStem(), i) ;
					if(tokens.get(i).getWord().length() > 1)//???
						allAdjectives.add(tmpAdjective);
				}
				
			}
			//PTR16: Check that a 'Adjective' is effectively a 'Adjective'. Prepositions are most commonly followed by a 'Noun' phrase or 'Pronoun' or Adjective   
			for(int i = 0; i < allAdjectives.size(); i++ ){
				CustomToken verb = allAdjectives.get(i);
				//GIVEN (Antecedent)
				if(SpecialVerb.NOUNS_AND_VERBS_HASH.containsKey(verb.getStem())) {
					//WHEN
					//Previous Tokens (contains POS tags:)
					String regexprevpostags = ".*(DT|PDT|IN|POS|PRP\\$|JJ.?)$";
					String prevPOSs = getPosTagsAsString(tokens, 0, verb.getIndex());
					//Next Tokens (contains POS tags:)
					
					if(prevPOSs.matches(regexprevpostags) ) {
						//THEN (Adjust Token)
						tokens.get(verb.getIndex()).setConfirmedAdjective(true);
											
					}

					
				}
			}
			//PTR18: Check that an 'Adjective' is a 'Verb'. Modifiers are most commonly followed by adjectives
  			for(int i = 0; i < allAdjectives.size(); i++ ){
				CustomToken adjective = allAdjectives.get(i);
				//GIVEN (Antecedent)
				if(SpecialVerb.NOUNS_AND_VERBS_HASH.containsKey(adjective.getStem()) && !tokens.get(allAdjectives.get(i).getIndex()).isConfirmedAdjective()) {
					//WHEN
					//Previous Tokens (contains POS tags:)
					String regexprevpostags = ".*(([^(VB.?)]|^))$";
					String prevPOSs = getPosTagsAsString(tokens, 0, adjective.getIndex());
					//Next Tokens (contains POS tags:)
					 
					String regexnextpostags = "^(DT|PDT|IN|NN.?|PRP.?|JJ.?|RB.?|VBD).*";
					String nextPOSs =  getPosTagsAsString(tokens, adjective.getIndex()+1, tokens.size());
					if(prevPOSs.matches(regexprevpostags) && nextPOSs.matches(regexnextpostags) ) {
						//THEN (Adjust Token)
						tokens = updatePosTagPrepositionWithVerb(tokens, adjective);
																	
					}

				}
			}
  			
  			//PTR19: Check that a 'Verb' is an 'Adjective'. Adjectives are made from regular and irregular verbs by adding the suffixes -ing and -ed. 
  			for(int i = 0; i < allVerbs.size(); i++ ){
				CustomToken verb = allVerbs.get(i);
				//GIVEN (Antecedent)
					//WHEN
					//Previous Tokens (contains POS tags:)
					String regexprevpostags = ".*(DT|PDT|JJ.?|IN)$";
					String prevPOSs = getPosTagsAsString(tokens, 0, verb.getIndex());
					//Next Tokens (contains POS tags:)
					
					if(prevPOSs.matches(regexprevpostags)) {
						//THEN (Adjust Token)
						if(!tokens.get(allVerbs.get(i).getIndex() - 1).getPosTag().equals(PosTagEnum.IN.name()) && !verb.getPosTag().equals(PosTagEnum.VBG.name())) {
							tokens.get(verb.getIndex()).setConfirmedAdjective(true);
							tokens = updatePosTagVerbWithAdjective(tokens, verb);
							
						}
					}

			}
			
  			//PTR20: Check that a 'Verb' with -ing and -ed is an 'Adjective'. The 'Adjective' is preceded by a Verb (+ Determiner + Noun or Adjective)
  			for(int i = 0; i < allVerbs.size(); i++ ){
				CustomToken verb = allVerbs.get(i);
				//GIVEN (Antecedent)
					//WHEN
					//Previous Tokens (contains POS tags:)
					String regexprevpostags = ".*((VB|VBZ|VBP)(\\s+(PDT|DT))?(\\s+(NN.?|JJ.?|RB.?))*)$";
					String prevPOSs = getPosTagsAsString(tokens, 0, verb.getIndex());
					//Next Tokens (contains POS tags:)
					String regexnextpostags = "^(NN.?).*"; 
					String nextPOSs =  getPosTagsAsString(tokens, verb.getIndex()+1, tokens.size());
					
					if(prevPOSs.matches(regexprevpostags) && nextPOSs.matches(regexnextpostags)) {
						//THEN (Adjust Token)
						if((verb.getPosTag().matches("(VBD|VBN)") && tokens.get(allVerbs.get(i).getIndex() - 1).getStem().equals(VERB_TO_HAVE))
								|| (verb.getPosTag().equals(PosTagEnum.VBG.name()) && tokens.get(allVerbs.get(i).getIndex() - 1).getStem().equals(VERB_TO_BE)) ) {
							tokens.get(verb.getIndex()).setConfirmedVerb(true);
							
						} else {
							tokens.get(verb.getIndex()).setConfirmedAdjective(true);
							tokens = updatePosTagVerbWithAdjective(tokens, verb);
							
						}
					}

			}
  			
		}
		return tokens;
	}
	

	/**
	* Adjust special USE CASE KEYWORDS: 
	* <br>Check that a ADVERB is a VERB: back/RB to/TO step/VB 1/CD
	* <br>Check that a NOUN is a VERB: return/NN to/TO step/VB 1/CD
	* <br>Check that a VERB is a NOUN: go/VB to/TO step/VB 1/CD
	* @Title: Adjust VERB and NOUN POS TAGS
	* @Goal: Return Tokens from a sentence with adjusted VERBS and NOUNS by applying Rules to adjust the accuracy of POS Tagger

	* @Resource: tokens
	**/
	public static List<CustomToken> adjustUseCaseKeywords(List<CustomToken> tokens){ 
		//Search for structures like NOUN + ["to"] + VERB + CARDINAL NUMBER
		// IF NOUN is NOUN_AND_VERB THEN NOUN is a VERB and VERB is a NOUN
		// Ex.return/NN to/TO step/VB 1/CD

		//Search for structures like ADVERB + ["to"] + VERB + CARDINAL NUMBER
		// IF ADVERB is NOUN_AND_VERB THEN ADVERB is a VERB  and VERB is a NOUN
		// Ex. back/RB to/TO step/VB 1/CD

		//Search for structures like VERB + ["to"] + VERB + CARDINAL NUMBER
		// IF VERB is NOUN_AND_VERB THEN VERB is a NOUN
		// Ex. go/VB to/TO step/VB 1/CD

		
		String regexusecasereturnkeyword = "(goto|go[e]?|back|return|resume|proceed)[s]?";

		if (tokens != null && !tokens.isEmpty()) {
			//Get all VERBS that can be NOUN_AND_VERB followed by CARDINAL_NUMBER
			List<CustomToken> allVerbsThatCanBeNouns = new ArrayList<>();
			for(int i = 0; i < tokens.size(); i++ ){
				String baseForm = getVerbBaseFromThirdPerson(tokens.get(i).getWord());
				if(SpecialVerb.NOUNS_AND_VERBS_HASH.containsKey(baseForm)) {					
					if(i < tokens.size() - 1)
						if ( (  tokens.get(i).getPosTag().equals(PosTagEnum.VB.name()) 
								|| tokens.get(i).getPosTag().equals(PosTagEnum.VBP.name()) 
								|| tokens.get(i).getPosTag().equals(PosTagEnum.VBZ.name())
								) && tokens.get(i+1).getPosTag().equals(PosTagEnum.CD.name())
								){

							CustomToken tmpVerb = new CustomToken(tokens.get(i).getWord(), tokens.get(i).getPosTag(), tokens.get(i).getStem(), i) ;
							if(tokens.get(i).getWord().length() > 1)//???
								allVerbsThatCanBeNouns.add(tmpVerb);
						}						
				}
			}

			//Check that a VERB is a NOUN
			//Update NOUN/ADVERB that is a VERB
			for(int i = 0; i < allVerbsThatCanBeNouns.size(); i++ ){
				//if before token we have "(return|back|go)" [followed by "to"] THEN current token is a NOUN (VB | VBP -> NN,  VBZ -> NNS) 

				//get previous token "(resume|return|back|go)"
				CustomToken prevToken = null;
				if(tokens.get(allVerbsThatCanBeNouns.get(i).getIndex()-1) != null 
						&& tokens.get(allVerbsThatCanBeNouns.get(i).getIndex()-1).getPosTag().equals(PosTagEnum.TO.name())
						&& tokens.get(allVerbsThatCanBeNouns.get(i).getIndex()-2) != null)
					prevToken = tokens.get(allVerbsThatCanBeNouns.get(i).getIndex()-2);
				else if(tokens.get(allVerbsThatCanBeNouns.get(i).getIndex()-1) != null)
					prevToken = tokens.get(allVerbsThatCanBeNouns.get(i).getIndex()-1);	


				if(prevToken != null ) {

					String baseForm = prevToken.getStem();
					//IF previous token is "(resume|return|back|go|goto)" THEN Update tokens
					if(baseForm.replaceAll(regexusecasereturnkeyword, EMPTY_CHAR).equals(EMPTY_CHAR)) {

						//Update current token "VERB" that is a NOUN
						CustomToken  verbAndNoun = allVerbsThatCanBeNouns.get(i);
						int tokenIndex = verbAndNoun.getIndex();//Index in Tokens returned by NLP tool
						if(tokenIndex <  tokens.size()){

							String posTag = PosTagEnum.NN.name(); //singular
							//if current token ends with 's'
							if (verbAndNoun.getWord().toLowerCase().endsWith("s"))
								posTag = PosTagEnum.NNS.name(); //plural

							//if current token ends with 'ss'
							if (verbAndNoun.getWord().toLowerCase().endsWith("ss")) //ex. address
								posTag = PosTagEnum.NN.name();

							verbAndNoun.setPosTag(posTag);
							tokens.set(tokenIndex, verbAndNoun);
						} 					 

						//Update previous token "NOUN/ADVERB" that is a VERB
						tokenIndex = prevToken.getIndex();//Index in Tokens returned by NLP tool
						if(tokenIndex <  tokens.size()){

							String posTag = PosTagEnum.VBP.name(); //present
							//if current token ends with 's'
							if (prevToken.getWord().toLowerCase().endsWith("s"))
								posTag = PosTagEnum.VBZ.name(); //third form

							//if current token ends with 'ss'
							if (prevToken.getWord().toLowerCase().endsWith("ss")) //ex. address
								posTag = PosTagEnum.VBP.name();

							if (posTag.equals(PosTagEnum.VBP.name()) && tokenIndex == 0) //first token
								posTag = PosTagEnum.VB.name();

							prevToken.setPosTag(posTag);
							tokens.set(tokenIndex, prevToken);
						} 			

					}					
				}
			}
		}

		return tokens;
	}
	


	
	/**
	 * Return base verb from a Present Tense - Third Person verb
	 * https://github.com/takafumir/javascript-lemmatizer/blob/master/js/lemmatizer.js
	 * http://www.grammar.cl/Present/Verbs_Third_Person.htm	
	 * @param word
	 * @return
	 */
	public static String getVerbBaseFromThirdPerson(String word) {
		if(word != null && !word.isEmpty()) {
			word = word.toUpperCase();
			String baseForm = word;
			//Regular verbs end with "s" or "es"
			if (word.endsWith("S")) {
				baseForm = word.substring(0, word.length() - 1);
				if (word.endsWith("CESS") || word.endsWith("SESS")) { //ex. process, access, assess, possess
					baseForm = word;
				}	
			//Irregular Verbs
				//If the base_verb ends in SS, X, CH, SH or the letter O, we add + ES in the third person.
				if (word.endsWith("OES") || word.endsWith("SSES") || word.endsWith("XES") || word.endsWith("CHES") || word.endsWith("SHES")) { //ex. goes -> go, kisses -> kiss, fixes -> fix, Watches -> Watch , Crashes -> Crash
					baseForm = word.substring(0, word.length() - 2);
				}
				//If the verb ends in a Consonant + Y, we remove the Y and + IES in the third person
				if (word.endsWith("IES")) { //ex. Carries ->	Carry, 	Hurries -> Hurry, Studies -> Study, Denies -> Deny
					baseForm = word.substring(0, word.length() - 3) + "Y";
				}
			}
			return baseForm.toLowerCase();	
		}
		return word;
	}
	
	public static boolean isVowel(String character) {
		String regExVowel = "[aAeEiIyYoOuUwW]";
		if(character != null && !character.isEmpty() && character.length() == 1) {
			character = character.replaceAll(regExVowel, "");
			if(character.equals("") || character.length() == 0)
				return true;
		}
		return false;
	}
	
	public static boolean isPlural(String word) {
		word = word.toUpperCase();
  		if(word.length() > 1){
  			word = word.substring(word.length() - 2);
			if (word.endsWith("S")) {
				word = word.substring(0, word.length() - 1);
				if (isVowel(word))
					return true;
			}	 
		} else {
			return false;	
		}
		return false;
	}
	
	/**
	 * Check the list of Custom Tokens contains one element equal to informed Token 
	 * @param list
	 * @param token
	 * @return
	 */
	public static boolean containsToken(List<CustomToken> list, CustomToken token){
		if (token != null){
			if (list != null && !list.isEmpty()) {
				for(int i = 0; i < list.size(); i++){
					//equal
					if 	(list.get(i).getWord().equals(token.getWord())) {
						return true;
					}
					
				}
			}	
		}
		return false;
	}
	/**
	 * Check the list contains a similar element like informed word 
	 * @param list
	 * @param word
	 * @return
	 */
	public static boolean containsSimilarWord(List<String> list, String word){
		bool listing = list.get(i).toUpperCase().equals(word);
		if (word != null && !word.isEmpty()  ){
			word = word.toUpperCase();
			if (list != null && !list.isEmpty()) {
				for(int i = 0; i < list.size(); i++){
					//equal
					if 	(listing) {
						return true;
					}
					
				}
			}	
		}
		return false;
	}
	
	/**
	 * Check the list contains one element equal to informed word 
	 * @param list
	 * @param word
	 * @return
	 */
	public static boolean containsWord(List<String> list, String word){
		bool listing = list.get(i).toUpperCase().equals(word);
		if (word != null && !word.isEmpty()  ){
			word = word.toUpperCase();
			if (list != null && !list.isEmpty()) {
				for(int i = 0; i < list.size(); i++){
					//equal
					if 	(listing) {
						return true;
					}
					
				}
			}	
		}
		return false;
	}
	
	/**
	 * Get number of words from a sentence
	 * @param sentence
	 * @return
	 */
	public static int getNumberOfWords(String sentence){
				
		sentence = sentence.trim();
		String[] words = sentence.split(RegularExpression.REGEX_WHITE_SPACE);
		
		return words.length;
	}
	
	public static void main(String[] args) {
		
		INLPAnalyzer nlpAnalyzer = CoreNLPAnalyzer.getInstance();//singleton
		//Text to analyze
		List<CustomToken> tokens = null;		
		
		String text1 = "System displays a tree view of available groups and channels and marks it";
				
		tokens = nlpAnalyzer.getTokens(text1);
		//Pos tags
		logger.log("before: " +getPosTagsAsString(tokens, 0, tokens.size()));
    	adjustPosTags(tokens);
    	logger.log("after: " + getPosTagsAsString(tokens, 0, tokens.size()));
	}
}
