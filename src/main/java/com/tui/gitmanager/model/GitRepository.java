package com.tui.gitmanager.model;

import java.util.Map;

public class GitRepository {
    private String name;
    private Map<String, String> branches;

    public GitRepository(String name, Map<String, String> branches) {
        this.name = name;
        this.branches = branches;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, String> getBranches() {
        return branches;
    }

    public void setBranches(Map<String, String> branches) {
        this.branches = branches;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof GitRepository))
            return false;
        GitRepository other = (GitRepository)o;

        return this.name.equals(other.name) && this.branches.equals(other.branches);
    }
}
