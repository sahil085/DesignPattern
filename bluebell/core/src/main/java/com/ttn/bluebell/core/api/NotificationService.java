package com.ttn.bluebell.core.api;

import com.ttn.bluebell.durable.model.event.notification.*;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * Created by ttn on 29/9/16.
 */
public interface NotificationService {

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    void handler(NotificationRequest notificationRequest);

    @Async
    @EventListener
    void handler(NotificationRequestWithPayload notificationRequest);

}
