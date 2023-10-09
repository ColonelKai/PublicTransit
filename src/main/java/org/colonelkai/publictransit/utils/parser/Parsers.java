package org.colonelkai.publictransit.utils.parser;

import org.colonelkai.publictransit.line.CostType;
import org.colonelkai.publictransit.utils.serializers.Serializers;
import org.core.config.parser.Parser;
import org.core.config.parser.StringParser;

public class Parsers {

    public static final StringParser<CostType> COST_TYPE = new StringSerializerParser<>(Serializers.COST_TYPE);

    private Parsers() {
        throw new RuntimeException("Should not create");
    }

}
