package com.flexicore;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.*;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.classworlds.realm.ClassRealm;
import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.JavaType;
import org.jboss.forge.roaster.model.source.JavaClassSource;

import java.io.*;
import java.nio.file.Files;
import java.util.*;


@Mojo(name="process",threadSafe=true,requiresDependencyResolution= ResolutionScope.COMPILE,defaultPhase= LifecyclePhase.PROCESS_SOURCES)
public class App extends AbstractMojo
{

    @Component
    protected MavenProject project;

    @Parameter( defaultValue="${project.build.directory}/generated-sources/apt", required = true)
    private File outputDir;

    @Parameter( defaultValue="false", required = true)
    private boolean safe;


    public void execute() throws MojoExecutionException, MojoFailureException {
List<String> sourceFolders=project.getCompileSourceRoots();

         HashMap<String,String> map= new HashMap<>();
        HashMap<String,String> canonical= new HashMap<>();

       for (String folder: sourceFolders){
                File f= new File(folder);
           String[] exts=new String[1];
           exts[0]="java";
           Collection<File> javaFiles = FileUtils.listFiles(f,exts,true);
            for (File jFile: javaFiles){
                JavaType<?> javaClass =
                        null;
                try {
                    javaClass = Roaster.parse(jFile);
                } catch ( IOException e) {
                    e.printStackTrace();
                    continue;
                }
                String jname=javaClass.getCanonicalName();
               map.put(jname,jFile.getAbsolutePath());
                if(jFile.getName().endsWith("_.java")){
                    canonical.put(jname,jFile.getAbsolutePath());
                }
            }


       }


       for (Map.Entry<String,String> entry: canonical.entrySet()){
                String nonCanonicalName=entry.getKey().substring(0,entry.getKey().length()-1);
           String path=map.get(nonCanonicalName);
           if(path!=null){
               File jFile= new File(path);
               JavaClassSource javaClass =
                       null;
               try {
                   javaClass = Roaster.parse(JavaClassSource.class,jFile);
               } catch (IOException e) {
                   e.printStackTrace();
                   continue;
               }
               String nonCanonicalSuper=javaClass.getSuperType();
               if(nonCanonicalSuper.equals(Object.class.getCanonicalName())){
                   continue;
               }

               String canonicalSuper=nonCanonicalSuper+"_";
               if(!safe||canonical.containsKey(canonicalSuper)){
                   jFile= new File(entry.getValue());
                   try {
                       javaClass=Roaster.parse(JavaClassSource.class,jFile);
                   } catch (IOException e) {
                       e.printStackTrace();
                       continue;
                   }

                   javaClass.setSuperType(canonicalSuper);
                   try {
                       Files.write(jFile.toPath(), javaClass.toString().getBytes());
                   } catch (IOException e) {
                       e.printStackTrace();
                   }
               }
           }
       }








    }
}
