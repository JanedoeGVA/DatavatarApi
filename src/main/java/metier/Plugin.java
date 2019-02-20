package metier;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import org.eclipse.persistence.jaxb.JAXBContextFactory;
import org.eclipse.persistence.jaxb.JAXBContextProperties;
import org.eclipse.persistence.jaxb.xmlmodel.ObjectFactory;
import org.json.JSONObject;

import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.builder.api.DefaultApi10a;
import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.exceptions.OAuthException;
import com.github.scribejava.core.model.OAuth1AccessToken;
import com.github.scribejava.core.model.OAuth1RequestToken;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.OAuthConstants;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.oauth.OAuth10aService;
import com.github.scribejava.core.oauth.OAuth20Service;
import com.google.gson.Gson;

import domaine.oauth1a.Oauth1AccessToken;
import domaine.oauth1a.Oauth1Authorisation;
import domaine.oauth2.Oauth2AccessToken;
import domaine.oauth2.Oauth2Authorisation;
import outils.SymmetricAESKey;
import outils.Utils;

public class Plugin {

	private static final Logger LOG = Logger.getLogger(Plugin.class.getName());

	public static Oauth1Authorisation oauth10Authorisation(String provider,OAuth10aService service) {
		LOG.log(Level.INFO, String.format("Performing authorisation OAUTH1 for %s", provider));
		OAuth1RequestToken oauth1AuthRequest = null;
		try {
			oauth1AuthRequest = service.getRequestToken();
		} catch (IOException | InterruptedException | ExecutionException e) {
			LOG.log(Level.WARNING, e.getMessage(),e);
		}
		Oauth1Authorisation oauth1Auth = new Oauth1Authorisation();
		oauth1Auth.setRequestTokenKey(oauth1AuthRequest.getToken());
		oauth1Auth.setRequestTokenSecret(SymmetricAESKey.encrypt(oauth1AuthRequest.getTokenSecret()));
		oauth1Auth.setUrlVerification(service.getAuthorizationUrl(oauth1AuthRequest));
		oauth1Auth.setVerifier("");
		oauth1Auth.setProvider(provider);
		LOG.log(Level.INFO, "Request token created");
		return oauth1Auth;
	}

	public static Oauth2Authorisation oauth20UrlVerification(String provider,OAuth20Service service) {
		LOG.log(Level.INFO, String.format("Setting URL verification OAUTH2 for %s", provider));
		Oauth2Authorisation oauth2Auth = new Oauth2Authorisation();
		oauth2Auth.setProvider(provider);
		oauth2Auth.setUrlVerification(service.getAuthorizationUrl());
		LOG.log(Level.INFO, String.format("URL verification is %s", oauth2Auth.getUrlVerification()));
		return oauth2Auth;
	}

	public static Oauth1AccessToken oauth10AccessToken(String provider,String requestTokenKey,String encryptedRequestTokenSecret,String verifier,OAuth10aService service) {
		LOG.log(Level.INFO, String.format("Performing AccessToken OAuth1 for", provider));
		final OAuth1RequestToken requestToken = new OAuth1RequestToken(requestTokenKey, SymmetricAESKey.decrypt(encryptedRequestTokenSecret));
		OAuth1AccessToken oauth1AccessToken = null;
		try {
			oauth1AccessToken = service.getAccessToken(requestToken, verifier);
		} catch (IOException | InterruptedException | ExecutionException e) {
			LOG.log(Level.WARNING,e.getMessage(),e);
		}
		Oauth1AccessToken accessToken = new Oauth1AccessToken(provider,SymmetricAESKey.encrypt(oauth1AccessToken.getToken()),SymmetricAESKey.encrypt(oauth1AccessToken.getTokenSecret()));
		LOG.log(Level.INFO, String.format("AccessToken Oauth1 for %s created successfull",provider));
		return accessToken;
	}

	public static Oauth2AccessToken oauth20AccessToken (String provider,String code,OAuth20Service service) {
		LOG.log(Level.INFO, String.format("Performing AccessToken OAuth2 for %s", provider));
		OAuth2AccessToken oauth2accessToken = null;
		try {
			oauth2accessToken = service.getAccessToken(code);
		} catch (IOException | InterruptedException | ExecutionException e) {
			LOG.log(Level.WARNING, e.getMessage(),e);
		} catch (Exception ex) {
			LOG.log(Level.WARNING, ex.getMessage(),ex);
		}
		LOG.log(Level.INFO, "creating access token");
		Oauth2AccessToken accessToken = new Oauth2AccessToken(provider,SymmetricAESKey.encrypt(oauth2accessToken.getAccessToken()),SymmetricAESKey.encrypt(oauth2accessToken.getRefreshToken()));
		LOG.log(Level.INFO, String.format("AccessToken Oauth2 for %s created successfull",provider));
		return accessToken;
	}

	public static Oauth2AccessToken refreshAccessToken (String provider,String refreshToken, OAuth20Service service) {
		LOG.log(Level.INFO, String.format("Performing RefreshToken for", provider));
		Oauth2AccessToken oauth2accessToken = null;
		try {
			OAuth2AccessToken token = service.refreshAccessToken(SymmetricAESKey.decrypt(refreshToken));
			oauth2accessToken = new Oauth2AccessToken(provider, SymmetricAESKey.encrypt(token.getAccessToken()), SymmetricAESKey.encrypt(token.getRefreshToken()));
			LOG.log(Level.INFO, String.format("RefreshToken created for %s",provider));
		} catch (IOException e) {
			LOG.log(Level.WARNING, e.getMessage(),e);
		} catch (InterruptedException e) {
			LOG.log(Level.WARNING, e.getMessage(),e);
		} catch (ExecutionException e) {
			LOG.log(Level.WARNING, e.getMessage(),e);
		} catch (OAuthException e) {
			LOG.log(Level.WARNING, e.getMessage(),e);
		}
		return oauth2accessToken;
	}

	public static void revoke(String tokenToRevoke,OAuth20Service service) {
		LOG.log(Level.INFO, String.format("Revoking Token on %s", service.getApi().getRevokeTokenEndpoint()));
		try {
			service.revokeToken(SymmetricAESKey.decrypt(tokenToRevoke));
			LOG.log(Level.INFO, "Revoke success");
		} catch (IOException | InterruptedException | ExecutionException e) {
			LOG.log(Level.INFO, String.format("Revoke fail %s",e.getMessage()),e);
		}

	}

	public static OAuth10aService getOauth1Service(String props,String callBackURL,DefaultApi10a api) {
		LOG.log(Level.INFO, String.format("Getting OAUTH1 Service"));
		final OAuth10aService service = new ServiceBuilder(Utils.getProps(props,OAuthConstants.CLIENT_ID))
				.apiSecret(Utils.getProps(props,OAuthConstants.CLIENT_SECRET))
				.callback(callBackURL)
				.build(api);
		return service;
	}

	public static OAuth20Service getOauth2Service(String props,String callBackUrl,DefaultApi20 api) {
		LOG.log(Level.INFO, String.format("Getting OAUTH2 Service"));
		final String secretState = "secret" + new Random().nextInt(999_999);
		final OAuth20Service service = new ServiceBuilder(Utils.getProps(props,OAuthConstants.CLIENT_ID))
				.apiSecret(Utils.getProps(props,OAuthConstants.CLIENT_SECRET))
				.scope(Utils.getProps(props,OAuthConstants.SCOPE))
				.callback(callBackUrl)
				.state(secretState)
				.build(api);
		return service;
	}

	public static boolean isAccessTokenExpired (Oauth2AccessToken oauth2AccessToken) {
		long expireTime = Utils.getJSONDecodedAccessToken(SymmetricAESKey.decrypt(oauth2AccessToken.getAccessTokenKey())).getJSONObject("part_02").getLong("exp");
		long currentTime = new Date().getTime()/1000L;
		boolean isExpired = expireTime<currentTime;
		return isExpired; 
	}

	public static void stateToken (Oauth2AccessToken oauth2AccessToken) {
		long expireTime = Utils.getJSONDecodedAccessToken(SymmetricAESKey.decrypt(oauth2AccessToken.getAccessTokenKey())).getJSONObject("part_02").getLong("exp");
		long currentTime = new Date().getTime()/1000L;
		boolean isExpired = expireTime<currentTime;
		LOG.log(Level.INFO,"access token decrypt : " + SymmetricAESKey.decrypt(oauth2AccessToken.getAccessTokenKey()));
		LOG.log(Level.INFO,"refresh access token decrypt : " + SymmetricAESKey.decrypt(oauth2AccessToken.getRefreshTokenKey()));
		LOG.log(Level.INFO,"Expire time : "+ expireTime);
		LOG.log(Level.INFO,"Current time : "+ currentTime);
		LOG.log(Level.INFO,"expire in : " + (expireTime - currentTime) + "secondes");
	}

	/************************************************* Data******************************************************/
	public static <T> T unMarshallGenericJSON(String json, Class<T> classT) {
		LOG.log(Level.INFO, String.format("unMarshall processing for class %s", classT.getName()));
		Map<String, Object> properties = new HashMap<>();
		properties.put(JAXBContextProperties.MEDIA_TYPE, MediaType.APPLICATION_JSON);
		properties.put(JAXBContextProperties.JSON_INCLUDE_ROOT, false);
		properties.put(Marshaller.JAXB_FORMATTED_OUTPUT, true); // Uniquement n�cessaire si on veut Marshall
		T t = null;
		try {
			JAXBContext jaxbContext = JAXBContextFactory.createContext(new Class[]{classT,ObjectFactory.class}, properties);
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			StreamSource jsonSource = new StreamSource(new StringReader(json));
			t = unmarshaller.unmarshal(jsonSource, classT).getValue();
			LOG.log(Level.INFO, String.format("unMarshall success for class %s", classT.getName()));
		} catch (JAXBException e) {
			LOG.log(Level.SEVERE,e.getMessage(),e);
		}    
		return t;
	}

	@SuppressWarnings("unchecked")
	public static <T> ArrayList<T> unMarshallGenJSONArray(Response response, Class<T> classT) {
		LOG.log(Level.INFO, String.format("unMarshall processing for an array of class %s", classT.getName()));
		Map<String, Object> properties = new HashMap<>();
		properties.put(JAXBContextProperties.MEDIA_TYPE, MediaType.APPLICATION_JSON);
		properties.put(JAXBContextProperties.JSON_INCLUDE_ROOT, false);
		properties.put(Marshaller.JAXB_FORMATTED_OUTPUT, true); // Uniquement n�cessaire si on veut Marshall
		ArrayList<T> lstT = new ArrayList<>();
		try {
			JAXBContext jaxbContext = JAXBContextFactory.createContext(new Class[]{classT,ObjectFactory.class}, properties);
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			StreamSource jsonSource = new StreamSource(new StringReader(response.getBody()));
			lstT = (ArrayList<T>)unmarshaller.unmarshal(jsonSource, classT).getValue();
			LOG.log(Level.INFO, String.format("unMarshall success for an array of class %s", classT.getName()));
		} catch (JAXBException | IOException e) {
			LOG.log(Level.SEVERE,e.getMessage(),e);
		}
		return lstT;
	}

}
