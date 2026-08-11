package the.garage.platform.iam.infrastructure.persistence.jpa.converters;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import the.garage.platform.iam.domain.model.valueobjects.Email;

/**
 * Converts {@link Email} between the domain model and the persistence column value.
 * Follows the same pattern as {@code EmailAddressPersistenceConverter} in the reference project.
 */
@Converter(autoApply = false)
public class EmailPersistenceConverter implements AttributeConverter<Email, String> {

    @Override
    public String convertToDatabaseColumn(Email attribute) {
        return attribute == null ? null : attribute.address();
    }

    @Override
    public Email convertToEntityAttribute(String dbData) {
        return dbData == null ? null : new Email(dbData);
    }
}