/*
 * Copyright 2011 CodeGist.org
 *
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 *
 *  ===================================================================
 *
 *  More information at http://www.codegist.org.
 */

package org.codegist.web.servlet;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 */
public class CRestRedirectorServlet extends HttpServlet {

     private Redirects redirects;

    @Override
    public void init(ServletConfig config) throws ServletException {
        redirects = Redirects.build(config.getInitParameter("redirects.config"));
    }

    @Override
    protected void doGet(HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws ServletException, IOException {
        if(redirects.hasRedirect(httpRequest.getRequestURI())) {
            Redirects.Redirect to = redirects.getRedirect(httpRequest.getRequestURI());
            httpResponse.setStatus(to.getStatus());
            httpResponse.setHeader("Location", to.getTo());
        }else{
            httpResponse.sendError(404);
        }
    }

}
