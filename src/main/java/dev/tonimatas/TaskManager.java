package dev.tonimatas;

import dev.tonimatas.tasks.AutoRoleTask;
import dev.tonimatas.tasks.CountTask;
import dev.tonimatas.tasks.TemporalChannelTask;
import dev.tonimatas.tasks.JoinLeaveMessageTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TaskManager {
    public static Logger LOGGER = LoggerFactory.getLogger(TaskManager.class);
    public static CountTask countTask = new CountTask();
    public static AutoRoleTask autoRoleTask = new AutoRoleTask();
    public static TemporalChannelTask temporalChannelTask = new TemporalChannelTask();
    public static JoinLeaveMessageTask joinLeaveMessageTask = new JoinLeaveMessageTask();
    
    public static void init() {
        temporalChannelTask.start();
        LOGGER.info("Games has been initialised successfully.");
    }
}
