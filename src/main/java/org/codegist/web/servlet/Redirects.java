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

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 */
public class Redirects {

    private final Map<String,Redirect> redirects;

    public Redirects(Map<String, Redirect> redirects) {
        this.redirects = redirects;
    }

    public boolean hasRedirect(String url) {
        return redirects.containsKey(url);
    }

    public Redirect getRedirect(String url){
        return redirects.get(url);
    }

    public static Redirects build(String config){
        try {
            DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = db.parse(new FileInputStream(config));
            NodeList nodeList = doc.getElementsByTagName("redirect");

            XPath xpath = XPathFactory.newInstance().newXPath();
            XPathExpression xfrom = xpath.compile("./from/text()");
            XPathExpression xto = xpath.compile("./to/text()");
            XPathExpression xstatus = xpath.compile("./to/@status");

             Map<String,Redirect> redirects = new HashMap<String, Redirect>();
            for(int i = 0, max = nodeList.getLength(); i < max; i++){
                Node redirectNode = nodeList.item(i);
                String from = xfrom.evaluate(redirectNode);
                String to = xto.evaluate(redirectNode);
                int status = Integer.parseInt(xstatus.evaluate(redirectNode));
                redirects.put(from, new Redirect(from, to, status));
            }
            return new Redirects(redirects);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static class Redirect {
        private final String from;
        private final String to;
        private final int status;

        public Redirect(String from, String to, int status) {
            this.from = from;
            this.to = to;
            this.status = status;
        }

        public String getFrom() {
            return from;
        }

        public String getTo() {
            return to;
        }

        public int getStatus() {
            return status;
        }
    }
}
