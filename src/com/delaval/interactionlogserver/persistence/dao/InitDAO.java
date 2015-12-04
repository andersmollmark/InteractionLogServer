package com.delaval.interactionlogserver.persistence.dao;

import com.delaval.interactionlogserver.persistence.ConnectionFactory;
import com.delaval.interactionlogserver.persistence.entity.LogContext;
import com.delaval.interactionlogserver.persistence.entity.LogContent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import simpleorm.sessionjdbc.SSessionJdbc;

import java.sql.SQLException;

/**
 * Creates all the needed tables in DB.
 */
public class InitDAO {

    private static final Logger LOGGER = LoggerFactory.getLogger(InitDAO.class);

    /**
     * Creates the database-tables needed.
     */
    public void createTables() throws Exception {
        SSessionJdbc session = null;
        try {
            session = ConnectionFactory.getInstance().getSession(ConnectionFactory.LOG_SERVER_CONN_NAME);
            dropAllTables(session);
            createTables(session);
        } finally {
            if (session != null) {
                session.close();
                ConnectionFactory.getInstance().closeConnection();
            }
        }
    }

    private void dropAllTables(SSessionJdbc ses) {
        ses.begin();
        dropTableNoError(ses, LogContent.LOG_CONTENT.getTableName());
        dropTableNoError(ses, LogContext.LOG_CONTEXT.getTableName());
        ses.commit();
    }

    private void dropTableNoError(SSessionJdbc ses, String table) {
        ses.flush();
        ses.getDriver().dropTableNoError(table);
        ses.commit();
        ses.begin();
    }

    private void createTables(SSessionJdbc ses) {
        ses.begin();
        ses.rawUpdateDB(ses.getDriver().createTableSQL(LogContext.LOG_CONTEXT));
        ses.rawUpdateDB(ses.getDriver().createTableSQL(LogContent.LOG_CONTENT));
        ses.commit();
    }

}
