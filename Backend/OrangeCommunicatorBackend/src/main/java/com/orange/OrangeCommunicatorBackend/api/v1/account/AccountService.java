package com.orange.OrangeCommunicatorBackend.api.v1.account;

import com.orange.OrangeCommunicatorBackend.api.v1.account.requestBody.AccountLoginRequestBody;
import com.orange.OrangeCommunicatorBackend.api.v1.account.requestBody.AccountRefreshTokenRequestBody;
import com.orange.OrangeCommunicatorBackend.api.v1.account.requestBody.AccountRegisterRequestBody;
import com.orange.OrangeCommunicatorBackend.api.v1.account.responseBody.AccountRegisterResponseBody;
import com.orange.OrangeCommunicatorBackend.api.v1.account.responseBody.AccountTokenBody;
import com.orange.OrangeCommunicatorBackend.api.v1.account.support.AccountMaper;
import com.orange.OrangeCommunicatorBackend.config.KeycloakClientConfig;
import com.orange.OrangeCommunicatorBackend.dbEntities.User;
import com.orange.OrangeCommunicatorBackend.dbRepositories.UserRepository;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.jdbc.datasource.UserCredentialsDataSourceAdapter;
import org.springframework.stereotype.Service;

import javax.ws.rs.core.Response;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class AccountService {
    private final UserRepository userRepository;
    private final AccountMaper accountMaper;

    public AccountService(UserRepository userRepository, AccountMaper accountMaper) {
        this.userRepository = userRepository;
        this.accountMaper = accountMaper;
    }

    public AccountTokenBody login(AccountLoginRequestBody accountLoginRequestBody){
        String stringResponse = getToken(accountLoginRequestBody);

        return accountMaper.toAccountTokenBody(stringResponse);
    }

    public AccountTokenBody refresh(AccountRefreshTokenRequestBody accountRefreshTokenRequestBody){
        String stringResponse = getRefreshToken(accountRefreshTokenRequestBody);
        return accountMaper.toAccountTokenBody(stringResponse);
    }

    public AccountRegisterResponseBody register(AccountRegisterRequestBody accountRegisterRequestBody){

        int statusId = 0;
        try {
            Keycloak keycloak = KeycloakClientConfig.keycloak();
            UsersResource usersRessource = keycloak.realm(KeycloakClientConfig.getRealm()).users();

            UserRepresentation user = createUserRepresenatation(accountRegisterRequestBody.getUserName());
            Response result = usersRessource.create(user);

            statusId = result.getStatus();

            if (statusId == 201) {

                String userId = result.getLocation().getPath().replaceAll(".*/([^/]+)$", "$1");

                usersRessource.get(userId).resetPassword(createPasswordCredentials(accountRegisterRequestBody.
                        getPassword()));


                RealmResource realmResource = keycloak.realm(KeycloakClientConfig.getRealm());

                RoleRepresentation roleRepresentation = realmResource.roles().get("ROLE_USER").toRepresentation();
                realmResource.users().get(userId).roles().realmLevel().add(Arrays.asList(roleRepresentation));
                User u = accountMaper.toUser(accountRegisterRequestBody);
                u.setKeycloak_id(userId);
                userRepository.save(u);

                return accountMaper.toAccountRegisterResponse(u);

            }

            else if (statusId == 409) {


            } else {


            }

        } catch (Exception e) {
            e.printStackTrace();

        }

        return null;

    }


    private UserRepresentation createUserRepresenatation(String userName){
        UserRepresentation user = new UserRepresentation();
        user.setUsername(userName);
        user.setEnabled(true);
        return user;
    }

    private CredentialRepresentation createPasswordCredentials(String password){
        CredentialRepresentation passwordCred = new CredentialRepresentation();
        passwordCred.setTemporary(false);
        passwordCred.setType(CredentialRepresentation.PASSWORD);
        passwordCred.setValue(password);
        return  passwordCred;
    }

    private String getToken(AccountLoginRequestBody accountLoginRequestBody){
        String responseToken = null;
        try {

            List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
            urlParameters.add(new BasicNameValuePair("grant_type", "password"));
            urlParameters.add(new BasicNameValuePair("client_id", accountLoginRequestBody.getClient_id()));
            urlParameters.add(new BasicNameValuePair("username", accountLoginRequestBody.getUsername()));
            urlParameters.add(new BasicNameValuePair("password", accountLoginRequestBody.getPassword()));
            urlParameters.add(new BasicNameValuePair("client_secret", accountLoginRequestBody.getClient_secret()));

            responseToken = sendPost(urlParameters);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return responseToken;

    }

    private String sendPost(List<NameValuePair> urlParameters) throws Exception {
        HttpClient client = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost(KeycloakClientConfig.getAuthUrl() + "/realms/" +
                KeycloakClientConfig.getRealm() + "/protocol/openid-connect/token");

        post.setEntity(new UrlEncodedFormEntity(urlParameters));

        HttpResponse response = client.execute(post);

        BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

        StringBuffer result = new StringBuffer();
        String line = "";
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }

        return result.toString();
    }

    private String getRefreshToken(AccountRefreshTokenRequestBody accountRefreshTokenRequestBody) {

        String responseToken = null;
        try {

            List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
            urlParameters.add(new BasicNameValuePair("grant_type", "refresh_token"));
            urlParameters.add(new BasicNameValuePair("client_id", accountRefreshTokenRequestBody.getClient_id()));
            urlParameters.add(new BasicNameValuePair("refresh_token", accountRefreshTokenRequestBody.getRefresh_token()));
            urlParameters.add(new BasicNameValuePair("client_secret", accountRefreshTokenRequestBody.getClient_secret()));

            responseToken = sendPost(urlParameters);

        } catch (Exception e) {
            e.printStackTrace();

        }

        return responseToken;

    }

}
