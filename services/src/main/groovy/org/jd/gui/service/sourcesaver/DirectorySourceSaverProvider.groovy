/*
 * Copyright (c) 2008-2015 Emmanuel Dupuy
 * This program is made available under the terms of the GPLv3 License.
 */

package org.jd.gui.service.sourcesaver

import groovy.transform.CompileStatic
import org.jd.gui.api.API
import org.jd.gui.api.model.Container
import org.jd.gui.spi.SourceSaver

import java.nio.file.Files
import java.nio.file.Path

class DirectorySourceSaverProvider extends AbstractSourceSaverProvider {

    /**
     * @return local + optional external selectors
     */
    String[] getSelectors() { ['*:dir:*'] + externalSelectors }

    String getSourcePath(Container.Entry entry) { entry.path }

    int getFileCount(API api, Container.Entry entry) { getFileCount(api, entry.children) }

    @CompileStatic
    protected int getFileCount(API api, Collection<Container.Entry> entries) {
        int count = 0

        for (def e : entries) {
            count += api.getSourceSaver(e)?.getFileCount(api, e)
        }

        return count
    }

    void save(API api, SourceSaver.Controller controller, SourceSaver.Listener listener, Path path, Container.Entry entry) {
        save(api, controller, listener, path, entry.children)
    }

    @CompileStatic
    protected void save(API api, SourceSaver.Controller controller, SourceSaver.Listener listener, Path path, Collection<Container.Entry> entries) {
        Files.createDirectories(path)

        for (def e : entries) {
            if (controller.isCancelled()) {
                break
            }

            def saver = api.getSourceSaver(e)

            if (saver) {
                def sp = saver.getSourcePath(e)
                def p = path.fileSystem.getPath(sp)
                saver.save(api, controller, listener, p, e)
            }
        }
    }
}
