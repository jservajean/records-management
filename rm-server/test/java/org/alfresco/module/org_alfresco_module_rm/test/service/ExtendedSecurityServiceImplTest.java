/*
 * Copyright (C) 2005-2012 Alfresco Software Limited.
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
package org.alfresco.module.org_alfresco_module_rm.test.service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.alfresco.module.org_alfresco_module_rm.security.ExtendedSecurityService;
import org.alfresco.module.org_alfresco_module_rm.test.util.BaseRMTestCase;
import org.alfresco.service.cmr.repository.NodeRef;

/**
 * Records management security service test.
 * 
 * @author Roy Wetherall
 */
public class ExtendedSecurityServiceImplTest extends BaseRMTestCase
{
    private ExtendedSecurityService extendedSecurityService;
    
    private NodeRef record;
    private NodeRef recordToo;
    private NodeRef moveRecordCategory;
    private NodeRef moveRecordFolder;
    
    @Override
    protected boolean isUserTest()
    {
        return true;
    }
    
    @Override
    protected void initServices()
    {
        super.initServices();
        
        extendedSecurityService = (ExtendedSecurityService)applicationContext.getBean("ExtendedSecurityService");
    }
    
    @Override
    protected void setupTestDataImpl()
    {
        super.setupTestDataImpl();
        
        record = utils.createRecord(rmFolder, "record.txt");
        recordToo = utils.createRecord(rmFolder, "recordToo.txt");    
        
        moveRecordCategory = rmService.createRecordCategory(filePlan, "moveRecordCategory");
        moveRecordFolder = rmService.createRecordFolder(moveRecordCategory, "moveRecordFolder");
    }
    
    public void testExtendedReaders()
    {
        doTestInTransaction(new Test<Void>()
        {
            public Void run()
            {
                assertFalse(extendedSecurityService.hasExtendedReaders(filePlan));
                assertFalse(extendedSecurityService.hasExtendedReaders(rmContainer));
                assertFalse(extendedSecurityService.hasExtendedReaders(rmFolder));
                assertFalse(extendedSecurityService.hasExtendedReaders(record));
                
                assertNull(extendedSecurityService.getExtendedReaders(record));
                
                Set<String> extendedReaders = new HashSet<String>(2);
                extendedReaders.add("monkey");
                extendedReaders.add("elephant");
                
                extendedSecurityService.setExtendedReaders(record, extendedReaders);
                
                Map<String, Integer> testMap = new HashMap<String, Integer>(2);
                testMap.put("monkey", Integer.valueOf(1));
                testMap.put("elephant", Integer.valueOf(1));
                
                checkExtendedReaders(filePlan, testMap);
                checkExtendedReaders(rmContainer, testMap);
                checkExtendedReaders(rmFolder, testMap);
                checkExtendedReaders(record, testMap);
                
                Set<String> extendedReadersToo = new HashSet<String>(2);
                extendedReadersToo.add("monkey");
                extendedReadersToo.add("snake");
                
                extendedSecurityService.setExtendedReaders(recordToo, extendedReadersToo);
                
                Map<String, Integer> testMapToo = new HashMap<String, Integer>(2);
                testMapToo.put("monkey", Integer.valueOf(1));
                testMapToo.put("snake", Integer.valueOf(1));
                
                Map<String, Integer> testMapThree = new HashMap<String, Integer>(3);
                testMapThree.put("monkey", Integer.valueOf(2));
                testMapThree.put("elephant", Integer.valueOf(1));
                testMapThree.put("snake", Integer.valueOf(1));
                
                checkExtendedReaders(filePlan, testMapThree);
                checkExtendedReaders(rmContainer, testMapThree);
                checkExtendedReaders(rmFolder, testMapThree);
                checkExtendedReaders(recordToo, testMapToo);
                
                // test remove (with no parent inheritance)
                
                Set<String> removeMap1 = new HashSet<String>(2);
                removeMap1.add("elephant");
                removeMap1.add("monkey");
                
                extendedSecurityService.removeExtendedReaders(rmFolder, removeMap1, false);
                
                Map<String, Integer> testMapFour = new HashMap<String, Integer>(2);
                testMapFour.put("monkey", Integer.valueOf(1));
                testMapFour.put("snake", Integer.valueOf(1));
                
                checkExtendedReaders(filePlan, testMapThree);
                checkExtendedReaders(rmContainer, testMapThree);
                checkExtendedReaders(rmFolder, testMapFour);
                checkExtendedReaders(recordToo, testMapToo);
                
                // test remove (apply to parents)
                
                Set<String> removeMap2 = new HashSet<String>(1);
                removeMap2.add("snake");
                
                extendedSecurityService.removeExtendedReaders(recordToo, removeMap2, true);
                
                testMapThree.remove("snake");
                testMapFour.remove("snake");
                testMapToo.remove("snake");
                
                checkExtendedReaders(filePlan, testMapThree);
                checkExtendedReaders(rmContainer, testMapThree);
                checkExtendedReaders(rmFolder, testMapFour);
                checkExtendedReaders(recordToo, testMapToo);
                
                return null;
            }
        });
    }    
    
    public void testMove()
    {
        doTestInTransaction(new Test<Void>()
        {
            Map<String, Integer> testMap = new HashMap<String, Integer>(2);
            
            public Void run() throws Exception
            {
                testMap.put("monkey", Integer.valueOf(1));
                testMap.put("elephant", Integer.valueOf(1));                
                
                assertFalse(extendedSecurityService.hasExtendedReaders(filePlan));
                assertFalse(extendedSecurityService.hasExtendedReaders(rmContainer));
                assertFalse(extendedSecurityService.hasExtendedReaders(rmFolder));
                assertFalse(extendedSecurityService.hasExtendedReaders(record));
                assertFalse(extendedSecurityService.hasExtendedReaders(moveRecordCategory));
                assertFalse(extendedSecurityService.hasExtendedReaders(moveRecordFolder));
                
                assertNull(extendedSecurityService.getExtendedReaders(record));
                
                Set<String> extendedReaders = new HashSet<String>(2);
                extendedReaders.add("monkey");
                extendedReaders.add("elephant");
                
                extendedSecurityService.setExtendedReaders(record, extendedReaders);
                
                checkExtendedReaders(filePlan, testMap);
                checkExtendedReaders(rmContainer, testMap);
                checkExtendedReaders(rmFolder, testMap);
                checkExtendedReaders(record, testMap);
                assertFalse(extendedSecurityService.hasExtendedReaders(moveRecordCategory));
                assertFalse(extendedSecurityService.hasExtendedReaders(moveRecordFolder));
                
                fileFolderService.move(record, moveRecordFolder, "movedRecord");
                
                return null;
            }
            
            @Override
            public void test(Void result) throws Exception
            {
                checkExtendedReaders(filePlan, testMap);
                assertFalse(extendedSecurityService.hasExtendedReaders(rmContainer));
                assertFalse(extendedSecurityService.hasExtendedReaders(rmFolder));
                checkExtendedReaders(moveRecordCategory, testMap);
                checkExtendedReaders(moveRecordFolder, testMap);
                checkExtendedReaders(record, testMap);
            }
        });        
    }
    
    
    @SuppressWarnings("unchecked")
    private void checkExtendedReaders(NodeRef nodeRef, Map<String, Integer> testMap)
    {
        assertTrue(extendedSecurityService.hasExtendedReaders(nodeRef));
        
        Map<String, Integer> readersMap = (Map<String,Integer>)nodeService.getProperty(nodeRef, PROP_READERS);
        assertNotNull(readersMap);
        assertEquals(testMap.size(), readersMap.size());
        
        for (Map.Entry<String, Integer> entry: testMap.entrySet())
        {
            assertTrue(readersMap.containsKey(entry.getKey()));
            assertEquals(entry.getKey(), entry.getValue(), readersMap.get(entry.getKey()));
            
        }
        
        Set<String> readers = extendedSecurityService.getExtendedReaders(nodeRef);
        assertNotNull(readers);
        assertEquals(testMap.size(), readers.size());
    }
}