package org.zkmaster.backend.mocks;

import org.zkmaster.backend.entity.Host;
import org.zkmaster.backend.entity.ZKTransaction;
import org.zkmaster.backend.exceptions.node.NodeCreateException;
import org.zkmaster.backend.exceptions.node.NodeDeleteException;
import org.zkmaster.backend.exceptions.node.NodeExistsException;
import org.zkmaster.backend.exceptions.node.NodeSaveException;

import java.util.LinkedList;
import java.util.List;

public class ZKTransactionFake implements ZKTransaction {
    private final Host host;
    private List<String> actions = new LinkedList<>();

    public ZKTransactionFake(Host host) {
        this.host = host;
    }

    public ZKTransactionFake(Host host, List<String> actions) {
        this.host = host;
        this.actions = actions;
    }


    @Override
    public ZKTransaction create(String path, String value) {
        actions.add("ADD" + COMMAND_SPLIT + path + VALUE_SPLIT + value);
        return this;
    }

    @Override
    public ZKTransaction delete(String path) {
        actions.add("DEL" + COMMAND_SPLIT + path + VALUE_SPLIT + ' ');
        return this;
    }

    @Override
    public ZKTransaction update(String path, String value) {
        actions.add("UPD" + COMMAND_SPLIT + path + VALUE_SPLIT + value);
        return this;
    }

    @Override
    public <E extends Exception> boolean commit(String errMsg, E exception) throws E {
        try {
            System.out.println("ZKTransactionFake :: print()");
            print();
            for (var action : actions) {
                System.out.println("ZKTransactionFake :: action==" + action + " RUN");
                if (action.startsWith("ADD")) {
                    host.create(extractPath(action), extractValue(action));
                } else if (action.startsWith("DEL")) {
                    host.delete(extractPath(action));
                } else if (action.startsWith("UPD")) {
                    host.setData(extractPath(action), extractValue(action));
                }
                System.out.println("ZKTransactionFake :: action==" + action + " END");
            }
        } catch (NodeExistsException | NodeCreateException
                | NodeDeleteException | NodeSaveException origException) {
            System.err.println(errMsg);
            origException.printStackTrace();
            throw exception;
        }
        return true;
    }

    public void print() {
        System.out.println("ZKTransactionMock#print() RUN");
        actions.forEach(System.out::println);
        System.out.println("ZKTransactionMock#print() END");
    }

    @Override
    public String toString() {
        var builder = new StringBuilder("ZKTransactionFake :: toSting()==");
        actions.forEach(builder::append);
        return builder.toString();
    }

    private static final String COMMAND_SPLIT = " :: ";
    private static final String VALUE_SPLIT = " : ";

    private static String extractPath(String action) {
        return action.substring(action.indexOf(COMMAND_SPLIT) + COMMAND_SPLIT.length(),
                action.indexOf(VALUE_SPLIT));
    }

    private static String extractValue(String action) {
        return action.substring(action.indexOf(VALUE_SPLIT) + VALUE_SPLIT.length());
    }

}
