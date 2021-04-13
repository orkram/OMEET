package com.orange.OrangeCommunicatorBackend.api.v1.account;

import com.orange.OrangeCommunicatorBackend.api.v1.account.requestBody.AccountRegisterRequestBody;
import com.orange.OrangeCommunicatorBackend.api.v1.account.responseBody.AccountRegisterResponseBody;
import com.orange.OrangeCommunicatorBackend.api.v1.account.support.AccountMaper;
import com.orange.OrangeCommunicatorBackend.config.KeycloakClientConfig;
import com.orange.OrangeCommunicatorBackend.dbEntities.User;
import com.orange.OrangeCommunicatorBackend.dbRepositories.UserRepository;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;

import javax.ws.rs.core.Response;
import java.util.Arrays;

@Service
public class AccountService {
    private final UserRepository userRepository;
    private final AccountMaper accountMaper;

    public AccountService(UserRepository userRepository, AccountMaper accountMaper) {
        this.userRepository = userRepository;
        this.accountMaper = accountMaper;
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

}
