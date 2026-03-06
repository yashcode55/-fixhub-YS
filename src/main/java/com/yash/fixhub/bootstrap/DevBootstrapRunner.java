package com.yash.fixhub.bootstrap;

import com.yash.fixhub.engine.FixEngineManager;
import com.yash.fixhub.service.SessionBootstrapService;

public class DevBootstrapRunner {

    public static void main(String[] args) throws Exception {

    	FixEngineManager engine = new FixEngineManager();

    	SessionBootstrapService bootstrap =
    	        new SessionBootstrapService(engine.getSessionManager());

    	bootstrap.loadAndRegister("config/sessions/buy_CLIENT_A.ini");
    	bootstrap.loadAndRegister("config/sessions/buy_CLIENT_B.ini");
    	bootstrap.loadAndRegister("config/sessions/sell_EXCHANGE_NSE.ini");
    	bootstrap.loadAndRegister("config/sessions/sell_EXCHANGE_BSE.ini");

    	bootstrap.startEngines();
    }
}