package gmetrica.util;

import static java.util.TimeZone.getTimeZone;

import java.text.SimpleDateFormat;
import java.util.logging.Logger;

import javax.ws.rs.Produces;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

import com.fasterxml.jackson.databind.ObjectMapper;


@Provider
@Produces("application/json")
public class JacksonConfigurator implements ContextResolver<ObjectMapper> {

    /**
     *
     */
    public JacksonConfigurator() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        dateFormat.setTimeZone(getTimeZone("UTC"));
        mapper.setDateFormat(dateFormat);
    }

    @Override
    public ObjectMapper getContext(Class<?> clazz) {
        return mapper;
    }

    private final ObjectMapper mapper = new ObjectMapper();
    private static final Logger LOG = Logger.getLogger(JacksonConfigurator.class.getName());
}