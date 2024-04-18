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

import org.apache.nifi.util.TestRunner;
import org.apache.nifi.util.TestRunners;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

public class ConvertFahrenheitToCelsiusTest {

    private TestRunner testRunner;

    @BeforeEach
    public void init() {
        testRunner = TestRunners.newTestRunner(ConvertFahrenheitToCelsius.class);
    }

    @Test
    public void testProcessor() {
        testCelsius();
    }

    public void testCelsius() {
        Assertions.assertEquals(BigDecimal.valueOf(-9.4), ConvertFahrenheitToCelsius.toCelsius(BigDecimal.valueOf(15)));
        Assertions.assertEquals(BigDecimal.valueOf(-9.3), ConvertFahrenheitToCelsius.toCelsius(BigDecimal.valueOf(15.3)));
        Assertions.assertEquals(BigDecimal.valueOf(-0.6), ConvertFahrenheitToCelsius.toCelsius(BigDecimal.valueOf(31)));

        Assertions.assertEquals(BigDecimal.valueOf(0.0), ConvertFahrenheitToCelsius.toCelsius(BigDecimal.valueOf(32)));

        Assertions.assertEquals(BigDecimal.valueOf(2.2), ConvertFahrenheitToCelsius.toCelsius(BigDecimal.valueOf(36)));
        Assertions.assertEquals(BigDecimal.valueOf(11.0), ConvertFahrenheitToCelsius.toCelsius(BigDecimal.valueOf(51.8)));
        Assertions.assertEquals(BigDecimal.valueOf(37.8), ConvertFahrenheitToCelsius.toCelsius(BigDecimal.valueOf(100)));
    }

}
