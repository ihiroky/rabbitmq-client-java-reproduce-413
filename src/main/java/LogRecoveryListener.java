import com.rabbitmq.client.Recoverable;
import com.rabbitmq.client.RecoveryListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public class LogRecoveryListener implements RecoveryListener {

    private static final Logger LOG = LoggerFactory.getLogger(LogRecoveryListener.class);

    static final LogRecoveryListener INSTANCE = new LogRecoveryListener();

    private LogRecoveryListener() {
    }

    @Override
    public void handleRecovery(Recoverable recoverable) {
        LOG.info("[handleRecovery] {}", recoverable);
    }

    @Override
    public void handleRecoveryStarted(Recoverable recoverable) {
        LOG.info("[handleRecoveryStarted] {}", recoverable);
    }
}
