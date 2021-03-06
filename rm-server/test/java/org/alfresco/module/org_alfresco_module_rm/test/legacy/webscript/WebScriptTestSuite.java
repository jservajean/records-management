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
package org.alfresco.module.org_alfresco_module_rm.test.legacy.webscript;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * RM WebScript test suite
 *
 * @author Roy Wetherall
 */
@RunWith(Suite.class)
@SuiteClasses(
{
    DispositionRestApiTest.class,
    EventRestApiTest.class,
    RMCaveatConfigScriptTest.class,
    RMConstraintScriptTest.class,
    //RmRestApiTest.class,
    RoleRestApiTest.class,
    DataSetRestApiTest.class,
    EmailMapScriptTest.class,
    EmailMapKeysRestApiTest.class,
    CapabilitiesRestApiTest.class,
    ActionDefinitionsRestApiTest.class,
    RmClassesRestApiTest.class,
    RmPropertiesRestApiTest.class,
    RmAuthoritiesRestApiTest.class,
    SubstitutionSuggestionsRestApiTest.class
})
public class WebScriptTestSuite
{
}
