/*
 * Copyright (C) 2005-2014 Alfresco Software Limited.
 *
 * This file is part of Alfresco
 *
 * Alfresco is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Alfresco is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Alfresco. If not, see <http://www.gnu.org/licenses/>.
 */
package org.alfresco.po.share.site;

import org.springframework.stereotype.Component;

/**
 * Collaboration site dashboard
 * 
 * @author Roy Wetherall
 */
@Component
public class CollaborationSiteDashboard extends CollaborationSitePage
{
    /**
     * Get the URL of the page
     */
    public String getPageURL(String ... context)
    {
        throw new UnsupportedOperationException("This page does not have a direct access URL set.");
    }

}
