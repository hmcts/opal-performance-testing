package simulations.Scripts.Scenario.OpalLogin;

import simulations.Scripts.Headers.Headers;
import simulations.Scripts.Utilities.AppConfig;
import simulations.Scripts.Utilities.Feeders;
import simulations.Scripts.Utilities.UserInfoLogger;
import io.gatling.javaapi.core.*;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

import java.util.concurrent.ThreadLocalRandom;

import simulations.Scripts.RequestBodyBuilder.RequestBodyBuilder;

public final class LoginScenario {

    private LoginScenario() {}

    public static ChainBuilder LoginRequest() {

        return group("OPAL Login").on(
                exec(http("OPAL - Sso - Login - /")
                        .get(AppConfig.UrlConfig.BASE_URL + "/sso/login")
                        .headers(Headers.getHeaders(1))
                //        .check(status().is(302)) 
                                       
                )
                .exec(UserInfoLogger.logDetailedErrorMessage("OPAL - Sso - Login - /"))

                .exitHereIfFailed() 

                .exec(
                    http("OPAL - Oauth2 - v2.0 - Authorize")
                    .get(AppConfig.UrlConfig.AUTH_URL + AppConfig.TenantConfig.AAD_TENANT_ID +"/oauth2/v2.0/authorize?" +
                    "client_id="+ AppConfig.TenantConfig.CLIENT_ID +"&scope="+ AppConfig.TenantConfig.SCOPE +
                    "&redirect_uri="+ AppConfig.TenantConfig.REDIRECT_URL + 
                    "&client-request-id="+ AppConfig.TenantConfig.CLIENT_REQUEST_ID +"&response_mode=form_post" +
                    "&response_type=code&x-client-SKU=msal.js.node&x-client-VER=2.11.0&x-client-OS=linux&x-client-CPU=x64&client_info=1")
                    .disableFollowRedirect() 
                    .headers(Headers.getHeaders(9))                    
                    .check(Feeders.saveApiCanary())
                    .check(Feeders.saveSessionId())                    
                    .check(Feeders.saveSFT())
                    .check(Feeders.saveSCtx())
                    .check(Feeders.saveCanary())
                    .check(Feeders.saveClientRequestId())
                )
                .exec(session -> {
                    System.out.println("apiCanary = " + session.getString("apiCanary"));
                    System.out.println("sessionId = " + session.getString("sessionId"));
                    System.out.println("getSFT = " + session.getString("getSFT"));
                    System.out.println("getSCtx = " + session.getString("getSCtx"));
                    System.out.println("getCanary = " + session.getString("getCanary"));
                    System.out.println("getClientRequestId = " + session.getString("getClientRequestId"));
                    return session;
                })

                .exec(session -> {
                    String loginRequestPayload = RequestBodyBuilder.buildLoginRequestBody(session);
                    return session.set("loginRequestPayload", loginRequestPayload);
                })
                .exec(
                    http("OPAL - Common - GetCredentialType")
                    .post(AppConfig.UrlConfig.AUTH_URL +"common/GetCredentialType?mkt=en-US")
                        .headers(Headers.getHeaders(2))
                        .body(StringBody(session -> session.get("loginRequestPayload"))).asJson()
                )                       
            .exitHereIfFailed() 
                        
            ///This Gets changed and causes internal users to fail log in. If this happens manually go into the portal with 1 user and grab it from the cookies. 
            .exec(addCookie(Cookie("brcap", "0; MSFPC=GUID=2a4ebe9c35eb4b32b7e1ebe6023b970e&HASH=2a4e&LV=202510&V=4&LU=1761747270244; x-ms-gateway-slice=estsfd; stsservicecookie=estsfd; AADSSO=NA|NoExtension; ESTSSSOTILES=1; AADSSOTILES=1; esctx-J8VJ5IQkAQ=AQABCQEAAABlMNzVhAPUTrARzfQjWPtK8DsGYYGKc4V8yXaGWD3FMPOFAwAeu4sTnsXs-_wV9JWgpBvKiuWAJ8-I9viIwUKw8LWjpGWtvbRpx8KHJ-zRrH-YfZcnkAtkCxfqH6DR2KIK0ZjRrVK6kLanLrB2v4p7Hg-DoN0TM4bHIXC0lM2n1CAA; esctx-xkmXKosVMKc=AQABCQEAAABlMNzVhAPUTrARzfQjWPtKGiulVp1YrrNbZcR7Ya5rQEw8mzqZ9jfeouxR216J0QBSsIDgD5VQK7Aemv1khGm6xWzmQBNmkqO1WZAwibfQRtJNbuL1773pw0Esbljpdzh8zgjPdUMRDmhdt6S576EWX1_oI8oPwdrjDaUTfzPLPSAA; esctx-BvCQofzmoYA=AQABCQEAAABlMNzVhAPUTrARzfQjWPtKGxDmR9UWkheIx10kNdIRsmXawTpttBRdXzPzd_6_mvb_aW3DkWvuioBrRl76g2ozXILQLrWKN3FpaB4onrSUyDcOsvivi312NLjXxL5s2n4NfQsZQ5RgYbDW65deGLGYlixv0tHiPIirw-a9F-c2riAA; esctx-FCtZLtnRt8=AQABCQEAAABlMNzVhAPUTrARzfQjWPtKCvIWbi8yg5jc7Yp7ZtAZyy_D22SXI8MXJN9pRrXDHs8XD-Np6-Cg08HoVKRfNhmFEYsNtxSDgrCOonsP6Ynb8gqgTofuEvkYUkrFsFPbNyF95ZiktIEk6ckZSgh6daqssW0SUwklCLZ_FzRofQyQ3SAA; esctx-Ie0T6EbEcg8=AQABCQEAAABlMNzVhAPUTrARzfQjWPtKxgGbBih6C5-3zoBdCXg_1A52248ron6SBdx4w-m8j1f9OFHgA83NVQCNCX3ZLamo2FV4XgkZieeX-naaIo0IZW-xvYNqe2ydEHWlFBi_NZVxYjgKwgqNqdQCQx27l9cBTvxwIUf4AW4JfWlnRrZc6yAA; esctx-7McJvY432o=AQABCQEAAABlMNzVhAPUTrARzfQjWPtKcdWLbF4EBlYcnyN46rWILMSnwVUpHiivUzzbi8Lnt8S4xD5VU9exsrO6c-mLxPRZB9g3Id3D0KGTfeBIpns2CoaLif6oLn-fy3A-FLeFYGjuXAOmI1dULA-dMBVQIUwDtcEe8k3veZhe_ouUnNvWuiAA; esctx-v0qS27GYvAY=AQABCQEAAABlMNzVhAPUTrARzfQjWPtK91eNkauRZQw8ksfQXrHDGLeOJI1j8kYSc7D39k1ArM6cUoIbQ479HpvhfZv7lZr6uAXOclR0ZD3agAJyXzu4COqzKl9xQgeSoBvIBZeHUUepBVYWmS6N_EaoASVFr_jQHVDUzZ33uwS0-cPv4Ytc3SAA; wlidperf=FR=L&ST=1767952750092; esctx-Q9kzZ4BdL6Y=AQABCQEAAABlMNzVhAPUTrARzfQjWPtKsyzYlHWcoYlq6mfBlJDm-8YZY3oZojZdqjBwnxmcrnCgcY3ZEiV5WtlU85GIIwFk-RIkUViyfTCO8K-J4EdN1xvnRuTgq99Cj6aRatqCqOZtTM40505Y0NIQizLyEETdLphnfaeyckYNPoYwnNDlmSAA; CCState=Q3JZQkNoZGhjMmhzWlhrdVkyOXZjR1Z5UUdodFkzUnpMbTVsZEJJQkFTSUpDWUFDUmRXek05eElLZ2tKeHg5Q0h6TlUza2d5S2dvU0NoQU1BQUFBQUFBQUFNQUFBQUFBQUFBQUVna0pZRVNFUTdKTzNrZ2FDUW5mbnlCM1YwemVTRElxQ2hJS0VNUGxQb2VNWit0QWpFM3JnZzhkVDFzU0NRbWpacjZNdlU3ZVNCb0pDVjc5WDhCaVRONUlPQUJJQUZJU0NoQnQrUjlUNlFvcVJvMHR2c2ZBdENDQ1doSUtFRDdiRDFva1laWkpyc2Y0NXFVelhJb0tiUW9RWVM1amIyOXdaWEpBWTJkcExtTnZiUklCQURJcUNoSUtFQ2cvdnUwZWRDWkhzcTZjdC9wUHpPWVNDUW53ZFptYThGRGVTQm9KQ2VhMlFNNlZUdDVJT0FGSUFGSVNDaENNeHY2NUxja2VScHFYUFFPZzhZdUNXaElLRU5nRnV4MHI3VEZMcVNGSWc1NWJUWUlTRWdvUS8zQUhZb0swS1UrWUNSV0FMa0lscXhvSkNjbTBRTTZWVHQ1SU1uNENBQUVmQVFBQUFHVXczTldFQTlST3NCSE45Q05ZKzBvREFPei9CUUQwLzBNbllrQ3RDaVZvcGhMZzdSaElUTVJzakhGcytJVmpjL3BWSGlOT2w0TGswcSs2TjBOOXFOZzNMU3FXdXp2TXhQSnNDNUliZi9Ec2pZVHJxeHptaU56REt2OVFLZDg2SUNQWVBocktTUStwRmw5SmpaZjdPZFZjK2wwWDliWT0=; esctx-aqiKTPtoBbU=AQABCQEAAABlMNzVhAPUTrARzfQjWPtKqWr0VoVDyLAbBs_rw2w9BeLqdUkjm_EKx5HXK316FtNkMS_3nhuqzgc7b07JAjP4qbPXW0_jMNEDVCcQUkeLy8RuIlxtTLUaFyd4ZDVXeBmZoJRXMQrhyr38eE-FK4qqf34IDu9A6ELbTc44nZhSAyAA; SignInStateCookie=CAgABFgIAAABlMNzVhAPUTrARzfQjWPtKAwDs_wUA9P8VEobScHrMNogiOy2ZpnWrY4Vr17K1hoL0z59kcrV4nhRAMbu8--vkf3RKNXfPeGpqhPxL1uaMG3H0; ESTSAUTHLIGHT=+; ESTSAUTHPERSISTENT=1.ASAAbfkfU-kKKkaNLb7HwLQgguM5HJxEHYhOs_1ffjtyLEUNAGAgAA.AgABFwQAAABlMNzVhAPUTrARzfQjWPtKAwDs_wUA9P-FRqKfWeZyGxYt8tcX7NXuvx6dRowCMZIBsTGqf9c3gvzlykcW0dFbrFLXV9fLP29zVeufCtsTO9u6k_9w0Tz6ov-eP6jsmpZUoaPA_FGc1BjqCLLS8HgN6qbjapTiPGuOhzqjoq4pQWyoawpwCeto4JbAnEFjN4e7wmJQmC_mB84L5aVbxsitYdlwCUCLCc7sDGD8HwNX5aw5laJCF4fOqpRPvDsXuBxZ-HX91IxdV8FkzcnuhJ6TeOfwAf3lzILpEWPl6Om2Ykjj7KRq7McfR4csLXvLjGpxRW6Gp_7KYMn6q0bV8Olh9dwPfh13-bxB2-NrlMW-uxebSxep5q2RomWjsda4vYyzMN8xiAc5lWt7l774nvITLy4cbvBvj338xGqvBwMoPD1tnyJ-Us-vnmP44z101jadJXbd5kTFxgraYrfzlDFli5hlOU759Hpp-ZNbsqnwjL4PlNgNr86L4AAHkNyIsoGnMrxhoe-ET6v7ymHeC0gtVx5XsFdNAC35qfMkLpqXDAr2xRJcbo4PWQtl0r3VB67xZQ4hiKGzZmBcRnbVECwT59a98M3LykwMgBz-xKbSZlSIRtIhTdQMc2oWxhH2n5fA88hwi0pzSyUufVxWeTJrrEmgX28edKZOOgt8Mk8y5tWzV0eyLrVpjuqqgsSYrADOWjD-ixEQF_QZT1FBhst1meMA2u_bprIRsKQFfci_1wL7lJCVOsJZB5wPWzYd06noNhpEoYEhHNQhFQDDcl5QHq5e5C9XBWwvZeJgMNXVZ2yLD3N2xKacvJnPkQyzLq1Yk9iVv_d-1k6VrFA7K5pqwK5KiOS-A7iY9SWFdg6KJgSfgRS1d5P7zVuVkI2ly_aRCmQwbefRkko; ESTSAUTH=1.ASAAbfkfU-kKKkaNLb7HwLQgguM5HJxEHYhOs_1ffjtyLEUNAGAgAA.AgABFwQAAABlMNzVhAPUTrARzfQjWPtKAwDs_wUA9P-LGTVyCVebqxS_wsC_NngBu2G1z7LK1AT_nzv4pvjQh0AgwQywqRfYWMD8h0ZbPpRyTETcZhl81nAV; buid=1.ASAAbfkfU-kKKkaNLb7HwLQgguM5HJxEHYhOs_1ffjtyLEUNAGAgAA.AQABGgEAAABlMNzVhAPUTrARzfQjWPtKxruGWjrZCKQVTITgLDGFxJFLYwUo4-QH56Z9_tZERlKEOjdsd74dI_z5DrzWurtn38jbwPm89hWGyoyaeq8MDM3nUyvwZiSrMmbMPvibmwqUrflWbDXIBljP0VD3d7ne2twDLBIUhv58gHVE3wXlQDDG6HMdzacBUsFqjiaBOjMgAA; esctx=PAQABBwEAAABlMNzVhAPUTrARzfQjWPtKS1497bCLYveTbvJ8P0b0eeqf1rAmVvN-QLzME73z9FfHcUF3VS8XNWRviDz_4316usq99LYjDwLLgNmkrTeKbp4A8-ed5RA--0QNWjlTe59xyGPGFQnzoD0hDA_mXS112ajeSzvtx6xkXSUCUcbm_o2D2OQSSFXZ2-uIRsTkzZggAA; esctx-YV2bCvDtvAo=AQABCQEAAABlMNzVhAPUTrARzfQjWPtKvvIC9h6QCRGA5oMh6GNqMzAA6yRxDlxFCYE6hPhr8Xd1CVBenvAIbHOB6mX5uqGZ2MJD3aUcJXNrJzgBsDxWHyY2zYrRqUnDMvNDDvevKW5ZOiwNGOnHyHjEBsAHFJjJVrE-oL_4boAy9vzXZtuXIyAA; fpc=Am3nJhLwbfpFgjKxblnAA7zcvULVAQAAAGLJ8uAOAAAA6N6JJQEAAACGyfLgDgAAAL-1YkABAAAAb8jy4A4AAAA")))

            .exec(
                http("OPAL - Login")
                .post(AppConfig.UrlConfig.AUTH_URL + AppConfig.TenantConfig.AAD_TENANT_ID +"/login")
                .headers(Headers.getHeaders(10))
                .formParam("i13", "0")
                .formParam("login", "#{Username}")
                .formParam("loginfmt", "#{Username}")
                .formParam("type", "11")
                .formParam("LoginOptions", "3")
                .formParam("lrt", "")
                .formParam("lrtPartition", "")
                .formParam("hisRegion", "")
                .formParam("hisScaleUnit", "")
                .formParam("passwd", "#{Password}")
                .formParam("ps", "2")
                .formParam("psRNGCDefaultType", "")
                .formParam("psRNGCEntropy", "")
                .formParam("psRNGCSLK", "")
                .formParam("canary", "#{getCanary}")
                .formParam("ctx", "#{getSCtx}")
                .formParam("hpgrequestid", "#{sessionId}")
                .formParam("flowToken", "#{getSFT}")
                .formParam("PPSX", "")
                .formParam("NewUser", "1")
                .formParam("FoundMSAs", "")
                .formParam("fspost", "0")
                .formParam("i21", "0")
                .formParam("CookieDisclosure", "0")
                .formParam("IsFidoSupported", "1")
                .formParam("isSignupPost", "0")
                .formParam("DfpArtifact", "")
                .formParam("i19", "23305")
                .check(Feeders.saveTokenCode())
                .check(Feeders.saveClientInfo())
                .check(Feeders.saveSessionState())
            )     
            .exec(session -> {
                System.out.println("TokenCode = " + session.getString("TokenCode"));
                System.out.println("getClientInfo = " + session.getString("getClientInfo"));
                System.out.println("getSessionState = " + session.getString("getSessionState"));
                return session;
            })
                        
            .exitHereIfFailed() 
    
            .exec(
                http("OPAL - sso - login-callback")
                .post(AppConfig.UrlConfig.BASE_URL + "/sso/login-callback")
                .headers(Headers.getHeaders(4))
                .formParam("code", "#{TokenCode}")
                .formParam("client_info", "#{getClientInfo}")
                .formParam("session_state", "#{getSessionState}")
            )
            .exec(
                http("OPAL - sso - Authenticated")
                .get(AppConfig.UrlConfig.BASE_URL + "/sso/authenticated")
                .headers(Headers.getHeaders(5))
            )
            .exec(
              http("OPAL - Session - Expiry")
              .get(AppConfig.UrlConfig.BASE_URL + "/session/expiry")
                .headers(Headers.getHeaders(6))
            )
            .exec(
              http("OPAL - Opal-User-Service - Users - 0 - state")
              .get(AppConfig.UrlConfig.BASE_URL + "/opal-user-service/users/0/state")
                .headers(Headers.getHeaders(7))
                .check(
                    jsonPath("$.business_unit_users[*].business_unit_id")
                        .findAll()
                        .saveAs("businessUnitIds"),

                    jsonPath("$.business_unit_users[*].business_unit_user_id")
                        .findAll()
                        .saveAs("businessUnitUserIds")
                )
            )
            .exec(
              http("OPAL - Opal-User-Service - Users - 0 - state")
              .get(AppConfig.UrlConfig.BASE_URL + "/opal-user-service/users/0/state")
                .headers(Headers.getHeaders(7))
           )
        );            
    }
}
                     