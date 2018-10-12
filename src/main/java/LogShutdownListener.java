import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ShutdownListener;
import com.rabbitmq.client.ShutdownSignalException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;

/**
 *
 */
public class LogShutdownListener implements ShutdownListener {

    private static final Logger LOG = LoggerFactory.getLogger(LogShutdownListener.class);

    static final LogShutdownListener INSTANCE = new LogShutdownListener();

    private LogShutdownListener() {
    }

    @Override
    public void shutdownCompleted(ShutdownSignalException cause) {
        Object ref = cause.getReference();
        Connection conn;
        int channelNo = -1;
        if (ref instanceof Connection) {
            conn = (Connection) ref;
        } else if (ref instanceof Channel) {
            Channel channel = (Channel) ref;
            conn = channel.getConnection();
            channelNo = channel.getChannelNumber();
        } else {
            LOG.info("[shutdownCompleted] {}", cause.toString());
            return;
        }

        InetAddress address = conn.getAddress();
        int port = conn.getPort();
        String id = conn.getId();

        LOG.info("[shutdownCompleted] host:[{}:{}] id:[{}] chNo:[{}] - {}",
                address, port, id, channelNo, cause.toString());
    }
}
