//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.08.16 at 10:50:18 AM PET 
//

package org.openfact.ubl.pe.commons;

import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_21.MonetaryTotalType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_21.AmountType;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

/**
 * This object contains factory methods for each Java content interface and Java
 * element interface generated in the org.openfact.models.common package.
 * <p>
 * An ObjectFactory allows you to programatically construct new instances of the
 * Java representation for XML content. The Java representation of XML content
 * can consist of schema derived interfaces and classes representing the binding
 * of schema type definitions, element declarations and model groups. Factory
 * methods for each of these are provided in this class.
 */
@XmlRegistry
public class InvoiceFactory {

	private final static QName _AdditionalInformation_QNAME = new QName(
			"urn:sunat:names:specification:ubl:peru:schema:xsd:SunatAggregateComponents-1", "AdditionalInformation");
	private final static QName _AdditionalMonetaryTotal_QNAME = new QName(
			"urn:sunat:names:specification:ubl:peru:schema:xsd:SunatAggregateComponents-1", "AdditionalMonetaryTotal");
	private final static QName _AdditionalProperty_QNAME = new QName(
			"urn:sunat:names:specification:ubl:peru:schema:xsd:SunatAggregateComponents-1", "AdditionalProperty");
	private final static QName _ReferenceAmount_QNAME = new QName(
			"urn:sunat:names:specification:ubl:peru:schema:xsd:SunatAggregateComponents-1", "ReferenceAmount");
	private final static QName _TotalAmount_QNAME = new QName(
			"urn:sunat:names:specification:ubl:peru:schema:xsd:SunatAggregateComponents-1", "TotalAmount");
	private final static QName _SUNATTransaction_QNAME = new QName(
			"urn:sunat:names:specification:ubl:peru:schema:xsd:SunatAggregateComponents-1", "SUNATTransaction");
	private final static QName _SUNATEmbededDespatchAdvice_QNAME = new QName(
			"urn:sunat:names:specification:ubl:peru:schema:xsd:SunatAggregateComponents-1",
			"SUNATEmbededDespatchAdvice");
	private final static QName _SUNATShipment_QNAME = new QName(
			"urn:sunat:names:specification:ubl:peru:schema:xsd:SunatAggregateComponents-1", "SUNATShipment");
	private final static QName _SUNATShipmentStage_QNAME = new QName(
			"urn:sunat:names:specification:ubl:peru:schema:xsd:SunatAggregateComponents-1", "SUNATShipmentStage");
	private final static QName _SUNATTransportMeans_QNAME = new QName(
			"urn:sunat:names:specification:ubl:peru:schema:xsd:SunatAggregateComponents-1", "SUNATTransportMeans");
	private final static QName _SUNATRoadTransport_QNAME = new QName(
			"urn:sunat:names:specification:ubl:peru:schema:xsd:SunatAggregateComponents-1", "SUNATRoadTransport");
	private final static QName _SUNATDespatchLine_QNAME = new QName(
			"urn:sunat:names:specification:ubl:peru:schema:xsd:SunatAggregateComponents-1", "SUNATDespatchLine");
	private final static QName _SUNATCarrierParty_QNAME = new QName(
			"urn:sunat:names:specification:ubl:peru:schema:xsd:SunatAggregateComponents-1", "SUNATCarrierParty");
	private final static QName _SUNATFiscalPath_QNAME = new QName(
			"urn:sunat:names:specification:ubl:peru:schema:xsd:SunatAggregateComponents-1", "SUNATFiscalPath");
	private final static QName _AnticipatedMonetaryTotal_QNAME = new QName(
			"urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2", "AnticipatedMonetaryTotal");
	/*private final static QName _Perception_QNAME = new QName(
			"urn:sunat:names:specification:ubl:peru:schema:xsd:Perception-1", "Perception");
	private final static QName _Retention_QNAME = new QName(
			"urn:sunat:names:specification:ubl:peru:schema:xsd:Retention-1", "Retention");
	private final static QName _VoidedDocuments_QNAME = new QName(
			"urn:sunat:names:specification:ubl:peru:schema:xsd:VoidedDocuments-1", "VoidedDocuments");
	private final static QName _SummaryDocuments_QNAME = new QName(
			"urn:sunat:names:specification:ubl:peru:schema:xsd:SummaryDocuments-1", "SummaryDocuments");*/
	/**
	 * Create a new ObjectFactory that can be used to create new instances of
	 * schema derived classes for package: org.openfact.models.common
	 */
	public InvoiceFactory() {
	}

	/**
	 * Create an instance of {@link JAXBElement
	 * }{@code <}{@link AdditionalInformationTypeSunatAgg }{@code >}}
	 */
	@XmlElementDecl(namespace = "urn:sunat:names:specification:ubl:peru:schema:xsd:SunatAggregateComponents-1", name = "AdditionalInformation")
	public JAXBElement<AdditionalInformationTypeSunatAgg> createAdditionalInformation(
			AdditionalInformationTypeSunatAgg value) {
		return new JAXBElement<AdditionalInformationTypeSunatAgg>(_AdditionalInformation_QNAME,
				AdditionalInformationTypeSunatAgg.class, null, value);
	}

	/**
	 * Create an instance of {@link JAXBElement
	 * }{@code <}{@link AdditionalMonetaryTotalType }{@code >}}
	 */
	@XmlElementDecl(namespace = "urn:sunat:names:specification:ubl:peru:schema:xsd:SunatAggregateComponents-1", name = "AdditionalMonetaryTotal")
	public JAXBElement<AdditionalMonetaryTotalType> createAdditionalMonetaryTotal(AdditionalMonetaryTotalType value) {
		return new JAXBElement<AdditionalMonetaryTotalType>(_AdditionalMonetaryTotal_QNAME,
				AdditionalMonetaryTotalType.class, null, value);
	}

	/**
	 * Create an instance of {@link JAXBElement
	 * }{@code <}{@link AdditionalPropertyType }{@code >}}
	 */
	@XmlElementDecl(namespace = "urn:sunat:names:specification:ubl:peru:schema:xsd:SunatAggregateComponents-1", name = "AdditionalProperty")
	public JAXBElement<AdditionalPropertyType> createAdditionalProperty(AdditionalPropertyType value) {
		return new JAXBElement<AdditionalPropertyType>(_AdditionalProperty_QNAME, AdditionalPropertyType.class, null,
				value);
	}

	/**
	 * Create an instance of {@link JAXBElement
	 * }{@code <}{@link  }{@code >}}
	 */
	@XmlElementDecl(namespace = "urn:sunat:names:specification:ubl:peru:schema:xsd:SunatAggregateComponents-1", name = "ReferenceAmount")
	public JAXBElement<AmountType> createReferenceAmount(AmountType value) {
		return new JAXBElement<AmountType>(_ReferenceAmount_QNAME, AmountType.class, null, value);
	}

	/**
	 * Create an instance of {@link JAXBElement
	 * }{@code <}{@link  }{@code >}}
	 */
	@XmlElementDecl(namespace = "urn:sunat:names:specification:ubl:peru:schema:xsd:SunatAggregateComponents-1", name = "TotalAmount")
	public JAXBElement<AmountType> createTotalAmount(AmountType value) {
		return new JAXBElement<AmountType>(_TotalAmount_QNAME, AmountType.class, null, value);
	}

	/**
	 * Create an instance of {@link JAXBElement
	 * }{@code <}{@link SUNATTransactionType }{@code >}}
	 */
	@XmlElementDecl(namespace = "urn:sunat:names:specification:ubl:peru:schema:xsd:SunatAggregateComponents-1", name = "SUNATTransaction")
	public JAXBElement<SUNATTransactionType> createSUNATTransaction(SUNATTransactionType value) {
		return new JAXBElement<SUNATTransactionType>(_SUNATTransaction_QNAME, SUNATTransactionType.class, null, value);
	}

	/**
	 * Create an instance of {@link JAXBElement
	 * }{@code <}{@link SUNATEmbededDespatchAdviceType }{@code >}}
	 */
	@XmlElementDecl(namespace = "urn:sunat:names:specification:ubl:peru:schema:xsd:SunatAggregateComponents-1", name = "SUNATEmbededDespatchAdvice")
	public JAXBElement<SUNATEmbededDespatchAdviceType> createSUNATEmbededDespatchAdvice(
			SUNATEmbededDespatchAdviceType value) {
		return new JAXBElement<SUNATEmbededDespatchAdviceType>(_SUNATEmbededDespatchAdvice_QNAME,
				SUNATEmbededDespatchAdviceType.class, null, value);
	}

	/**
	 * Create an instance of {@link JAXBElement
	 * }{@code <}{@link SUNATShipmentType }{@code >}}
	 */
	@XmlElementDecl(namespace = "urn:sunat:names:specification:ubl:peru:schema:xsd:SunatAggregateComponents-1", name = "SUNATShipment")
	public JAXBElement<SUNATShipmentType> createSUNATShipment(SUNATShipmentType value) {
		return new JAXBElement<SUNATShipmentType>(_SUNATShipment_QNAME, SUNATShipmentType.class, null, value);
	}

	/**
	 * Create an instance of {@link JAXBElement
	 * }{@code <}{@link SUNATShipmentStageType }{@code >}}
	 */
	@XmlElementDecl(namespace = "urn:sunat:names:specification:ubl:peru:schema:xsd:SunatAggregateComponents-1", name = "SUNATShipmentStage")
	public JAXBElement<SUNATShipmentStageType> createSUNATShipmentStage(SUNATShipmentStageType value) {
		return new JAXBElement<SUNATShipmentStageType>(_SUNATShipmentStage_QNAME, SUNATShipmentStageType.class, null,
				value);
	}

	/**
	 * Create an instance of {@link JAXBElement
	 * }{@code <}{@link SUNATTransportMeansType }{@code >}}
	 */
	@XmlElementDecl(namespace = "urn:sunat:names:specification:ubl:peru:schema:xsd:SunatAggregateComponents-1", name = "SUNATTransportMeans")
	public JAXBElement<SUNATTransportMeansType> createSUNATTransportMeans(SUNATTransportMeansType value) {
		return new JAXBElement<SUNATTransportMeansType>(_SUNATTransportMeans_QNAME, SUNATTransportMeansType.class, null,
				value);
	}

	/**
	 * Create an instance of {@link JAXBElement
	 * }{@code <}{@link SUNATRoadTransportType }{@code >}}
	 */
	@XmlElementDecl(namespace = "urn:sunat:names:specification:ubl:peru:schema:xsd:SunatAggregateComponents-1", name = "SUNATRoadTransport")
	public JAXBElement<SUNATRoadTransportType> createSUNATRoadTransport(SUNATRoadTransportType value) {
		return new JAXBElement<SUNATRoadTransportType>(_SUNATRoadTransport_QNAME, SUNATRoadTransportType.class, null,
				value);
	}

	/**
	 * Create an instance of {@link JAXBElement
	 * }{@code <}{@link SUNATDespatchLineType }{@code >}}
	 */
	@XmlElementDecl(namespace = "urn:sunat:names:specification:ubl:peru:schema:xsd:SunatAggregateComponents-1", name = "SUNATDespatchLine")
	public JAXBElement<SUNATDespatchLineType> createSUNATDespatchLine(SUNATDespatchLineType value) {
		return new JAXBElement<SUNATDespatchLineType>(_SUNATDespatchLine_QNAME, SUNATDespatchLineType.class, null,
				value);
	}

	/**
	 * Create an instance of {@link JAXBElement
	 * }{@code <}{@link SUNATCarrierPartyType }{@code >}}
	 */
	@XmlElementDecl(namespace = "urn:sunat:names:specification:ubl:peru:schema:xsd:SunatAggregateComponents-1", name = "SUNATCarrierParty")
	public JAXBElement<SUNATCarrierPartyType> createSUNATCarrierParty(SUNATCarrierPartyType value) {
		return new JAXBElement<SUNATCarrierPartyType>(_SUNATCarrierParty_QNAME, SUNATCarrierPartyType.class, null,
				value);
	}

	/**
	 * Create an instance of {@link JAXBElement
	 * }{@code <}{@link SUNATFiscalPathType }{@code >}}
	 */
	@XmlElementDecl(namespace = "urn:sunat:names:specification:ubl:peru:schema:xsd:SunatAggregateComponents-1", name = "SUNATFiscalPath")
	public JAXBElement<SUNATFiscalPathType> createSUNATFiscalPath(SUNATFiscalPathType value) {
		return new JAXBElement<SUNATFiscalPathType>(_SUNATFiscalPath_QNAME, SUNATFiscalPathType.class, null, value);
	}

	/**
	 * Create an instance of {@link JAXBElement
	 * }{@code <}{@link MonetaryTotalType }{@code >}}
	 */
	@XmlElementDecl(namespace = "urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2", name = "AnticipatedMonetaryTotal")
	public JAXBElement<MonetaryTotalType> createAnticipatedMonetaryTotal(MonetaryTotalType value) {
		return new JAXBElement<MonetaryTotalType>(_AnticipatedMonetaryTotal_QNAME, MonetaryTotalType.class, null,
				value);
	}

/*	@XmlElementDecl(namespace = "urn:sunat:names:specification:ubl:peru:schema:xsd:Retention-1", name = "Retention")
	public JAXBElement<RetentionType> createRetention(RetentionType value) {
		return new JAXBElement<RetentionType>(_Retention_QNAME, RetentionType.class, null, value);
	}

	@XmlElementDecl(namespace = "urn:sunat:names:specification:ubl:peru:schema:xsd:Perception-1", name = "Perception")
	public JAXBElement<PerceptionType> createPerception(PerceptionType value) {
		return new JAXBElement<PerceptionType>(_Perception_QNAME, PerceptionType.class, null, value);
	}

	@XmlElementDecl(namespace = "urn:sunat:names:specification:ubl:peru:schema:xsd:VoidedDocuments-1", name = "VoidedDocuments")
	public JAXBElement<VoidedDocumentsType> createVoidedDocuments(VoidedDocumentsType value) {
		return new JAXBElement<VoidedDocumentsType>(_VoidedDocuments_QNAME, VoidedDocumentsType.class, null,
				value);
	}

	@XmlElementDecl(namespace = "urn:sunat:names:specification:ubl:peru:schema:xsd:SummaryDocuments-1", name = "SummaryDocuments")
	public JAXBElement<SummaryDocumentsType> createSummaryDocuments(SummaryDocumentsType value) {
		return new JAXBElement<SummaryDocumentsType>(_SummaryDocuments_QNAME, SummaryDocumentsType.class,
				null, value);
	}*/


}
