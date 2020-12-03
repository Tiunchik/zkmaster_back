package org.zkmaster.backend.adapters;

import org.apache.zookeeper.data.Stat;

import java.util.StringJoiner;

/**
 * 2 reasons why it exists:
 * - extra documentation.
 * - (would be) addition features.
 */
public class StateAdapter {
    private final Stat original;

    public StateAdapter(Stat original) {
        this.original = original;
    }

    /**
     * normal toString() -_-.
     *
     * @return -
     */
    @Override
    public String toString() {
        return new StringJoiner(", ", StateAdapter.class.getSimpleName() + "[", "]")
                .add("czxid=" + original.getCzxid())
                .add("mzxid=" + original.getMzxid())
                .add("ctime=" + original.getCtime())
                .add("mtime=" + original.getMtime())
                .add("version=" + original.getVersion())
                .add("cversion=" + original.getCversion())
                .add("aversion=" + original.getAversion())
                .add("ephemeralOwner=" + original.getEphemeralOwner())
                .add("dataLength=" + original.getDataLength())
                .add("numChildren=" + original.getNumChildren())
                .add("pzxid=" + original.getPzxid())
                .toString();
    }
}
