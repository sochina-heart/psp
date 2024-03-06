package com.sochina.freemarker.domain.properties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DependencyProperties {
    private String groupId;
    private String artifactId;
    private String version;
    private String classifier;
    private String scope;
    private String optional;
    private String systemPath;
    private String type;
}
