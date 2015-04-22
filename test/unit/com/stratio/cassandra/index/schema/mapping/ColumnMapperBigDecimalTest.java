/*
 * Copyright 2014, Stratio.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.stratio.cassandra.index.schema.mapping;

import com.stratio.cassandra.index.schema.Schema;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.DocValuesType;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class ColumnMapperBigDecimalTest {

    @Test
    public void testConstructorWithoutArgs() {
        ColumnMapperBigDecimal mapper = new ColumnMapperBigDecimal(null, null, null, null);
        Assert.assertEquals(ColumnMapper.INDEXED, mapper.isIndexed());
        Assert.assertEquals(ColumnMapper.SORTED, mapper.isSorted());
        Assert.assertEquals(ColumnMapperBigDecimal.DEFAULT_INTEGER_DIGITS, mapper.getIntegerDigits());
        Assert.assertEquals(ColumnMapperBigDecimal.DEFAULT_DECIMAL_DIGITS, mapper.getDecimalDigits());
    }

    @Test
    public void testConstructorWithAllArgs() {
        ColumnMapperBigDecimal mapper = new ColumnMapperBigDecimal(false, true, 10, 5);
        Assert.assertFalse(mapper.isIndexed());
        Assert.assertTrue(mapper.isSorted());
        Assert.assertEquals(10, mapper.getIntegerDigits());
        Assert.assertEquals(5, mapper.getDecimalDigits());
    }

    @Test()
    public void testValueNull() {
        ColumnMapperBigDecimal mapper = new ColumnMapperBigDecimal(null, null, 10, 10);
        String parsed = mapper.base("test", null);
        Assert.assertNull(parsed);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValueIntegerDigitsZero() {
        new ColumnMapperBigDecimal(null, null, 0, 10);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValueDecimalDigitsZero() {
        new ColumnMapperBigDecimal(null, null, 10, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValueBothDigitsZero() {
        new ColumnMapperBigDecimal(null, null, 0, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValueIntegerDigitsNegative() {
        new ColumnMapperBigDecimal(null, null, -1, 10);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValueDecimalDigitsNegative() {
        new ColumnMapperBigDecimal(null, null, 10, -1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValueBothDigitsNegative() {
        new ColumnMapperBigDecimal(null, null, -1, -1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValueBooleanTrue() {
        ColumnMapperBigDecimal mapper = new ColumnMapperBigDecimal(null, null, 100, 100);
        mapper.base("test", true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValueBooleanFalse() {
        ColumnMapperBigDecimal mapper = new ColumnMapperBigDecimal(null, null, 100, 100);
        mapper.base("test", false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValueUUID() {
        ColumnMapperBigDecimal mapper = new ColumnMapperBigDecimal(null, null, 100, 100);
        mapper.base("test", UUID.fromString("550e8400-e29b-41d4-a716-446655440000"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValueDate() {
        ColumnMapperBigDecimal mapper = new ColumnMapperBigDecimal(null, null, 100, 100);
        mapper.base("test", new Date());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValueStringInvalid() {
        ColumnMapperBigDecimal mapper = new ColumnMapperBigDecimal(null, null, 100, 100);
        mapper.base("test", "0s0");
    }

    // /////////////

    @Test
    public void testValueStringMinPositive() {
        ColumnMapperBigDecimal mapper = new ColumnMapperBigDecimal(null, null, 4, 4);
        String parsed = mapper.base("test", "1");
        Assert.assertEquals("10000.9999", parsed);
    }

    @Test
    public void testValueStringMaxPositive() {
        ColumnMapperBigDecimal mapper = new ColumnMapperBigDecimal(null, null, 4, 4);
        String parsed = mapper.base("test", "9999.9999");
        Assert.assertEquals("19999.9998", parsed);
    }

    @Test
    public void testValueStringMinNegative() {
        ColumnMapperBigDecimal mapper = new ColumnMapperBigDecimal(null, null, 4, 4);
        String parsed = mapper.base("test", "-1");
        Assert.assertEquals("09998.9999", parsed);
    }

    @Test
    public void testValueStringMaxNegative() {
        ColumnMapperBigDecimal mapper = new ColumnMapperBigDecimal(null, null, 4, 4);
        String parsed = mapper.base("test", "-9999.9999");
        Assert.assertEquals("00000.0000", parsed);
    }

    @Test
    public void testValueStringZero() {
        ColumnMapperBigDecimal mapper = new ColumnMapperBigDecimal(null, null, 4, 4);
        String parsed = mapper.base("test", "0");
        Assert.assertEquals("09999.9999", parsed);
    }

    @Test
    public void testValueStringLeadingZeros() {
        ColumnMapperBigDecimal mapper = new ColumnMapperBigDecimal(null, null, 4, 4);
        String parsed = mapper.base("test", "000.042");
        Assert.assertEquals("10000.0419", parsed);
    }

    // // ///

    @Test
    public void testValueIntegerMinPositive() {
        ColumnMapperBigDecimal mapper = new ColumnMapperBigDecimal(null, null, 4, 4);
        String parsed = mapper.base("test", 1);
        Assert.assertEquals("10000.9999", parsed);
    }

    @Test
    public void testValueIntegerMaxPositive() {
        ColumnMapperBigDecimal mapper = new ColumnMapperBigDecimal(null, null, 4, 4);
        String parsed = mapper.base("test", 9999.9999);
        Assert.assertEquals("19999.9998", parsed);
    }

    @Test
    public void testValueIntegerMinNegative() {
        ColumnMapperBigDecimal mapper = new ColumnMapperBigDecimal(null, null, 4, 4);
        String parsed = mapper.base("test", -1);
        Assert.assertEquals("09998.9999", parsed);
    }

    @Test
    public void testValueIntegerMaxNegative() {
        ColumnMapperBigDecimal mapper = new ColumnMapperBigDecimal(null, null, 4, 4);
        String parsed = mapper.base("test", -9999.9999);
        Assert.assertEquals("00000.0000", parsed);
    }

    @Test
    public void testValueIntegerZero() {
        ColumnMapperBigDecimal mapper = new ColumnMapperBigDecimal(null, null, 4, 4);
        String parsed = mapper.base("test", 0);
        Assert.assertEquals("09999.9999", parsed);
    }

    // //////

    @Test(expected = IllegalArgumentException.class)
    public void testValueTooBigInteger() {
        ColumnMapperBigDecimal mapper = new ColumnMapperBigDecimal(null, null, 4, 4);
        mapper.base("test", 10000);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValueTooBigDecimal() {
        ColumnMapperBigDecimal mapper = new ColumnMapperBigDecimal(null, null, 4, 4);
        mapper.base("test", 42.00001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValueTooSmallInteger() {
        ColumnMapperBigDecimal mapper = new ColumnMapperBigDecimal(null, null, 4, 4);
        mapper.base("test", -10000);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValueTooSmallDecimal() {
        ColumnMapperBigDecimal mapper = new ColumnMapperBigDecimal(null, null, 4, 4);
        mapper.base("test", -0.00001);
    }

    // /////

    @Test
    public void testValueIntegerNegativeMaxSort() {
        ColumnMapperBigDecimal mapper = new ColumnMapperBigDecimal(null, null, 8, 100);
        String lower = mapper.base("test", -99999999);
        String upper = mapper.base("test", -99999998);
        int compare = lower.compareTo(upper);
        Assert.assertTrue(compare < 0);
    }

    @Test
    public void testValueIntegerNegativeMinSort() {
        ColumnMapperBigDecimal mapper = new ColumnMapperBigDecimal(null, null, 8, 100);
        String lower = mapper.base("test", -2);
        String upper = mapper.base("test", -1);
        int compare = lower.compareTo(upper);
        Assert.assertTrue(compare < 0);
    }

    @Test
    public void testValueIntegerPositiveMaxSort() {
        ColumnMapperBigDecimal mapper = new ColumnMapperBigDecimal(null, null, 8, 100);
        String lower = mapper.base("test", 99999998);
        String upper = mapper.base("test", 99999999);
        int compare = lower.compareTo(upper);
        Assert.assertTrue(compare < 0);
    }

    @Test
    public void testValueIntegerPositiveMinSort() {
        ColumnMapperBigDecimal mapper = new ColumnMapperBigDecimal(null, null, 8, 100);
        String lower = mapper.base("test", 1);
        String upper = mapper.base("test", 2);
        int compare = lower.compareTo(upper);
        Assert.assertTrue(compare < 0);
    }

    @Test
    public void testValueIntegerNegativeZeroSort() {
        ColumnMapperBigDecimal mapper = new ColumnMapperBigDecimal(null, null, 8, 100);
        String lower = mapper.base("test", -1);
        String upper = mapper.base("test", 0);
        int compare = lower.compareTo(upper);
        Assert.assertTrue(compare < 0);
    }

    @Test
    public void testValueIntegerPositiveZeroSort() {
        ColumnMapperBigDecimal mapper = new ColumnMapperBigDecimal(null, null, 8, 100);
        String lower = mapper.base("test", 0);
        String upper = mapper.base("test", 1);
        int compare = lower.compareTo(upper);
        Assert.assertTrue(compare < 0);
    }

    @Test
    public void testValueIntegerExtremeSort() {
        ColumnMapperBigDecimal mapper = new ColumnMapperBigDecimal(null, null, 8, 100);
        String lower = mapper.base("test", -99999999);
        String upper = mapper.base("test", 99999999);
        int compare = lower.compareTo(upper);
        Assert.assertTrue(compare < 0);
    }

    @Test
    public void testValueIntegerNegativePositiveSort() {
        ColumnMapperBigDecimal mapper = new ColumnMapperBigDecimal(null, null, 8, 100);
        String lower = mapper.base("test", -1);
        String upper = mapper.base("test", 1);
        int compare = lower.compareTo(upper);
        Assert.assertTrue(compare < 0);
    }

    @Test
    public void testValueDecimalNegativeMaxSort() {
        ColumnMapperBigDecimal mapper = new ColumnMapperBigDecimal(null, null, 2, 8);
        String lower = mapper.base("test", -0.99999999);
        String upper = mapper.base("test", -0.99999998);
        int compare = lower.compareTo(upper);
        Assert.assertTrue(compare < 0);
    }

    @Test
    public void testValueDecimalNegativeMinSort() {
        ColumnMapperBigDecimal mapper = new ColumnMapperBigDecimal(null, null, 2, 8);
        String lower = mapper.base("test", -0.2);
        String upper = mapper.base("test", -0.1);
        int compare = lower.compareTo(upper);
        Assert.assertTrue(compare < 0);
    }

    @Test
    public void testValueDecimalPositiveMaxSort() {
        ColumnMapperBigDecimal mapper = new ColumnMapperBigDecimal(null, null, 2, 8);
        String lower = mapper.base("test", 0.99999998);
        String upper = mapper.base("test", 0.99999999);
        int compare = lower.compareTo(upper);
        Assert.assertTrue(compare < 0);
    }

    @Test
    public void testValueDecimalPositiveMinSort() {
        ColumnMapperBigDecimal mapper = new ColumnMapperBigDecimal(null, null, 2, 8);
        String lower = mapper.base("test", 0.1);
        String upper = mapper.base("test", 0.2);
        int compare = lower.compareTo(upper);
        Assert.assertTrue(compare < 0);
    }

    @Test
    public void testValueDecimalNegativeZeroSort() {
        ColumnMapperBigDecimal mapper = new ColumnMapperBigDecimal(null, null, 2, 8);
        String lower = mapper.base("test", -0.1);
        String upper = mapper.base("test", 0.0);
        int compare = lower.compareTo(upper);
        Assert.assertTrue(compare < 0);
    }

    @Test
    public void testValueDecimalPositiveZeroSort() {
        ColumnMapperBigDecimal mapper = new ColumnMapperBigDecimal(null, null, 2, 8);
        String lower = mapper.base("test", 0.0);
        String upper = mapper.base("test", 0.1);
        int compare = lower.compareTo(upper);
        Assert.assertTrue(compare < 0);
    }

    @Test
    public void testValueDecimalExtremeSort() {
        ColumnMapperBigDecimal mapper = new ColumnMapperBigDecimal(null, null, 2, 8);
        String lower = mapper.base("test", -0.99999999);
        String upper = mapper.base("test", 0.99999999);
        int compare = lower.compareTo(upper);
        Assert.assertTrue(compare < 0);
    }

    @Test
    public void testValueDecimalNegativePositiveSort() {
        ColumnMapperBigDecimal mapper = new ColumnMapperBigDecimal(null, null, 2, 8);
        String lower = mapper.base("test", -0.1);
        String upper = mapper.base("test", 0.1);
        int compare = lower.compareTo(upper);
        Assert.assertTrue(compare < 0);
    }

    // ////

    @Test
    public void testValueNegativeMaxSort() {
        ColumnMapperBigDecimal mapper = new ColumnMapperBigDecimal(null, null, 4, 4);
        String lower = mapper.base("test", -9999.9999);
        String upper = mapper.base("test", -9999.9998);
        int compare = lower.compareTo(upper);
        Assert.assertTrue(compare < 0);
    }

    @Test
    public void testValueNegativeMinSort() {
        ColumnMapperBigDecimal mapper = new ColumnMapperBigDecimal(null, null, 4, 4);
        String lower = mapper.base("test", -0.0002);
        String upper = mapper.base("test", -0.0001);
        int compare = lower.compareTo(upper);
        Assert.assertTrue(compare < 0);
    }

    @Test
    public void testValuePositiveMaxSort() {
        ColumnMapperBigDecimal mapper = new ColumnMapperBigDecimal(null, null, 4, 4);
        String lower = mapper.base("test", 9999.9998);
        String upper = mapper.base("test", 9999.9999);
        int compare = lower.compareTo(upper);
        Assert.assertTrue(compare < 0);
    }

    @Test
    public void testValuePositiveMinSort() {
        ColumnMapperBigDecimal mapper = new ColumnMapperBigDecimal(null, null, 4, 4);
        String lower = mapper.base("test", 0.0001);
        String upper = mapper.base("test", 0.0002);
        int compare = lower.compareTo(upper);
        Assert.assertTrue(compare < 0);
    }

    @Test
    public void testValueNegativeZeroSort() {
        ColumnMapperBigDecimal mapper = new ColumnMapperBigDecimal(null, null, 4, 4);
        String lower = mapper.base("test", -0.0001);
        String upper = mapper.base("test", 0.0);
        int compare = lower.compareTo(upper);
        Assert.assertTrue(compare < 0);
    }

    @Test
    public void testValuePositiveZeroSort() {
        ColumnMapperBigDecimal mapper = new ColumnMapperBigDecimal(null, null, 4, 4);
        String lower = mapper.base("test", 0.0);
        String upper = mapper.base("test", 0.0001);
        int compare = lower.compareTo(upper);
        Assert.assertTrue(compare < 0);
    }

    @Test
    public void testValueExtremeSort() {
        ColumnMapperBigDecimal mapper = new ColumnMapperBigDecimal(null, null, 4, 4);
        String lower = mapper.base("test", -9999.9999);
        String upper = mapper.base("test", 9999.9999);
        int compare = lower.compareTo(upper);
        Assert.assertTrue(compare < 0);
    }

    @Test
    public void testValueNegativePositiveSort() {
        ColumnMapperBigDecimal mapper = new ColumnMapperBigDecimal(null, null, 4, 4);
        String lower = mapper.base("test", -2.4);
        String upper = mapper.base("test", 2.4);
        int compare = lower.compareTo(upper);
        Assert.assertTrue(compare < 0);
    }

    @Test
    public void testValuePositivePositionsSort() {
        ColumnMapperBigDecimal mapper = new ColumnMapperBigDecimal(null, null, 4, 4);
        String lower = mapper.base("test", 1.9);
        String upper = mapper.base("test", 1.99);
        int compare = lower.compareTo(upper);
        Assert.assertTrue(compare < 0);
    }

    @Test
    public void testValueNegativePositionsSort() {
        ColumnMapperBigDecimal mapper = new ColumnMapperBigDecimal(null, null, 4, 4);
        String lower = mapper.base("test", -1.9999);
        String upper = mapper.base("test", -1.9);
        int compare = lower.compareTo(upper);
        Assert.assertTrue(compare < 0);
    }

    @Test
    public void testFieldsIndexedSorted() {
        ColumnMapperBigDecimal mapper = new ColumnMapperBigDecimal(true, true, 4, 4);
        List<Field> fields = mapper.fields("name", "42.43");
        Assert.assertNotNull(fields);
        Assert.assertEquals(2, fields.size());
        Field field = fields.get(0);
        Assert.assertEquals("10042.4299", field.stringValue());
        Assert.assertEquals("name", field.name());
        Assert.assertFalse(field.fieldType().stored());
        field = fields.get(1);
        Assert.assertEquals(DocValuesType.SORTED, field.fieldType().docValuesType());
    }

    @Test
    public void testFieldsIndexedUnSorted() {
        ColumnMapperBigDecimal mapper = new ColumnMapperBigDecimal(true, false, 4, 4);
        List<Field> fields = mapper.fields("name", "42.43");
        Assert.assertNotNull(fields);
        Assert.assertEquals(1, fields.size());
        Field field = fields.get(0);
        Assert.assertEquals("10042.4299", field.stringValue());
        Assert.assertEquals("name", field.name());
        Assert.assertFalse(field.fieldType().stored());
    }

    @Test
    public void testFieldsUnindexedSorted() {
        ColumnMapperBigDecimal mapper = new ColumnMapperBigDecimal(false, true, 4, 4);
        List<Field> fields = mapper.fields("name", "42.43");
        Assert.assertNotNull(fields);
        Assert.assertEquals(1, fields.size());
        Field field = fields.get(0);
        Assert.assertEquals(DocValuesType.SORTED, field.fieldType().docValuesType());
    }

    @Test
    public void testFieldsUnindexedUnSorted() {
        ColumnMapperBigDecimal mapper = new ColumnMapperBigDecimal(false, false, 4, 4);
        List<Field> fields = mapper.fields("name", "42.43");
        Assert.assertNotNull(fields);
        Assert.assertEquals(0, fields.size());
    }

    @Test
    public void testExtractAnalyzers() {
        ColumnMapperBigDecimal mapper = new ColumnMapperBigDecimal(null, null, 10, 10);
        String analyzer = mapper.getAnalyzer();
        Assert.assertEquals(ColumnMapper.KEYWORD_ANALYZER, analyzer);
    }

    @Test
    public void testParseJSONWithoutArgs() throws IOException {
        String json = "{fields:{age:{type:\"bigdec\"}}}";
        Schema schema = Schema.fromJson(json);
        ColumnMapper columnMapper = schema.getMapper("age");
        Assert.assertNotNull(columnMapper);
        Assert.assertEquals(ColumnMapperBigDecimal.class, columnMapper.getClass());
        Assert.assertEquals(ColumnMapper.INDEXED, columnMapper.isIndexed());
        Assert.assertEquals(ColumnMapper.SORTED, columnMapper.isSorted());
        Assert.assertEquals(ColumnMapperBigDecimal.DEFAULT_DECIMAL_DIGITS,
                            ((ColumnMapperBigDecimal) columnMapper).getDecimalDigits());
        Assert.assertEquals(ColumnMapperBigDecimal.DEFAULT_INTEGER_DIGITS,
                            ((ColumnMapperBigDecimal) columnMapper).getIntegerDigits());
    }

    @Test
    public void testParseJSONWithAllArgs() throws IOException {
        String json = "{fields:{age:{type:\"bigdec\", indexed:\"false\", sorted:\"true\", " +
                      "integer_digits:20, decimal_digits:30}}}";
        Schema schema = Schema.fromJson(json);
        ColumnMapper columnMapper = schema.getMapper("age");
        Assert.assertNotNull(columnMapper);
        Assert.assertEquals(ColumnMapperBigDecimal.class, columnMapper.getClass());
        Assert.assertFalse(columnMapper.isIndexed());
        Assert.assertTrue(columnMapper.isSorted());
        Assert.assertEquals(20, ((ColumnMapperBigDecimal) columnMapper).getIntegerDigits());
        Assert.assertEquals(30, ((ColumnMapperBigDecimal) columnMapper).getDecimalDigits());
    }

    @Test
    public void testParseJSONEmpty() throws IOException {
        String json = "{fields:{}}";
        Schema schema = Schema.fromJson(json);
        ColumnMapper columnMapper = schema.getMapper("age");
        Assert.assertNull(columnMapper);
    }

    @Test(expected = IOException.class)
    public void testParseJSONInvalid() throws IOException {
        String json = "{fields:{age:{}}";
        Schema.fromJson(json);
    }
}
