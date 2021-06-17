//Autorzy kodu źródłowego: Bartosz Panuś
//Kod został utworzony w ramach kursu Projekt Zespołowy
//na Politechnice Wrocławskiej
package com.orange.OrangeCommunicatorBackend.api.v1.account;

import com.orange.OrangeCommunicatorBackend.api.v1.account.requestBody.AccountChangePasswordRequestBody;
import com.orange.OrangeCommunicatorBackend.api.v1.account.requestBody.AccountLoginRequestBody;
import com.orange.OrangeCommunicatorBackend.api.v1.account.requestBody.AccountRefreshTokenRequestBody;
import com.orange.OrangeCommunicatorBackend.api.v1.account.requestBody.AccountRegisterRequestBody;
import com.orange.OrangeCommunicatorBackend.api.v1.account.responseBody.AccountLogoutResponseBody;
import com.orange.OrangeCommunicatorBackend.api.v1.account.responseBody.AccountRegisterResponseBody;
import com.orange.OrangeCommunicatorBackend.api.v1.account.responseBody.AccountTokenResponseBody;
import com.orange.OrangeCommunicatorBackend.api.v1.account.support.AccountExceptionSupplier;
import com.orange.OrangeCommunicatorBackend.api.v1.account.support.AccountMaper;
import com.orange.OrangeCommunicatorBackend.api.v1.account.support.exceptions.AccountExistsException;
import com.orange.OrangeCommunicatorBackend.api.v1.users.settings.support.SettingsMapper;
import com.orange.OrangeCommunicatorBackend.api.v1.users.support.UserExceptionSupplier;
import com.orange.OrangeCommunicatorBackend.config.KeycloakClientConfig;
import com.orange.OrangeCommunicatorBackend.dbEntities.Settings;
import com.orange.OrangeCommunicatorBackend.dbEntities.User;
import com.orange.OrangeCommunicatorBackend.dbRepositories.SettingsRepository;
import com.orange.OrangeCommunicatorBackend.dbRepositories.UserRepository;
import com.orange.OrangeCommunicatorBackend.generalServicies.MailService;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.ws.rs.core.Response;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@Service
public class AccountService {
    private final UserRepository userRepository;
    private final AccountMaper accountMaper;
    private final SettingsRepository settingsRepository;
    private final SettingsMapper settingsMapper;
    private final MailService mailService;

    @Value("${auth.client}")
    private String clientForAccounts;
    @Value("${auth.secret}")
    private String secretForAccounts;

    public AccountService(UserRepository userRepository, AccountMaper accountMaper, SettingsRepository settingsRepository, SettingsMapper settingsMapper, MailService mailService) {
        this.userRepository = userRepository;
        this.accountMaper = accountMaper;

        this.settingsRepository = settingsRepository;
        this.settingsMapper = settingsMapper;
        this.mailService = mailService;
    }



    public AccountTokenResponseBody login(AccountLoginRequestBody accountLoginRequestBody){
        accountLoginRequestBody.setUsername(accountLoginRequestBody.getUsername().toLowerCase(Locale.ROOT));
        String stringResponse = getToken(accountLoginRequestBody);
        return accountMaper.toAccountTokenBody(stringResponse);
    }

    public AccountTokenResponseBody refresh(AccountRefreshTokenRequestBody accountRefreshTokenRequestBody){
        String stringResponse = getRefreshToken(accountRefreshTokenRequestBody);
        return accountMaper.toAccountTokenBody(stringResponse);
    }

    public AccountLogoutResponseBody logout(String userName){
        userName = userName.toLowerCase(Locale.ROOT);

        User user = userRepository.findById(userName).orElseThrow(UserExceptionSupplier.userNotFoundException(userName));
        Keycloak keycloak = KeycloakClientConfig.keycloak();
        UsersResource userResource = keycloak.realm(KeycloakClientConfig.getRealm()).users();
        userResource.get(user.getKeycloakId()).logout();
        return new AccountLogoutResponseBody("true");
    }

    public void changePassword(AccountChangePasswordRequestBody accountChangePasswordRequestBody, String userName){
        userName = userName.toLowerCase(Locale.ROOT);

        Keycloak keycloak = KeycloakClientConfig.keycloak();
        User user = userRepository.findById(userName).orElseThrow(UserExceptionSupplier.userNotFoundException(userName));

        UsersResource usersResource = keycloak.realm(KeycloakClientConfig.getRealm()).users();
        usersResource.get(user.getKeycloakId()).resetPassword(createPasswordCredentials(accountChangePasswordRequestBody.
                getPassword()));
    }

    public AccountRegisterResponseBody register(AccountRegisterRequestBody accountRegisterRequestBody){
        accountRegisterRequestBody.setUserName(accountRegisterRequestBody.getUserName().toLowerCase(Locale.ROOT));
        if(accountRegisterRequestBody.getImgURL() != null && accountRegisterRequestBody.getImgURL().equals("")){
            accountRegisterRequestBody.setImgURL(null);
        }

        int statusId = 0;
        try {
            Keycloak keycloak = KeycloakClientConfig.keycloak();
            UsersResource usersResource = keycloak.realm(KeycloakClientConfig.getRealm()).users();

            UserRepresentation user = createUserRepresenatation(accountRegisterRequestBody.getUserName());
            Response result = usersResource.create(user);

            statusId = result.getStatus();

            if (statusId == HttpStatus.SC_CREATED) {

                String userId = result.getLocation().getPath().replaceAll(".*/([^/]+)$", "$1");

                usersResource.get(userId).resetPassword(createPasswordCredentials(accountRegisterRequestBody.
                        getPassword()));

                user.setEmail(accountRegisterRequestBody.geteMail());
                usersResource.get(userId).update(user);

                RealmResource realmResource = keycloak.realm(KeycloakClientConfig.getRealm());

                RoleRepresentation roleRepresentation = realmResource.roles().get("ROLE_USER").toRepresentation();
                realmResource.users().get(userId).roles().realmLevel().add(Arrays.asList(roleRepresentation));
                User u = accountMaper.toUser(accountRegisterRequestBody);
                u.setKeycloakId(userId);
                try {
                    userRepository.save(u);
                    Settings settings = settingsMapper.createDefaultSettings(u);
                    settingsRepository.save(settings);

                    sendConfirm(u);


                } catch (Exception e) {
                    e.printStackTrace();
                    usersResource.get(userId).remove();
                    if (userRepository.existsById(u.getUserName())) {
                        userRepository.deleteById(u.getUserName());
                    }
                    throw AccountExceptionSupplier.creatingAccountException().get();
                }
                return accountMaper.toAccountRegisterResponse(u);
            }

            else if (statusId == HttpStatus.SC_CONFLICT) {
                throw AccountExceptionSupplier.accountExistsException().get();
            } else {
                throw AccountExceptionSupplier.creatingAccountException().get();
            }

        } catch (AccountExistsException e){
            throw AccountExceptionSupplier.accountExistsException().get();
        } catch (Exception e) {
            e.printStackTrace();
            throw AccountExceptionSupplier.creatingAccountException().get();
        }
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
        return passwordCred;
    }

    private String getToken(AccountLoginRequestBody accountLoginRequestBody){
        String responseToken = null;
        try {

            List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
            urlParameters.add(new BasicNameValuePair("grant_type", "password"));
            urlParameters.add(new BasicNameValuePair("client_id", clientForAccounts));
            urlParameters.add(new BasicNameValuePair("username", accountLoginRequestBody.getUsername()));
            urlParameters.add(new BasicNameValuePair("password", accountLoginRequestBody.getPassword()));
            urlParameters.add(new BasicNameValuePair("client_secret", secretForAccounts));


            responseToken = sendPost(urlParameters);

        } catch (Exception e) {
            e.printStackTrace();
            throw AccountExceptionSupplier.tokenAcquireException().get();
        }

        return responseToken;

    }

    private String sendPost(List<NameValuePair> urlParameters) throws Exception {
        HttpClient client = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost(KeycloakClientConfig.getAuthUrl() + "/realms/" +
                KeycloakClientConfig.getRealm() + "/protocol/openid-connect/token");

        post.setEntity(new UrlEncodedFormEntity(urlParameters));

        HttpResponse response = client.execute(post);
        if(response.getStatusLine().getStatusCode() != HttpStatus.SC_OK){
            throw AccountExceptionSupplier.tokenAcquireException().get();
        }

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
            urlParameters.add(new BasicNameValuePair("client_id", clientForAccounts));
            urlParameters.add(new BasicNameValuePair("refresh_token", accountRefreshTokenRequestBody.getRefreshToken()));
            urlParameters.add(new BasicNameValuePair("client_secret", secretForAccounts));

            responseToken = sendPost(urlParameters);

        } catch (Exception e) {
            e.printStackTrace();
            throw AccountExceptionSupplier.tokenAcquireException().get();
        }
        return responseToken;
    }

    private void sendConfirm(User user) throws MessagingException {
        String email = user.getEMail();
        String subject = "Kaliber-Confirmation";
        String emailText = "Hey,<br>Account of user \"" + user.getUserName() + "\" was successfully created!!<br>";
        mailService.sendMail(email, subject, emailText, true);
    }

}
