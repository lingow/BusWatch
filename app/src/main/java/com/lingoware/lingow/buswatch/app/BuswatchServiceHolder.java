package com.lingoware.lingow.buswatch.app;

import com.lingoware.lingow.buswatch.common.service.BusWatchService;
import com.lingoware.lingow.buswatch.server.MockService;

/**
 * Created by lingow on 10/05/15.
 */
public class BuswatchServiceHolder {

    static final BusWatchService service = new MockService();
    //TODO set this to be something meaningfull
    private static final String WSDLURL = "";
    static BuswatchServiceHolder buswatchServiceHolder;

    private BuswatchServiceHolder() {
    }

    public static BuswatchServiceHolder getInstance() {
        if (buswatchServiceHolder == null) {
            buswatchServiceHolder = new BuswatchServiceHolder();
        }
        return buswatchServiceHolder;
    }

    public BusWatchService getService() {
        if (service == null) {
            initService();
        }
        return service;
    }

    private void initService() {
        /*TODO uncomment when the service is ready

        URL url = null;

        try {
            url = new URL(WSDLURL);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            Log.wtf("SERVICE", "The WSDLURL is all wrong");
        }
        QName qname = new QName("http://service.common.buswatch.lingow.lingoware.com/","BusWatchService");
		Service service = Service.create(url, qname);

		this.service = service.getPort(BusWatchService.class);

		*/

    }

}
