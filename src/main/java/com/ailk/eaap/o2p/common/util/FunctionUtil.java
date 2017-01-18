/** 
 * Project Name:o2p-common 
 * File Name:FunctionUtil.java 
 * Package Name:com.ailk.eaap.o2p.common.util 
 * Date:2015年7月9日下午2:18:57 
 * Copyright (c) 2015, www.asiainfo.com All Rights Reserved. 
 * 
 */

package com.ailk.eaap.o2p.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.asiainfo.foundation.log.Logger;

/**
 * <p>
 * ClassName:FunctionUtil <br>
 * <p>
 * Function: TODO ADD FUNCTION. <br>
 * <p>
 * Reason: TODO ADD REASON. <br>
 * <p>
 * Date: 2015年7月9日 下午2:18:57 <br>
 * 
 * @author zhongming
 * @since JDK 1.6
 */
public class FunctionUtil
{

    private static final Logger LOG = Logger.getLog(FunctionUtil.class);

    public static Object split(String nodeValue, String pattern)
    {
        Matcher matcher = Pattern.compile(pattern).matcher(nodeValue);
        String value = "";
        if (matcher.find())
        {
            value = matcher.group(0);
        }
        return value;
    }

    public static Object merge(Object... nodeValues)
    {
        StringBuffer sbu = new StringBuffer();
        for (Object nodeValue : nodeValues)
        {
            sbu.append(nodeValue);
        }
        return sbu.toString();
    }

    public static Object dateFormatter(String date, String formatter)
            throws ParseException
    {
        SimpleDateFormat formater = new SimpleDateFormat(formatter);
        Date formatterDate = new Date();
        formatterDate = formater.parse(date);
        return formatterDate;
    }

    public static Object substring(String value, int form, int to)
    {
        if (form < value.length() && to <= value.length())
        {
            return value.substring(form, to);
        }
        else
        {
            LOG.error("substring error form {" + form + "} to {" + to + "}");
        }
        return "";
    }

    public static String stringFormatter(String date) throws ParseException
    {
        Date dateFormatter = stringToDate(date);
        return dateToString(dateFormatter);
    }

    public static String dateToString(Date date)
    {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HHmmss");
        return formatter.format(date);
    }

    public static Date stringToDate(String date) throws ParseException
    {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-ddHHmmss");
        Date formatterDate = new Date();
        formatterDate = formatter.parse(date);
        return formatterDate;
    }

    @SuppressWarnings("deprecation")
    public static String date2String(String date, String formatter)
    {
        Date d = new Date(date);
        SimpleDateFormat formater = new SimpleDateFormat(formatter);
        return formater.format(d);
    }
}
