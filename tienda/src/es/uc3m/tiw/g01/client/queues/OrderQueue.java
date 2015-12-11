package es.uc3m.tiw.g01.client.queues;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.Session;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import es.uc3m.tiw.g01.model.entities.Purchase;

public class OrderQueue {

  public void enqueuePurchase(Purchase purchase) throws NamingException {

    MessageProducer messageProducer;
    ObjectMessage purchaseMessage;
    try {
      // Get the JNDI Context
      InitialContext jndiContext = new InitialContext();

      // Create the Connection Factory
      ConnectionFactory connectionFactory =
          (ConnectionFactory) jndiContext.lookup("jms/_tiwConnectionFactory");
      Queue queue = (Queue) jndiContext.lookup("jms/pedidos");

      Connection connection = connectionFactory.createConnection();
      Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

      messageProducer = session.createProducer(queue);
      purchaseMessage = session.createObjectMessage(Purchase.class);
      purchaseMessage.setObject(purchase);

      messageProducer.send(purchaseMessage);
      messageProducer.close();
      session.close();
      connection.close();
    } catch (JMSException e) {
      e.printStackTrace();
    }

  }

}
