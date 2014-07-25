package com.sample;

import static org.junit.Assert.assertEquals;

import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderError;
import org.drools.builder.KnowledgeBuilderErrors;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.io.ResourceFactory;
import org.drools.logger.KnowledgeRuntimeLogger;
import org.drools.logger.KnowledgeRuntimeLoggerFactory;
import org.drools.runtime.StatefulKnowledgeSession;
import org.junit.Before;
import org.junit.Test;

/**
 * This is a sample class to launch a rule.
 */
public class DroolsTest {

	private KnowledgeBase kbase;
	
	@Before
    public void setup() {
		// Construct rule base from pre-built rules
        KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
        //kbuilder.add(ResourceFactory.newClassPathResource("Sample.drl"), ResourceType.DRL);
        kbuilder.add(ResourceFactory.newFileResource("src/main/rules/Sample.drl"), ResourceType.DRL);
        KnowledgeBuilderErrors errors = kbuilder.getErrors();
        if (errors.size() > 0) {
            for (KnowledgeBuilderError error: errors) {
                System.err.println(error);
            }
            throw new IllegalArgumentException("Could not parse knowledge.");
        }
        kbase = KnowledgeBaseFactory.newKnowledgeBase();
        kbase.addKnowledgePackages(kbuilder.getKnowledgePackages());
    }

	@Test
	public void testRules(){
        StatefulKnowledgeSession ksession = kbase.newStatefulKnowledgeSession();
        KnowledgeRuntimeLogger logger = KnowledgeRuntimeLoggerFactory.newFileLogger(ksession, "test");
        
        // Set up a POJO for testing
        Message message = new Message();
        message.setMessage("Hello World");
        message.setStatus(Message.HELLO);
        
        //Message m2 = new Message();
        //m2.setMessage("Goodbye cruel world");
        //m2.setStatus(Message.GOODBYE);
        
        // Insert FACTS into the engine
        ksession.insert(message);
        //ksession.insert(m2);
        
        // Fire the rules
        ksession.fireAllRules();

        // Interrogate the results
//        for ( Object o : ksession.getObjects() ){
//        	if ( o instanceof Message ){
//        		assertEquals( Message.GOODBYE, ((Message)o).getStatus());
//        		assertEquals( "Goodbye cruel world", ((Message)o).getMessage() );
//        	}
//        }
        logger.close();
	}
	

    public class Message {

        public static final int HELLO = 0;
        public static final int GOODBYE = 1;

        private String message;
        private int status;

        public String getMessage() {
            return this.message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public int getStatus() {
            return this.status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

    }

}
