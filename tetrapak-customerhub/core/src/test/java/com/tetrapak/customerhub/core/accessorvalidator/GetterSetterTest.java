package com.tetrapak.customerhub.core.accessorvalidator;

import org.junit.Test;

import com.openpojo.reflection.impl.PojoClassFactory;
import com.openpojo.validation.Validator;
import com.openpojo.validation.ValidatorBuilder;
import com.openpojo.validation.test.impl.GetterTester;
import com.openpojo.validation.test.impl.SetterTester;
import com.tetrapak.customerhub.core.models.MyEquipmentModel;

/**
 * @author ojaswarn
 * The Class GetterSetterTest.
 */
public class GetterSetterTest {

    /** The Constant ACCESSOR_VALIDATOR. */
    private static final Validator ACCESSOR_VALIDATOR = ValidatorBuilder.create().with(new GetterTester())
            .with(new SetterTester()).build();

    /**
     * Test accesors.
     */
    @Test
    public void testAccesors() {
    	validateAccessors(MyEquipmentModel.class);
    }

    /**
     * Validate accessors.
     *
     * @param clazz the clazz
     */
    public static void validateAccessors(final Class<?> clazz) {
        ACCESSOR_VALIDATOR.validate(PojoClassFactory.getPojoClass(clazz));
    }
}
