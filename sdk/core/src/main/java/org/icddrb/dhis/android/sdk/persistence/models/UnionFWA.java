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



import com.raizlabs.android.dbflow.sql.builder.Condition;
import com.raizlabs.android.dbflow.sql.language.Delete;
import com.raizlabs.android.dbflow.sql.language.Select;

import org.icddrb.dhis.android.sdk.controllers.wrappers.OptionSetWrapper;
import org.icddrb.dhis.android.sdk.persistence.Dhis2Application;
import org.icddrb.dhis.android.sdk.persistence.models.meta.DbOperation;
import org.icddrb.dhis.android.sdk.utils.DbUtils;
import org.icddrb.dhis.android.sdk.utils.api.ValueType;

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
        System.out.println("Norway - Org unit size: " + orgOptionSets.size());
        String defaultOrg = "";

        if (orgOptionSets.size() > 0) {
            HashMap<String, List<OrganisationUnitUser>> unionUsers = this.getUnionUsers(orgOptionSets);

            System.out.println("Norway - users org size: " + unionUsers.size());

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
                                        if ("".equals(defaultOrg)) { defaultOrg = org.getId(); }
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

            simulateLoad(defaultOrg);
        }
    }

    public void addOrg(String id, String name) {
        mOrgUnits.add(new UnionFWADropDownItem(id, name, null));
    }

    public void addUsers(String orgId, List<UnionFWADropDownItem> users) {
        mUsers.put(orgId, users);
    }

    public String getFullName(String userId) {
        if (userId == null || userId.isEmpty()) {
            return null;
        }

        for (Map.Entry<String, List<UnionFWADropDownItem>> users : mUsers.entrySet()) {
            for (UnionFWADropDownItem user : users.getValue()) {
                if (user.getAlternateId().equals(userId)) {
                   return user.getLabel();
                }
            }
        }
        return null;
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

    public String size() {
        return "Organisations: " + mOrgUnits.size() + " Users: " + mUsers.size();
    }

    public OptionSet getOrganizationOptionSet() {
        OptionSet os = new OptionSet();
        String osString = null;

        int i = 0;
        List<Option> opList = new ArrayList<>();
        List<UnionFWADropDownItem> optionSets = getOrgUnits();
        if (optionSets!=null && optionSets.size() > 0) {
            for (UnionFWADropDownItem ou : optionSets) {
                Option o = new Option();
                o.setSortIndex(i);
                o.setOptionSet(ou.getId());
                o.setName(ou.getLabel());
                o.setCode(ou.getId());
                o.setUid(o.getOptionSet() + ou.getLabel().toLowerCase().replace(" ", "-").replace(",", ""));
                opList.add(o);
                osString = o.getOptionSet();
                i++;
                //System.out.println("Norway - Organization to dropdown " + ou.getLabel() + ": " + ou.getId());
            }
        } else {
            System.out.println("Norway - no new organisations found");
        }

        os.setVersion(2);
        os.setOptions(opList);
        os.setValueType(ValueType.ORGANISATION_UNIT);
        os.setUid(osString);

        List<OptionSet> osList = new ArrayList<>();
        osList.add(os);
        List<DbOperation> operations = OptionSetWrapper.getOperations(osList);
        DbUtils.applyBatch(operations);

        return os;
    }

    public OptionSet getAllUsersOptionSet(String chosenOrg, String guid) {
        List<UnionFWADropDownItem> optionSets = getUsers(chosenOrg);

        OptionSet os = new OptionSet();
        int i = 0;
        if (optionSets != null && optionSets.size() > 0) {
            List<Option> options = new ArrayList<>();
            String osString = "";
            for (UnionFWADropDownItem user : optionSets) {
                Option o = new Option();
                o.setSortIndex(i);
                o.setOptionSet(user.getId());
                o.setName(user.getLabel());
                o.setCode(user.getAlternateId());
                o.setUid(o.getOptionSet() + user.getLabel().toLowerCase().replace(" ", "-").replace(",", ""));
                options.add(o);
                osString += o.getOptionSet();
                // System.out.println("Norway - Adding User to dropdown " + user.getLabel() + ": " + user.getId() + " username: "+user.getAlternateId());
                i++;
            }

            os.setVersion(2);
            os.setUid((guid==null) ? osString : guid);
            os.setOptions(options);
            os.setValueType(ValueType.USERNAME);

            List<OptionSet> osList = new ArrayList<>(); osList.add(os);
            List<DbOperation> operations = OptionSetWrapper.getOperations(osList);
            DbUtils.applyBatch(operations);

        } else {
            System.out.println("Norway - no new users found for "+chosenOrg);
        }

        return os;
    }

    public void updateFWADropdown(String orgId, String userId) {
        // Norway: update FWA name based on chosen union
        printOptionStatus("before",orgId, userId);

        // Delete that list
        new Delete()
                .from(Option.class)
                .where((Condition.column(Option$Table.OPTIONSET).is(userId))).query();
        printOptionStatus("deleted", orgId, userId);

        // create and save new list with existing guid
        getAllUsersOptionSet(orgId, userId);
        printOptionStatus("now", orgId, userId);
    }


    private void simulateLoad(String defaultOrg) {
        // Simulate union selection
        Dhis2Application.dhisController.getAppPreferences().putChosenOrg(defaultOrg);
        OptionSet os = getAllUsersOptionSet(defaultOrg, null);
        Dhis2Application.dhisController.getAppPreferences().putUserOptionId(os.getUid());
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

    private static void printOptionStatus(String state, String org, String uid) {
        /*List<Option> options = new Select()
                .from(Option.class)
                .where((Condition.column(Option$Table.OPTIONSET).is(uid)))
                .queryList();

        System.out.println("Norway - row change ("+state+"): " + org
                + " uid: " + uid
                + " size: "+ options.size());
         */
    }

    private boolean inArray(List<String> a, String i) {
        for (String s : a) {
            if (s.trim().equalsIgnoreCase(i.trim())) return true;
        }
        return false;
    }
}
