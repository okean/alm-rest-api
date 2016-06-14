package org.alm;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.lang.StringUtils;

public final class Dao
{
    private Dao()
    {
    }

    public static boolean isLoggedIn(String username, String password)
    {
        return false;
    }

    public static String isAuthenticated() throws Exception
    {
        String isAuthenticatedUrl = "qcbin/rest/is-authenticated";

        try
        {
            RestConnector.instance().get(isAuthenticatedUrl, Response.class, null, null);

            return null;
        }
        catch (ResponseException ex)
        {
            Response response = ex.response();

            if (response.getStatus() != Status.UNAUTHORIZED.getStatusCode())
            {
                throw ex;
            }

            String authPoint = response.getHeaderString(HttpHeaders.WWW_AUTHENTICATE);

            if (StringUtils.isNotBlank(authPoint))
            {
                authPoint = authPoint.split("=")[1].replace("\"", "");
                authPoint += "/authenticate";

                return authPoint;
            }

            throw new Exception("Invalid authentication point");
        }
    }
}
