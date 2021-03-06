/*
 * Copyright (C) 2005-2015 Alfresco Software Limited.
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
package org.alfresco.module.org_alfresco_module_rm.classification;

import org.alfresco.module.org_alfresco_module_rm.classification.ClassificationServiceException.MalformedConfiguration;
import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This class is responsible for providing the configured classification levels, dealing with
 * JSON schema as part of that.
 *
 * @author Neil Mc Erlean
 * @since 3.0
 */
class Configuration
{
    public final String configLocation;

    public Configuration(String classpathLocation)
    {
        this.configLocation = classpathLocation;
    }

    /**
     * Gets the list (in descending order) of classification levels - as defined in the system configuration.
     *
     * @return the configured classification levels in descending order, or an empty list if there are none.
     */
    public List<ClassificationLevel> getConfiguredLevels()
    {
        List<ClassificationLevel> result;
        try (final InputStream in = this.getClass().getResourceAsStream(configLocation))
        {
            if (in == null) { result = Collections.emptyList(); }
            else
            {
                final String jsonString = IOUtils.toString(in);
                final JSONArray jsonArray = new JSONArray(new JSONTokener(jsonString));

                result = new ArrayList<>(jsonArray.length());

                for (int i = 0; i < jsonArray.length(); i++)
                {
                    final JSONObject nextObj = jsonArray.getJSONObject(i);
                    final String name = nextObj.getString("name");
                    final String displayLabelKey = nextObj.getString("displayLabel");
                    result.add(new ClassificationLevel(name, displayLabelKey));
                }
            }
        }
        catch (IOException | JSONException e)
        {
            throw new MalformedConfiguration("Could not read classification level configuration", e);
        }
        return result;
    }
}
