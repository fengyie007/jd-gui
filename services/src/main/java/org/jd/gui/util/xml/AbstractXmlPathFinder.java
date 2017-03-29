/*
 * Copyright (c) 2008-2015 Emmanuel Dupuy
 * This program is made available under the terms of the GPLv3 License.
 */

package org.jd.gui.util.xml;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.StringReader;
import java.util.*;

public abstract class AbstractXmlPathFinder {

    protected HashMap<String, HashSet<String>> tagNameToPaths = new HashMap<>();
    protected StringBuffer sb = new StringBuffer(200);

    public AbstractXmlPathFinder(Collection<String> paths) {
        for (String path : paths) {
            if ((path != null) && (path.length() > 0)) {
                // Normalize path
                path = '/' + path;
                int lastIndex = path.lastIndexOf('/');
                String lastTagName = path.substring(lastIndex+1);

                // Add tag names to map
                HashSet<String> setOfPaths = tagNameToPaths.get(lastTagName);
                if (setOfPaths == null) {
                    tagNameToPaths.put(lastTagName, setOfPaths = new HashSet<>());
                }
                setOfPaths.add(path);
            }
        }
    }

    public void find(String text) {
        sb.setLength(0);

        try {
            XMLInputFactory factory = XMLInputFactory.newInstance();
            XMLStreamReader reader = factory.createXMLStreamReader(new StringReader(text));

            String tagName = "";
            int offset = 0;

            while (reader.hasNext()) {
                reader.next();

                switch (reader.getEventType())
                {
                case XMLStreamReader.START_ELEMENT:
                    sb.append('/').append(tagName = reader.getLocalName());
                    offset = reader.getLocation().getCharacterOffset();
                    break;
                case XMLStreamReader.END_ELEMENT:
                    sb.setLength(sb.length() - reader.getLocalName().length() - 1);
                    break;
                case XMLStreamReader.CHARACTERS:
                    HashSet<String> setOfPaths = tagNameToPaths.get(tagName);

                    if (setOfPaths != null) {
                        String path = sb.toString();

                        if (setOfPaths.contains(path)) {
                            // Search start offset
                            while (offset > 0) {
                                if (text.charAt(offset) == '>') {
                                    break;
                                } else {
                                    offset--;
                                }
                            }

                            handle(path.substring(1), reader.getText(), offset+1);
                        }
                    }
                    break;
                }
            }
        } catch (XMLStreamException ignore) {
        }
    }

    public abstract void handle(String path, String text, int position);
}
