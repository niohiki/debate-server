package org.niohiki.debateserver.handlers;

import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.niohiki.debateserver.Names;

public class StaticHandler extends ContextHandler {

    public StaticHandler() {
        super(Names.staticResourceContext);
        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setDirectoriesListed(true);
        resourceHandler.setResourceBase("/");
        resourceHandler.setResourceBase(this.getClass().getClassLoader().
                getResource(Names.staticResourcePath).toExternalForm());
        setHandler(resourceHandler);
    }
}
