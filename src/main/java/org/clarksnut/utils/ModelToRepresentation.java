package org.clarksnut.utils;

import org.clarksnut.models.DocumentModel;
import org.clarksnut.models.DocumentVersionModel;
import org.clarksnut.models.IndexedDocumentModel;
import org.clarksnut.models.*;
import org.clarksnut.representations.idm.*;
import org.clarksnut.services.resources.DocumentsService;
import org.clarksnut.services.resources.SpacesService;
import org.clarksnut.services.resources.UsersService;

import javax.ejb.Stateless;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Stateless
public class ModelToRepresentation {

    public UserRepresentation.Data toRepresentation(UserModel model, UriInfo uriInfo) {
        UserRepresentation.Data rep = new UserRepresentation.Data();

        rep.setId(model.getIdentityID());
        rep.setType(ModelType.IDENTITIES.getAlias());

        // Links
        GenericLinksRepresentation links = new GenericLinksRepresentation();
        URI self = uriInfo.getBaseUriBuilder()
                .path(UsersService.class)
                .path(UsersService.class, "getUser")
                .build(model.getIdentityID());
        links.setSelf(self.toString());

        rep.setLinks(links);

        // Attributes
        UserAttributesRepresentation attributes = new UserAttributesRepresentation();
        attributes.setUserID(model.getId());
        attributes.setIdentityID(model.getIdentityID());
        attributes.setProviderType(model.getProviderType());
        attributes.setUsername(model.getUsername());
        attributes.setFullName(model.getFullName());
        attributes.setRegistrationCompleted(model.isRegistrationCompleted());
        attributes.setBio(model.getBio());
        attributes.setEmail(model.getEmail());
        attributes.setCompany(model.getCompany());
        attributes.setImageURL(model.getImageURL());
        attributes.setUrl(model.getUrl());
        attributes.setCreatedAt(model.getCreatedAt());
        attributes.setUpdatedAt(model.getUpdatedAt());

        attributes.setFavoriteSpaces(model.getFavoriteSpaces());
        attributes.setOwnedSpaces(model.getOwnedSpaces().stream().map(SpaceModel::getId).collect(Collectors.toSet()));
        attributes.setCollaboratedSpaces(model.getCollaboratedSpaces().stream().map(SpaceModel::getId).collect(Collectors.toSet()));

        attributes.setDefaultLanguage(model.getDefaultLanguage());

        rep.setAttributes(attributes);
        return rep;
    }

    public SpaceRepresentation.Data toRepresentation(SpaceModel model, UriInfo uriInfo) {
        SpaceRepresentation.Data rep = new SpaceRepresentation.Data();

        rep.setId(model.getId());
        rep.setType(ModelType.SPACES.getAlias());

        // Links
        GenericLinksRepresentation links = new GenericLinksRepresentation();
        URI self = uriInfo.getBaseUriBuilder()
                .path(SpacesService.class)
                .path(SpacesService.class, "getSpace")
                .build(model.getId());
        links.setSelf(self.toString());

        rep.setLinks(links);

        // Relationships
        SpaceRepresentation.Relationships relationships = new SpaceRepresentation.Relationships();
        List<SpaceRepresentation.OwnedBy> owners = new ArrayList<>();
        relationships.setOwnedBy(owners);
        rep.setRelationships(relationships);

        for (UserModel user : model.getOwners()) {
            UserRepresentation.Data userData = new UserRepresentation.Data();
            userData.setId(user.getIdentityID());
            userData.setType(ModelType.IDENTITIES.getAlias());
            userData.setScope(PermissionType.OWNER.toString());

            SpaceRepresentation.OwnedBy ownedBy = new SpaceRepresentation.OwnedBy();
            ownedBy.setData(userData); // save

            GenericLinksRepresentation ownedLinks = new GenericLinksRepresentation();
            ownedLinks.setSelf(uriInfo.getBaseUriBuilder()
                    .path(UsersService.class)
                    .path(UsersService.class, "getUser")
                    .build(user.getIdentityID()).toString());
            ownedBy.setLinks(ownedLinks); // save

            owners.add(ownedBy);
        }

        // Attributes
        SpaceRepresentation.Attributes attributes = new SpaceRepresentation.Attributes();
        rep.setAttributes(attributes);

        attributes.setName(model.getName());
        attributes.setAssignedId(model.getAssignedId());
        attributes.setDescription(model.getDescription());
        attributes.setCreatedAt(model.getCreatedAt());
        attributes.setUpdatedAt(model.getUpdatedAt());

        return rep;
    }

    public DocumentRepresentation.Data toRepresentation(UserModel user, DocumentModel model, UriInfo uriInfo) {
        DocumentRepresentation.Data rep = new DocumentRepresentation.Data();

        IndexedDocumentModel indexedDocument = model.getIndexedDocument();
        DocumentVersionModel documentCurrentVersion = model.getCurrentVersion();
        List<DocumentVersionModel> documentVersions = model.getVersions();


        rep.setId(model.getId());
        rep.setType(ModelType.UBL_DOCUMENT.getAlias());

        // Links
        DocumentRepresentation.DocumentLink links = new DocumentRepresentation.DocumentLink();
        URI self = uriInfo.getBaseUriBuilder()
                .path(DocumentsService.class)
                .path(DocumentsService.class, "getDocument")
                .build(model.getId());

        links.setSelf(self.toString());

        rep.setLinks(links);

        // Attributes
        DocumentRepresentation.Attributes attributes = new DocumentRepresentation.Attributes();
        rep.setAttributes(attributes);

        attributes.setId(model.getId());
        attributes.setType(model.getType());
        attributes.setAssignedId(model.getAssignedId());
        attributes.setSupplierAssignedId(model.getSupplier().getAssignedId());

        attributes.setIssueDate(documentCurrentVersion.getIssueDate());
        attributes.setCurrency(documentCurrentVersion.getCurrency());
        attributes.setAmount(documentCurrentVersion.getAmount());
        attributes.setTax(documentCurrentVersion.getTax());

        attributes.setSupplierName(documentCurrentVersion.getSupplierName());
        attributes.setSupplierStreetAddress(documentCurrentVersion.getSupplierStreetAddress());
        attributes.setSupplierCity(documentCurrentVersion.getSupplierCity());
        attributes.setSupplierCountry(documentCurrentVersion.getSupplierCountry());
        attributes.setCustomerAssignedId(documentCurrentVersion.getCustomerAssignedId());
        attributes.setCustomerName(documentCurrentVersion.getCustomerName());
        attributes.setCustomerStreetAddress(documentCurrentVersion.getCustomerStreetAddress());
        attributes.setCustomerCity(documentCurrentVersion.getCustomerCity());
        attributes.setCustomerCountry(documentCurrentVersion.getCustomerCountry());

        attributes.setViewed(indexedDocument.getUserViews().contains(user.getId()));
        attributes.setStarred(indexedDocument.getUserStars().contains(user.getId()));
        attributes.setChecked(indexedDocument.getUserChecks().contains(user.getId()));

        attributes.setCreatedAt(model.getCreatedAt());
        attributes.setUpdatedAt(model.getUpdatedAt());

        attributes.setVersions(documentVersions.stream().map(DocumentVersionModel::getId).collect(Collectors.toSet()));

        return rep;
    }

    public RequestRepresentation.Data toRepresentation(RequestModel model) {
        RequestRepresentation.Data rep = new RequestRepresentation.Data();

        rep.setId(model.getId());
        rep.setType(ModelType.REQUEST_ACCESS_TO_SPACE.getAlias());

        // Attributes
        RequestRepresentation.Attributes attributes = new RequestRepresentation.Attributes();
        rep.setAttributes(attributes);

        attributes.setSpace(model.getSpace().getId());
        attributes.setUser(model.getUser().getIdentityID()); // WARNING: REMOVE IDENTITY ID
        attributes.setScope(model.getScope().toString());
        attributes.setMessage(model.getMessage());
        attributes.setStatus(model.getStatus().toString());
        attributes.setCreatedAt(model.getCreatedAt());
        attributes.setUpdatedAt(model.getUpdatedAt());

        return rep;
    }

}
