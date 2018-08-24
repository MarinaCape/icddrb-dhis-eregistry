package org.icddrb.dhis.android.sdk.controllers.metadata;

import android.content.Context;
import android.util.Log;
import com.raizlabs.android.dbflow.sql.builder.Condition;
import com.raizlabs.android.dbflow.sql.language.Delete;
import com.raizlabs.android.dbflow.sql.language.Select;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.icddrb.dhis.android.sdk.C0845R;
import org.icddrb.dhis.android.sdk.controllers.ApiEndpointContainer;
import org.icddrb.dhis.android.sdk.controllers.DhisController;
import org.icddrb.dhis.android.sdk.controllers.LoadingController;
import org.icddrb.dhis.android.sdk.controllers.ResourceController;
import org.icddrb.dhis.android.sdk.controllers.SyncStrategy;
import org.icddrb.dhis.android.sdk.controllers.wrappers.AssignedProgramsWrapper;
import org.icddrb.dhis.android.sdk.controllers.wrappers.OptionSetWrapper;
import org.icddrb.dhis.android.sdk.controllers.wrappers.ProgramWrapper;
import org.icddrb.dhis.android.sdk.events.LoadingMessageEvent.EventType;
import org.icddrb.dhis.android.sdk.network.APIException;
import org.icddrb.dhis.android.sdk.network.DhisApi;
import org.icddrb.dhis.android.sdk.persistence.models.Attribute;
import org.icddrb.dhis.android.sdk.persistence.models.AttributeValue;
import org.icddrb.dhis.android.sdk.persistence.models.Conflict;
import org.icddrb.dhis.android.sdk.persistence.models.Constant;
import org.icddrb.dhis.android.sdk.persistence.models.Dashboard;
import org.icddrb.dhis.android.sdk.persistence.models.DashboardElement;
import org.icddrb.dhis.android.sdk.persistence.models.DashboardItem;
import org.icddrb.dhis.android.sdk.persistence.models.DashboardItemContent;
import org.icddrb.dhis.android.sdk.persistence.models.DataElement;
import org.icddrb.dhis.android.sdk.persistence.models.DataValue;
import org.icddrb.dhis.android.sdk.persistence.models.Enrollment;
import org.icddrb.dhis.android.sdk.persistence.models.Event;
import org.icddrb.dhis.android.sdk.persistence.models.FailedItem;
import org.icddrb.dhis.android.sdk.persistence.models.ImportCount;
import org.icddrb.dhis.android.sdk.persistence.models.ImportSummary;
import org.icddrb.dhis.android.sdk.persistence.models.Interpretation;
import org.icddrb.dhis.android.sdk.persistence.models.InterpretationComment;
import org.icddrb.dhis.android.sdk.persistence.models.InterpretationElement;
import org.icddrb.dhis.android.sdk.persistence.models.Option;
import org.icddrb.dhis.android.sdk.persistence.models.Option.Table;
import org.icddrb.dhis.android.sdk.persistence.models.OptionSet;
import org.icddrb.dhis.android.sdk.persistence.models.OrganisationUnit;
import org.icddrb.dhis.android.sdk.persistence.models.OrganisationUnit.TYPE;
import org.icddrb.dhis.android.sdk.persistence.models.OrganisationUnitProgramRelationship;
import org.icddrb.dhis.android.sdk.persistence.models.Program;
import org.icddrb.dhis.android.sdk.persistence.models.ProgramIndicator;
import org.icddrb.dhis.android.sdk.persistence.models.ProgramIndicatorToSectionRelationship;
import org.icddrb.dhis.android.sdk.persistence.models.ProgramRule;
import org.icddrb.dhis.android.sdk.persistence.models.ProgramRuleAction;
import org.icddrb.dhis.android.sdk.persistence.models.ProgramRuleVariable;
import org.icddrb.dhis.android.sdk.persistence.models.ProgramStage;
import org.icddrb.dhis.android.sdk.persistence.models.ProgramStageDataElement;
import org.icddrb.dhis.android.sdk.persistence.models.ProgramStageSection;
import org.icddrb.dhis.android.sdk.persistence.models.ProgramTrackedEntityAttribute;
import org.icddrb.dhis.android.sdk.persistence.models.Relationship;
import org.icddrb.dhis.android.sdk.persistence.models.RelationshipType;
import org.icddrb.dhis.android.sdk.persistence.models.SystemInfo;
import org.icddrb.dhis.android.sdk.persistence.models.TrackedEntityAttribute;
import org.icddrb.dhis.android.sdk.persistence.models.TrackedEntityAttributeGeneratedValue;
import org.icddrb.dhis.android.sdk.persistence.models.TrackedEntityAttributeGroup;
import org.icddrb.dhis.android.sdk.persistence.models.TrackedEntityAttributeValue;
import org.icddrb.dhis.android.sdk.persistence.models.TrackedEntityInstance;
import org.icddrb.dhis.android.sdk.persistence.models.TrackedEntityType;
import org.icddrb.dhis.android.sdk.persistence.models.User;
import org.icddrb.dhis.android.sdk.persistence.models.UserAccount;
import org.icddrb.dhis.android.sdk.persistence.models.UserGroup;
import org.icddrb.dhis.android.sdk.persistence.models.UserGroupAccess;
import org.icddrb.dhis.android.sdk.persistence.models.meta.DbOperation;
import org.icddrb.dhis.android.sdk.persistence.preferences.AppPreferences;
import org.icddrb.dhis.android.sdk.persistence.preferences.DateTimeManager;
import org.icddrb.dhis.android.sdk.persistence.preferences.ResourceType;
import org.icddrb.dhis.android.sdk.utils.DbUtils;
import org.icddrb.dhis.android.sdk.utils.NetworkUtils;
import org.icddrb.dhis.android.sdk.utils.UiUtils;
import org.icddrb.dhis.android.sdk.utils.Utils;
import org.icddrb.dhis.android.sdk.utils.api.ProgramType;
import org.icddrb.dhis.client.sdk.ui.AppPreferencesImpl;
import org.joda.time.DateTime;

public final class MetaDataController extends ResourceController {
    private static final String CLASS_TAG = "MetaDataController";
    private static final long TRACKED_ENTITY_ATTRITBUTE_GENERATED_VALUE_THRESHOLD = 100;

    private MetaDataController() {
    }

    public static boolean isDataLoaded(Context context) {
        if (LoadingController.isLoadFlagEnabled(context, ResourceType.ASSIGNEDPROGRAMS) && DateTimeManager.getInstance().getLastUpdated(ResourceType.ASSIGNEDPROGRAMS) == null) {
            return false;
        }
        if (LoadingController.isLoadFlagEnabled(context, ResourceType.OPTIONSETS) && DateTimeManager.getInstance().getLastUpdated(ResourceType.OPTIONSETS) == null) {
            return false;
        }
        if (LoadingController.isLoadFlagEnabled(context, ResourceType.TRACKEDENTITYATTRIBUTEGROUPS) && DateTimeManager.getInstance().getLastUpdated(ResourceType.TRACKEDENTITYATTRIBUTEGROUPS) == null) {
            return false;
        }
        if (LoadingController.isLoadFlagEnabled(context, ResourceType.TRACKEDENTITYATTRIBUTES) && DateTimeManager.getInstance().getLastUpdated(ResourceType.TRACKEDENTITYATTRIBUTES) == null) {
            return false;
        }
        if (LoadingController.isLoadFlagEnabled(context, ResourceType.CONSTANTS) && DateTimeManager.getInstance().getLastUpdated(ResourceType.CONSTANTS) == null) {
            return false;
        }
        if (LoadingController.isLoadFlagEnabled(context, ResourceType.PROGRAMRULES) && DateTimeManager.getInstance().getLastUpdated(ResourceType.PROGRAMRULES) == null) {
            return false;
        }
        if (LoadingController.isLoadFlagEnabled(context, ResourceType.PROGRAMRULEVARIABLES) && DateTimeManager.getInstance().getLastUpdated(ResourceType.PROGRAMRULEVARIABLES) == null) {
            return false;
        }
        if (LoadingController.isLoadFlagEnabled(context, ResourceType.PROGRAMRULEACTIONS) && DateTimeManager.getInstance().getLastUpdated(ResourceType.PROGRAMRULEACTIONS) == null) {
            return false;
        }
        if (LoadingController.isLoadFlagEnabled(context, ResourceType.RELATIONSHIPTYPES) && DateTimeManager.getInstance().getLastUpdated(ResourceType.RELATIONSHIPTYPES) == null) {
            return false;
        }
        Log.d(CLASS_TAG, "Meta data is loaded!");
        return true;
    }

    public static List<RelationshipType> getRelationshipTypes() {
        return new Select().from(RelationshipType.class).queryList();
    }

    public static RelationshipType getRelationshipType(String relation) {
        return (RelationshipType) new Select().from(RelationshipType.class).where(Condition.column("id").is(relation)).querySingle();
    }

    public static List<Option> getOptions(String optionSetId) {
        return new Select().from(Option.class).where(Condition.column("optionSet").is(optionSetId)).orderBy(Table.SORTINDEX).queryList();
    }

    public static List<ProgramStageSection> getProgramStageSections(String programStageId) {
        return new Select().from(ProgramStageSection.class).where(Condition.column("programStage").is(programStageId)).orderBy(true, "sortOrder").queryList();
    }

    public static List<ProgramStageDataElement> getProgramStageDataElements(ProgramStageSection section) {
        if (section == null) {
            return null;
        }
        return new Select().from(ProgramStageDataElement.class).where(Condition.column("programStageSection").is(section.getUid())).orderBy("sortOrder").queryList();
    }

    public static List<ProgramStageDataElement> getProgramStageDataElements(ProgramStage programStage) {
        if (programStage == null) {
            return null;
        }
        return new Select().from(ProgramStageDataElement.class).where(Condition.column("programStage").is(programStage.getUid())).orderBy("sortOrder").queryList();
    }

    public static List<UserGroupAccess> getUserGroupAccess(String id) {
        if (id == null) {
            return null;
        }
        return new Select().from(UserGroupAccess.class).where(Condition.column("uniqid").is(id)).queryList();
    }

    public static List<UserGroup> getUserGroups(String id) {
        if (id == null) {
            return null;
        }
        return new Select().from(UserGroup.class).where(Condition.column("uniqid").is(id)).queryList();
    }

    public static List<Attribute> getAttributes() {
        return new Select().from(Attribute.class).orderBy("id").queryList();
    }

    public static List<AttributeValue> getAttributeValues() {
        return new Select().from(AttributeValue.class).orderBy("id").queryList();
    }

    public static List<AttributeValue> getAttributeValues(DataElement dataElement) {
        if (dataElement == null) {
            return null;
        }
        return new Select().from(AttributeValue.class).where(Condition.column("dataElement").is(dataElement.getUid())).orderBy("id").queryList();
    }

    public static AttributeValue getAttributeValue(Long id) {
        if (id == null) {
            return null;
        }
        return (AttributeValue) new Select().from(AttributeValue.class).where(Condition.column("id").is(id)).querySingle();
    }

    public static Attribute getAttribute(String attributeId) {
        if (attributeId == null) {
            return null;
        }
        return (Attribute) new Select().from(Attribute.class).where(Condition.column("id").is(attributeId)).querySingle();
    }

    public static TrackedEntityType getTrackedEntityType(String trackedEntity) {
        return (TrackedEntityType) new Select().from(TrackedEntityType.class).where(Condition.column("id").is(trackedEntity)).querySingle();
    }

    public static List<ProgramTrackedEntityAttribute> getProgramTrackedEntityAttributes(String program) {
        return new Select().from(ProgramTrackedEntityAttribute.class).where(Condition.column("program").is(program)).orderBy(true, "sortOrder").queryList();
    }

    public static List<Program> getProgramsForOrganisationUnit(String organisationUnitId, ProgramType... kinds) {
        List<OrganisationUnitProgramRelationship> organisationUnitProgramRelationships = new Select().from(OrganisationUnitProgramRelationship.class).where(Condition.column("organisationUnitId").is(organisationUnitId)).queryList();
        List<Program> programs = new ArrayList();
        for (OrganisationUnitProgramRelationship oupr : organisationUnitProgramRelationships) {
            if (kinds != null) {
                for (ProgramType kind : kinds) {
                    programs.addAll(new Select().from(Program.class).where(Condition.column("id").is(oupr.getProgramId())).and(Condition.column(Program.Table.PROGRAMTYPE).is(kind.toString())).queryList());
                }
            }
        }
        return programs;
    }

    public static List<ProgramStage> getProgramStages(String program) {
        return new Select().from(ProgramStage.class).where(Condition.column("program").is(program)).orderBy("sortOrder").queryList();
    }

    public static List<ProgramIndicator> getProgramIndicators(String program) {
        return new Select().from(ProgramIndicator.class).where(Condition.column("program").is(program)).orderBy("id").queryList();
    }

    public static ProgramStage getProgramStage(String programStageUid) {
        return (ProgramStage) new Select().from(ProgramStage.class).where(Condition.column("id").is(programStageUid)).querySingle();
    }

    public static TrackedEntityAttribute getTrackedEntityAttribute(String trackedEntityAttributeId) {
        return (TrackedEntityAttribute) new Select().from(TrackedEntityAttribute.class).where(Condition.column("id").is(trackedEntityAttributeId)).querySingle();
    }

    public static List<TrackedEntityAttribute> getTrackedEntityAttributes() {
        return new Select().from(TrackedEntityAttribute.class).queryList();
    }

    public static List<TrackedEntityInstance> getTrackedEntityInstancesFromServer() {
        return new Select().from(TrackedEntityInstance.class).where(Condition.column("fromServer").is(Boolean.valueOf(true))).queryList();
    }

    public static List<TrackedEntityAttributeGroup> getTrackedEntityAttributeGroups() {
        return new Select().from(TrackedEntityAttributeGroup.class).queryList();
    }

    public static List<TrackedEntityAttributeGeneratedValue> getTrackedEntityAttributeGeneratedValues() {
        return new Select().from(TrackedEntityAttributeGeneratedValue.class).queryList();
    }

    public static Constant getConstant(String id) {
        return (Constant) new Select().from(Constant.class).where(Condition.column("id").is(id)).querySingle();
    }

    public static List<Constant> getConstants() {
        return new Select().from(Constant.class).queryList();
    }

    public static List<ProgramRule> getProgramRules() {
        return new Select().from(ProgramRule.class).queryList();
    }

    public static List<ProgramRuleVariable> getProgramRuleVariables() {
        return new Select().from(ProgramRuleVariable.class).queryList();
    }

    public static List<ProgramRuleAction> getProgramRuleActions() {
        return new Select().from(ProgramRuleAction.class).queryList();
    }

    public static ProgramRuleVariable getProgramRuleVariable(String id) {
        return (ProgramRuleVariable) new Select().from(ProgramRuleVariable.class).where(Condition.column("id").is(id)).querySingle();
    }

    public static ProgramRuleVariable getProgramRuleVariableByName(String name) {
        return (ProgramRuleVariable) new Select().from(ProgramRuleVariable.class).where(Condition.column("name").is(name)).querySingle();
    }

    public static List<String> getAssignedPrograms() {
        List<OrganisationUnitProgramRelationship> organisationUnitProgramRelationships = new Select().from(OrganisationUnitProgramRelationship.class).queryList();
        List<String> assignedPrograms = new ArrayList();
        for (OrganisationUnitProgramRelationship relationship : organisationUnitProgramRelationships) {
            if (!assignedPrograms.contains(relationship.getProgramId())) {
                assignedPrograms.add(relationship.getProgramId());
            }
        }
        return assignedPrograms;
    }

    public static OrganisationUnit getOrganisationUnit(String id) {
        return (OrganisationUnit) new Select().from(OrganisationUnit.class).where(Condition.column("id").is(id)).querySingle();
    }

    public static SystemInfo getSystemInfo() {
        return (SystemInfo) new Select().from(SystemInfo.class).querySingle();
    }

    public static Program getProgram(String programId) {
        if (programId == null) {
            return null;
        }
        return (Program) new Select().from(Program.class).where(Condition.column("id").is(programId)).querySingle();
    }

    public static List<OrganisationUnit> getAssignedOrganisationUnits() {
        return new Select().from(OrganisationUnit.class).where(Condition.column("type").eq(TYPE.ASSIGNED)).queryList();
    }

    public static List<OrganisationUnitProgramRelationship> getOrganisationUnitProgramRelationships() {
        return new Select().from(OrganisationUnitProgramRelationship.class).queryList();
    }

    public static List<DataElement> getDataElements() {
        return new Select().from(DataElement.class).queryList();
    }

    public static DataElement getDataElement(String dataElementId) {
        return (DataElement) new Select().from(DataElement.class).where(Condition.column("id").is(dataElementId)).querySingle();
    }

    public static User getUser() {
        return (User) new Select().from(User.class).querySingle();
    }

    public static UserAccount getUserAccount() {
        return (UserAccount) new Select().from(UserAccount.class).querySingle();
    }

    public static OptionSet getOptionSet(String optionSetId) {
        return (OptionSet) new Select().from(OptionSet.class).where(Condition.column("id").is(optionSetId)).querySingle();
    }

    public static List<OptionSet> getOptionSets() {
        return new Select().from(OptionSet.class).queryList();
    }

    public static List<ProgramIndicator> getProgramIndicatorsByProgram(String program) {
        return new Select().from(ProgramIndicator.class).where(Condition.column("program").is(program)).queryList();
    }

    public static List<ProgramIndicator> getProgramIndicatorsByProgramStage(String programStage) {
        List<ProgramIndicatorToSectionRelationship> relations = new Select().from(ProgramIndicatorToSectionRelationship.class).where(Condition.column(ProgramIndicatorToSectionRelationship.Table.PROGRAMSECTION).is(programStage)).queryList();
        List<ProgramIndicator> indicators = new ArrayList();
        if (!(relations == null || relations.isEmpty())) {
            for (ProgramIndicatorToSectionRelationship relation : relations) {
                indicators.add(relation.getProgramIndicator());
            }
        }
        return indicators;
    }

    public static List<ProgramIndicator> getProgramIndicatorsBySection(String section) {
        List<ProgramIndicatorToSectionRelationship> relations = new Select().from(ProgramIndicatorToSectionRelationship.class).where(Condition.column(ProgramIndicatorToSectionRelationship.Table.PROGRAMSECTION).is(section)).queryList();
        List<ProgramIndicator> indicators = new ArrayList();
        if (!(relations == null || relations.isEmpty())) {
            for (ProgramIndicatorToSectionRelationship relation : relations) {
                indicators.add(relation.getProgramIndicator());
            }
        }
        return indicators;
    }

    public static void clearMetaDataLoadedFlags() {
        DateTimeManager.getInstance().deleteLastUpdated(ResourceType.ASSIGNEDPROGRAMS);
        for (String program : getAssignedPrograms()) {
            DateTimeManager.getInstance().deleteLastUpdated(ResourceType.PROGRAM, program);
        }
        DateTimeManager.getInstance().deleteLastUpdated(ResourceType.OPTIONSETS);
        DateTimeManager.getInstance().deleteLastUpdated(ResourceType.TRACKEDENTITYATTRIBUTES);
        DateTimeManager.getInstance().deleteLastUpdated(ResourceType.TRACKEDENTITYATTRIBUTEGROUPS);
        DateTimeManager.getInstance().deleteLastUpdated(ResourceType.CONSTANTS);
        DateTimeManager.getInstance().deleteLastUpdated(ResourceType.PROGRAMRULES);
        DateTimeManager.getInstance().deleteLastUpdated(ResourceType.PROGRAMRULEVARIABLES);
        DateTimeManager.getInstance().deleteLastUpdated(ResourceType.PROGRAMRULEACTIONS);
        DateTimeManager.getInstance().deleteLastUpdated(ResourceType.RELATIONSHIPTYPES);
    }

    public static void wipe() {
        try {
            Delete.tables(Attribute.class, AttributeValue.class, Conflict.class, Constant.class, Dashboard.class, DashboardElement.class, DashboardItem.class, DashboardItemContent.class, DataElement.class, DataValue.class, Enrollment.class, Event.class, FailedItem.class, ImportCount.class, ImportSummary.class, Interpretation.class, InterpretationComment.class, InterpretationElement.class, Option.class, OptionSet.class, OrganisationUnit.class, OrganisationUnitProgramRelationship.class, Program.class, ProgramIndicator.class, ProgramIndicatorToSectionRelationship.class, ProgramRule.class, ProgramRuleAction.class, ProgramRuleVariable.class, ProgramStage.class, ProgramStageDataElement.class, ProgramStageSection.class, ProgramTrackedEntityAttribute.class, Relationship.class, RelationshipType.class, SystemInfo.class, TrackedEntityAttributeGeneratedValue.class, TrackedEntityAttributeValue.class, TrackedEntityAttribute.class, TrackedEntityInstance.class, TrackedEntityType.class, User.class, UserGroupAccess.class, UserGroup.class, UserAccount.class);
        } catch (Exception e) {
            System.out.println("Norway " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void loadMetaData(Context context, DhisApi dhisApi, SyncStrategy syncStrategy) throws APIException {
        Log.d(CLASS_TAG, "loadMetaData");
        UiUtils.postProgressMessage(context.getString(C0845R.string.loading_metadata), EventType.METADATA);
        updateMetaDataItems(context, dhisApi, syncStrategy);
    }

    private static void updateTrackedDataItems(Context context, DhisApi dhisApi, DateTime serverDateTime) {
        if (dhisApi == null && DhisController.getInstance().getDhisApi() != null) {
        }
    }

    private static void updateMetaDataItems(Context context, DhisApi dhisApi, SyncStrategy syncStrategy) throws APIException {
        if (dhisApi == null) {
            dhisApi = DhisController.getInstance().getDhisApi();
            if (dhisApi == null) {
                return;
            }
        }
        SystemInfo serverSystemInfo = dhisApi.getSystemInfo();
        serverSystemInfo.save();
        new AppPreferencesImpl(context).setApiVersion(serverSystemInfo.getVersion());
        DateTime serverDateTime = serverSystemInfo.getServerDate();
        if (LoadingController.isLoadFlagEnabled(context, ResourceType.ASSIGNEDPROGRAMS) && ResourceController.shouldLoad(serverDateTime, ResourceType.ASSIGNEDPROGRAMS)) {
            getAssignedProgramsDataFromServer(dhisApi, serverDateTime);
        }
        if (LoadingController.isLoadFlagEnabled(context, ResourceType.PROGRAMS)) {
            List<String> assignedPrograms = getAssignedPrograms();
            if (assignedPrograms != null) {
                for (String program : assignedPrograms) {
                    if (ResourceController.shouldLoad(serverDateTime, ResourceType.PROGRAMS, program)) {
                        getProgramDataFromServer(dhisApi, program, serverDateTime, syncStrategy);
                    }
                }
            }
        }
        if (LoadingController.isLoadFlagEnabled(context, ResourceType.OPTIONSETS) && ResourceController.shouldLoad(serverDateTime, ResourceType.OPTIONSETS)) {
            getOptionSetDataFromServer(dhisApi, serverDateTime, syncStrategy);
        }
        if (LoadingController.isLoadFlagEnabled(context, ResourceType.TRACKEDENTITYATTRIBUTES) && ResourceController.shouldLoad(serverDateTime, ResourceType.TRACKEDENTITYATTRIBUTES)) {
            getTrackedEntityAttributeDataFromServer(dhisApi, serverDateTime);
        }
        if (LoadingController.isLoadFlagEnabled(context, ResourceType.TRACKEDENTITYATTRIBUTEGROUPS) && ResourceController.shouldLoad(serverDateTime, ResourceType.TRACKEDENTITYATTRIBUTEGROUPS)) {
            getTrackedEntityAttributeGroupDataFromServer(dhisApi, serverDateTime);
        }
        if (LoadingController.isLoadFlagEnabled(context, ResourceType.CONSTANTS) && ResourceController.shouldLoad(serverDateTime, ResourceType.CONSTANTS)) {
            getConstantsDataFromServer(dhisApi, serverDateTime);
        }
        if (LoadingController.isLoadFlagEnabled(context, ResourceType.PROGRAMRULES) && ResourceController.shouldLoad(serverDateTime, ResourceType.PROGRAMRULES)) {
            getProgramRulesDataFromServer(dhisApi, serverDateTime);
        }
        if (LoadingController.isLoadFlagEnabled(context, ResourceType.PROGRAMRULEVARIABLES) && ResourceController.shouldLoad(serverDateTime, ResourceType.PROGRAMRULEVARIABLES)) {
            getProgramRuleVariablesDataFromServer(dhisApi, serverDateTime);
        }
        if (LoadingController.isLoadFlagEnabled(context, ResourceType.PROGRAMRULEACTIONS) && ResourceController.shouldLoad(serverDateTime, ResourceType.PROGRAMRULEACTIONS)) {
            getProgramRuleActionsDataFromServer(dhisApi, serverDateTime);
        }
        if (LoadingController.isLoadFlagEnabled(context, ResourceType.RELATIONSHIPTYPES) && ResourceController.shouldLoad(serverDateTime, ResourceType.RELATIONSHIPTYPES)) {
            getRelationshipTypesDataFromServer(dhisApi, serverDateTime);
        }
        List<TrackedEntityAttribute> trackedEntityAttributes = getTrackedEntityAttributes();
        if (trackedEntityAttributes != null && !trackedEntityAttributes.isEmpty()) {
            getTrackedEntityAttributeGeneratedValuesFromServer(context, dhisApi, getTrackedEntityAttributes(), serverDateTime);
        }
    }

    private static void getAssignedProgramsDataFromServer(DhisApi dhisApi, DateTime serverDateTime) throws APIException {
        Log.d(CLASS_TAG, "getAssignedProgramsDataFromServer");
        UserAccount userAccount = dhisApi.getUserAccount();
        Map<String, Program> programMap = new HashMap();
        for (Program program : userAccount.getPrograms()) {
            programMap.put(program.getUid(), program);
        }
        List<OrganisationUnit> organisationUnitList = userAccount.getOrganisationUnits();
        for (OrganisationUnit organisationUnit : organisationUnitList) {
            organisationUnit.setType(TYPE.ASSIGNED);
        }
        Set<String> teiSearchOrganisationUnitUids = null;
        if (!(userAccount.getTeiSearchOrganisationUnits() == null || userAccount.getTeiSearchOrganisationUnits().isEmpty())) {
            teiSearchOrganisationUnitUids = new HashSet();
            for (OrganisationUnit organisationUnit2 : userAccount.getTeiSearchOrganisationUnits()) {
                teiSearchOrganisationUnitUids.add(organisationUnit2.getId());
            }
        }
        List<OrganisationUnit> teiSearchOrganisationUnits = null;
        if (teiSearchOrganisationUnitUids != null) {
            Map<String, String> queryMap = new HashMap();
            queryMap.put("fields", "[id,displayName,programs]");
            String filter = "id:in:[";
            for (String orgUnitUid : teiSearchOrganisationUnitUids) {
                if (queryMap.containsKey("filter")) {
                    queryMap.put("filter", ((String) queryMap.get("filter")) + "," + orgUnitUid);
                } else {
                    queryMap.put("filter", filter + orgUnitUid);
                }
            }
            queryMap.put("filter", ((String) queryMap.get("filter")) + "]");
            teiSearchOrganisationUnits = (List) dhisApi.getOrganisationUnits(queryMap).get(ApiEndpointContainer.ORGANISATIONUNITS);
        }
        if (teiSearchOrganisationUnits != null) {
            for (OrganisationUnit searchOrgUnit : teiSearchOrganisationUnits) {
                boolean isOrgUnitAssigned = false;
                for (OrganisationUnit assignedOrganisationUnit : organisationUnitList) {
                    if (searchOrgUnit.getId().equals(assignedOrganisationUnit)) {
                        isOrgUnitAssigned = true;
                        break;
                    }
                }
                if (!isOrgUnitAssigned) {
                    searchOrgUnit.setType(TYPE.SEARCH);
                }
            }
            organisationUnitList.addAll(teiSearchOrganisationUnits);
        }
        for (OrganisationUnit organisationUnit22 : organisationUnitList) {
            if (!(organisationUnit22.getPrograms() == null || organisationUnit22.getPrograms().isEmpty())) {
                List<Program> assignedProgramToUnit = new ArrayList();
                for (Program program2 : organisationUnit22.getPrograms()) {
                    if (programMap.containsKey(program2.getUid())) {
                        assignedProgramToUnit.add(programMap.get(program2.getUid()));
                    }
                }
                organisationUnit22.setPrograms(assignedProgramToUnit);
            }
        }
        DbUtils.applyBatch(AssignedProgramsWrapper.getOperations(organisationUnitList));
        DateTimeManager.getInstance().setLastUpdated(ResourceType.ASSIGNEDPROGRAMS, serverDateTime);
    }

    private static void getProgramDataFromServer(DhisApi dhisApi, String uid, DateTime serverDateTime, SyncStrategy syncStrategy) throws APIException {
        Log.d(CLASS_TAG, "getProgramDataFromServer");
        Program program = updateProgram(dhisApi, uid, DateTimeManager.getInstance().getLastUpdated(ResourceType.PROGRAM, uid), syncStrategy);
        DateTimeManager.getInstance().setLastUpdated(ResourceType.PROGRAM, uid, serverDateTime);
    }

    private static Program updateProgram(DhisApi dhisApi, String uid, DateTime lastUpdated, SyncStrategy syncStrategy) throws APIException {
        Map<String, String> QUERY_MAP_FULL = new HashMap();
        QUERY_MAP_FULL.put("fields", "*,trackedEntityType[*],programIndicators[*],programStages[*,!dataEntryForm,program[id],programIndicators[*],userGroupAccesses,programStageSections[*,programStageDataElements[*,programStage[id],dataElement[*,id,attributeValues[*,attribute[*]],optionSet[id]]],programIndicators[*]],programStageDataElements[*,programStage[id],dataElement[*,optionSet[id]]]],programTrackedEntityAttributes[*,trackedEntityAttribute[*]],!organisationUnits");
        if (syncStrategy == SyncStrategy.DOWNLOAD_ONLY_NEW && lastUpdated != null) {
            QUERY_MAP_FULL.put("filter", "lastUpdated:gt:" + lastUpdated.toString());
        }
        Program updatedProgram = dhisApi.getProgram(uid, QUERY_MAP_FULL);
        List<DbOperation> operations = ProgramWrapper.setReferences(updatedProgram);
        DbUtils.applyBatch(operations);
        operations.clear();
        for (ProgramIndicator programIndicator : updatedProgram.getProgramIndicators()) {
            operations.add(DbOperation.save(programIndicator));
        }
        DbUtils.applyBatch(operations);
        return updatedProgram;
    }

    private static void getOptionSetDataFromServer(DhisApi dhisApi, DateTime serverDateTime, SyncStrategy syncStrategy) throws APIException {
        Log.d(CLASS_TAG, "getOptionSetDataFromServer");
        Map<String, String> QUERY_MAP_FULL = new HashMap();
        QUERY_MAP_FULL.put("fields", "*,options[*]");
        DateTime lastUpdated = DateTimeManager.getInstance().getLastUpdated(ResourceType.OPTIONSETS);
        if (syncStrategy == SyncStrategy.DOWNLOAD_ONLY_NEW && lastUpdated != null) {
            QUERY_MAP_FULL.put("filter", "lastUpdated:gt:" + lastUpdated.toString());
        }
        DbUtils.applyBatch(OptionSetWrapper.getOperations(NetworkUtils.unwrapResponse(dhisApi.getOptionSets(QUERY_MAP_FULL), ApiEndpointContainer.OPTION_SETS)));
        DateTimeManager.getInstance().setLastUpdated(ResourceType.OPTIONSETS, serverDateTime);
    }

    private static void getTrackedEntityAttributeGroupDataFromServer(DhisApi dhisApi, DateTime serverDateTime) throws APIException {
        Log.d(CLASS_TAG, "getTrackedEntityAttributeDataFromServer");
        ResourceController.saveResourceDataFromServer(ResourceType.TRACKEDENTITYATTRIBUTEGROUPS, dhisApi, (List) NetworkUtils.unwrapResponse(dhisApi.getTrackedEntityAttributeGroups(ResourceController.getBasicQueryMap(DateTimeManager.getInstance().getLastUpdated(ResourceType.TRACKEDENTITYATTRIBUTEGROUPS))), ApiEndpointContainer.TRACKED_ENTITY_ATTRIBUTE_GROUPS), getTrackedEntityAttributeGroups(), serverDateTime);
    }

    private static void getTrackedEntityAttributeDataFromServer(DhisApi dhisApi, DateTime serverDateTime) throws APIException {
        Log.d(CLASS_TAG, "getTrackedEntityAttributeDataFromServer");
        ResourceController.saveResourceDataFromServer(ResourceType.TRACKEDENTITYATTRIBUTES, dhisApi, (List) NetworkUtils.unwrapResponse(dhisApi.getTrackedEntityAttributes(ResourceController.getBasicQueryMap(DateTimeManager.getInstance().getLastUpdated(ResourceType.TRACKEDENTITYATTRIBUTES))), ApiEndpointContainer.TRACKED_ENTITY_ATTRIBUTES), getTrackedEntityAttributes(), serverDateTime);
    }

    public static void getTrackedEntityAttributeGeneratedValuesFromServer(Context context, DhisApi dhisApi, List<TrackedEntityAttribute> trackedEntityAttributes, DateTime serverDateTime) {
        DateTime lastUpdated = DateTimeManager.getInstance().getLastUpdated(ResourceType.TRACKEDENTITYATTRIBUTEGENERATEDVALUES);
        String orgId = new AppPreferences(context).getChosenOrg();
        if (orgId == null || "".equals(orgId)) {
            orgId = "OSLO";
        }
        for (TrackedEntityAttribute trackedEntityAttribute : trackedEntityAttributes) {
            if (trackedEntityAttribute.isGenerated()) {
                System.out.println("Norway - TA: " + trackedEntityAttribute.getDisplayName() + " ");
                long numberOfGeneratedTrackedEntityAttributesToFetch = shouldFetchGeneratedTrackedEntityAttributeValues(trackedEntityAttribute, serverDateTime);
                if (numberOfGeneratedTrackedEntityAttributesToFetch > 0) {
                    ResourceController.saveBaseValueDataFromServer(ResourceType.TRACKEDENTITYATTRIBUTEGENERATEDVALUES, "", dhisApi.getTrackedEntityAttributeGeneratedValues(trackedEntityAttribute.getUid(), numberOfGeneratedTrackedEntityAttributesToFetch, orgId), getTrackedEntityAttributeGeneratedValues(), serverDateTime, false);
                }
            }
        }
    }

    private static long shouldFetchGeneratedTrackedEntityAttributeValues(TrackedEntityAttribute trackedEntityAttribute, DateTime serverDateTime) {
        checkIfGeneratedTrackedEntityAttributeValuesHasExpired(serverDateTime);
        long numberOfTrackedEntityAttributeGeneratedValues = (long) new Select().from(TrackedEntityAttributeGeneratedValue.class).where(Condition.column("trackedEntityAttribute").eq(trackedEntityAttribute.getUid())).queryList().size();
        if (numberOfTrackedEntityAttributeGeneratedValues < TRACKED_ENTITY_ATTRITBUTE_GENERATED_VALUE_THRESHOLD) {
            return TRACKED_ENTITY_ATTRITBUTE_GENERATED_VALUE_THRESHOLD - numberOfTrackedEntityAttributeGeneratedValues;
        }
        return 0;
    }

    public static void checkIfGeneratedTrackedEntityAttributeValuesHasExpired(DateTime serverDateTime) {
        for (TrackedEntityAttributeGeneratedValue trackedEntityAttributeGeneratedValue : new Select().from(TrackedEntityAttributeGeneratedValue.class).where(Condition.column(TrackedEntityAttributeGeneratedValue.Table.EXPIRYDATE).lessThan(serverDateTime)).queryList()) {
            trackedEntityAttributeGeneratedValue.delete();
        }
    }

    private static void getConstantsDataFromServer(DhisApi dhisApi, DateTime serverDateTime) throws APIException {
        Log.d(CLASS_TAG, "getConstantsDataFromServer");
        ResourceController.saveResourceDataFromServer(ResourceType.CONSTANTS, dhisApi, (List) NetworkUtils.unwrapResponse(dhisApi.getConstants(ResourceController.getBasicQueryMap(DateTimeManager.getInstance().getLastUpdated(ResourceType.CONSTANTS))), ApiEndpointContainer.CONSTANTS), getConstants(), serverDateTime);
    }

    private static void getProgramRulesDataFromServer(DhisApi dhisApi, DateTime serverDateTime) throws APIException {
        Log.d(CLASS_TAG, "getProgramRulesDataFromServer");
        List<ProgramRule> programRules = NetworkUtils.unwrapResponse(dhisApi.getProgramRules(ResourceController.getBasicQueryMap(DateTimeManager.getInstance().getLastUpdated(ResourceType.PROGRAMRULES))), ApiEndpointContainer.PROGRAMRULES);
        List<ProgramRule> validProgramRules = new ArrayList();
        for (ProgramRule programRule : programRules) {
            if (!(programRule.getCondition() == null || programRule.getCondition().isEmpty())) {
                validProgramRules.add(programRule);
            }
        }
        ResourceController.saveResourceDataFromServer(ResourceType.PROGRAMRULES, dhisApi, (List) validProgramRules, getProgramRules(), serverDateTime);
    }

    private static void getProgramRuleVariablesDataFromServer(DhisApi dhisApi, DateTime serverDateTime) throws APIException {
        Log.d(CLASS_TAG, "getProgramRuleVariablesDataFromServer");
        ResourceController.saveResourceDataFromServer(ResourceType.PROGRAMRULEVARIABLES, dhisApi, (List) NetworkUtils.unwrapResponse(dhisApi.getProgramRuleVariables(ResourceController.getBasicQueryMap(DateTimeManager.getInstance().getLastUpdated(ResourceType.PROGRAMRULEVARIABLES))), ApiEndpointContainer.PROGRAMRULEVARIABLES), getProgramRuleVariables(), serverDateTime);
    }

    private static void getProgramRuleActionsDataFromServer(DhisApi dhisApi, DateTime serverDateTime) throws APIException {
        Log.d(CLASS_TAG, "getProgramRuleActionsDataFromServer");
        ResourceController.saveResourceDataFromServer(ResourceType.PROGRAMRULEACTIONS, dhisApi, (List) NetworkUtils.unwrapResponse(dhisApi.getProgramRuleActions(ResourceController.getBasicQueryMap(DateTimeManager.getInstance().getLastUpdated(ResourceType.PROGRAMRULEACTIONS))), ApiEndpointContainer.PROGRAMRULEACTIONS), getProgramRuleActions(), serverDateTime);
    }

    private static void getRelationshipTypesDataFromServer(DhisApi dhisApi, DateTime serverDateTime) throws APIException {
        Log.d(CLASS_TAG, "getRelationshipTypesDataFromServer");
        ResourceType resource = ResourceType.RELATIONSHIPTYPES;
        ResourceController.saveResourceDataFromServer(resource, dhisApi, (List) NetworkUtils.unwrapResponse(dhisApi.getRelationshipTypes(ResourceController.getBasicQueryMap(DateTimeManager.getInstance().getLastUpdated(resource))), ApiEndpointContainer.RELATIONSHIPTYPES), getRelationshipTypes(), serverDateTime);
    }

    public static TrackedEntityAttributeGeneratedValue getTrackedEntityAttributeGeneratedValue(TrackedEntityAttribute trackedEntityAttribute) {
        List<TrackedEntityAttributeGeneratedValue> trackedEntityAttributeGeneratedValues = new Select().from(TrackedEntityAttributeGeneratedValue.class).where(Condition.column("trackedEntityAttribute").eq(trackedEntityAttribute.getUid())).queryList();
        if (trackedEntityAttributeGeneratedValues == null || trackedEntityAttributeGeneratedValues.isEmpty()) {
            return null;
        }
        return (TrackedEntityAttributeGeneratedValue) trackedEntityAttributeGeneratedValues.get(0);
    }

    public static TrackedEntityAttributeGeneratedValue getTrackedEntityAttributeGeneratedValue(String trackedEntityAttributeGeneratedValue) {
        List<TrackedEntityAttributeGeneratedValue> trackedEntityAttributeGeneratedValues = new Select().from(TrackedEntityAttributeGeneratedValue.class).where(Condition.column("value").eq(trackedEntityAttributeGeneratedValue)).queryList();
        if (trackedEntityAttributeGeneratedValues == null || trackedEntityAttributeGeneratedValues.isEmpty()) {
            return null;
        }
        return (TrackedEntityAttributeGeneratedValue) trackedEntityAttributeGeneratedValues.get(0);
    }

    public static boolean performSearchBeforeEnrollment() {
        return getSearchAttributeGroup() != null;
    }

    public static TrackedEntityAttributeGroup getSearchAttributeGroup() {
        for (TrackedEntityAttributeGroup attributeGroup : getTrackedEntityAttributeGroups()) {
            if (attributeGroup.getDescription().equals("SEARCH_GROUP")) {
                return attributeGroup;
            }
        }
        return null;
    }

    public static Hashtable<String, List<Program>> getAssignedProgramsByOrganisationUnit() {
        List<OrganisationUnit> assignedOrganisationUnits = getAssignedOrganisationUnits();
        Hashtable<String, List<Program>> programsForOrganisationUnits = new Hashtable();
        for (OrganisationUnit organisationUnit : assignedOrganisationUnits) {
            if (!(organisationUnit.getId() == null || organisationUnit.getId().length() == Utils.randomUUID.length())) {
                List<Program> programsForOrgUnit = new ArrayList();
                List<Program> programsForOrgUnitSEWoR = getProgramsForOrganisationUnit(organisationUnit.getId(), ProgramType.WITHOUT_REGISTRATION, ProgramType.WITH_REGISTRATION);
                if (programsForOrgUnitSEWoR != null) {
                    programsForOrgUnit.addAll(programsForOrgUnitSEWoR);
                    if (programsForOrgUnitSEWoR.size() > 0) {
                        programsForOrganisationUnits.put(organisationUnit.getId(), programsForOrgUnit);
                    }
                }
            }
        }
        return programsForOrganisationUnits;
    }
}
