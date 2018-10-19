package org.xacml.multiple.decision.internal;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.wso2.carbon.identity.core.util.IdentityCoreInitializedEvent;
import org.wso2.carbon.user.core.service.RealmService;

/**
 * @scr.component name="org.xacml.multiple.decision.component" immediate=true
 * @scr.reference name="user.realmservice.default"
 * interface="org.wso2.carbon.user.core.service.RealmService"
 * cardinality="1..1" policy="dynamic" bind="setRealmService"
 * unbind="unsetRealmService"
 * @scr.reference name="identityCoreInitializedEventService"
 * interface="org.wso2.carbon.identity.core.util.IdentityCoreInitializedEvent"
 * cardinality="1..1" policy="dynamic" bind="setIdentityCoreInitializedEventService"
 * unbind="unsetIdentityCoreInitializedEventService"
 */
public class XACMLExtensionServiceComponent {

    private static Log log = LogFactory.getLog(XACMLExtensionServiceComponent.class);
    private static RealmService realmService;

    @Activate
    protected void activate(ComponentContext ctxt)
    {
        log.info("ResourceFinderService bundle activated");
    }

    public void setRealmService(RealmService realmService)
    {
        XACMLExtensionDataHolder.getInstance().setRealmService(realmService);
    }

    protected void unsetRealmService(RealmService realmService)
    {
        XACMLExtensionDataHolder.getInstance().setRealmService(null);
    }

    protected void unsetIdentityCoreInitializedEventService(
            IdentityCoreInitializedEvent identityCoreInitializedEvent) {
        /* reference IdentityCoreInitializedEvent service to guarantee that this component will wait until identity core
         is started */
    }

    protected void setIdentityCoreInitializedEventService(
            IdentityCoreInitializedEvent identityCoreInitializedEvent) {
        /* reference IdentityCoreInitializedEvent service to guarantee that this component will wait until identity core
         is started */
    }
}
