package the.garage.platform.notifications.infrastructure.persistence.jpa.converters;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import the.garage.platform.notifications.domain.model.valueobjects.EmailAddress;

@Converter(autoApply = false)
public class EmailAddressPersistenceConverter implements AttributeConverter<EmailAddress, String> {

    @Override
    public String convertToDatabaseColumn(EmailAddress attribute) {
        return attribute == null ? null : attribute.value();
    }

    @Override
    public EmailAddress convertToEntityAttribute(String dbData) {
        return dbData == null ? null : new EmailAddress(dbData);
    }
}