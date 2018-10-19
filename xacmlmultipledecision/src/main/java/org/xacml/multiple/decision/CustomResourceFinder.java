package org.xacml.multiple.decision;

import org.wso2.balana.ctx.EvaluationCtx;
import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.identity.entitlement.pip.PIPResourceFinder;
import org.wso2.carbon.user.api.UserStoreException;
import org.wso2.carbon.user.api.UserStoreManager;
import org.wso2.carbon.user.core.service.RealmService;
import org.xacml.multiple.decision.internal.XACMLExtensionDataHolder;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

public class CustomResourceFinder implements PIPResourceFinder {
    private static final String SUBJECT_CATEGORY = "urn:oasis:names:tc:xacml:1.0:subject-category:access-subject";
    private DataSource dataSource;

    public void init(Properties properties)
            throws Exception
    {}

    public String getModuleName()
    {
        return "Role_Resource_Finder";
    }

    public Set<String> findChildResources(String s, EvaluationCtx evaluationCtx)
            throws Exception
    {
        return getAllRoles();
    }

    public Set<String> findDescendantResources(String s, EvaluationCtx evaluationCtx)
            throws Exception
    {
        return null;
    }

    public boolean overrideDefaultCache()
    {
        return false;
    }

    public void clearCache() {}

    private Set<String> getAllRoles()
    {
        RealmService realmService = XACMLExtensionDataHolder.getInstance().getRealmService();

        String[] roles = null;
        try
        {
            UserStoreManager userStoreManager = realmService.getTenantUserRealm(
                    PrivilegedCarbonContext.getThreadLocalCarbonContext().getTenantId()).getUserStoreManager();
            roles = userStoreManager.getRoleNames();
        }
        catch (UserStoreException e)
        {
            roles = new String[0];
            e.printStackTrace();
        }
        return new HashSet(Arrays.asList(roles));
    }
}

