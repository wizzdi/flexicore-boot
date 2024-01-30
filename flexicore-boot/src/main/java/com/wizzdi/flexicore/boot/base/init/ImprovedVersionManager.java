package com.wizzdi.flexicore.boot.base.init;

import com.github.zafarkhaja.semver.Version;
import org.pf4j.DefaultVersionManager;
import org.pf4j.util.StringUtils;

public class ImprovedVersionManager extends DefaultVersionManager {

    @Override
    public boolean checkVersionConstraint(String version, String constraint) {
        return StringUtils.isNullOrEmpty(constraint) || "*".equals(constraint) || isSatisfies(version, constraint);

    }

    private static boolean isSatisfies(String version, String constraint) {
        try{
            return Version.valueOf(version).satisfies(constraint);
        }
        catch (Throwable e){
            return false;
        }
    }
}
