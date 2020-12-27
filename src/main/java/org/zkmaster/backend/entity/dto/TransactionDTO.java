package org.zkmaster.backend.entity.dto;

import org.zkmaster.backend.entity.ZKNode;

import java.util.List;

public class TransactionDTO {

    private List<ZKNode> deletes; // list of Tree - signature like "deletes"
    private List<ZKNode> updates; // list of Tree - signature like "updates"
    private List<ZKNode> creates; // list of Tree - signature like "creates"

    private List<RequestDTO> deletesList; // List of usual request - signature like "deletes"
    private List<RequestDTO> updatesList; // List of usual request - signature like "updates"
    private List<RequestDTO> createsList; // List of usual request - signature like "creates"

}
