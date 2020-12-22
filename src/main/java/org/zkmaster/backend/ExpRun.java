package org.zkmaster.backend;

import java.util.LinkedList;
import java.util.List;
import java.util.StringJoiner;

public class ExpRun {

    public static void main(String[] args) {
        var q = new LinkedList<>(List.of(
//                new Model("/", "val0"),
//                new Model("/1", "val1"),
                new Model("/1/1", "value rename"),  // <<==
                new Model("/1/1/1", "value child 1"),
                new Model("/1/1/1/1", "value child 1-1")
        ));

        q.add(new Model("/1/1/1/2", "value child 1-2"));



        var createTransaction = new LinkedList<Model>();
        var deletePaths = new LinkedList<Model>();
        var renamePath = "/1/1";
        var renameValue = "set";
        var rslCorePath = renamePath + "/" + renameValue;
//        var corePathWithoutName = renamePath + '/';
        var corePathWithoutName = renamePath.substring(0, renamePath.lastIndexOf('/') + 1);
        var oldName = renamePath.substring(renamePath.lastIndexOf('/') + 1);
        System.out.println("LOG :: oldName==" + oldName);

        int i = 0;

        while (!q.isEmpty()) {
            var current = q.removeFirst();
            System.out.println("INIT :: #" + i + " -- current= " + current);
            var temp = new Model(
                    replacePath(rslCorePath, current.path, corePathWithoutName, renameValue, oldName),
                    current.getValue()
            );
            createTransaction.add(temp);
            i++;
        }

        int j = 0;
        for (var each: createTransaction) {
            System.out.println("RESULT :: " + j + ' ' + each.getPath() + ' ' + each.getValue());
            j++;
        }

    }

    /**
     * WAY 1: just replace
     * WAY 2: RegExp(count num of slashes in original path and replace by regExp)
     * WAY 3: original length && new length
     * WAY 4: split("/")
     *
     * /1/1   :: ? oldCorePath
     * /1/set :: ? corePath
     * /1/1/1 :: oldPath
     * set    :: newPathValue
     * /1/    :: corePathWithoutName
     * 1      :: ? oldName
     *
     * /1/set/1 :: return
     */
    private static String replacePath(String corePath, String oldPath,
                                      String corePathWithoutName, String newPathValue,
                                      String oldName) {

        System.out.println("method: Replace START");
        System.out.println("LOG :: corePath==" + corePath);
        System.out.println("LOG :: oldPath==" + oldPath);
        System.out.println("LOG :: corePathWithoutName==" + corePathWithoutName);
        System.out.println("LOG :: newPathValue==" + newPathValue);
        System.out.println("LOG :: oldName==" + oldName);
        System.out.println("method: Replace RUN");

        var corePathWithoutNameLength = corePathWithoutName.length();
        System.out.println("LOG :: corePathWithoutNameLength==" + corePathWithoutNameLength);
//        var firstPart = oldPath.substring(0, corePathWithoutNameLength -1);
        var firstPart = corePathWithoutName;
        System.out.println("LOG :: firstPart==" + firstPart);
//        var secondPart = oldPath.substring(firstPart.length() -1 + oldName.length() -1);
        var secondPart = oldPath.substring(corePathWithoutNameLength -1 + oldName.length() +1);
        System.out.println("LOG :: secondPart==" + secondPart);

        var rsl = firstPart + newPathValue  + secondPart;


        System.out.println("Replace FINISH");
        return rsl;
    }

    private static class Model {
        final String path;
        final String value;

        public Model(String path, String value) {
            this.path = path;
            this.value = value;
        }

        public String getPath() {
            return path;
        }

        public String getValue() {
            return value;
        }


        @Override
        public String toString() {
            return new StringJoiner(", ", Model.class.getSimpleName() + "[", "]")
                    .add("path='" + path + "'")
                    .add("value='" + value + "'")
                    .toString();
        }
    }
}
