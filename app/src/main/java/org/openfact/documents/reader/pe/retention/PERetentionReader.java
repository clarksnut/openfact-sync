package org.openfact.documents.reader.pe.retention;

import org.jboss.logging.Logger;
import org.openfact.documents.DocumentReader;
import org.openfact.documents.GenericDocument;
import org.openfact.documents.InteractType;
import org.openfact.documents.jpa.entity.DocumentEntity;
import org.openfact.documents.jpa.entity.DocumentSpaceEntity;
import org.openfact.documents.reader.SupportedType;
import org.openfact.documents.reader.pe.common.PEUtils;
import org.openfact.documents.reader.pe.common.jaxb.retention.RetentionType;
import org.openfact.files.XmlUBLFileModel;
import org.openfact.models.db.jpa.entity.SpaceEntity;
import org.openfact.models.utils.OpenfactModelUtils;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.xml.bind.JAXBException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

@Stateless
@SupportedType(value = "Retention")
public class PERetentionReader implements DocumentReader {

    private static final Logger logger = Logger.getLogger(PERetentionReader.class);

    @Inject
    private PEUtils peUtils;

    @Override
    public String getSupportedDocumentType() {
        return "Retention";
    }

    @Override
    public int getPriority() {
        return 1;
    }

    @Override
    public GenericDocument read(XmlUBLFileModel file) {
        RetentionType retentionType;
        try {
            retentionType = OpenfactModelUtils.unmarshall(file.getDocument(), RetentionType.class);
        } catch (JAXBException e) {
            return null;
        }

        SpaceEntity senderSpaceEntity = peUtils.getSpace(retentionType.getAgentParty());
        SpaceEntity receiverSpaceEntity = peUtils.getSpace(retentionType.getReceiverParty());

        DocumentEntity documentEntity = new DocumentEntity();

        DocumentSpaceEntity documentSpaceSenderEntity = new DocumentSpaceEntity();
        documentSpaceSenderEntity.setId(OpenfactModelUtils.generateId());
        documentSpaceSenderEntity.setType(InteractType.SENDER);
        documentSpaceSenderEntity.setSpace(senderSpaceEntity);
        documentSpaceSenderEntity.setDocument(documentEntity);

        DocumentSpaceEntity documentSpaceReceiverEntity = new DocumentSpaceEntity();
        documentSpaceReceiverEntity.setId(OpenfactModelUtils.generateId());
        documentSpaceReceiverEntity.setType(InteractType.RECEIVER);
        documentSpaceReceiverEntity.setSpace(receiverSpaceEntity);
        documentSpaceReceiverEntity.setDocument(documentEntity);

        documentEntity.setAssignedId(retentionType.getId().getValue());
        documentEntity.setSpaces(new HashSet<>(Arrays.asList(documentSpaceSenderEntity, documentSpaceReceiverEntity)));
        documentEntity.setSupplierAssignedId(retentionType.getAgentParty().getPartyIdentification().get(0).getIDValue());
        documentEntity.setSupplierName(retentionType.getAgentParty().getPartyLegalEntity().get(0).getRegistrationName().getValue());
        documentEntity.setCustomerAssignedId(retentionType.getReceiverParty().getPartyIdentification().get(0).getIDValue());
        documentEntity.setCustomerName(retentionType.getReceiverParty().getPartyLegalEntity().get(0).getRegistrationName().getValue());
        documentEntity.setCurrency(retentionType.getSunatTotalPaid().getCurrencyID());
        documentEntity.setAmount(retentionType.getSunatTotalPaid().getValue().floatValue());
        documentEntity.setIssueDate(retentionType.getIssueDate().getValue().toGregorianCalendar().getTime());

        Map<String, String> tags = new HashMap<>();
        tags.put("reader", "peru");
        documentEntity.setTags(tags);

        return new GenericDocument() {
            @Override
            public DocumentEntity getEntity() {
                return documentEntity;
            }

            @Override
            public Object getJaxb() {
                return retentionType;
            }
        };
    }

}
