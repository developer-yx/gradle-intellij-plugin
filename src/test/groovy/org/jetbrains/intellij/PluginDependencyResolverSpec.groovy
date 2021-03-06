package org.jetbrains.intellij

import org.jetbrains.intellij.dependency.PluginDependencyManager

class PluginDependencyResolverSpec extends IntelliJPluginSpecBase {
    def 'find jar-type plugin'() {
        given:
        def manager = new PluginDependencyManager(gradleHome, null)

        when:
        def plugin = manager.resolve('org.jetbrains.postfixCompletion', '0.8-beta', null)

        then:
        plugin != null
        plugin.artifact.name == 'intellij-postfix.jar'
        collectFilePaths(plugin.jarFiles, manager.cacheDirectoryPath) ==
                ['/org.jetbrains.postfixCompletion-0.8-beta/intellij-postfix.jar'] as Set
    }

    def 'find zip-type plugin'() {
        given:
        def manager = new PluginDependencyManager(gradleHome, null)

        when:
        def plugin = manager.resolve('org.intellij.plugins.markdown', '8.5.0.20160208', null)

        then:
        plugin != null
        plugin.artifact.name == 'markdown'
        collectFilePaths(plugin.jarFiles, manager.cacheDirectoryPath) ==
                ['/org.intellij.plugins.markdown-8.5.0.20160208/markdown/lib/markdown-javafx-preview.jar',
                 '/org.intellij.plugins.markdown-8.5.0.20160208/markdown/lib/Loboevolution.jar',
                 '/org.intellij.plugins.markdown-8.5.0.20160208/markdown/lib/intellij-markdown.jar',
                 '/org.intellij.plugins.markdown-8.5.0.20160208/markdown/lib/markdown.jar'] as Set
    }

    private static def collectFilePaths(Collection<File> files, String cacheDir) {
        def paths = new HashSet()
        files.each {
            paths << adjustWindowsPath(it.absolutePath.substring(cacheDir.length()))
        }
        paths
    }

}
