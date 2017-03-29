/*
 * Copyright (c) 2008-2015 Emmanuel Dupuy
 * This program is made available under the terms of the GPLv3 License.
 */

package org.jd.gui.service.pastehandler

import org.jd.gui.api.API
import org.jd.gui.spi.PasteHandler
import org.jd.gui.view.component.LogPage

class LogPasteHandler implements PasteHandler {
    protected static int counter = 0

    boolean accept(Object obj) { obj instanceof String }

    void paste(API api, Object obj) {
        def title = 'clipboard-' + (++counter) + '.log'
        def uri = URI.create('memory://' + title)
        def content = obj?.toString()
        api.addPanel(title, null, null, new LogPage(api, uri, content))
    }
}
