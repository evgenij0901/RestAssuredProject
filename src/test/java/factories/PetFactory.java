package factories;
import constants.Constants;
import models.CategoryDTO;
import models.PetDTO;
import models.TagDTO;
import net.datafaker.Faker;

import java.util.List;

public class PetFactory {
    public PetDTO getBasicPetWithCorrectData() {
        Faker faker = new Faker();
        CategoryDTO categoryDto = CategoryDTO.builder()
                .id(faker.number().randomDigitNotZero())
                .name(faker.name().name())
                .build();

        TagDTO tagDto = TagDTO.builder()
                .id(faker.number().randomDigitNotZero())
                .name(faker.name().name())
                .build();

        return PetDTO.builder()
                .id(faker.number().randomDigitNotZero())
                .name(faker.name().name())
                .tags(List.of(tagDto))
                .category(categoryDto)
                .status(Constants.AVAILABLE)
                .build();
    }
}
