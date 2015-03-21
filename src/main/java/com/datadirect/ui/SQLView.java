package com.datadirect.ui;

import com.vaadin.data.util.sqlcontainer.connection.J2EEConnectionPool;
import com.vaadin.data.util.sqlcontainer.connection.JDBCConnectionPool;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.*;
import java.util.logging.Logger;

/**
 * Created by jmeritt on 3/20/15.
 */
public class SQLView extends CustomComponent implements View {

    public static final String NAME = "sql";
    private final DataSource source;
    private JDBCConnectionPool m_pool;
    private Table table;

    public SQLView() {

        source = new EngineDataSource();
        table = new Table("Catalogs");
        table.addContainerProperty("Catalog", String.class, null);
        table.addContainerProperty("Schema", String.class, null);
        table.addContainerProperty("Table", String.class, null);


        VerticalLayout fields = new VerticalLayout(table);
        fields.setCaption("Database Metadata");
        fields.setSpacing(true);
        fields.setMargin(new MarginInfo(true, true, true, false));
        fields.setSizeUndefined();

        // The view root layout
        VerticalLayout viewLayout = new VerticalLayout(fields);
        viewLayout.setSizeFull();
        viewLayout.setComponentAlignment(fields, Alignment.MIDDLE_CENTER);
        viewLayout.setStyleName(Reindeer.LAYOUT_BLUE);
        setCompositionRoot(viewLayout);

    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        if (m_pool != null)
            m_pool.destroy();
        m_pool = new J2EEConnectionPool(source);
//            TableQuery query = new TableQuery("ClientBrowser", m_pool, new DefaultSQLGenerator());
//            SQLContainer sql = new SQLContainer(query);
//            table = new Table("Showing a Salesforce table", sql);


        Connection conn = null;
        try {
            conn = m_pool.reserveConnection();
            DatabaseMetaData meta = conn.getMetaData();
            String catalog = null;
            String schemaPattern = null;
            String tableNamePattern = null;
            String[] types = null;

            ResultSet result = meta.getTables(catalog, schemaPattern, tableNamePattern, types);
            for (int i = 1; result.next(); i++) {
                table.addItem(new Object[]{result.getString(1), result.getString(2), result.getString(3)}, i);
            }
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        } finally {
            if (conn != null)
                m_pool.releaseConnection(conn);
        }

        table.setPageLength(table.size());

        m_pool.releaseConnection(conn);

    }

    private class EngineDataSource implements DataSource {
        @Override
        public Connection getConnection() throws SQLException {
            return Engine.get().getConnection();
        }

        @Override
        public Connection getConnection(String username, String password) throws SQLException {
            return Engine.get().getConnection();
        }

        @Override
        public PrintWriter getLogWriter() throws SQLException {
            return null;
        }

        @Override
        public void setLogWriter(PrintWriter out) throws SQLException {

        }

        @Override
        public int getLoginTimeout() throws SQLException {
            return 0;
        }

        @Override
        public void setLoginTimeout(int seconds) throws SQLException {

        }

        @Override
        public Logger getParentLogger() throws SQLFeatureNotSupportedException {
            return null;
        }

        @Override
        public <T> T unwrap(Class<T> iface) throws SQLException {
            return null;
        }

        @Override
        public boolean isWrapperFor(Class<?> iface) throws SQLException {
            return false;
        }
    }
}
