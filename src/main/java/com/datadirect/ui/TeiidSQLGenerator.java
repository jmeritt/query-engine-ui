package com.datadirect.ui;

import com.vaadin.data.util.sqlcontainer.query.generator.DefaultSQLGenerator;

/**
 * Created by jmeritt on 3/21/15.
 */
public class TeiidSQLGenerator extends DefaultSQLGenerator {
    @Override
    protected StringBuffer generateLimits(StringBuffer sb, int offset,
                                          int pagelength) {
        sb.append(" LIMIT ").append(pagelength).append(" , ")
                .append(offset);
        return sb;
    }

}
