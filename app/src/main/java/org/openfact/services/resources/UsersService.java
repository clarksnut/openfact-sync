package org.openfact.services.resources;

import org.jboss.logging.Logger;
import org.keycloak.jose.jws.JWSInputException;
import org.keycloak.representations.AccessToken;
import org.keycloak.util.TokenUtil;
import org.openfact.models.QueryModel;
import org.openfact.models.SpaceProvider;
import org.openfact.models.UserModel;
import org.openfact.models.UserProvider;
import org.openfact.models.utils.ModelToRepresentation;
import org.openfact.representation.idm.*;
import org.openfact.services.managers.UserManager;
import org.openfact.services.resources.utils.PATCH;
import org.openfact.services.util.SSOContext;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
import java.util.stream.Collectors;

@Stateless
@Path("/users")
@Consumes(MediaType.APPLICATION_JSON)
public class UsersService {

    private static final Logger logger = Logger.getLogger(UsersService.class);

    @Context
    private UriInfo uriInfo;

    @Inject
    private SpaceProvider spaceProvider;

    @Inject
    private UserProvider userProvider;

    @Inject
    private UserManager userManager;

    @Inject
    private ModelToRepresentation modelToRepresentation;

    private UserModel getUserByIdentityID(String identityID) {
        UserModel user = userProvider.getUserByIdentityID(identityID);
        if (user == null) {
            throw new NotFoundException();
        }
        return user;
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public UserRepresentation updateExtProfile(@Context final HttpServletRequest httpServletRequest,
                                               final UserRepresentation userRepresentation) {
        SSOContext ssoContext = new SSOContext(httpServletRequest);
        AccessToken accessToken = ssoContext.getParsedAccessToken();

        String kcUserID = (String) accessToken.getOtherClaims().get("userID");
        UserModel user = getUserByIdentityID(kcUserID);

        // Offline token
        UserAttributesRepresentation attributes = userRepresentation.getData().getAttributes();

        if (attributes != null && attributes.getRefreshToken() != null) {
            String offlineToken = attributes.getRefreshToken();
            if (offlineToken != null) {
                try {
                    if (TokenUtil.isOfflineToken(offlineToken)) {
                        user.setOfflineRefreshToken(offlineToken);
                    } else {
                        throw new BadRequestException("Invalid Token Type");
                    }
                } catch (JWSInputException e) {
                    logger.error("Could not decode token", e);
                }
            }
        }

        // Is registration completed
        if (attributes != null) {
            Boolean registrationCompleted = attributes.getRegistrationCompleted();
            if (registrationCompleted != null) {
                user.setRegistrationCompleted(registrationCompleted);
            }
        }

        // Build result
        return modelToRepresentation.toRepresentation(user, uriInfo).toUserRepresentation();
    }

    @PATCH
    @Produces(MediaType.APPLICATION_JSON)
    public UserRepresentation saveRecentContexts(@Context final HttpServletRequest httpServletRequest,
                                   final UserRepresentation userRepresentation) {

        SSOContext ssoContext = new SSOContext(httpServletRequest);
        AccessToken accessToken = ssoContext.getParsedAccessToken();

        String kcUserID = (String) accessToken.getOtherClaims().get("userID");
        UserModel user = getUserByIdentityID(kcUserID);

        // Build result
        return modelToRepresentation.toRepresentation(user, uriInfo).toUserRepresentation();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public GenericDataRepresentation getUsers(@QueryParam("filter[username]") String usernameFilter) {
        QueryModel.Builder queryBuilder = QueryModel.builder();

        if (usernameFilter != null) {
            queryBuilder.addFilter(UserModel.USERNAME, usernameFilter);
        }

        return new GenericDataRepresentation(userProvider.getUsers(queryBuilder.build()).stream()
                .map(f -> modelToRepresentation.toRepresentation(f, uriInfo))
                .collect(Collectors.toList()));
    }

    @GET
    @Path("{identityID}")
    @Produces(MediaType.APPLICATION_JSON)
    public UserRepresentation getUser(@PathParam("identityID") String identityID) {
        UserModel user = getUserByIdentityID(identityID);
        return modelToRepresentation.toRepresentation(user, uriInfo).toUserRepresentation();
    }

//    @GET
//    @Path("/{userId}/repositories")
//    @Produces(MediaType.APPLICATION_JSON)
//    public List<RepositoryRepresentation> getRepositories(@PathParam("userId") String userId) {
//        UserModel user = getUser(userId);
//
//        return user.getRepositories().stream()
//                .map(f -> modelToRepresentation.toRepresentation(f))
//                .collect(Collectors.toList());
//    }
//
//    @POST
//    @Path("/{userId}/repositories")
//    @Produces(MediaType.APPLICATION_JSON)
//    public void refreshRepositories(@PathParam("userId") String userId) {
//        UserModel user = getUser(userId);
//
//        userManager.refreshUserAvailableRepositories(user);
//        userManager.syncUserRepositories(user);
//    }
//
//    /**
//     * Search spaces from user and getInstance back it to you.
//     *
//     * @return spaces from user
//     */
//    @GET
//    @Path("/{userId}/spaces")
//    @Produces(MediaType.APPLICATION_JSON)
//    public List<SpaceRepresentation> getSpaces(@PathParam("userId") String userId) {
//        UserModel user = getUser(userId);
//
//        Stream<SpaceRepresentation> sharedSpaces = user.getSharedSpaces().stream()
//                .map(f -> modelToRepresentation.toRepresentation(f));
//        Stream<SpaceRepresentation> ownedSpaces = user.getOwnedSpaces().stream()
//                .map(f -> modelToRepresentation.toRepresentation(f, true));
//
//        return Stream.concat(ownedSpaces, sharedSpaces).collect(Collectors.toList());
//    }
//
//    @POST
//    @Path("/{userId}/spaces")
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response createSpace(@PathParam("userId") String userId, final SpaceRepresentation rep) {
//        UserModel user = getUser(userId);
//
//        SpaceModel space = spaceProvider.getByAssignedId(rep.getAssignedId());
//        if (space == null) {
//            // Claim space
//            space = spaceProvider.addSpace(rep.getAssignedId(), user);
//        } else {
//            space.requestAccess(user, new HashSet<>(Collections.singletonList(PermissionType.READ)));
//        }
//
//        URI location = uriInfo.getBaseUriBuilder().path(space.getId()).build();
//        logger.debugv("space claimed success, sending back: {0}", location.toString());
//
//        return Response.status(Response.Status.CREATED).entity(modelToRepresentation.toRepresentation(space, false)).build();
//    }

}