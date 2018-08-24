package org.icddrb.dhis.android.sdk.network;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.List;
import java.util.Map;
import org.icddrb.dhis.android.sdk.persistence.models.ApiResponse;
import org.icddrb.dhis.android.sdk.persistence.models.Constant;
import org.icddrb.dhis.android.sdk.persistence.models.Dashboard;
import org.icddrb.dhis.android.sdk.persistence.models.DashboardItem;
import org.icddrb.dhis.android.sdk.persistence.models.DashboardItemContent;
import org.icddrb.dhis.android.sdk.persistence.models.Enrollment;
import org.icddrb.dhis.android.sdk.persistence.models.Event;
import org.icddrb.dhis.android.sdk.persistence.models.Interpretation;
import org.icddrb.dhis.android.sdk.persistence.models.OptionSet;
import org.icddrb.dhis.android.sdk.persistence.models.OrganisationUnit;
import org.icddrb.dhis.android.sdk.persistence.models.OrganisationUnitUser;
import org.icddrb.dhis.android.sdk.persistence.models.Program;
import org.icddrb.dhis.android.sdk.persistence.models.ProgramRule;
import org.icddrb.dhis.android.sdk.persistence.models.ProgramRuleAction;
import org.icddrb.dhis.android.sdk.persistence.models.ProgramRuleVariable;
import org.icddrb.dhis.android.sdk.persistence.models.RelationshipType;
import org.icddrb.dhis.android.sdk.persistence.models.SystemInfo;
import org.icddrb.dhis.android.sdk.persistence.models.TrackedEntityAttribute;
import org.icddrb.dhis.android.sdk.persistence.models.TrackedEntityAttributeGeneratedValue;
import org.icddrb.dhis.android.sdk.persistence.models.TrackedEntityAttributeGroup;
import org.icddrb.dhis.android.sdk.persistence.models.TrackedEntityInstance;
import org.icddrb.dhis.android.sdk.persistence.models.UserAccount;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;
import retrofit.http.Query;
import retrofit.http.QueryMap;
import retrofit.mime.TypedString;

public interface DhisApi {
    @DELETE("/dashboards/{uid}")
    Response deleteDashboard(@Path("uid") String str);

    @DELETE("/dashboards/{dashboardUId}/items/{itemUId}")
    Response deleteDashboardItem(@Path("dashboardUId") String str, @Path("itemUId") String str2);

    @DELETE("/dashboards/{dashboardUid}/items/{itemUid}/content/{contentUid}")
    Response deleteDashboardItemContent(@Path("dashboardUid") String str, @Path("itemUid") String str2, @Path("contentUid") String str3);

    @DELETE("/events/{eventUid}")
    Response deleteEvent(@Path("eventUid") String str);

    @DELETE("/interpretations/{uid}")
    Response deleteInterpretation(@Path("uid") String str);

    @DELETE("/interpretations/{interpretationUid}/comments/{commentUid}")
    Response deleteInterpretationComment(@Path("interpretationUid") String str, @Path("commentUid") String str2);

    @GET("/me/programs/")
    Response getAssignedPrograms(@QueryMap Map<String, String> map);

    @GET("/charts?paging=false")
    Map<String, List<DashboardItemContent>> getCharts(@QueryMap Map<String, String> map);

    @GET("/constants?paging=false")
    Map<String, List<Constant>> getConstants(@QueryMap Map<String, String> map);

    @GET("/29/me/")
    UserAccount getCurrentUserAccount(@QueryMap Map<String, String> map);

    @GET("/dashboards/{uid}")
    Dashboard getDashboard(@Path("uid") String str, @QueryMap Map<String, String> map);

    @GET("/dashboardItems/{uid}")
    DashboardItem getDashboardItem(@Path("uid") String str, @QueryMap Map<String, String> map);

    @GET("/dashboardItems?paging=false")
    Map<String, List<DashboardItem>> getDashboardItems(@QueryMap Map<String, String> map);

    @GET("/dashboards?paging=false")
    Map<String, List<Dashboard>> getDashboards(@QueryMap Map<String, String> map);

    @GET("/enrollments/{enrollmentUid}")
    Enrollment getEnrollment(@Path("enrollmentUid") String str, @QueryMap Map<String, String> map);

    @GET("/enrollments?skipPaging=true&ouMode=ACCESSIBLE")
    Map<String, List<Enrollment>> getEnrollments(@Query("trackedEntityInstance") String str, @QueryMap Map<String, String> map);

    @GET("/enrollments?skipPaging=true&ouMode=ACCESSIBLE")
    Map<String, List<Enrollment>> getEnrollmentsByOrgUnit(@Query("orgUnit") String str, @QueryMap Map<String, String> map);

    @GET("/events/{eventUid}")
    Event getEvent(@Path("eventUid") String str, @QueryMap Map<String, String> map);

    @GET("/eventCharts?paging=false")
    Map<String, List<DashboardItemContent>> getEventCharts(@QueryMap Map<String, String> map);

    @GET("/eventReports?paging=false")
    Map<String, List<DashboardItemContent>> getEventReports(@QueryMap Map<String, String> map);

    @GET("/events")
    JsonNode getEventUids(@Query("program") String str, @Query("orgUnit") String str2, @QueryMap Map<String, String> map);

    @GET("/events?page=0")
    List<Event> getEvents(@Query("program") String str, @Query("orgUnit") String str2, @Query("pageSize") int i, @QueryMap Map<String, String> map);

    @GET("/events?page=0")
    List<Event> getEvents(@Query("program") String str, @Query("orgUnit") String str2, @QueryMap Map<String, String> map);

    @GET("/events?skipPaging=true&ouMode=ACCESSIBLE")
    JsonNode getEventsForEnrollment(@Query("program") String str, @Query("programStatus") String str2, @Query("trackedEntityInstance") String str3, @QueryMap Map<String, String> map);

    @GET("/events?skipPaging=true&ouMode=ACCESSIBLE&")
    List<Event> getEventsForTrackedEntityInstance(@Query("program") String str, @QueryMap Map<String, String> map);

    @GET("/interpretations/{uid}")
    Interpretation getInterpretation(@Path("uid") String str, @QueryMap Map<String, String> map);

    @GET("/interpretations/?paging=false")
    Map<String, List<Interpretation>> getInterpretations(@QueryMap Map<String, String> map);

    @GET("/maps?paging=false")
    Map<String, List<DashboardItemContent>> getMaps(@QueryMap Map<String, String> map);

    @GET("/events?skipPaging=true")
    Map<String, List<Event>> getNEvents(@Query("program") String str, @Query("orgUnit") String str2, @QueryMap Map<String, String> map);

    @GET("/optionSets?paging=false")
    Map<String, List<OptionSet>> getOptionSets(@QueryMap Map<String, String> map);

    @GET("/organisationUnits?paging=false")
    Map<String, List<OrganisationUnit>> getOrganisationUnits(@QueryMap(encodeValues = false) Map<String, String> map);

    @GET("/organisationUnits?paging=false&fields=id,displayName,parent,level,users[id,displayName,userCredentials[username,userRoles[id,displayName,name]]]")
    Map<String, List<OrganisationUnitUser>> getOrgsAndUsers();

    @GET("/programs/{programUid}")
    Program getProgram(@Path("programUid") String str, @QueryMap Map<String, String> map);

    @GET("/programRuleActions?paging=false")
    Map<String, List<ProgramRuleAction>> getProgramRuleActions(@QueryMap Map<String, String> map);

    @GET("/programRuleVariables?paging=false")
    Map<String, List<ProgramRuleVariable>> getProgramRuleVariables(@QueryMap Map<String, String> map);

    @GET("/programRules?paging=false")
    Map<String, List<ProgramRule>> getProgramRules(@QueryMap Map<String, String> map);

    @GET("/relationshipTypes?paging=false")
    Map<String, List<RelationshipType>> getRelationshipTypes(@QueryMap Map<String, String> map);

    @Headers({"Accept: application/text"})
    @GET("/reportTables/{id}/data.html")
    Response getReportTableData(@Path("id") String str);

    @GET("/reportTables?paging=false")
    Map<String, List<DashboardItemContent>> getReportTables(@QueryMap Map<String, String> map);

    @GET("/reports?paging=false")
    Map<String, List<DashboardItemContent>> getReports(@QueryMap Map<String, String> map);

    @GET("/documents?paging=false")
    Map<String, List<DashboardItemContent>> getResources(@QueryMap Map<String, String> map);

    @GET("/system/info/")
    SystemInfo getSystemInfo();

    @GET("/trackedEntityAttributes/{trackedEntityAttribute}/generateAndReserve")
    List<TrackedEntityAttributeGeneratedValue> getTrackedEntityAttributeGeneratedValues(@Path("trackedEntityAttribute") String str, @Query("numberToReserve") long j, @Query("ORG_UNIT_CODE") String str2);

    @GET("/trackedEntityAttributeGroups?paging=false")
    Map<String, List<TrackedEntityAttributeGroup>> getTrackedEntityAttributeGroups(@QueryMap Map<String, String> map);

    @GET("/trackedEntityAttributes?paging=false")
    Map<String, List<TrackedEntityAttribute>> getTrackedEntityAttributes(@QueryMap Map<String, String> map);

    @GET("/trackedEntityInstances/{trackedEntityInstanceUid}")
    TrackedEntityInstance getTrackedEntityInstance(@Path("trackedEntityInstanceUid") String str, @QueryMap Map<String, String> map);

    @GET("/trackedEntityInstances?skipPaging=true")
    Map<String, List<TrackedEntityInstance>> getTrackedEntityInstances(@Query("ou") String str, @QueryMap(encodeValues = false) Map<String, String> map);

    @GET("/trackedEntityInstances?skipPaging=true&ouMode=ACCESSIBLE")
    Map<String, List<TrackedEntityInstance>> getTrackedEntityInstancesFromAllAccessibleOrgUnits(@Query("ou") String str, @QueryMap(encodeValues = false) Map<String, String> map);

    @GET("/me?fields=userGroups,organisationUnits[id,displayName,programs[id,userGroupAccesses,trackedEntityType]],userCredentials[userRoles[programs[id],id]],teiSearchOrganisationUnits")
    UserAccount getUserAccount();

    @GET("/users?paging=false")
    Map<String, List<DashboardItemContent>> getUsers(@QueryMap Map<String, String> map);

    @Headers({"Content-Type: text/plain"})
    @POST("/interpretations/chart/{uid}")
    Response postChartInterpretation(@Path("uid") String str, @Body TypedString typedString);

    @POST("/dashboards/")
    Response postDashboard(@Body Dashboard dashboard);

    @POST("/dashboards/{dashboardUId}/items/content")
    Response postDashboardItem(@Path("dashboardUId") String str, @Query("type") String str2, @Query("id") String str3, @Body String str4);

    @POST("/events/?strategy=DELETE")
    ApiResponse postDeletedEvents(@Body Map<String, List<Event>> map);

    @POST("/enrollments/")
    Response postEnrollment(@Body Enrollment enrollment);

    @POST("/events/")
    Response postEvent(@Body Event event);

    @POST("/events/")
    ApiResponse postEvents(@Body Map<String, List<Event>> map);

    @Headers({"Content-Type: text/plain"})
    @POST("/interpretations/{interpretationUid}/comments")
    Response postInterpretationComment(@Path("interpretationUid") String str, @Body TypedString typedString);

    @Headers({"Content-Type: text/plain"})
    @POST("/interpretations/map/{uid}")
    Response postMapInterpretation(@Path("uid") String str, @Body TypedString typedString);

    @Headers({"Content-Type: text/plain"})
    @POST("/interpretations/reportTable/{uid}")
    Response postReportTableInterpretation(@Path("uid") String str, @Body TypedString typedString);

    @POST("/trackedEntityInstances/")
    Response postTrackedEntityInstance(@Body TrackedEntityInstance trackedEntityInstance);

    @POST("/trackedEntityInstances/?strategy=CREATE_AND_UPDATE")
    ApiResponse postTrackedEntityInstances(@Body Map<String, List<TrackedEntityInstance>> map);

    @PUT("/dashboards/{uid}")
    Response putDashboard(@Path("uid") String str, @Body Dashboard dashboard);

    @PUT("/enrollments/{enrollmentUid}")
    Response putEnrollment(@Path("enrollmentUid") String str, @Body Enrollment enrollment);

    @PUT("/events/{eventUid}")
    Response putEvent(@Path("eventUid") String str, @Body Event event);

    @Headers({"Content-Type: text/plain"})
    @PUT("/interpretations/{interpretationUid}/comments/{commentUid}")
    Response putInterpretationComment(@Path("interpretationUid") String str, @Path("commentUid") String str2, @Body TypedString typedString);

    @Headers({"Content-Type: text/plain"})
    @PUT("/interpretations/{uid}")
    Response putInterpretationText(@Path("uid") String str, @Body TypedString typedString);

    @PUT("/trackedEntityInstances/{trackedEntityInstanceUid}")
    Response putTrackedEntityInstance(@Path("trackedEntityInstanceUid") String str, @Body TrackedEntityInstance trackedEntityInstance);
}
