package metier;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.ExecutionException;


import javax.ws.rs.core.MediaType;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

import org.eclipse.persistence.jaxb.JAXBContextFactory;
import org.eclipse.persistence.jaxb.JAXBContextProperties;
import org.eclipse.persistence.jaxb.xmlmodel.ObjectFactory;

import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.builder.api.DefaultApi10a;
import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.exceptions.OAuthException;
import com.github.scribejava.core.model.OAuth1AccessToken;
import com.github.scribejava.core.model.OAuth1RequestToken;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.OAuthConstants;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth10aService;
import com.github.scribejava.core.oauth.OAuth20Service;

import domaine.oauth.OauthAccessToken;
import domaine.oauth.ProtectedDataOauth;
import domaine.oauth.ProtectedListDataOauth;
import domaine.oauth1a.Oauth1AccessToken;
import domaine.oauth1a.Oauth1Authorisation;
import domaine.oauth2.Oauth2AccessToken;
import domaine.oauth2.Oauth2Authorisation;
import domaine.oauth2.ProtectedDataOauth2;
import outils.SymmetricAESKey;
import outils.Utils;


public class Plugin {
	
	public static Oauth1Authorisation oauth10Authorisation(String apiName,OAuth10aService service) {
		OAuth1RequestToken oauth1AuthRequest = null;
		try {
			oauth1AuthRequest = service.getRequestToken();
		} catch (IOException | InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Oauth1Authorisation oauth1Auth = new Oauth1Authorisation();
		System.out.println("@oauth10Authorisation Request token = " + oauth1AuthRequest.getToken());
        System.out.println("@oauth10Authorisation Request Secret = " + oauth1AuthRequest.getTokenSecret());
		oauth1Auth.setRequestTokenKey(oauth1AuthRequest.getToken());
		oauth1Auth.setRequestTokenSecret(SymmetricAESKey.encrypt(oauth1AuthRequest.getTokenSecret()));
		oauth1Auth.setUrlVerification(service.getAuthorizationUrl(oauth1AuthRequest));
		oauth1Auth.setVerifier("");
		oauth1Auth.setApi(apiName);
		return oauth1Auth;
	}
	
	public static Oauth2Authorisation oauth20UrlVerification(String apiName,OAuth20Service service) {
    		Oauth2Authorisation oauth2Auth = new Oauth2Authorisation();
    		oauth2Auth.setApi(apiName);
    		oauth2Auth.setUrlVerification(service.getAuthorizationUrl());
        return oauth2Auth;
    }
	
	public static Oauth1AccessToken oauth10AccessToken(String api,String requestTokenKey,String encryptedRequestTokenSecret,String verifier,OAuth10aService service) {
		final OAuth1RequestToken requestToken = new OAuth1RequestToken(requestTokenKey, SymmetricAESKey.decrypt(encryptedRequestTokenSecret));
		System.out.println("@oauth10AccessToken Request token = " + requestToken.getToken());
        System.out.println("@oauth10AccessToken Request Secret = " + requestToken.getTokenSecret());
        OAuth1AccessToken oauth1AccessToken = null;
		try {
			oauth1AccessToken = service.getAccessToken(requestToken, verifier);
		} catch (IOException | InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        Oauth1AccessToken accessToken = new Oauth1AccessToken(api,SymmetricAESKey.encrypt(oauth1AccessToken.getToken()),SymmetricAESKey.encrypt(oauth1AccessToken.getTokenSecret()),true);
        System.out.println("@oauth10AccessToken Access Token = " + oauth1AccessToken.getToken());
        System.out.println("@oauth10AccessToken AccessSecret = " + oauth1AccessToken.getTokenSecret());
        return accessToken;
	}
	
	public static Oauth2AccessToken oauth20AccessToken (String api,String code,OAuth20Service service) {
    	OAuth2AccessToken oauth2accessToken = null;
		try {
			oauth2accessToken = service.getAccessToken(code);
		} catch (IOException | InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	Oauth2AccessToken accessToken = new Oauth2AccessToken(api,SymmetricAESKey.encrypt(oauth2accessToken.getAccessToken()),SymmetricAESKey.encrypt(oauth2accessToken.getRefreshToken()),true);
    	return accessToken;
    }
	
	public static void refreshAccessToken (Oauth2AccessToken accessToken, OAuth20Service service) {
		OAuth2AccessToken oauth2accessToken = null;
		try {
			oauth2accessToken = service.refreshAccessToken(SymmetricAESKey.decrypt(accessToken.getRefreshTokenKey()));
			System.out.println("REFRESHTOKEN");
		} catch (IOException e) {
			System.out.println("IO");
			e.printStackTrace();
		} catch (InterruptedException e) {
			System.out.println("Interrupted");
			e.printStackTrace();
		} catch (ExecutionException e) {
			System.out.println("Execution");
			e.printStackTrace();
		} catch (OAuthException oauthe) {
			oauthe.printStackTrace();
			System.out.println("accessTokensettofalse");
			accessToken.setIsValide(false);
			return;
		}
    	accessToken.setAccessTokenKey(SymmetricAESKey.encrypt(oauth2accessToken.getAccessToken()));
    	accessToken.setRefreshTokenKey(SymmetricAESKey.encrypt(oauth2accessToken.getRefreshToken()));
	}
	
	public static void revoke(String tokenToRevoke,OAuth20Service service) {
		try {
			service.revokeToken(SymmetricAESKey.decrypt(tokenToRevoke));
		} catch (IOException | InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public static OAuth10aService getOauth1Service(String props,String callBackURL,DefaultApi10a api) {
		System.out.println("@getOauth1Service api = "+ api);
		final OAuth10aService service = new ServiceBuilder(Utils.getProps(props,OAuthConstants.CLIENT_ID))
				.apiSecret(Utils.getProps(props,OAuthConstants.CLIENT_SECRET))
				.callback(callBackURL)
				.debug()
				.build(api);
		return service;
	}

	public static OAuth20Service getOauth2Service(String props,String callBackUrl,DefaultApi20 api) {
    	final String secretState = "secret" + new Random().nextInt(999_999);
    	final OAuth20Service service = new ServiceBuilder(Utils.getProps(props,OAuthConstants.CLIENT_ID))
                .apiSecret(Utils.getProps(props,OAuthConstants.CLIENT_SECRET))
                .scope(Utils.getProps(props,OAuthConstants.SCOPE))
                .callback(callBackUrl)
                .state(secretState)
                .debug()
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
		System.out.println("access token decrypt : " + SymmetricAESKey.decrypt(oauth2AccessToken.getAccessTokenKey()));
		System.out.println("refresh access token decrypt : " + SymmetricAESKey.decrypt(oauth2AccessToken.getRefreshTokenKey()));
		System.out.println("Expire time : "+ expireTime);
		System.out.println("Current time : "+ currentTime);
		System.out.println("expire in : " + (expireTime - currentTime) + "secondes");
	}
	
	/************************************************* Data******************************************************/
	public static <T> T unMarshallGenericJSON(Response response, Class<T> classT) {
        Map<String, Object> properties = new HashMap<>();
        properties.put(JAXBContextProperties.MEDIA_TYPE, MediaType.APPLICATION_JSON);
        properties.put(JAXBContextProperties.JSON_INCLUDE_ROOT, false);
        properties.put(Marshaller.JAXB_FORMATTED_OUTPUT, true); // Uniquement n�cessaire si on veut Marshall
        T t = null;
		try {
			JAXBContext jaxbContext = JAXBContextFactory.createContext(new Class[]{classT,ObjectFactory.class}, properties);
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			StreamSource jsonSource = new StreamSource(new StringReader(response.getBody()));
		    t = unmarshaller.unmarshal(jsonSource, classT).getValue();
		    //Ne sert � rien illustre juste comment on peut passer de l'objet au JSON
	        Marshaller marshaller = jaxbContext.createMarshaller();
	        marshaller.marshal(t,System.out);
	        System.out.println();
	        StringWriter sw = new StringWriter();
	        marshaller.marshal(t,sw);
	        System.out.println("@marshallJson Marshall = " + sw.toString()); 
		} catch (JAXBException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}    
        return t;
	}

	@SuppressWarnings("unchecked")
	public static <T> ArrayList<T> unMarshallGenJSONArray(Response response, Class<T> classT) {
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
	        //Ne sert � rien illustre juste comment on peut passer de l'objet au JSON
	        Marshaller marshaller = jaxbContext.createMarshaller();
	        marshaller.marshal(lstT,System.out);
	        System.out.println();
	        StringWriter sw = new StringWriter();
	        marshaller.marshal(lstT,sw);
	        System.out.println("@marshallJson Marshall = " + sw.toString()); 
		} catch (JAXBException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return lstT;
	}
	
	/*
	public static <T> ProtectedListDataOauth<T,Oauth1AccessToken> getGenericListProtectedRessources(Oauth1AccessToken accessToken,OAuth10aService service,Verb verb,Class<T> classT,String urlApiRequest) {
		final OAuth1AccessToken oAuth1AccessToken = new OAuth1AccessToken(SymmetricAESKey.decrypt(accessToken.getAccessTokenKey()),SymmetricAESKey.decrypt(accessToken.getAccessTokenSecret()));
		final OAuthRequest request = new OAuthRequest(verb, urlApiRequest);
		System.out.println("Headers " + request.getHeaders().toString());
	    service.signRequest(oAuth1AccessToken, request);
	    System.out.println("Owner token " + oAuth1AccessToken.getToken());
	    System.out.println("Owner secret " + oAuth1AccessToken.getTokenSecret());
	    Response resp = null;
	    ProtectedListDataOauth<T,Oauth1AccessToken> protectedLstDataOauth = new ProtectedListDataOauth<>();
		try {
			resp = service.execute(request);
			System.out.println("response " + resp.getCode() + resp.getBody());
		    System.out.println("Request body : " + resp.getBody());
		    if (resp.getCode() == javax.ws.rs.core.Response.Status.FORBIDDEN.getStatusCode()) { //error 403 token has been revoke
		    	accessToken.setIsValide(false);
		    }
		} catch (InterruptedException | ExecutionException | IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		System.out.println("Request code : " + resp.getCode());
	    ArrayList<T> lstT = new ArrayList<>();
	    try {
	    	lstT = unMarshallGenJSONArray(resp,classT);
	    } catch (JAXBException e) {
	    	// TODO Auto-generated catch block
	    	e.printStackTrace();
	    }
	    protectedLstDataOauth.setOauthAccessTokenT(accessToken);
	    protectedLstDataOauth.setLstProtectedDataT(lstT);
        System.out.println("is valide : " + accessToken.getIsValide()); 
        return protectedLstDataOauth;
		
	}
	
	public static <T> ProtectedDataOauth<T,Oauth2AccessToken> getGenericProtectedRessources(Oauth2AccessToken accessToken, OAuth20Service service, Class<T> classT, Verb verb, String urlRequest) {
		//******************************************************
		//TODO code just for printing result
		System.out.println("is valide : " + accessToken.getIsValide()); 
		stateToken(accessToken);
		if (isAccessTokenExpired(accessToken)) {
			System.out.println("ACCESS TOKEN NEED TO BE REFRESH!!!!!");
		}
		//*****************************************************
        OAuthRequest request = new OAuthRequest(verb, urlRequest);
        request.addHeader("x-li-format", "json");
        //add header for authentication (Fitbit complication..... :()
        request.addHeader("Authorization", "Bearer " + SymmetricAESKey.decrypt(accessToken.getAccessTokenKey()));
        Response response = null;
		try {
			response = service.execute(request);
		} catch (InterruptedException | ExecutionException | IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        System.out.println("Response code/message : " + response.getCode() + response.getMessage());
        ProtectedDataOauth<T,Oauth2AccessToken> protectedDataOauth = new ProtectedDataOauth<>();
        if (response.getCode() == javax.ws.rs.core.Response.Status.UNAUTHORIZED.getStatusCode()) {
        	System.out.println("Refreshing processing...");
        	refreshAccessToken(accessToken, service);
        	if (accessToken.getIsValide()) {
        		request = new OAuthRequest(verb, urlRequest);
                request.addHeader("x-li-format", "json");
                //add header for authentication (Fitbit complication..... :()
                request.addHeader("Authorization", "Bearer " + SymmetricAESKey.decrypt(accessToken.getAccessTokenKey()));
            	try {
    				response = service.execute(request);
    			} catch (InterruptedException | ExecutionException | IOException e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			}
        	} else {
        		protectedDataOauth.setOauthAccessTokenT(accessToken);
        		protectedDataOauth.setProtectedDataT(null);
        		System.out.println("Invalide token...");
        		return protectedDataOauth;
        	}
        }
       
        String json = null;
        try {
        	json = response.getBody();
        } catch (IOException e) {
        	// TODO Auto-generated catch block
        	e.printStackTrace();
        }
        T t = unMarshallGenericJSON(json,classT);
        protectedDataOauth.setOauthAccessTokenT(accessToken);
        protectedDataOauth.setProtectedDataT(t);
        System.out.println("is valide : " + accessToken.getIsValide()); 
        return protectedDataOauth;
	}
	
	public static <T> ProtectedDataOauth<T, Oauth1AccessToken> getGenericProtectedRessources (Oauth1AccessToken accessToken, OAuth10aService service, Class<T> classT, Verb verb, String urlRequest) {
		final OAuth1AccessToken oAuth1AccessToken = new OAuth1AccessToken(SymmetricAESKey.decrypt(accessToken.getAccessTokenKey()),SymmetricAESKey.decrypt(accessToken.getAccessTokenSecret()));
		final OAuthRequest request = new OAuthRequest(verb, urlRequest);
	    service.signRequest(oAuth1AccessToken, request);
	    Response response = null;
		try {
			response = service.execute(request);
			System.out.println("response " + response.getCode() + response.getBody());
			//TODO si le toke a �t� revoke trouver un moyen de r�cup�rer l'erreur et envoyer le token invalider
		} catch (InterruptedException | ExecutionException | IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
	    String json = null;
	    try {
    		json = response.getBody();
	    } catch (Exception e) {
			// TODO: handle exception
		}
        T t = unMarshallGenericJSON(json,classT);
        ProtectedDataOauth<T, Oauth1AccessToken> protectedDataOauth = new ProtectedDataOauth<>();
        protectedDataOauth.setOauthAccessTokenT(accessToken);
        protectedDataOauth.setProtectedDataT(t);
        return protectedDataOauth;
	}
	
	public static <T> T getGenericProtectedRessourcesP(Oauth1AccessToken accessToken, OAuth10aService service, Class<T> classT, Verb verb, String urlRequest){
		final OAuth1AccessToken oAuth1AccessToken = new OAuth1AccessToken(SymmetricAESKey.decrypt(accessToken.getAccessTokenKey()),SymmetricAESKey.decrypt(accessToken.getAccessTokenSecret()));
		final OAuthRequest request = new OAuthRequest(verb, urlRequest);
	    service.signRequest(oAuth1AccessToken, request);
	    Response response = null;
		try {
			response = service.execute(request);
		} catch (InterruptedException | ExecutionException | IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	    String json = null;
	    try {
    		json = response.getBody();
	    } catch (Exception e) {
			// TODO: handle exception
		}
        T t = unMarshallGenericJSON(json,classT);
        return t;
	}*/

	
}
