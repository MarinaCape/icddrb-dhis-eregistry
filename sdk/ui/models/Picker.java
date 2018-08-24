package org.icddrb.dhis.client.sdk.ui.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.icddrb.dhis.client.sdk.utils.Preconditions;

public class Picker implements Serializable {
    private final List<Picker> children;
    private final String hint;
    private final String id;
    private final boolean isRoot;
    private final String name;
    private final Picker parent;
    private Picker selectedChild;

    public static class Builder {
        private String hint;
        private String id;
        private boolean isRoot;
        private String name;
        private Picker parent;

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder hint(String hint) {
            this.hint = hint;
            return this;
        }

        public Builder parent(Picker parent) {
            this.parent = parent;
            return this;
        }

        public Builder asRoot() {
            this.isRoot = true;
            return this;
        }

        public Picker build() {
            if (this.parent == null) {
                this.isRoot = true;
            }
            return new Picker(this.id, this.name, this.hint, this.parent, this.isRoot);
        }
    }

    private Picker(String id, String name, String hint, Picker parent, boolean isRoot) {
        this.id = id;
        this.name = name;
        this.hint = hint;
        this.parent = parent;
        this.isRoot = isRoot;
        this.children = new ArrayList();
    }

    public String getHint() {
        return this.hint;
    }

    public String getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public Picker getParent() {
        return this.parent;
    }

    public boolean isLeaf() {
        return this.children.isEmpty();
    }

    public boolean isRoot() {
        return this.isRoot;
    }

    public boolean addChild(Picker picker) {
        Preconditions.isNull(picker, "Picker must not be null");
        if (this.children.isEmpty()) {
            return this.children.add(picker);
        }
        if (picker.isRoot() && areChildrenRoots()) {
            return this.children.add(picker);
        }
        if (!picker.isRoot() && !areChildrenRoots()) {
            return this.children.add(picker);
        }
        throw new IllegalArgumentException("All child nodes should be of the same type (leaf or root)");
    }

    public List<Picker> getChildren() {
        return this.children;
    }

    public Picker getSelectedChild() {
        return this.selectedChild;
    }

    public boolean areChildrenRoots() {
        for (Picker child : this.children) {
            if (!child.isRoot()) {
                return false;
            }
        }
        return true;
    }

    public void setSelectedChild(Picker selectedChild) {
        if (selectedChild == null || !selectedChild.isRoot()) {
            if (this.selectedChild != null) {
                this.selectedChild.setSelectedChild(null);
            } else if (areChildrenRoots()) {
                for (Picker child : this.children) {
                    child.setSelectedChild(null);
                }
            }
            this.selectedChild = selectedChild;
            return;
        }
        throw new IllegalArgumentException("root picker cannot be set as selected child of given picker");
    }

    public String toString() {
        return "Picker{hint='" + this.hint + '\'' + ", id='" + this.id + '\'' + ", name='" + this.name + '\'' + ", children=" + this.children + ", selectedChild=" + this.selectedChild + '}';
    }

    public boolean equals(Object o) {
        boolean z = true;
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Picker picker = (Picker) o;
        if (this.hint != null) {
            if (!this.hint.equals(picker.hint)) {
                return false;
            }
        } else if (picker.hint != null) {
            return false;
        }
        if (this.id != null) {
            if (!this.id.equals(picker.id)) {
                return false;
            }
        } else if (picker.id != null) {
            return false;
        }
        if (this.name != null) {
            z = this.name.equals(picker.name);
        } else if (picker.name != null) {
            z = false;
        }
        return z;
    }

    public int hashCode() {
        int result;
        int hashCode;
        int i = 0;
        if (this.hint != null) {
            result = this.hint.hashCode();
        } else {
            result = 0;
        }
        int i2 = result * 31;
        if (this.id != null) {
            hashCode = this.id.hashCode();
        } else {
            hashCode = 0;
        }
        hashCode = (i2 + hashCode) * 31;
        if (this.name != null) {
            i = this.name.hashCode();
        }
        return hashCode + i;
    }
}
