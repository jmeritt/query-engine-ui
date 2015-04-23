package com.datadirect.ui;

import com.datadirect.platform.D2CQueryEngineImpl;
import com.datadirect.platform.QueryEngine;

/**
 * Created by jmeritt on 3/19/15.
 */
public class Engine
{
    private static QueryEngine m_engine;

    static QueryEngine get()
    {
        return m_engine;
    }

    static QueryEngine get(String username, String password)
    {
        //purposeful race condition - ahh prototypes
        m_engine = QueryEngine.create("localhost", 31000, username, password);
        return m_engine;
    }
}
