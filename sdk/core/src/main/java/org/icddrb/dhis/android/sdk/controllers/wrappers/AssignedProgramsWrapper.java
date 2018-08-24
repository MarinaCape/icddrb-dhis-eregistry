package org.icddrb.dhis.android.sdk.controllers.wrappers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.icddrb.dhis.android.sdk.controllers.ApiEndpointContainer;
import org.icddrb.dhis.android.sdk.controllers.DhisController;
import org.icddrb.dhis.android.sdk.controllers.metadata.MetaDataController;
import org.icddrb.dhis.android.sdk.persistence.models.OrganisationUnit;
import org.icddrb.dhis.android.sdk.persistence.models.OrganisationUnitProgramRelationship;
import org.icddrb.dhis.android.sdk.persistence.models.Program;
import org.icddrb.dhis.android.sdk.persistence.models.meta.DbOperation;
import org.icddrb.dhis.android.sdk.utils.StringConverter;
import retrofit.client.Response;
import retrofit.converter.ConversionException;

public class AssignedProgramsWrapper extends JsonDeserializer<List<OrganisationUnit>> {
    public List<OrganisationUnit> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        List<OrganisationUnit> organisationUnits = new ArrayList();
        JsonNode organisationUnitsNode = ((JsonNode) p.getCodec().readTree(p)).get(ApiEndpointContainer.ORGANISATIONUNITS);
        if (organisationUnitsNode != null) {
            Iterator<JsonNode> nodes = organisationUnitsNode.elements();
            while (nodes.hasNext()) {
                organisationUnits.add((OrganisationUnit) DhisController.getInstance().getObjectMapper().readValue(((JsonNode) nodes.next()).toString(), OrganisationUnit.class));
            }
        }
        return organisationUnits;
    }

    public List<OrganisationUnit> deserialize(Response response) throws ConversionException, IOException {
        List<OrganisationUnit> organisationUnits = new ArrayList();
        JsonNode organisationUnitsNode = DhisController.getInstance().getObjectMapper().readTree(new StringConverter().fromBody(response.getBody(), (Type) String.class)).get(ApiEndpointContainer.ORGANISATIONUNITS);
        if (organisationUnitsNode != null) {
            Iterator<JsonNode> nodes = organisationUnitsNode.elements();
            while (nodes.hasNext()) {
                organisationUnits.add((OrganisationUnit) DhisController.getInstance().getObjectMapper().readValue(((JsonNode) nodes.next()).toString(), OrganisationUnit.class));
            }
        }
        return organisationUnits;
    }

    public static List<DbOperation> getOperations(List<OrganisationUnit> organisationUnits) {
        List<DbOperation> operations = new ArrayList();
        if (organisationUnits != null) {
            for (OrganisationUnitProgramRelationship oldOrganisationUnitProgramRelationship : MetaDataController.getOrganisationUnitProgramRelationships()) {
                operations.add(DbOperation.delete(oldOrganisationUnitProgramRelationship));
            }
            for (OrganisationUnit organisationUnit : organisationUnits) {
                for (Program program : organisationUnit.getPrograms()) {
                    OrganisationUnitProgramRelationship orgUnitProgram = new OrganisationUnitProgramRelationship();
                    orgUnitProgram.setOrganisationUnitId(organisationUnit.getId());
                    orgUnitProgram.setProgramId(program.getUid());
                    operations.add(DbOperation.save(orgUnitProgram));
                }
                operations.add(DbOperation.save(organisationUnit));
            }
        }
        return operations;
    }
}
