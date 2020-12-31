package com.wizzdi.flexicore.boot.base;

import com.wizzdi.flexicore.boot.base.app.App;
import com.wizzdi.flexicore.boot.base.init.FlexiCorePluginManager;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Set;
import java.util.stream.Collectors;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = App.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
public class PluginLoadingTest {

    @Autowired
    private FlexiCorePluginManager flexiCorePluginManager;

    @Test
    public void testNoFailedPlugins(){
        Set<String> started = flexiCorePluginManager.getStartedPlugins().stream().map(f -> f.getPluginId()).collect(Collectors.toSet());
        Set<String> resolved = flexiCorePluginManager.getResolvedPlugins().stream().map(f -> f.getPluginId()).collect(Collectors.toSet());
        Assert.assertEquals(started,resolved);

    }

}
