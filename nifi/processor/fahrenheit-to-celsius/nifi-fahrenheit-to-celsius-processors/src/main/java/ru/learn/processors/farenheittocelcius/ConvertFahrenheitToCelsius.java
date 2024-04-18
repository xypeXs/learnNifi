/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ru.learn.processors.farenheittocelcius;

import org.apache.nifi.annotation.behavior.ReadsAttribute;
import org.apache.nifi.annotation.behavior.ReadsAttributes;
import org.apache.nifi.annotation.behavior.WritesAttribute;
import org.apache.nifi.annotation.behavior.WritesAttributes;
import org.apache.nifi.annotation.documentation.CapabilityDescription;
import org.apache.nifi.annotation.documentation.SeeAlso;
import org.apache.nifi.annotation.documentation.Tags;
import org.apache.nifi.annotation.lifecycle.OnScheduled;
import org.apache.nifi.components.PropertyDescriptor;
import org.apache.nifi.flowfile.FlowFile;
import org.apache.nifi.processor.AbstractProcessor;
import org.apache.nifi.processor.ProcessContext;
import org.apache.nifi.processor.ProcessSession;
import org.apache.nifi.processor.ProcessorInitializationContext;
import org.apache.nifi.processor.Relationship;
import org.apache.nifi.util.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Tags({"learn"})
@CapabilityDescription("Converts Fahrenheit temperature to Celsius")
@SeeAlso({})
@ReadsAttributes({
        @ReadsAttribute(attribute = ConvertFahrenheitToCelsius.TEMPERATURE_UNIT_ATTRIBUTE, description = "Единцы измерения температуры"),
        @ReadsAttribute(attribute = ConvertFahrenheitToCelsius.TEMPERATURE_ATTRIBUTE, description = "Значение температуры"),
})
@WritesAttributes({
        @WritesAttribute(attribute = ConvertFahrenheitToCelsius.TEMPERATURE_ATTRIBUTE, description = "Значение температуры")
})
public class ConvertFahrenheitToCelsius extends AbstractProcessor {

    public static final String TEMPERATURE_UNIT_ATTRIBUTE = "temperature_unit";
    public static final String TEMPERATURE_ATTRIBUTE = "temperature";

    public static final String FAHRENHEIT_IDENTIFIER = "°F";
    public static final String CELSIUS_IDENTIFIER = "°C";

    public static final Relationship SUCCESS_REL = new Relationship.Builder()
            .name("success")
            .description("success")
            .build();

    public static final Relationship FAILURE_REL = new Relationship.Builder()
            .name("failure")
            .description("failure")
            .build();

    private List<PropertyDescriptor> descriptors;

    private Set<Relationship> relationships;

    @Override
    protected void init(final ProcessorInitializationContext context) {
        descriptors = new ArrayList<>();
        descriptors = Collections.unmodifiableList(descriptors);

        relationships = new HashSet<>();
        relationships.add(SUCCESS_REL);
        relationships.add(FAILURE_REL);
        relationships = Collections.unmodifiableSet(relationships);
    }

    @Override
    public Set<Relationship> getRelationships() {
        return this.relationships;
    }

    @Override
    public final List<PropertyDescriptor> getSupportedPropertyDescriptors() {
        return descriptors;
    }

    @OnScheduled
    public void onScheduled(final ProcessContext context) {

    }

    @Override
    public void onTrigger(final ProcessContext context, final ProcessSession session) {
        FlowFile flowFile = session.get();
        if (flowFile == null) {
            return;
        }
        try {
            String temperatureUnit = flowFile.getAttribute(TEMPERATURE_UNIT_ATTRIBUTE);
            String temperatureValue = flowFile.getAttribute(TEMPERATURE_ATTRIBUTE);

            if (StringUtils.isNotBlank(temperatureUnit) && StringUtils.isNotBlank(temperatureValue) && FAHRENHEIT_IDENTIFIER.equalsIgnoreCase(temperatureUnit)) {
                BigDecimal temperatureFahrenheitValue = new BigDecimal(temperatureValue);
                BigDecimal temperatureCelsiusValue = toCelsius(temperatureFahrenheitValue);

                session.putAttribute(flowFile, TEMPERATURE_ATTRIBUTE, temperatureCelsiusValue.toString());
                session.putAttribute(flowFile, TEMPERATURE_UNIT_ATTRIBUTE, CELSIUS_IDENTIFIER);
            }
            session.transfer(flowFile, SUCCESS_REL);
        } catch (Exception e) {
            session.transfer(flowFile, FAILURE_REL);
        }
    }

    public static BigDecimal toCelsius(BigDecimal temperatureFahrenheitValue) {
        return temperatureFahrenheitValue
                .setScale(1, RoundingMode.HALF_UP)
                .subtract(BigDecimal.valueOf(32))
                .multiply(BigDecimal.valueOf(5))
                .divide(BigDecimal.valueOf(9), RoundingMode.HALF_UP);
    }
}
