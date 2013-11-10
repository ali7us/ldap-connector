/*
 * $Id$
 * --------------------------------------------------------------------------------------
 * Copyright (c) MuleSource, Inc.  All rights reserved.  http://www.mulesource.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
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
    private LDAPResultSet rs = null;
    private int pageSize = -1;
    
    /**
     * 
     */
    public LDAPPagingDelegate(LDAPResultSet rs, int pageSize)
    {
        this.rs = rs;
        this.pageSize = pageSize;
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


