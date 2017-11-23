import javax.jms.*;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
public class amqreceiver {
    public static void main(String[] args) throws JMSException {
        Connection connection =new ActiveMQConnectionFactory("tcp://localhost:61616").createConnection();
        connection.start();
        System.out.println("ClientID: "+connection.getClientID());
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Queue queueResponse=session.createQueue("RequestQ");
        MessageConsumer receiver= session.createConsumer(queueResponse);
        TextMessage msg=(TextMessage)receiver.receive();
        System.out.println("Odebralem: >"+msg.getText()+"< "+msg.getJMSMessageID()+" "+msg.getJMSCorrelationID());

        TextMessage msgResponse=session.createTextMessage("Potwierdzam >"+msg.getText()+"< "+msg.getJMSMessageID()+" "+msg.getJMSCorrelationID());
        msgResponse.setJMSCorrelationID(msg.getJMSCorrelationID());
        MessageProducer sender= session.createProducer((Queue)msg.getJMSReplyTo());
        sender.send(msgResponse);

        sender.close();
        receiver.close();
        session.close();
        connection.stop();
    }
}
