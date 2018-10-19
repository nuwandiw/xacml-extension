package org.xacml.multiple.decision.internal;

import org.wso2.carbon.user.core.service.RealmService;

public class XACMLExtensionDataHolder {

    private static XACMLExtensionDataHolder instance = new XACMLExtensionDataHolder();
    private RealmService realmService;

    public static XACMLExtensionDataHolder getInstance()
    {
        return instance;
    }

    public RealmService getRealmService()
    {
        return this.realmService;
    }

    public void setRealmService(RealmService realmService)
    {
        this.realmService = realmService;
    }
}
