package org.icddrb.dhis.android.sdk.persistence.models;

import com.raizlabs.android.dbflow.sql.builder.Condition;
import com.raizlabs.android.dbflow.sql.language.Delete;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import org.icddrb.dhis.android.sdk.controllers.wrappers.OptionSetWrapper;
import org.icddrb.dhis.android.sdk.persistence.Dhis2Application;
import org.icddrb.dhis.android.sdk.persistence.models.ImportCount.Table;
import org.icddrb.dhis.android.sdk.persistence.models.OrganisationUnit.TYPE;
import org.icddrb.dhis.android.sdk.utils.DbUtils;
import org.icddrb.dhis.android.sdk.utils.api.ValueType;

public class UnionFWA implements Serializable {
    private List<UnionFWADropDownItem> mOrgUnits = new ArrayList();
    private HashMap<String, List<UnionFWADropDownItem>> mUsers = new HashMap();

    public void init(List<OrganisationUnitUser> orgOptionSets) {
        System.out.println("Norway - Org unit size: " + orgOptionSets.size());
        String defaultOrg = "";
        if (orgOptionSets.size() > 0) {
            HashMap<String, List<OrganisationUnitUser>> unionUsers = getUnionUsers(orgOptionSets);
            System.out.println("Norway - users org size: " + unionUsers.size());
            for (OrganisationUnitUser org : orgOptionSets) {
                if (unionUsers.containsKey(org.getId()) && !((List) unionUsers.get(org.getId())).isEmpty()) {
                    for (OrganisationUnitUser o : (List) unionUsers.get(org.getId())) {
                        List<User> userOptionSets = o.getUsers();
                        if (userOptionSets.size() > 0) {
                            List<UnionFWADropDownItem> users = new ArrayList();
                            for (User user : userOptionSets) {
                                UserCredentials uc = user.getUserCredential();
                                if (uc != null && (uc.hasRole("FWA: Family Welfare Assistant") || uc.hasRole("Field Worker Program Access (no authorities)") || uc.hasRole("FWV Test"))) {
                                    if ("".equals(defaultOrg)) {
                                        defaultOrg = org.getId();
                                    }
                                    users.add(new UnionFWADropDownItem(user.getUid(), user.getDisplayName(), uc.getUsername()));
                                }
                            }
                            if (users.size() > 0) {
                                addUsers(org.getId(), users);
                            }
                        }
                    }
                }
                if (org.getLevel() == 5) {
                    addOrg(org.getId(), org.getLabel());
                }
            }
            simulateLoad(defaultOrg);
        }
    }

    public void addOrg(String id, String name) {
        this.mOrgUnits.add(new UnionFWADropDownItem(id, name, null));
    }

    public void addUsers(String orgId, List<UnionFWADropDownItem> users) {
        this.mUsers.put(orgId, users);
    }

    public String getFullName(String userId) {
        if (userId == null || userId.isEmpty()) {
            return null;
        }
        for (Entry<String, List<UnionFWADropDownItem>> users : this.mUsers.entrySet()) {
            for (UnionFWADropDownItem user : (List) users.getValue()) {
                if (user.getAlternateId().equals(userId)) {
                    return user.getLabel();
                }
            }
        }
        return null;
    }

    public String getRootUnion(String orgId, List<OrganisationUnitUser> orgOptionSets) {
        for (Entry<String, List<OrganisationUnitUser>> entry : getUnionUsers(orgOptionSets).entrySet()) {
            for (OrganisationUnitUser ou : (List) entry.getValue()) {
                if (ou.getId().equals(orgId)) {
                    return (String) entry.getKey();
                }
            }
        }
        return null;
    }

    public List<UnionFWADropDownItem> getOrgUnits() {
        return this.mOrgUnits;
    }

    public List<UnionFWADropDownItem> getUsers(String orgId) {
        List<UnionFWADropDownItem> users = new ArrayList();
        for (Entry<String, List<UnionFWADropDownItem>> entry : this.mUsers.entrySet()) {
            if (orgId != null) {
                try {
                    if (orgId.equals(entry.getKey())) {
                        for (UnionFWADropDownItem u : (List) entry.getValue()) {
                            users.add(u);
                        }
                    }
                } catch (Exception e) {
                    System.out.println("Norway - FWA load exception: " + e.getMessage());
                    for (UnionFWADropDownItem u2 : (List) entry.getValue()) {
                        users.add(u2);
                    }
                }
            }
        }
        return users;
    }

    public String size() {
        return "Organisations: " + this.mOrgUnits.size() + " Users: " + this.mUsers.size();
    }

    public OptionSet getOrganizationOptionSet() {
        OptionSet os = new OptionSet();
        String osString = null;
        int i = 0;
        List<Option> opList = new ArrayList();
        List<UnionFWADropDownItem> optionSets = getOrgUnits();
        if (optionSets == null || optionSets.size() <= 0) {
            System.out.println("Norway - no new organisations found");
        } else {
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
            }
        }
        os.setVersion(2);
        os.setOptions(opList);
        os.setValueType(ValueType.ORGANISATION_UNIT);
        os.setUid(osString);
        List<OptionSet> osList = new ArrayList();
        osList.add(os);
        DbUtils.applyBatch(OptionSetWrapper.getOperations(osList));
        return os;
    }

    public OptionSet getAllUsersOptionSet(String chosenOrg, String guid) {
        List<UnionFWADropDownItem> optionSets = getUsers(chosenOrg);
        OptionSet os = new OptionSet();
        int i = 0;
        if (optionSets == null || optionSets.size() <= 0) {
            System.out.println("Norway - no new users found for " + chosenOrg);
        } else {
            List<Option> options = new ArrayList();
            String osString = "";
            for (UnionFWADropDownItem user : optionSets) {
                Option o = new Option();
                o.setSortIndex(i);
                o.setOptionSet(user.getId());
                o.setName(user.getLabel());
                o.setCode(user.getAlternateId());
                o.setUid(o.getOptionSet() + user.getLabel().toLowerCase().replace(" ", "-").replace(",", ""));
                options.add(o);
                osString = osString + o.getOptionSet();
                i++;
            }
            os.setVersion(2);
            if (guid != null) {
                osString = guid;
            }
            os.setUid(osString);
            os.setOptions(options);
            os.setValueType(ValueType.USERNAME);
            List<OptionSet> osList = new ArrayList();
            osList.add(os);
            DbUtils.applyBatch(OptionSetWrapper.getOperations(osList));
        }
        return os;
    }

    public void updateFWADropdown(String orgId, String userId) {
        printOptionStatus("before", orgId, userId);
        new Delete().from(Option.class).where(Condition.column("optionSet").is(userId)).query();
        printOptionStatus(Table.DELETED, orgId, userId);
        getAllUsersOptionSet(orgId, userId);
        printOptionStatus("now", orgId, userId);
    }

    private void simulateLoad(String defaultOrg) {
        Dhis2Application.dhisController.getAppPreferences().putChosenOrg(defaultOrg);
        Dhis2Application.dhisController.getAppPreferences().putUserOptionId(getAllUsersOptionSet(defaultOrg, null).getUid());
    }

    private HashMap<String, List<OrganisationUnitUser>> getUnionUsers(List<OrganisationUnitUser> orgOptionSets) {
        List<String> ids = new ArrayList();
        HashMap<String, String> levels = new HashMap();
        HashMap<String, List<OrganisationUnitUser>> idMap = new HashMap();
        if (orgOptionSets.size() > 0) {
            for (OrganisationUnitUser org : orgOptionSets) {
                OrganisationUnit o = new OrganisationUnit();
                o.setId(org.getId());
                o.setLevel(org.getLevel());
                o.setLabel(org.getLabel());
                o.setParent(org.getParent());
                o.setType(TYPE.ASSIGNED);
                o.save();
            }
            for (OrganisationUnitUser org2 : orgOptionSets) {
                if (org2.getLevel() == 5) {
                    ids.add(org2.getId());
                    List<OrganisationUnitUser> n = new ArrayList();
                    n.add(org2);
                    idMap.put(org2.getId(), n);
                }
            }
            for (OrganisationUnitUser org22 : orgOptionSets) {
                if (org22.getLevel() == 6 && inArray(ids, org22.getParent())) {
                    ((List) idMap.get(org22.getParent())).add(org22);
                    levels.put(org22.getId(), org22.getParent());
                }
            }
            for (OrganisationUnitUser org222 : orgOptionSets) {
                if (org222.getLevel() == 7 && levels.containsKey(org222.getParent())) {
                    ((List) idMap.get(levels.get(org222.getParent()))).add(org222);
                }
            }
        }
        return idMap;
    }

    private static void printOptionStatus(String state, String org, String uid) {
    }

    private boolean inArray(List<String> a, String i) {
        for (String s : a) {
            if (s.trim().equalsIgnoreCase(i.trim())) {
                return true;
            }
        }
        return false;
    }
}
