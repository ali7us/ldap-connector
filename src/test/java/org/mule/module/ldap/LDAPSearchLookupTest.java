/**
 * Copyright (c) MuleSoft, Inc. All rights reserved. http://www.mulesoft.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.md file.
 */

package org.mule.module.ldap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.mule.module.ldap.api.LDAPEntry;

public class LDAPSearchLookupTest extends AbstractLDAPConnectorEmbeddedLDAPTest
{

    /**
     * 
     */
    public LDAPSearchLookupTest()
    {
    }

    @Override
    protected String getConfigResources()
    {
        return "search-lookup-mule-config.xml";
    }
    
    @Test
    public void testAnonymousLookup() throws Exception
    {
        LDAPEntry result = (LDAPEntry) runFlow("testAnonymousFlow", "uid=admin,ou=people,dc=mulesoft,dc=org");
        
        assertEquals("admin", result.getAttribute("uid").getValue());
        assertEquals("Administrator", result.getAttribute("cn").getValue());
        assertNull(result.getAttribute("sn"));
    }
    
    @Test
    public void testAdminSearch() throws Exception
    {
        @SuppressWarnings("unchecked")
        List<LDAPEntry> result = (List<LDAPEntry>) runFlow("testAdminFlow", "(ou=people)");

        assertEquals(5, result.size());
        
        for(LDAPEntry entry : result)
        {
            assertNotNull(entry.getAttribute("uid").getValue());
            assertNotNull(entry.getAttribute("cn").getValue());
            assertNull(entry.getAttribute("sn"));
        }
    }    
    
    @Test
    public void testSearchMaxResults() throws Exception
    {
        @SuppressWarnings("unchecked")
        List<LDAPEntry> result = (List<LDAPEntry>) runFlow("testSearchMaxResultsFlow", "2");
        
        assertEquals(2, result.size());
    }
    
    @Test
    public void testPagedResultSearch() throws Exception
    {
        @SuppressWarnings("unchecked")
        List<Object> result = (List<Object>) runFlow("testPagedResultSearchFlow", "(uid=user*)");
        List<String> cns = new ArrayList<String>();
        cns.add("User One");
        cns.add("User Two");
        cns.add("User Three");
        cns.add("User Four");
        cns.add("User Five");

        assertEquals(5, result.size());

        for(Object o : result)
        {
            assertTrue(cns.contains(o));
        }
    }
    
    @Test
    public void testPagedResultSearchAsync() throws Exception
    {
        @SuppressWarnings("unchecked")
        List<Object> result = (List<Object>) runFlow("testPagedResultSearchAsyncFlow", "(uid=user*)");

        //assertEquals(0, result.size());
    }
    
}


