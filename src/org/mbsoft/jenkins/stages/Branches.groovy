package org.mbsoft.jenkins.stages

/**
 * Created by marc on 2/03/17.
 */
enum Branches {

    DEVELOP('develop'),
    FEATURE('^feature\\/.*$'),
    RELEASE('^release\\/.*$'),
    HOTFIX('^hotfix\\/.*$'),
    MASTER('master'),
    TAGS('tags')

    private branch;

    private Branches(branch) {
        this.branch = branch
    }

    String branch() {
        return branch;
    }
}
