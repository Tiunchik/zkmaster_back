//package org.zkmaster.backend.entity.utils;
//
//public class KeyMasterRun {
//    private static final String secret = "secret";
//    private static final String sourceValue = "value";
//
//    public static void main(String[] args) throws Exception {
//        var keySpec = KeyMaster.genSecretKey(secret);
//        DevLog.print("keyMasterRun", "keySpec", keySpec);
//
//        var keySpecToString = KeyMaster.secretKeyToString(keySpec);
//        DevLog.print("keyMasterRun", "keySpecToString", keySpecToString);
//
//        var encodeValue  = KeyMaster.enc(sourceValue, keySpecToString);
//        DevLog.print("keyMasterRun", "encodeValue", encodeValue);
//
//        var decodeEncodedValue  = KeyMaster.dec(encodeValue, keySpecToString);
//        DevLog.print("keyMasterRun", "decodeEncodedValue", decodeEncodedValue);
//
//
////        var t = KeyMaster.genSecretKey(value);
////        DevLog.print("keyMasterRun", "t", t);
////
////        var v = KeyMaster.secretKeyToString(t);
////        DevLog.print("keyMasterRun", "v", v);
////
////        var c = KeyMaster.enc("text", "111");
////        DevLog.print("keyMasterRun", "c", c);
////
////        var d = KeyMaster.dec("text", "111");
////        DevLog.print("keyMasterRun", "d", d);
//    }
//
//}
