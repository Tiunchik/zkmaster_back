package org.zkmaster.backend.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.zkmaster.backend.devutil.DevLog;
import org.zkmaster.backend.exceptions.DataImportFailException;
import org.zkmaster.backend.exceptions.HostProviderNotFoundException;
import org.zkmaster.backend.exceptions.HostWrongAddressException;
import org.zkmaster.backend.exceptions.InjectionFailException;
import org.zkmaster.backend.exceptions.node.*;

/**
 * Transform Java exceptions into HTTP protocol answers.
 */
@ControllerAdvice
@CrossOrigin(value = {"*"})
public class ExceptionHandlersController {
    
    
    /* Node Exceptions */
    
    
    @ExceptionHandler(NodeCreateException.class)
    @ResponseStatus(value = HttpStatus.CONFLICT, reason = "Fail to create this node.")
    public void handleErrorNodeCreateException(Exception ex) {
        ex.printStackTrace();
    }
    
    @ExceptionHandler(NodeDeleteException.class)
    @ResponseStatus(value = HttpStatus.CONFLICT, reason = "Fail to delete this node.")
    public void handleErrorNodeDeleteException(Exception ex) {
        ex.printStackTrace();
    }
    
    @ExceptionHandler(NodeExistsException.class)
    @ResponseStatus(value = HttpStatus.CONFLICT, reason = "This node already exists.")
    public void handleErrorNodeExists(Exception ex) {
        ex.printStackTrace();
    }
    
    @ExceptionHandler(NodeReadException.class)
    @ResponseStatus(value = HttpStatus.CONFLICT, reason = "Fail to read this node.")
    public void handleErrorNodeReadException(Exception ex) {
        ex.printStackTrace();
    }
    
    @ExceptionHandler(NodeSaveException.class)
    @ResponseStatus(value = HttpStatus.CONFLICT, reason = "Fail to save this node.")
    public void handleErrorNodeSaveException(Exception ex) {
        ex.printStackTrace();
    }
    
    
    /* Non-node Exceptions */
    
    
    @ExceptionHandler(DataImportFailException.class)
    @ResponseStatus(value = HttpStatus.CONFLICT, reason = "Fail to import this data into target ZooKeeper.")
    public void handleErrorDataImportFailException(Exception ex) {
        ex.printStackTrace();
    }
    
    @ExceptionHandler(HostProviderNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Fail to find connection with ZooKeeper on server.")
    public void handleErrorHostProviderNotFoundException(Exception ex) {
        ex.printStackTrace();
    }
    
    @ExceptionHandler(HostWrongAddressException.class)
    @ResponseStatus(value = HttpStatus.CONFLICT, reason = "Wrong ZooKeeper address.")
    public void handleErrorHostWrongAddressException(Exception ex) {
        ex.printStackTrace();
    }
    
    @ExceptionHandler(InjectionFailException.class)
    @ResponseStatus(value = HttpStatus.CONFLICT, reason = "Fail to copy-paste nodes from source ZK to target ZK.")
    public void handleErrorInjectionFailException(Exception ex) {
        ex.printStackTrace();
    }
    
    
    /* Unexpected(all other) Exceptions */
    
    
    @ExceptionHandler({Exception.class})
    @ResponseStatus(value = HttpStatus.CONFLICT, reason = "IDK, see reason in Back-end logs.")
    public void handleError(Exception ex) {
        DevLog.print("ExceptionHandlersController", "Unexpected exception");
        ex.printStackTrace();
    }
    
}
