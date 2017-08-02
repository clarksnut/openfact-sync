package org.openfact.models;

import java.util.List;
import java.util.Set;

public interface UserModel {

    String getId();
    String getUsername();

    String getFullName();
    void setFullName(String fullName);

    boolean isRegistrationCompleted();
    void setRegistrationCompleted(boolean registrationCompleted);

    String getOfflineRefreshToken();
    void setOfflineRefreshToken(String refreshToken);

    Set<SpaceModel> getOwnedSpaces();
    Set<SharedSpaceModel> getSharedSpaces();

    List<RequestAccessToSpaceModel> getSpaceRequests();

    UserRepositoryModel addRepository(String name);
    List<UserRepositoryModel> getRepositories();
    boolean removeAllRepositories();

}
