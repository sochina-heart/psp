package com.sochina.base.domain.properties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseProperties implements Serializable {
    private static final long serialVersionUID = 1L;
    private boolean enable = true;
    private int order = -1;
    private List<String> excludeUrl = new ArrayList<>();
}
