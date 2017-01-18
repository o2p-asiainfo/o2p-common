/** 
 * Project Name:o2p-common 
 * File Name:TestBase.java 
 * Package Name:com.ailk.eaap.op2.common.test 
 * Date:2015年8月11日下午2:53:13 
 * Copyright (c) 2015, www.asiainfo.com All Rights Reserved. 
 * 
 */

package com.ailk.eaap.op2.common.test;

import org.springframework.context.ApplicationContext;

public class TestBase
{
    private ApplicationContext ctx;

    public ApplicationContext getCtx()
    {
        return this.ctx;
    }

    public void setCtx(ApplicationContext ctx)
    {
        this.ctx = ctx;
    }
}
