package io.quarkus.rest.runtime.jaxrs;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Objects;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.InterceptorContext;

import io.quarkus.rest.runtime.core.QuarkusRestRequestContext;
import io.quarkus.rest.runtime.core.Serialisers;

public abstract class QuarkusRestAbstractInterceptorContext implements InterceptorContext {
    protected final QuarkusRestRequestContext context;
    protected Annotation[] annotations;
    protected Class<?> type;
    protected Type genericType;
    protected MediaType mediaType;
    protected final Serialisers serialisers;
    // as the interceptors can change the type or mediaType, when that happens we need to find a new reader/writer
    protected boolean rediscoveryNeeded = false;

    public QuarkusRestAbstractInterceptorContext(QuarkusRestRequestContext context, Annotation[] annotations, Class<?> type,
            Type genericType, MediaType mediaType, Serialisers serialisers) {
        this.context = context;
        this.annotations = annotations;
        this.type = type;
        this.genericType = genericType;
        this.mediaType = mediaType;
        this.serialisers = serialisers;
    }

    public Object getProperty(String name) {
        return context.getProperty(name);
    }

    public Collection<String> getPropertyNames() {
        return context.getPropertyNames();
    }

    public void setProperty(String name, Object object) {
        context.setProperty(name, object);
    }

    public void removeProperty(String name) {
        context.removeProperty(name);
    }

    public Annotation[] getAnnotations() {
        return annotations;
    }

    public void setAnnotations(Annotation[] annotations) {
        Objects.requireNonNull(annotations);
        this.annotations = annotations;
    }

    public Class<?> getType() {
        return type;
    }

    public void setType(Class<?> type) {
        if ((this.type != type) && (type != null)) {
            rediscoveryNeeded = true;
        }
        this.type = type;
    }

    public Type getGenericType() {
        return genericType;
    }

    public void setGenericType(Type genericType) {
        this.genericType = genericType;
    }

    public MediaType getMediaType() {
        return mediaType;
    }

    public void setMediaType(MediaType mediaType) {
        if (this.mediaType != mediaType) {
            rediscoveryNeeded = true;
        }
        this.mediaType = mediaType;
    }

}