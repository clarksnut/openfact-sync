package org.openfact.services.broker.gmail;

import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Message;
import com.google.api.services.gmail.model.MessagePart;
import com.google.api.services.gmail.model.MessagePartBody;
import org.openfact.models.UserRepositoryModel;
import org.openfact.models.broker.BrokerElementModel;

import java.io.IOException;
import java.util.List;

import com.google.api.client.repackaged.org.apache.commons.codec.binary.Base64;
import org.openfact.models.broker.ReadBrokerException;

public class BrokerElementAdapter implements BrokerElementModel {

    private final UserRepositoryModel userRepository;
    private final Gmail client;
    private final Message message;

    public BrokerElementAdapter(UserRepositoryModel userRepository, Gmail client, Message message) {
        this.userRepository = userRepository;
        this.client = client;
        this.message = message;
    }

    @Override
    public byte[] getXml() throws ReadBrokerException {
        try {
            return getFileByExtension(".xml", ".XML");
        } catch (IOException e) {
            throw new ReadBrokerException("Could not retrieve xml document from gmail broker", e);
        }
    }

    @Override
    public byte[] getInvoice() throws ReadBrokerException {
        try {
            return getFileByExtension(".pdf", ".PDF");
        } catch (IOException e) {
            throw new ReadBrokerException("Could not retrieve pdf invoice from gmail broker", e);
        }
    }

    private byte[] getFileByExtension(String... validExtension) throws IOException {
        if (validExtension == null || validExtension.length == 0) {
            throw new IllegalStateException("Invalid extension");
        }

        List<MessagePart> parts = message.getPayload().getParts();
        for (MessagePart part : parts) {
            String filename = part.getFilename();
            if (filename != null && filename.length() > 0) {
                boolean endsWith = false;
                for (String extension : validExtension) {
                    if (filename.endsWith(extension)) {
                        endsWith = true;
                        break;
                    }
                }

                if (endsWith) {
                    String attachmentId = part.getBody().getAttachmentId();
                    MessagePartBody messagePartBody = client.users()
                            .messages()
                            .attachments()
                            .get(userRepository.getEmail(), message.getId(), attachmentId)
                            .execute();

                    Base64 base64url = new Base64(true);
                    return base64url.decodeBase64(messagePartBody.getData());
                }
            }
        }
        return null;
    }
}