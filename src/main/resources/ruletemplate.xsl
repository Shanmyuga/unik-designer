<?xml version="1.0"?>
<xsl:stylesheet  xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="text" />
	

	<xsl:template match="/">
		package com.esrx.drools;
	
	import com.cognizant.fufapp.data.model.QuestionFact;
	
	import com.cognizant.fufapp.data.model.DecisionModel; 
	rule "GRID_RULE1"
	    dialect "mvel"
	    when
	    
	     <xsl:for-each select="//QuestionCondition">
     QuestionFact( questID == "<xsl:value-of select="questID"></xsl:value-of>" , answerValue <xsl:value-of select="conditional"></xsl:value-of> "<xsl:value-of select="answerValue"></xsl:value-of>" )
	      
	         </xsl:for-each>
	        dm : DecisionModel( )
	    then
	    
	   <xsl:for-each select="//DecisionCondition">
    dm.createDecision("<xsl:value-of select="decisionkey"></xsl:value-of>","<xsl:value-of select="decisionValue"></xsl:value-of> ");
	      
	         </xsl:for-each>
	     
	end
	</xsl:template>
	
</xsl:stylesheet>