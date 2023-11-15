//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.01.20 at 10:49:14 PM CST 
//


package com.cognizant.unik.service.model;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.cognizant.fufapp.model package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _RuleCondition_QNAME = new QName("", "Condition");
    private final static QName _RuleDecision_QNAME = new QName("", "Decision");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.cognizant.fufapp.model
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Rule }
     * 
     */
    public Rule createRule() {
        return new Rule();
    }

    /**
     * Create an instance of {@link Rule.Decision }
     * 
     */
    public Rule.Decision createRuleDecision() {
        return new Rule.Decision();
    }

    /**
     * Create an instance of {@link Rule.Condition }
     * 
     */
    public Rule.Condition createRuleCondition() {
        return new Rule.Condition();
    }

    /**
     * Create an instance of {@link Rule.Decision.DecisionCondition }
     * 
     */
    public Rule.Decision.DecisionCondition createRuleDecisionDecisionCondition() {
        return new Rule.Decision.DecisionCondition();
    }

    /**
     * Create an instance of {@link Rule.Condition.QuestionCondition }
     * 
     */
    public Rule.Condition.QuestionCondition createRuleConditionQuestionCondition() {
        return new Rule.Condition.QuestionCondition();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Rule.Condition }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "Condition", scope = Rule.class)
    public JAXBElement<Rule.Condition> createRuleCondition(Rule.Condition value) {
        return new JAXBElement<Rule.Condition>(_RuleCondition_QNAME, Rule.Condition.class, Rule.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Rule.Decision }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "Decision", scope = Rule.class)
    public JAXBElement<Rule.Decision> createRuleDecision(Rule.Decision value) {
        return new JAXBElement<Rule.Decision>(_RuleDecision_QNAME, Rule.Decision.class, Rule.class, value);
    }

}