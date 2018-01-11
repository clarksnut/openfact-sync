package org.clarksnut.services.resources;

import org.clarksnut.documents.DocumentProvider;
import org.clarksnut.models.QueryModel;
import org.clarksnut.models.SpaceProvider;
import org.clarksnut.models.UserModel;
import org.clarksnut.models.UserProvider;
import org.clarksnut.representations.idm.GenericDataRepresentation;
import org.clarksnut.representations.idm.SpaceRepresentation;
import org.clarksnut.representations.idm.UserRepresentation;
import org.clarksnut.utils.ModelToRepresentation;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Stateless
@Path("search")
@Consumes(MediaType.APPLICATION_JSON)
public class SearchService {

    @Context
    private UriInfo uriInfo;

    @Inject
    private SpaceProvider spaceProvider;

    @Inject
    private UserProvider userProvider;

    @Inject
    private DocumentProvider documentProvider;

    @Inject
    private ModelToRepresentation modelToRepresentation;

    private UserModel getUserByIdentityID(String identityID) {
        UserModel user = userProvider.getUserByIdentityID(identityID);
        if (user == null) {
            throw new NotFoundException();
        }
        return user;
    }

    @GET
    @Path("spaces")
    public GenericDataRepresentation searchSpaces(@QueryParam("q") String filterText) {
        QueryModel.Builder queryBuilder = QueryModel.builder();

        if (filterText != null) {
            queryBuilder.filterText(filterText);
        }

        List<SpaceRepresentation.Data> data = spaceProvider.getSpaces(queryBuilder.build()).stream()
                .map(f -> modelToRepresentation.toRepresentation(f, uriInfo))
                .collect(Collectors.toList());
        return new GenericDataRepresentation<>(data);
    }

    @GET
    @Path("users")
    public GenericDataRepresentation searchUsers(@QueryParam("q") String filterText) {
        QueryModel.Builder queryBuilder = QueryModel.builder();

        if (filterText != null) {
            queryBuilder.filterText(filterText);
        }

        List<UserRepresentation.Data> data = userProvider.getUsers(queryBuilder.build()).stream()
                .map(f -> modelToRepresentation.toRepresentation(f, uriInfo))
                .collect(Collectors.toList());
        return new GenericDataRepresentation<>(data);
    }

//    @GET
//    @Path("documents")
//    public Response searchDocuments(@Context HttpServletRequest httpServletRequest,
//                                    @QueryParam("q") String q) throws ErrorResponseException {
//        // Query
//        DocumentQueryRepresentation query;
//        try {
//            query = new DocumentQueryRepresentation(q);
//        } catch (ParseException e) {
//            return ErrorResponse.error("Bad query request", Response.Status.BAD_REQUEST);
//        }
//
//        // User context
//        SSOContext ssoContext = new SSOContext(httpServletRequest);
//        AccessToken accessToken = ssoContext.getParsedAccessToken();
//        String kcUserID = (String) accessToken.getOtherClaims().get("userID");
//        UserModel user = getUserByIdentityID(kcUserID);
//
//        Set<SpaceModel> ownedSpaces = user.getOwnedSpaces();
//        Set<SpaceModel> collaboratedSpaces = user.getCollaboratedSpaces();
//
//        Set<String> allSpaces = new HashSet<>();
//        allSpaces.addAll(ownedSpaces.stream().map(SpaceModel::getAssignedId).collect(Collectors.toSet()));
//        allSpaces.addAll(collaboratedSpaces.stream().map(SpaceModel::getAssignedId).collect(Collectors.toSet()));
//        if (query.getSpaces() != null && !query.getSpaces().isEmpty()) {
//            allSpaces.retainAll(query.getSpaces());
//        }
//        if (allSpaces.isEmpty()) {
//            Map<String, Object> meta = new HashMap<>();
//            meta.put("totalCount", 0);
//            return Response.ok(new GenericDataRepresentation<>(new ArrayList<>(), Collections.emptyMap(), meta)).build();
//        }
//
//        // ES
//        QueryBuilder filterTextQuery;
//        if (query.getFilterText() != null && !query.getFilterText().trim().isEmpty() && !query.getFilterText().trim().equals("*")) {
//            filterTextQuery = QueryBuilders.multiMatchQuery(query.getFilterText(),
//                    DocumentModel.ASSIGNED_ID,
//                    DocumentModel.SUPPLIER_NAME,
//                    DocumentModel.CUSTOMER_NAME,
//                    DocumentModel.SUPPLIER_ASSIGNED_ID,
//                    DocumentModel.CUSTOMER_ASSIGNED_ID
//            );
//        } else {
//            filterTextQuery = QueryBuilders.matchAllQuery();
//        }
//
//        QueryBuilder typeQuery = null;
//        if (query.getTypes() != null && !query.getTypes().isEmpty()) {
//            typeQuery = QueryBuilders.termsQuery(DocumentModel.TYPE, query.getTypes());
//        }
//
//        QueryBuilder currencyQuery = null;
//        if (query.getCurrencies() != null && !query.getCurrencies().isEmpty()) {
//            currencyQuery = QueryBuilders.termsQuery(DocumentModel.CURRENCY, query.getCurrencies());
//        }
//
//        QueryBuilder tagSQuery = null;
//        if (query.getTags() != null && !query.getTags().isEmpty()) {
//            tagSQuery = QueryBuilders.termsQuery(DocumentModel.TAGS, query.getTags());
//        }
//
//        QueryBuilder starQuery = null;
//        if (query.getStarred() != null) {
//            starQuery = QueryBuilders.termQuery(DocumentModel.STARRED, query.getStarred());
//        }
//
//        RangeQueryBuilder amountQuery = null;
//        if (query.getGreaterThan() != null || query.getLessThan() != null) {
//            amountQuery = QueryBuilders.rangeQuery(DocumentModel.AMOUNT);
//            if (query.getGreaterThan() != null) {
//                amountQuery.gte(query.getGreaterThan());
//            }
//            if (query.getLessThan() != null) {
//                amountQuery.lte(query.getLessThan());
//            }
//        }
//
//        RangeQueryBuilder issueDateQuery = null;
//        if (query.getAfter() != null || query.getBefore() != null) {
//            issueDateQuery = QueryBuilders.rangeQuery(DocumentModel.ISSUE_DATE);
//            if (query.getAfter() != null) {
//                issueDateQuery.gte(query.getAfter());
//            }
//            if (query.getBefore() != null) {
//                issueDateQuery.lte(query.getBefore());
//            }
//        }
//
//        QueryBuilder roleQuery = null;
//        if (query.getRole() != null) {
//            switch (query.getRole()) {
//                case SENDER:
//                    roleQuery = QueryBuilders.termsQuery(DocumentModel.SUPPLIER_ASSIGNED_ID, allSpaces);
//                    break;
//                case RECEIVER:
//                    roleQuery = QueryBuilders.termsQuery(DocumentModel.CUSTOMER_ASSIGNED_ID, allSpaces);
//                    break;
//                default:
//                    throw new IllegalStateException("Invalid role:" + query.getRole());
//            }
//        }
//
//        QueryBuilder supplierQuery = QueryBuilders.termsQuery(DocumentModel.SUPPLIER_ASSIGNED_ID, allSpaces);
//        QueryBuilder customerQuery = QueryBuilders.termsQuery(DocumentModel.CUSTOMER_ASSIGNED_ID, allSpaces);
//
//        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery()
//                .must(filterTextQuery)
//                .should(supplierQuery)
//                .should(customerQuery)
//                .minimumShouldMatch(1);
//        if (typeQuery != null) {
//            boolQueryBuilder.filter(typeQuery);
//        }
//        if (currencyQuery != null) {
//            boolQueryBuilder.filter(currencyQuery);
//        }
//        if (tagSQuery != null) {
//            boolQueryBuilder.filter(tagSQuery);
//        }
//        if (starQuery != null) {
//            boolQueryBuilder.filter(starQuery);
//        }
//        if (amountQuery != null) {
//            boolQueryBuilder.filter(amountQuery);
//        }
//        if (issueDateQuery != null) {
//            boolQueryBuilder.filter(issueDateQuery);
//        }
//        if (roleQuery != null) {
//            boolQueryBuilder.must(roleQuery);
//        }
//
//        String orderBy;
//        if (query.getOrderBy() == null) {
//            orderBy = DocumentModel.ISSUE_DATE;
//        } else {
//            orderBy = Stream.of(query.getOrderBy().split("(?<=[a-z])(?=[A-Z])")).collect(Collectors.joining("_")).toLowerCase();
//        }
//
//        DocumentUserQueryModel documentQuery = DocumentUserQueryModel.builder()
//                .query("{\"query\":" + boolQueryBuilder.toString() + "}", true)
//                .orderBy(orderBy, query.isAsc())
//                .offset(query.getOffset() != null ? query.getOffset() : 0)
//                .limit(query.getLimit() != null ? query.getLimit() : 10)
//                .build();
//        List<DocumentModel> documents = documentProvider.getDocumentsUser(documentQuery);
//        int totalResults = documentProvider.getDocumentsUserSize(documentQuery);
//
//        // Meta
//        Map<String, Object> meta = new HashMap<>();
//        meta.put("totalCount", totalResults);
//
//        // Links
//        Map<String, String> links = new HashMap<>();
//
//        return Response.ok(new GenericDataRepresentation<>(documents.stream()
//                .map(f -> modelToRepresentation.toRepresentation(f, uriInfo))
//                .collect(Collectors.toList()), links, meta)).build();
//    }

    public static void main(String[] args) {
        String str = "issueDate";
        String s = Stream.of(str.split("(?<=[a-z])(?=[A-Z])")).collect(Collectors.joining("_"));
        String[] split = str.split("(?<=[a-z])(?=[A-Z])");
        System.out.println(s);
    }
}