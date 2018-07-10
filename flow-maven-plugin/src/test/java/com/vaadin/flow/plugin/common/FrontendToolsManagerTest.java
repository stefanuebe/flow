package com.vaadin.flow.plugin.common;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Collections;
import java.util.Map;

import com.github.eirslett.maven.plugins.frontend.lib.ProxyConfig;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import com.vaadin.flow.plugin.TestUtils;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FrontendToolsManagerTest {
    private static final String ES5_DIRECTORY_NAME = "es5";
    private static final String ES6_DIRECTORY_NAME = "es6";
    private static final String SHELL_FILE_NAME = "shell.html";

    @Rule
    public final TemporaryFolder temporaryDirectory = new TemporaryFolder();

    private File workingDirectory;
    private File es6Directory;
    private File outputDirectory;

    @Before
    public void setUp() throws IOException {
        workingDirectory = createTemporaryDirectory("workingDirectory");
        es6Directory = createSubDirectory(workingDirectory, "es6source");
        outputDirectory = createSubDirectory(workingDirectory,
                "outputDirectory");

        new JarContentsManager().copyFilesFromJarTrimmingBasePath(
                TestUtils.getTestJar("paper-button-with-dependencies.jar"),
                null, es6Directory);

        Files.write(workingDirectory.toPath().resolve(SHELL_FILE_NAME),
                Collections.singleton(String.format(
                        "<link rel='import' href='%s/bower_components/paper-button/paper-button.html'>",
                        es6Directory.getName())));
    }

    private File createTemporaryDirectory(String name) {
        try {
            return temporaryDirectory.newFolder(name);
        } catch (IOException e) {
            throw new AssertionError(String.format(
                    "Failed to create temporary directory with name '%s'",
                    name), e);
        }
    }

    private File createSubDirectory(File parent, String name) {
        File newDirectory = new File(parent, name);
        assertTrue(String.format("Failed to create es6 source directory '%s'",
                newDirectory), newDirectory.mkdirs());
        return newDirectory;
    }

    @Test
    public void test() {
        FrontendDataProvider dataProviderMock = mock(
                FrontendDataProvider.class);
        when(dataProviderMock.getEs6SourceDirectory()).thenReturn(es6Directory);
        when(dataProviderMock.shouldBundle()).thenReturn(true);
        when(dataProviderMock.shouldMinify()).thenReturn(true);
        when(dataProviderMock.shouldHash()).thenReturn(true);
        when(dataProviderMock.createShellFile(workingDirectory))
                .thenReturn(SHELL_FILE_NAME);

        FrontendToolsManager manager = manager(dataProviderMock);
        manager.installFrontendTools(new ProxyConfig(Collections.emptyList()),
                "v8.11.1", "v1.6.0", 0);

        Map<String, File> stringFileMap = manager
                .transpileFiles(outputDirectory, false);
        System.out.println(stringFileMap);
    }

    private FrontendToolsManager manager(FrontendDataProvider dataProvider) {
        return new FrontendToolsManager(workingDirectory, ES5_DIRECTORY_NAME,
                ES6_DIRECTORY_NAME, dataProvider);
    }
}