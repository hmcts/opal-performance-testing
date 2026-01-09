package simulations.Scripts.RequestBodyBuilder;

import io.gatling.javaapi.core.Session;


/**
 * Factory class for building various request bodies used in the application.
 * This class delegates to specialised builders for each type of request.
 */
public class RequestBodyBuilder {

        public static String buildLoginRequestBody(Session session) {

        String userName = session.get("Username") != null ? session.get("Username").toString() : "";
        String getSFT = session.get("getSFT") != null ? "" + session.get("getSFT").toString() + "" : "null";
        String getSCtx = session.get("getSCtx") != null ? "" + session.get("getSCtx").toString() + "" : "null";


        return String.format("{\"checkPhones\": false," 
        + "\"country\":\"GB\","
        + "\"federationFlags\": 0,"
        + "\"flowToken\":\"%s\","
        + "\"forceotclogin\": false,"
        + "\"isAccessPassSupported\": true,"
        + "\"isCookieBannerShown\": false,"
        + "\"isExternalFederationDisallowed\": false,"
        + "\"isFidoSupported\": true,"
        + "\"isOtherIdpSupported\": false,"
        + "\"isQrCodePinSupported\": true,"
        + "\"isRemoteConnectSupported\": false,"
        + "\"isRemoteNGCSupported\": true,"
        + "\"isSignup\": false,"
        + "\"originalRequest\":\"%s\","
        +"\"username\":\"%s\"}",        
        getSFT, getSCtx, userName );
    }    

    public static String buildGetCredentialType(Session session) {
    
        String originalRequest = "rQQIARAA02I20jOwUjFLsTQ2SDFJ1E1LTUzWNTFMTtW1SDGw0E00TbJMMzNOs7S0MCgS4hJ416We_lKtyH32q6pu_S-3Tq1itMgoKSkottLXT0ksKinWK0ktLtFLLCgo1svITQby81JL9BNLSzL0M_NKUovyEnP0kxNzcpISk7N3MDJeYGR8wch4i4nf3xGoxAhE5BdlVqW-YmL4xMSZll-UG1-QX1yyiVkl1dTcNM3MzFg3ydggUdfE3MJM18IyMUXX2NDSwsQoJS3VwtT4FDNbfkFqXmbKBRbGVyw8BsxWHBxcAgwSDAoMP1gYF7ECnc91LZXBes4Cp7X_GOPsdBQZTrHqF6cbFISl-zs7JZqZmfkUeziahmSVVkZWZqfnROaEBOdn-haZeRVoJ0Y6utoaWRlOYGP8wMbYwc6wi5Nsnx_gZfjBt-rzqSmfWla883jFr5MRVRFpZublnellVulb4eUSGlngnBQUaVIQFhTsblmYHOGtHRJRVpkaYWlhu0GA4YEAAwA1";
        String flowToken = session.get("flowToken") != null ? "" + session.get("flowToken").toString() + "" : "null";
        String userName = session.get("Username") != null ? session.get("Username").toString() : "";
        
        return String.format("{\"username\":\"%s\"," 
        + "\"isOtherIdpSupported\":false," 
        + "\"checkPhones\":false," 
        +"\"isRemoteNGCSupported\":true," 
        +"\"isCookieBannerShown\":false," 
        +"\"isFidoSupported\":true," 
        +"\"originalRequest\":\"%s\"," 
        +"\"country\":\"GB\"," 
        +"\"forceotclogin\":false," 
        +"\"isExternalFederationDisallowed\":false," 
        +"\"isRemoteConnectSupported\":false," 
        +"\"federationFlags\":0," 
        +"\"isSignup\":false," 
        +"\"flowToken\":\"%s\"," 
        +"\"isAccessPassSupported\":true,"
        +"\"isQrCodePinSupported\":true}",
        userName, originalRequest, flowToken);
    }
}