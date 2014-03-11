/**
 * Copyright (c) MuleSoft, Inc. All rights reserved. http://www.mulesoft.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.md file.
 */
package org.mule.module.ldap;

import java.util.ArrayList;
import java.util.List;

import org.mule.api.MuleException;
import org.mule.module.ldap.api.LDAPEntry;
import org.mule.module.ldap.api.LDAPException;
import org.mule.module.ldap.api.LDAPResultSet;
import org.mule.streaming.PagingDelegate;

public class LDAPPagingDelegate extends PagingDelegate<LDAPEntry>
{
    private static final int PAGING_DEFAULT_PAGE_SIZE = 200;
    
    private LDAPResultSet rs = null;
    /*
     * PageSize <= 0 indicates that the page will 
     */
    private int pageSize = -1;
    
    /**
     * 
     */
    public LDAPPagingDelegate(LDAPResultSet rs, int pageSize)
    {
        this.rs = rs;
        // If LDAP paging is enabled, then use it. If not, just use a default page size
        this.pageSize = pageSize > 0 ? pageSize : PAGING_DEFAULT_PAGE_SIZE;
    }

    @Override
    public void close() throws MuleException
    {
        if(this.rs != null) 
        {
            try
            {
                this.rs.close();
            }
            catch(LDAPException ex)
            {
                throw new RuntimeException(ex);
            }
        }
    }

    @Override
    public List<LDAPEntry> getPage()
    {
        try
        {
            // No more elements!
            if(!this.rs.hasNext())
            {
                return null;
            }
            
            List<LDAPEntry> page = new ArrayList<LDAPEntry>(this.pageSize);
            int count = 0;
            
            while(this.rs.hasNext() && count < this.pageSize)
            {
                page.add(this.rs.next());
                count++;
            }
            
            return page;
        }
        catch(LDAPException ex)
        {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public int getTotalResults()
    {
        return -1;
    }

}


