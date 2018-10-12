import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.RecoverableChannel;
import com.rabbitmq.client.RecoverableConnection;
import com.rabbitmq.client.impl.nio.NioParams;

/**
 *
 */
public class Main {
    public static void main(String[] args) throws Exception {
        String host = "172.18.1.11";

        // Wait until broker starts up.
        Util.retryUntilConnected(10, host, ConnectionFactory.DEFAULT_AMQP_PORT, Main::connect);

        Thread.sleep(Long.MAX_VALUE);
    }

    static Void connect() throws Exception {
        var factory = new ConnectionFactory();
        factory.setHost("172.18.1.11");
        factory.useNio();
        factory.setNioParams(new NioParams().setNbIoThreads(1));
        factory.setRequestedHeartbeat(5);

        var connection = (RecoverableConnection) factory.newConnection();
        connection.addRecoveryListener(LogRecoveryListener.INSTANCE);
        connection.addShutdownListener(LogShutdownListener.INSTANCE);
        var channel = (RecoverableChannel) connection.createChannel();
        channel.addRecoveryListener(LogRecoveryListener.INSTANCE);
        channel.addShutdownListener(LogShutdownListener.INSTANCE);

        return null;
    }
}
