package org.clarksnut.documents.jpa.entity;

import org.clarksnut.documents.DocumentProviderType;
import org.clarksnut.models.db.CreatableEntity;
import org.clarksnut.models.db.CreatedAtListener;
import org.clarksnut.models.db.UpdatableEntity;
import org.clarksnut.models.db.UpdatedAtListener;
import org.hibernate.annotations.Type;
import org.hibernate.search.annotations.*;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Indexed
@Table(name = "cl_document", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"type", "assigned_id", "supplier_assigned_id"})
})
@NamedQueries({
        @NamedQuery(name = "getDocumentByTypeAssignedIdAndSupplierAssignedId", query = "select d from DocumentEntity d where d.type = :type and d.assignedId = :assignedId and d.supplierAssignedId = :supplierAssignedId")
})
@EntityListeners({CreatedAtListener.class, UpdatedAtListener.class})
public class DocumentEntity implements CreatableEntity, UpdatableEntity, Serializable {

    @Id
    @Access(AccessType.PROPERTY)// Relationships often fetch id, but not entity.  This avoids an extra SQL
    @Column(name = "id", length = 36)
    private String id;

    @Field(name = "type")
    @Analyzer(definition = "staticTextAnalyzer")
    @NotNull
    @Column(name = "type")
    private String type;

    @Field(name = "assigned_id")
    @NotNull
    @Column(name = "assigned_id")
    private String assignedId;

    @NotNull
    @Column(name = "file_id")
    private String fileId;

    @Field(name = "amount", analyze = Analyze.NO)
    @SortableField
    @NumericField
    @Digits(integer = 10, fraction = 4)
    @Type(type = "org.hibernate.type.FloatType")
    @Column(name = "amount")
    private Float amount;

    @Digits(integer = 10, fraction = 4)
    @Type(type = "org.hibernate.type.FloatType")
    @Column(name = "tax")
    private Float tax;

    @Field(name = "currency")
    @Analyzer(definition = "staticTextAnalyzer")
    @Column(name = "currency")
    private String currency;

    @Field(name = "issue_date")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "issue_date")
    private Date issueDate;

    @Field(name = "supplier_name")
    @Analyzer(definition = "nameTextAnalyzer")
    @Column(name = "supplier_name")
    private String supplierName;

    @Field(name = "supplier_assigned_id")
    @NotNull
    @Column(name = "supplier_assigned_id")
    private String supplierAssignedId;

    @Column(name = "supplier_street_address")
    private String supplierStreetAddress;

    @Column(name = "supplier_city")
    private String supplierCity;

    @Column(name = "supplier_country")
    private String supplierCountry;

    @Field(name = "customer_name")
    @Analyzer(definition = "nameTextAnalyzer")
    @Column(name = "customer_name")
    private String customerName;

    @Field(name = "customer_assigned_id")
    @Column(name = "customer_assigned_id")
    private String customerAssignedId;

    @Column(name = "customer_street_address")
    private String customerStreetAddress;

    @Column(name = "customer_city")
    private String customerCity;

    @Column(name = "customer_country")
    private String customerCountry;

    @Field(name = "provider")
    @Analyzer(definition = "staticTextAnalyzer")
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "provider")
    private DocumentProviderType provider;

    @Field(name = "starred")
    @NotNull
    @Type(type = "org.hibernate.type.NumericBooleanType")
    @Column(name = "starred")
    private boolean starred;

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")
    private Date createdAt;

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at")
    private Date updatedAt;

    @IndexedEmbedded
    @ElementCollection
    @Column(name = "value")
    @CollectionTable(name = "document_tags", joinColumns = {@JoinColumn(name = "document_id")})
    private Set<String> tags = new HashSet<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAssignedId() {
        return assignedId;
    }

    public void setAssignedId(String assignedId) {
        this.assignedId = assignedId;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public Float getAmount() {
        return amount;
    }

    public void setAmount(Float amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Date getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(Date issueDate) {
        this.issueDate = issueDate;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getSupplierAssignedId() {
        return supplierAssignedId;
    }

    public void setSupplierAssignedId(String supplierAssignedId) {
        this.supplierAssignedId = supplierAssignedId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerAssignedId() {
        return customerAssignedId;
    }

    public void setCustomerAssignedId(String customerAssignedId) {
        this.customerAssignedId = customerAssignedId;
    }

    public DocumentProviderType getProvider() {
        return provider;
    }

    public void setProvider(DocumentProviderType providerType) {
        this.provider = providerType;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    @Override
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    @Override
    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Set<String> getTags() {
        return tags;
    }

    public void setTags(Set<String> tags) {
        this.tags = tags;
    }

    public boolean isStarred() {
        return starred;
    }

    public void setStarred(boolean stared) {
        this.starred = stared;
    }

    public String getSupplierStreetAddress() {
        return supplierStreetAddress;
    }

    public void setSupplierStreetAddress(String supplierStreetAddress) {
        this.supplierStreetAddress = supplierStreetAddress;
    }

    public String getSupplierCity() {
        return supplierCity;
    }

    public void setSupplierCity(String supplierCity) {
        this.supplierCity = supplierCity;
    }

    public String getSupplierCountry() {
        return supplierCountry;
    }

    public void setSupplierCountry(String supplierCountry) {
        this.supplierCountry = supplierCountry;
    }

    public String getCustomerStreetAddress() {
        return customerStreetAddress;
    }

    public void setCustomerStreetAddress(String customerStreetAddress) {
        this.customerStreetAddress = customerStreetAddress;
    }

    public String getCustomerCity() {
        return customerCity;
    }

    public void setCustomerCity(String customerCity) {
        this.customerCity = customerCity;
    }

    public String getCustomerCountry() {
        return customerCountry;
    }

    public void setCustomerCountry(String customerCountry) {
        this.customerCountry = customerCountry;
    }

    public Float getTax() {
        return tax;
    }

    public void setTax(Float tax) {
        this.tax = tax;
    }
}
