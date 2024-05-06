package com.wizzdi;

/*
 * Copyright 2001-2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;


@Mojo(name = "extract-plugins", defaultPhase = LifecyclePhase.PROCESS_TEST_SOURCES)

public class PluginTestMojo extends AbstractMojo {
    @Parameter( defaultValue = "${project}", readonly = true )
    protected MavenProject project;

    @Parameter( defaultValue="plugins", required = true)
    private String pluginsBasePackage;

    @Parameter( defaultValue="plugins", required = true)
    private String outputDirectory;

    public void execute()  throws MojoExecutionException {
        String testOutputDirectory = project.getBuild().getTestOutputDirectory();
        File pluginsDirectory = new File(testOutputDirectory,this.pluginsBasePackage);
        File outputDirectory = new File(project.getBuild().getDirectory(),this.outputDirectory);
        getLog().info("moving from  "+pluginsDirectory.getAbsolutePath()+" to "+outputDirectory.getAbsolutePath());
        if(!pluginsDirectory.exists()){
            getLog().info("no plugins to or plugins already extracted");
            return;
        }
        if(outputDirectory.exists()){
            //delete directory content and directory itself
            try {
                Files.walk(outputDirectory.toPath())
                        .sorted((o1, o2) -> -o1.compareTo(o2))
                        .map(Path::toFile)
                        .forEach(File::delete);
                Files.deleteIfExists(outputDirectory.toPath());
            } catch (IOException e) {
                throw new MojoExecutionException("failed extracting plugins",e);
            }

        }
        try {
            Files.move(pluginsDirectory.toPath(),outputDirectory.toPath());
            getLog().info("moved from  "+pluginsDirectory.getAbsolutePath()+" to "+outputDirectory.getAbsolutePath());

            File extensionFile = new File(testOutputDirectory,"META-INF/extensions.idx");
            if(extensionFile.exists()){
                getLog().info("removing extension file: "+extensionFile.getAbsolutePath());

                if(!extensionFile.delete()){
                    getLog().error("failed removing  extension file: "+extensionFile.getAbsolutePath());

                }
                getLog().info("removed  extension file: "+extensionFile.getAbsolutePath());

            }
        } catch (IOException e) {
            throw new MojoExecutionException("failed extracting plugins",e);
        }


    }
}
