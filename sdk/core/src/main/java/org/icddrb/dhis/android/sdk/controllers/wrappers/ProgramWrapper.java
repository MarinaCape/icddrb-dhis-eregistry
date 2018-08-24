package org.icddrb.dhis.android.sdk.controllers.wrappers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.icddrb.dhis.android.sdk.controllers.metadata.MetaDataController;
import org.icddrb.dhis.android.sdk.persistence.models.Attribute;
import org.icddrb.dhis.android.sdk.persistence.models.AttributeValue;
import org.icddrb.dhis.android.sdk.persistence.models.DataElement;
import org.icddrb.dhis.android.sdk.persistence.models.Program;
import org.icddrb.dhis.android.sdk.persistence.models.ProgramIndicator;
import org.icddrb.dhis.android.sdk.persistence.models.ProgramIndicatorToSectionRelationship;
import org.icddrb.dhis.android.sdk.persistence.models.ProgramStage;
import org.icddrb.dhis.android.sdk.persistence.models.ProgramStageDataElement;
import org.icddrb.dhis.android.sdk.persistence.models.ProgramStageSection;
import org.icddrb.dhis.android.sdk.persistence.models.ProgramTrackedEntityAttribute;
import org.icddrb.dhis.android.sdk.persistence.models.meta.DbOperation;

public class ProgramWrapper {
    public static List<DbOperation> setReferences(Program program) {
        List<DbOperation> operations = new ArrayList();
        Map<String, Attribute> attributes = new HashMap();
        if (!(program == null || program.getUid() == null)) {
            operations.addAll(update(program));
            operations.add(DbOperation.save(program));
            int sortOrder = 0;
            for (ProgramTrackedEntityAttribute ptea : program.getProgramTrackedEntityAttributes()) {
                ptea.setProgram(program.getUid());
                ptea.setSortOrder(sortOrder);
                operations.add(DbOperation.save(ptea));
                sortOrder++;
            }
            for (ProgramStage programStage : program.getProgramStages()) {
                operations.add(DbOperation.save(programStage));
                if (programStage.getProgramStageSections() == null || programStage.getProgramStageSections().isEmpty()) {
                    for (ProgramStageDataElement programStageDataElement : programStage.getProgramStageDataElements()) {
                        operations.add(DbOperation.save(programStageDataElement));
                        operations.addAll(saveDataElementAttributes(programStageDataElement.getDataElement(), attributes));
                    }
                    for (ProgramIndicator programIndicator : programStage.getProgramIndicators()) {
                        operations.add(DbOperation.save(programIndicator));
                        operations.add(saveStageRelation(programIndicator, programStage.getUid()));
                    }
                } else {
                    for (ProgramStageSection programStageSection : programStage.getProgramStageSections()) {
                        operations.add(DbOperation.save(programStageSection));
                        for (ProgramStageDataElement programStageDataElement2 : getProgramStageDataElementsBySection(programStage, programStageSection)) {
                            programStageDataElement2.setProgramStageSection(programStageSection.getUid());
                            operations.add(DbOperation.save(programStageDataElement2));
                            operations.addAll(saveDataElementAttributes(programStageDataElement2.getDataElementObj(), attributes));
                        }
                        for (ProgramIndicator programIndicator2 : programStageSection.getProgramIndicators()) {
                            operations.add(DbOperation.save(programIndicator2));
                            operations.add(saveStageRelation(programIndicator2, programStage.getUid()));
                            operations.add(saveStageRelation(programIndicator2, programStageSection.getUid()));
                        }
                    }
                }
            }
        }
        return operations;
    }

    private static List<ProgramStageDataElement> getProgramStageDataElementsBySection(ProgramStage programStage, ProgramStageSection programStageSection) {
        List<ProgramStageDataElement> programStageDataElements = new ArrayList();
        if (programStageSection.getDataElements() == null) {
            return programStageSection.getProgramStageDataElements();
        }
        for (ProgramStageDataElement programStageDataElement : programStage.getProgramStageDataElements()) {
            for (DataElement dataElement : programStageSection.getDataElements()) {
                if (dataElement.getUid().equals(programStageDataElement.getDataElement().getUid())) {
                    programStageDataElements.add(programStageDataElement);
                }
            }
        }
        return programStageDataElements;
    }

    private static List<DbOperation> update(Program program) {
        List<DbOperation> operations = new ArrayList();
        Program oldProgram = MetaDataController.getProgram(program.getUid());
        if (oldProgram != null) {
            for (ProgramTrackedEntityAttribute ptea : oldProgram.getProgramTrackedEntityAttributes()) {
                operations.add(DbOperation.delete(ptea));
            }
            for (ProgramStage programStage : program.getProgramStages()) {
                for (ProgramStageDataElement psde : programStage.getProgramStageDataElements()) {
                    operations.add(DbOperation.delete(psde));
                }
                for (ProgramStageSection programStageSection : programStage.getProgramStageSections()) {
                    operations.add(DbOperation.delete(programStageSection));
                }
                operations.add(DbOperation.delete(programStage));
            }
            for (ProgramIndicator programIndicator : program.getProgramIndicators()) {
                operations.add(DbOperation.delete(programIndicator));
            }
        }
        return operations;
    }

    private static DbOperation saveStageRelation(ProgramIndicator programIndicator, String programSection) {
        ProgramIndicatorToSectionRelationship stageRelation = new ProgramIndicatorToSectionRelationship();
        stageRelation.setProgramIndicator(programIndicator);
        stageRelation.setProgramSection(programSection);
        return DbOperation.save(stageRelation);
    }

    private static List<DbOperation> saveDataElementAttributes(DataElement dataElement, Map<String, Attribute> attributes) {
        List<DbOperation> operations = new ArrayList();
        List<AttributeValue> attributeValues = dataElement.getAttributeValues();
        if (!(attributeValues == null || attributeValues.isEmpty())) {
            for (AttributeValue attributeValue : attributeValues) {
                attributeValue.setDataElement(dataElement.getUid());
                operations.add(DbOperation.save(attributeValue));
                Attribute attribute = (Attribute) attributes.get(attributeValue.getAttributeId());
                if (attribute == null) {
                    attribute = attributeValue.getAttributeObj();
                }
                if (attribute == null) {
                    attribute = attributeValue.getAttribute();
                }
                attributes.put(attributeValue.getAttributeId(), attribute);
                operations.add(DbOperation.save(attribute));
            }
        }
        return operations;
    }
}
