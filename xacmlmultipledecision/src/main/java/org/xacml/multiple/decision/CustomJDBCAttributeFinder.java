package org.xacml.multiple.decision;

import org.wso2.carbon.identity.entitlement.pip.AbstractPIPAttributeFinder;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

public class CustomJDBCAttributeFinder extends AbstractPIPAttributeFinder {

    private static final String SHIPPER = "http://wso2.org/claims/shipper";
    private static final String RECEIVER = "http://wso2.org/claims/receiver";
    private static final String BILLING = "http://wso2.org/claims/billing";
    private DataSource dataSource;
    private Set<String> supportedAttributes = new HashSet();

    public void init(Properties properties) throws Exception {

        String dataSourceName = (String)properties.get("DataSourceName");
        if ((dataSourceName == null) || (dataSourceName.trim().length() == 0)) {
            throw new Exception("Data source name can not be null. " +
                    "Please configure it in the entitlement.properties file.");
        }
        Context ctx = new InitialContext();
        this.dataSource = ((DataSource)ctx.lookup(dataSourceName));

        this.supportedAttributes.add(SHIPPER);
        this.supportedAttributes.add(RECEIVER);
        this.supportedAttributes.add(BILLING);
    }

    public String getModuleName()
    {
        return "Custom_Data_Finder";
    }

    public Set<String> getAttributeValues(String subjectId, String resourceId, String actionId, String environmentId, String attributeId, String issuer)
            throws Exception
    {
        String attributeName = null;
        if (SHIPPER.equals(attributeId)) {
            attributeName = "title";
        } else if (RECEIVER.equals(attributeId)) {
            attributeName = "nickname";
        } else if (BILLING.equals(attributeId)) {
            attributeName = "costCenter";
        }
        if (attributeName == null) {
            throw new Exception("Invalid attribute id : " + attributeId);
        }
        String sqlStmt = "select UM_ATTR_VALUE from UM_USER_ATTRIBUTE where UM_ATTR_NAME='" +
                attributeName + "' and UM_USER_ID=(select UM_ID from UM_USER where UM_USER_NAME='" +
                subjectId + "');";

        Set<String> values = new HashSet();
        PreparedStatement prepStmt = null;
        ResultSet resultSet = null;
        Connection connection = null;
        try
        {
            connection = this.dataSource.getConnection();
            if (connection != null)
            {
                prepStmt = connection.prepareStatement(sqlStmt);
                resultSet = prepStmt.executeQuery();
                while (resultSet.next()) {
                    values.add(resultSet.getString(1));
                }
            }
            return values;
        }
        catch (SQLException e)
        {
            throw new Exception("Error while retrieving attribute values", e);
        }
        finally
        {
            try
            {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (prepStmt != null) {
                    prepStmt.close();
                }
                if (connection != null) {
                    connection.close();
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    public Set<String> getSupportedAttributes()
    {
        return this.supportedAttributes;
    }

}


