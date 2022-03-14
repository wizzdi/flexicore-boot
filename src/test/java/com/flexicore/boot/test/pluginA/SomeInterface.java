package com.flexicore.boot.test.pluginA;

import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import org.pf4j.Extension;

@Extension
public interface SomeInterface extends Plugin {

	String doSomething();
}
