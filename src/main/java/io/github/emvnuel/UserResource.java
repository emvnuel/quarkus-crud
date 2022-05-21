package io.github.emvnuel;

import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {

    private final UserRepository userRepository;

    public UserResource(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GET
    public List<UserResponse> findAll() {
        return userRepository.listAll().stream().map(UserResponse::new).collect(Collectors.toList());
    }

    @GET
    @Path("/{id}")
    public Response findById(@PathParam("id") Long id) {
        return userRepository.findByIdOptional(id)
                .map(UserResponse::new).map(Response::ok).map(Response.ResponseBuilder::build)
                .orElseThrow(NotFoundException::new);
    }

    @POST
    @Transactional
    public Response create(@Valid UserRequest request) {
        User user = request.toModel();
        userRepository.persist(user);
        return Response.ok(new UserResponse(user)).build();
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Response deleteById(@PathParam("id") Long id) {
        boolean deleted = userRepository.deleteById(id);
        if (!deleted) {
            return Response.status(404).build();
        }
        return Response.noContent().build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public Response update(@PathParam("id") Long id, UserRequest request) {
        return userRepository.findByIdOptional(id)
                .map(user -> {
                    user.update(request.name(), request.email(), request.telephone());
                    return user;
                })
                .map(UserResponse::new).map(Response::ok).map(Response.ResponseBuilder::build)
                .orElseThrow(NotFoundException::new);
    }

}
