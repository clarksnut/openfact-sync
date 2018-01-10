package org.clarksnut.mapper.document.pe.creditnote;

import oasis.names.specification.ubl.schema.xsd.creditnote_2.CreditNoteType;
import org.clarksnut.files.XmlUBLFileModel;
import org.clarksnut.mapper.document.DocumentMapped;
import org.clarksnut.mapper.document.DocumentMapperProvider;
import org.clarksnut.models.utils.ClarksnutModelUtils;
import org.jboss.logging.Logger;

import javax.xml.bind.JAXBException;

public class PECreditNoteParsedDocumentProvider implements DocumentMapperProvider {

    private static final Logger logger = Logger.getLogger(PECreditNoteParsedDocumentProvider.class);

    @Override
    public String getGroup() {
        return "peru";
    }

    @Override
    public String getSupportedDocumentType() {
        return "CreditNote";
    }

    @Override
    public DocumentMapped map(XmlUBLFileModel file) {
        CreditNoteType creditNoteType;
        try {
            creditNoteType = ClarksnutModelUtils.unmarshall(file.getDocument(), CreditNoteType.class);
        } catch (JAXBException e) {
            return null;
        }

        return new DocumentMapped() {
            @Override
            public DocumentBean getBean() {
                return new PECreditNoteBeanAdapter(creditNoteType);
            }

            @Override
            public Object getType() {
                return creditNoteType;
            }
        };
    }

}
