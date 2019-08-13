package metier.withings;

import com.github.scribejava.core.oauth.OAuth20Service;

import domaine.Param;
import domaine.oauth2.Oauth2AccessToken;
import domaine.oauth2.Oauth2Authorisation;
import metier.exception.UnAuthorizedException;
import pojo.HeartRate;
import metier.Plugin;


import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import com.github.scribejava.core.model.OAuthConstants;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Verb;

import org.json.JSONArray;
import org.json.JSONObject;
import outils.Constant;
import outils.SymmetricAESKey;
import pojo.HeartRateData;

import static javax.ws.rs.core.Response.Status.UNAUTHORIZED;

public class WithingsPlugin {

    private static final Logger LOG = Logger.getLogger(WithingsPlugin.class.getName());

    public static Oauth2Authorisation urlVerification() {
        Oauth2Authorisation oauth2Auth = Plugin.oauth20UrlVerification(Constant.WITHINGS_PROVIDER, getService());

        return oauth2Auth;
    }

    public static Oauth2Authorisation urlVerificationWithPrompt() {
        Oauth2Authorisation oauth2Authorisation = urlVerification();
        final URI uri = UriBuilder.fromUri("https://account.withings.com/logout").queryParam("r",oauth2Authorisation.getUrlVerification()).build();
        oauth2Authorisation.setUrlVerification(uri.toASCIIString());
        return oauth2Authorisation;
    }

    public static Oauth2AccessToken accessToken(String code) {
        Oauth2AccessToken accessToken = Plugin.oauth20AccessToken(code, getService());
        return accessToken;
    }

    private static OAuth20Service getService() {
        final OAuth20Service service = Plugin.getOauth2Service(Constant.WITHINGS_PROPS, Constant.WITHINGS_CALLBACK_URL, WithingsApi_Oauth20.instance());
        return service;
    }

    public static Oauth2AccessToken refresh(String encryptRefreshToken) {
        Oauth2AccessToken oauth2AccessToken = Plugin.refreshAccessToken(encryptRefreshToken, getService());
        return oauth2AccessToken;
    }



    public static void revoke (String token) throws IOException,InterruptedException, ExecutionException {
        LOG.log(Level.INFO,"revoking token");
        Plugin.revoke(token, getService());
    }

    private static HeartRateData parseHeartRate(JSONObject jsonObject) {
        HeartRateData heartRateData = new HeartRateData();
        try {
            JSONArray jsArray = jsonObject.getJSONObject("body").getJSONArray("measuregrps");
            LOG.log(Level.INFO, String.format("jsArray : %s", jsArray.length()));
            jsArray.forEach(item -> {
                final int date = ((JSONObject) item).getInt("date");
                final JSONObject jsonMesure = ((JSONObject) item).getJSONArray("measures").getJSONObject(0);
                final int hr = jsonMesure.getInt("value") / 100;
                LOG.log(Level.INFO, "hearth-rate : " + hr + "date : " + date);
                final HeartRate heartRate = new HeartRate(hr, date);
                heartRateData.addHeartRateData(heartRate);


            });
        } catch (Exception e) {
            LOG.log(Level.SEVERE, e.getMessage(), e);
        }
        return heartRateData;
    }

    public static HeartRateData getHeartRate(String encryptToken, String startDate, String endDate) throws UnAuthorizedException {
        LOG.log(Level.INFO, "token : " + SymmetricAESKey.decrypt(encryptToken));

        final ArrayList<Param> lstParams = new ArrayList<>();
        lstParams.add(new Param(Constant.WITHINGS_PARAM_ACTION, Constant.WITHINGS_PARAM_ACTION_GETMEAS,Param.TypeParam.QUERY_PARAM));
        lstParams.add(new Param(Constant.WITHINGS_PARAM_MEASTYPE, Constant.WITHINGS_PARAM_MEASTYPE_HR,Param.TypeParam.QUERY_PARAM));
        lstParams.add(new Param(OAuthConstants.ACCESS_TOKEN, SymmetricAESKey.decrypt(encryptToken),Param.TypeParam.QUERY_PARAM));
        lstParams.add(new Param(Constant.WITHINGS_PARAM_START_DATE, startDate,Param.TypeParam.QUERY_PARAM));
        lstParams.add(new Param(Constant.WITHINGS_PARAM_END_DATE, endDate,Param.TypeParam.QUERY_PARAM));
        JSONObject jsonObject = requestData(getService(), Verb.GET, Constant.WITHINGS_MEASURE_URL, lstParams);
        return parseHeartRate(jsonObject);

    }

    private static JSONObject requestData(OAuth20Service service, Verb verb, String urlRequest, ArrayList<Param> lstParams) throws UnAuthorizedException,BadRequestException,InternalServerErrorException {
        LOG.log(Level.INFO, String.format("Generate request with %s to URL : %s", verb, urlRequest));
        com.github.scribejava.core.model.Response response = Plugin.getResponse(service,urlRequest,verb,lstParams);
        String body = "{}";
        if (response.getCode() == Response.Status.OK.getStatusCode()) {
            try {
                body = response.getBody();
            } catch (Exception e) {
                LOG.log(Level.SEVERE, e.getMessage(), e);
            }
            JSONObject jsonObject = new JSONObject(body);
            final int status = jsonObject.getInt(Constant.WITHINGS_STATUS_CODE);
            LOG.log(Level.INFO, String.format("Response body : %s", body));
            LOG.log(Level.INFO, String.format("status : %s", status));
            LOG.log(Level.INFO, String.format("json obj length : %s", jsonObject.length()));
            if (status == 0) {
                LOG.log(Level.INFO, String.format("creating jsA : %s", status));
                return jsonObject;
            } else {
                if (status == UNAUTHORIZED.getStatusCode()) {
                    throw new UnAuthorizedException("");
                } else {
                    throw new BadRequestException();
                }
            }
        } else {
            throw new InternalServerErrorException();
        }
    }
}