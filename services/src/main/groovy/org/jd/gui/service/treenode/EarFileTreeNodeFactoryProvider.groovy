/*
 * Copyright (c) 2008-2015 Emmanuel Dupuy
 * This program is made available under the terms of the GPLv3 License.
 */

package org.jd.gui.service.treenode

import org.jd.gui.api.API
import org.jd.gui.api.feature.ContainerEntryGettable
import org.jd.gui.api.feature.UriGettable
import org.jd.gui.api.model.Container
import org.jd.gui.view.data.TreeNodeBean

import javax.swing.*
import javax.swing.tree.DefaultMutableTreeNode

class EarFileTreeNodeFactoryProvider extends ZipFileTreeNodeFactoryProvider {
    static final ImageIcon ICON = new ImageIcon(JarFileTreeNodeFactoryProvider.class.classLoader.getResource('org/jd/gui/images/ear_obj.gif'))

    /**
     * @return local + optional external selectors
     */
    String[] getSelectors() { ['*:file:*.ear'] + externalSelectors }

    public <T extends DefaultMutableTreeNode & ContainerEntryGettable & UriGettable> T make(API api, Container.Entry entry) {
        int lastSlashIndex = entry.path.lastIndexOf('/')
        def name = entry.path.substring(lastSlashIndex+1)
        def node = new TreeNode(entry, 'ear', new TreeNodeBean(label:name, icon:ICON))
        // Add dummy node
        node.add(new DefaultMutableTreeNode())
        return node
    }
}
