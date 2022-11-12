package com.alesj.qcl.app;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.google.common.io.Files;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import java.io.File;
import java.io.IOException;

/**
 * @author Ales Justin
 */
@ApplicationScoped
@Path("/")
public class GrafanaService {
    private static final Logger log = LoggerFactory.getLogger(GrafanaService.class);
    
    @GET
    @Path("/img/{name}")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public byte[] getImg(@PathParam("name") String name) throws IOException {
        log.info("Getting img: {}", name);
        File dir = new File("/usr/local/opt/grafana/share/grafana/public/img/");
        File file = new File(dir, name);
        if (file.isDirectory() || name.endsWith(".svg")) {
            file = new File(dir, "fav32.png");
        }
        return Files.toByteArray(file);
    }
}
