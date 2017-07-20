package biz.princeps.lib.test;

import biz.princeps.lib.storage.requests.AbstractRequest;

/**
 * Created by spatium on 17.07.17.
 */
public class TestRequests extends AbstractRequest {

    public void saveTab(TestTable tab){
        api.saveObject(tab);
    }
}
