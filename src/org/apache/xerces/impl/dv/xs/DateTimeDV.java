/*
 * Copyright 1999-2002,2004 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.xerces.impl.dv.xs;

import org.apache.xerces.impl.dv.InvalidDatatypeValueException;
import org.apache.xerces.impl.dv.ValidationContext;

/**
 * Validator for <dateTime> datatype (W3C Schema Datatypes)
 *
 * @author Elena Litani
 * @author Gopal Sharma, SUN Microsystem Inc.
 *
 * @version $Id$
 */
public class DateTimeDV extends AbstractDateTimeDV {

    public Object getActualValue(String content, ValidationContext context) throws InvalidDatatypeValueException {
        try{
            return parse(content);
        } catch(Exception ex){
            throw new InvalidDatatypeValueException("cvc-datatype-valid.1.2.1", new Object[]{content, "dateTime"});
        }
    }

    /**
     * Parses, validates and computes normalized version of dateTime object
     *
     * @param str    The lexical representation of dateTime object CCYY-MM-DDThh:mm:ss.sss
     *               with possible time zone Z or (-),(+)hh:mm
     * @param date   uninitialized date object
     * @return normalized dateTime representation
     * @exception SchemaDateTimeException Invalid lexical representation
     */
    protected DateTimeData parse(String str) throws SchemaDateTimeException {
        DateTimeData date = new DateTimeData(this);
        int len = str.length();
        int[] timeZone = new int[2];

        int end = indexOf (str, 0, len, 'T');

        // both time and date
        int dateEnd = getDate(str, 0, end, date);
        getTime(str, end+1, len, date, timeZone);

        //Check the separator character between Date and Time
        if (dateEnd != end) {
            throw new RuntimeException(str
                    + " is an invalid dateTime dataype value. "
                    + "Invalid character(s) seprating date and time values.");
        }

        //validate and normalize

        //REVISIT: do we need SchemaDateTimeException?
        validateDateTime(date, timeZone);

        if (date.utc!=0 && date.utc!='Z') {
            normalize(date, timeZone);
        }
        return date;
    }

}
