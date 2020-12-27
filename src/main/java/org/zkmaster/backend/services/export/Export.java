package org.zkmaster.backend.services.export;

import org.zkmaster.backend.entity.ZKNode;

import java.util.List;

public interface Export {

    public List<String> export(ZKNode root);

}
