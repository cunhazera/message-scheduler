package org.cunha.rest;

import io.smallrye.mutiny.Uni;
import org.cunha.dto.MessageDTO;
import org.cunha.service.MessageSchedulerService;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;

@Path("/messages")
public class MessageResource {

    @Inject
    private MessageSchedulerService service;

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> findMessage(@PathParam("id") Long id) {
        return service.getMessage(id)
                .onItem().transform(e -> e != null ? Response.ok(e).build() : Response.status(Response.Status.NOT_FOUND).build());
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> newMessage(MessageDTO message) {
        return service.scheduleNewMessage(message)
                .onItem().transform(
                        e -> Response.created(URI.create(String.format("/messages/%d", e.getId()))).entity(e).build()
                );
    }

    @DELETE
    @Path("{id}")
    public Uni<Response> removeScheduledMessage(@PathParam("id") Long id) {
        return service.deleteMessage(id)
                .onItem().transform(deleted -> deleted ? Response.Status.NO_CONTENT : Response.Status.NOT_FOUND)
                .onItem().transform(status -> Response.status(status).build());
    }

}
