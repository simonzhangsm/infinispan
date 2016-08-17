/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2013, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.jboss.as.clustering.infinispan.subsystem;

import org.jboss.as.clustering.infinispan.subsystem.CacheConfigOperationHandlers.CacheConfigAdd;
import org.jboss.as.controller.AttributeDefinition;
import org.jboss.as.controller.OperationStepHandler;
import org.jboss.as.controller.PathElement;
import org.jboss.as.controller.ReloadRequiredRemoveStepHandler;
import org.jboss.as.controller.ReloadRequiredWriteAttributeHandler;
import org.jboss.as.controller.SimpleAttributeDefinition;
import org.jboss.as.controller.SimpleAttributeDefinitionBuilder;
import org.jboss.as.controller.SimpleResourceDefinition;
import org.jboss.as.controller.registry.AttributeAccess;
import org.jboss.as.controller.registry.ManagementResourceRegistration;
import org.jboss.dmr.ModelType;

/**
 * CacheAuthorizationResource.
 *
 * @author Tristan Tarrant
 * @since 7.0
 */
public class CacheContainerAuthorizationResource extends SimpleResourceDefinition {

    static final SimpleAttributeDefinition AUDIT_LOGGER = new SimpleAttributeDefinitionBuilder(ModelKeys.AUDIT_LOGGER, ModelType.STRING, true)
        .setXmlName(Attribute.AUDIT_LOGGER.getLocalName())
        .setFlags(AttributeAccess.Flag.RESTART_RESOURCE_SERVICES)
        .build()
    ;

    static final SimpleAttributeDefinition MAPPER = new SimpleAttributeDefinitionBuilder(ModelKeys.MAPPER, ModelType.STRING, true)
        .setXmlName(Attribute.MAPPER.getLocalName())
        .setAllowExpression(true)
        .setFlags(AttributeAccess.Flag.RESTART_RESOURCE_SERVICES)
        .build()
    ;

    static final AttributeDefinition[] ATTRIBUTES = new AttributeDefinition[] { AUDIT_LOGGER, MAPPER };

    CacheContainerAuthorizationResource() {
        super(PathElement.pathElement(ModelKeys.AUTHORIZATION), new InfinispanResourceDescriptionResolver(ModelKeys.CACHE_CONTAINER, ModelKeys.SECURITY, ModelKeys.AUTHORIZATION), new CacheConfigAdd(ATTRIBUTES), ReloadRequiredRemoveStepHandler.INSTANCE);
    }

    @Override
    public void registerAttributes(ManagementResourceRegistration registration) {
        final OperationStepHandler writeHandler = new ReloadRequiredWriteAttributeHandler(ATTRIBUTES);
        for (AttributeDefinition attribute: ATTRIBUTES) {
            registration.registerReadWriteAttribute(attribute, null, writeHandler);
        }
    }

    @Override
    public void registerChildren(ManagementResourceRegistration resourceRegistration) {
        resourceRegistration.registerSubModel(new AuthorizationRoleResource());
    }


}
