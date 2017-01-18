/*
02. * $Header: /home/jerenkrantz/tmp/commons/commons-convert/cvs/home/cvs/jakarta-commons//httpclient/src/contrib/org/apache/commons/httpclient/contrib/ssl/AuthSSLInitializationError.java,v 1.2 2004/06/10 18:25:24 olegk Exp $
03. * $Revision$
04. * $Date$
05. *
06. * ====================================================================
07. *
08. *  Copyright 1999-2004 The Apache Software Foundation
09. *
10. *  Licensed under the Apache License, Version 2.0 (the "License");
11. *  you may not use this file except in compliance with the License.
12. *  You may obtain a copy of the License at
13. *
14. *      http://www.apache.org/licenses/LICENSE-2.0
15. *
16. *  Unless required by applicable law or agreed to in writing, software
17. *  distributed under the License is distributed on an "AS IS" BASIS,
18. *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
19. *  See the License for the specific language governing permissions and
20. *  limitations under the License.
21. * ====================================================================
22. *
23. * This software consists of voluntary contributions made by many
24. * individuals on behalf of the Apache Software Foundation.  For more
25. * information on the Apache Software Foundation, please see
26. * <http://www.apache.org/>.
27. */

package com.ailk.eaap.o2p.util.file.remote.session.http;

/**
 * <p>
33. * Signals fatal error in initialization of {@link AuthSSLProtocolSocketFactory}.
34. * </p>
35. * 
36. * @author <a href="mailto:oleg@ural.ru">Oleg Kalnichevski</a>
37. * 
38. * <p>
39. * DISCLAIMER: HttpClient developers DO NOT actively support this component.
40. * The component is provided as a reference material, which may be inappropriate
41. * for use without additional customization.
42. * </p>
43. */
public class AuthSSLInitializationError extends Error {

    /**
     * Creates a new AuthSSLInitializationError.
     */
    public AuthSSLInitializationError() {
        super();
    }

    /**
     * Creates a new AuthSSLInitializationError with the specified message.
     *
     * @param message error message
     */
    public AuthSSLInitializationError(String message) {
        super(message);
    }
}
