package de.usd.cstchef.operations.setter;

import burp.api.montoya.core.ByteArray;
import burp.api.montoya.http.message.requests.HttpRequest;
import burp.api.montoya.http.message.responses.HttpResponse;
import de.usd.cstchef.Utils.MessageType;
import de.usd.cstchef.operations.Operation;
import de.usd.cstchef.operations.Operation.OperationInfos;
import de.usd.cstchef.view.ui.VariableTextField;
import de.usd.cstchef.operations.OperationCategory;

@OperationInfos(name = "Remove HTTP Header", category = OperationCategory.SETTER, description = "Remove the specified HTTP Header.")
public class HttpHeaderRemove extends Operation {

    private VariableTextField header;

    @Override
    protected ByteArray perform(ByteArray input) throws Exception {

        MessageType messageType = parseMessageType(input);

        String headerName = header.getText();
        if(headerName.isEmpty()) {
            return input;
        }

        if(messageType == MessageType.REQUEST) {
            if(!HttpRequest.httpRequest(input).hasHeader(headerName)) {
                throw new IllegalArgumentException("Header not found.");
            }
            return HttpRequest.httpRequest(input).withRemovedHeader(headerName).toByteArray();
        }
        else if(messageType == MessageType.RESPONSE) {
            if(!HttpResponse.httpResponse(input).hasHeader(headerName)) {
                throw new IllegalArgumentException("Header not found.");
            }
            return HttpResponse.httpResponse(input).withRemovedHeader(headerName).toByteArray();
        }
        else {
            return parseRawMessage(input);
        }
    }


    @Override
    public void createUI() {
        this.header = new VariableTextField();

        this.addUIElement("Header Name", this.header);
    }
    
}
