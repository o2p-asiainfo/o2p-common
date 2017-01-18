package com.ailk.eaap.o2p.common;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.regex.Pattern;

/**
 * 
 * @author 颖勤
 *
 */
public class VersionUtil {
	private final static Pattern VERSION_SEPARATOR = Pattern.compile("[-_./;:]");
    /**
     * Will attempt to load the maven version for the given groupId and
     * artifactId.  Maven puts a pom.properties file in
     * META-INF/maven/groupId/artifactId, containing the groupId,
     * artifactId and version of the library.
     *
     * @param classLoader the ClassLoader to load the pom.properties file from
     * @param groupId the groupId of the library
     * @param artifactId the artifactId of the library
     * @return The version
     */
    public static Version mavenVersionFor(ClassLoader classLoader, String groupId, String artifactId) {
        InputStream pomPoperties = classLoader.getResourceAsStream("META-INF/maven/" + groupId
                + "/" + artifactId + "/pom.properties");
        if (pomPoperties != null) {
            try {
                Properties props = new Properties();
                props.load(pomPoperties);
                String versionStr = props.getProperty("version");
                String pomPropertiesArtifactId = props.getProperty("artifactId");
                String pomPropertiesGroupId = props.getProperty("groupId");
                return parseVersion(versionStr, pomPropertiesGroupId, pomPropertiesArtifactId);
            } catch (IOException e) {
                // Ignore
            } finally {
                try {
                    pomPoperties.close();
                } catch (IOException e) {
                    // Ignore
                }
            }
        }
        return Version.unknownVersion();
    }
    public static Version parseVersion(String versionStr, String groupId, String artifactId)
    {
        if (versionStr == null) {
            return null;
        }
        versionStr = versionStr.trim();
        if (versionStr.length() == 0) {
            return null;
        }
        String[] parts = VERSION_SEPARATOR.split(versionStr);
        int major = parseVersionPart(parts[0]);
        int minor = (parts.length > 1) ? parseVersionPart(parts[1]) : 0;
        int patch = (parts.length > 2) ? parseVersionPart(parts[2]) : 0;
        String snapshot = (parts.length > 3) ? parts[3] : null;

        return new Version(major, minor, patch, snapshot,
                groupId, artifactId);
    }   
    protected static int parseVersionPart(String partStr)
    {
        partStr = partStr.toString();
        int len = partStr.length();
        int number = 0;
        for (int i = 0; i < len; ++i) {
            char c = partStr.charAt(i);
            if (c > '9' || c < '0') break;
            number = (number * 10) + (c - '0');
        }
        return number;
    }    
}
