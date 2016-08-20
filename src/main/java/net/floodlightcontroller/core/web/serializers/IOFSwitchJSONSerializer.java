package net.floodlightcontroller.core.web.serializers;

import java.io.IOException;

import net.floodlightcontroller.core.IOFSwitch;
 
 
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
 
public class IOFSwitchJSONSerializer extends JsonSerializer<IOFSwitch> {
 
    /**
     * Handles serialization for IOFSwitch
     */
    @Override
    public void serialize(IOFSwitch theSwitch, JsonGenerator jGen,
                          SerializerProvider arg2) throws IOException,
                                                  JsonProcessingException {
        jGen.writeStartObject();
        jGen.writeStringField("dpid", theSwitch.getId().toString());
        jGen.writeEndObject();
    }
    public Class<IOFSwitch> handledType() {
        return IOFSwitch.class;
    }
}
