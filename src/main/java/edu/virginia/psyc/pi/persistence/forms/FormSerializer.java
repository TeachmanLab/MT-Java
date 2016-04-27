package edu.virginia.psyc.pi.persistence.forms;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;

/**
 * There is an element in the Form object that is called "json" that contains raw
 * json.  We need to raise this up to the top level so the content, when serialized is
 * not "json":{"cheese":"yes"} but just {"cheese":"yes"}
 * While you can accomplish this with Jackson's @JsonUnwrapped for serializing sub objects
 * it doesn't currently work when applied to raw json tagged with @JsonRawValue.  We
 * need both to happen.
 */
public class FormSerializer extends JsonSerializer<FormDAO>{

    private SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z");

    public FormSerializer() {
        format.setTimeZone(TimeZone.getTimeZone("America/New_York"));
    }

    private JsonNode stringToJsonNode(String json) throws IOException{
        ObjectMapper mapper = new ObjectMapper();
        JsonNode actualObj = mapper.readTree(json);
        return actualObj;
    }

    @Override
    public void serialize(FormDAO form, JsonGenerator jgen, SerializerProvider provider)
            throws IOException, JsonProcessingException {
        jgen.writeStartObject();
        jgen.writeNumberField("id", form.getId());
        jgen.writeStringField("participantRSA", form.getParticipantRSA());
        jgen.writeStringField("date", format.format(form.getDate()));
        JsonNode n = stringToJsonNode(form.getJson());
        Iterator<String> fields = n.fieldNames();
        while(fields.hasNext()) {
            String f = fields.next();
            jgen.writeObjectField(f, n.get(f));
        }
        jgen.writeEndObject();
    }

}