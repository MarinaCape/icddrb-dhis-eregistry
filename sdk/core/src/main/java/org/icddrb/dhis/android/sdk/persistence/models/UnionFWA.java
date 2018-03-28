package org.icddrb.dhis.android.sdk.persistence.models;
/*
 * Copyright (c) 2016, University of Oslo
 *
 * All rights reserved.
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 * Neither the name of the HISP project nor the names of its contributors may
 * be used to endorse or promote products derived from this software without
 * specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * Norway
 */


import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UnionFWA implements Serializable {

    private List<UnionFWADropDownItem> mOrgUnits;

    private HashMap<String, List<UnionFWADropDownItem>> mUsers;

    public UnionFWA() {
        mOrgUnits = new ArrayList<>();
        mUsers = new HashMap<>();
    }

    public void init(List<OrganisationUnitUser> orgOptionSets) {
        //System.out.println("Norway - Org unit size: " + orgOptionSets.size());

        if (orgOptionSets.size() > 0) {
            HashMap<String, List<OrganisationUnitUser>> unionUsers = this.getUnionUsers(orgOptionSets);

           // System.out.println("Norway - users org size: " + unionUsers.size());

            for (OrganisationUnitUser org : orgOptionSets) {
                if (unionUsers.containsKey(org.getId())) {
                    if (!unionUsers.get(org.getId()).isEmpty()) {
                        for (OrganisationUnitUser o : unionUsers.get(org.getId())) {
                            List<User> userOptionSets = o.getUsers();
                            //System.out.println("Norway - Getting users for " + o.getLabel() + "(" + o.getId() + ") found " + userOptionSets.size() + " users");
                            if (userOptionSets.size() > 0) {
                                List<UnionFWADropDownItem> users = new ArrayList<>();
                                for (User user : userOptionSets) {
                                    UserCredentials uc = user.getUserCredential();
                                    if (uc != null && (uc.hasRole("FWA: Family Welfare Assistant") ||

                                            uc.hasRole("Field Worker Program Access (no authorities)") ||
                                            uc.hasRole("FWV Test"))) {
                                        //System.out.println("Norway - Adding user " + user.getDisplayName() + " for " + o.getLabel());
                                        users.add(new UnionFWADropDownItem(user.getUid(), user.getDisplayName(), uc.getUsername()));
                                    }
                                }
                                if (users.size() > 0) {
                                    this.addUsers(org.getId(), users);
                                }
                            }
                        }
                    }
                }
                if (org.getLevel() == 5) {
                    this.addOrg(org.getId(), org.getLabel());
                }
            }
        }
    }

    public void addOrg(String id, String name) {
        mOrgUnits.add(new UnionFWADropDownItem(id, name, null));
    }

    public void addUsers(String orgId, List<UnionFWADropDownItem> users) {
        mUsers.put(orgId, users);
    }

    public String getRootUnion(String orgId, List<OrganisationUnitUser> orgOptionSets) {
        HashMap<String, List<OrganisationUnitUser>> mOrgUsers = this.getUnionUsers(orgOptionSets);
        for (Map.Entry<String, List<OrganisationUnitUser>> entry : mOrgUsers.entrySet()) {
            for (OrganisationUnitUser ou : entry.getValue()) {
                //System.out.println("Norway - comparing " + orgId + " to " + ou.getId());
                if (ou.getId().equals(orgId)) {
                    return entry.getKey();
                }
            }
        }
        return null;
    }

    public List<UnionFWADropDownItem> getOrgUnits() { return this.mOrgUnits; }

    public List<UnionFWADropDownItem> getUsers(String orgId) {
        List<UnionFWADropDownItem> users = new ArrayList<>();
        for(Map.Entry<String, List<UnionFWADropDownItem>> entry : mUsers.entrySet()) {
            try {
                if (orgId != null && orgId.equals(entry.getKey())) {
                    for (UnionFWADropDownItem u : entry.getValue()) {
                       // System.out.println("Norway - Adding " + u.getLabel() + " for " + orgId);
                        users.add(u);
                    }
                }
            } catch (Exception e) {
                System.out.println("Norway - FWA load exception: " + e.getMessage());
                for (UnionFWADropDownItem u : entry.getValue()) {
                    users.add(u);
                }
            }
        }

        return users;
    }

    private HashMap<String, List<OrganisationUnitUser>> getUnionUsers(List<OrganisationUnitUser> orgOptionSets) {
        List<String> ids = new ArrayList<>();
        HashMap<String, String> levels = new HashMap<>();
        HashMap<String, List<OrganisationUnitUser>> idMap = new HashMap<>();

        if (orgOptionSets.size() > 0) {
            // First do Unions
            for (OrganisationUnitUser org : orgOptionSets) {
                if (org.getLevel() == 5) {
                    ids.add(org.getId());
                    List<OrganisationUnitUser> n = new ArrayList<>();
                    n.add(org);
                    idMap.put(org.getId(), n);
                }
            }

            // Do next level
            for (OrganisationUnitUser org : orgOptionSets) {
                if (org.getLevel() == 6 && inArray(ids,org.getParent()) ) {
                    idMap.get(org.getParent()).add(org);
                    levels.put(org.getId(), org.getParent());
                }
            }
            // Do last level
            for (OrganisationUnitUser org : orgOptionSets) {
                if (org.getLevel() == 7 && levels.containsKey(org.getParent())) {
                    idMap.get(levels.get(org.getParent())).add(org);
                }
            }
        }
        return idMap;
    }

    private boolean inArray(List<String> a, String i) {
        for (String s : a) {
            if (s.trim().equalsIgnoreCase(i.trim())) return true;
        }
        return false;
    }
}
