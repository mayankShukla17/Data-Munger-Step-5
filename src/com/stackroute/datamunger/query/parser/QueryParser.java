package com.stackroute.datamunger.query.parser;


import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class QueryParser {

    private QueryParameter queryParameter = new QueryParameter();

    /*
     * this method will parse the queryString and will return the object of
     * QueryParameter class
     */
    public QueryParameter parseQuery(String queryString) {
        queryParameter.setFileName(getFileName(queryString));
        queryParameter.setBaseQuery(getBaseQuery(queryString));
        queryParameter.setOrderByFields(getOrderByFields(queryString));
        queryParameter.setGroupByFields(getGroupByFields(queryString));
        queryParameter.setFields(getFields(queryString));
        queryParameter.setLogicalOperators(getLogicalOperators(queryString));
        queryParameter.setAggregateFunctions(getAggregateFunctions(queryString));
        queryParameter.setRestrictions(getConditions(queryString));
        return queryParameter;
    }

    /*
     * extract the name of the file from the query. File name can be found after the
     * "from" clause.
     */

    public String getFileName(String queryString) {
        String stringFrom = queryString.split("from")[1].trim();
        String stringFileName = stringFrom.split(" ")[0].trim();
        return stringFileName;
    }

    //getBaseQuery() method
    public String getBaseQuery(String queryString) {
        String stringBaseQuery = "";
        if (queryString.contains("where")) {
            stringBaseQuery = queryString.toLowerCase().split("where")[0].trim();
        } else if (queryString.contains("group by") || queryString.contains("order by")) {
            stringBaseQuery = queryString.toLowerCase().split("group by|order by")[0].trim();
        } else {
            stringBaseQuery = queryString;
        }
        return stringBaseQuery;
    }

    /*
     * extract the order by fields from the query string. Please note that we will
     * need to extract the field(s) after "order by" clause in the query, if at all
     * the order by clause exists. For eg: select city,winner,team1,team2 from
     * data/ipl.csv order by city from the query mentioned above, we need to extract
     * "city". Please note that we can have more than one order by fields.
     */

    public ArrayList<String> getOrderByFields(String queryString) {
        String string = queryString.toLowerCase();
        String[] stringOrderByFields = null;
        ArrayList<String> list = new ArrayList<String>();
        if (string.contains("order by")) {
            String stringNotWhere = string.split("order by")[1].trim();
            if (stringNotWhere.contains(",")) {
                stringOrderByFields = stringNotWhere.split(",");
                for (int i = 0; i < stringOrderByFields.length; i++) {
                    list.add(stringOrderByFields[i]);
                }
            } else {
                list.add(stringNotWhere);
            }
            return list;
        } else {
            return null;
        }
    }

    /*
     * extract the group by fields from the query string. Please note that we will
     * need to extract the field(s) after "group by" clause in the query, if at all
     * the group by clause exists. For eg: select city,max(win_by_runs) from
     * data/ipl.csv group by city from the query mentioned above, we need to extract
     * "city". Please note that we can have more than one group by fields.
     */

    public List<String> getGroupByFields(String queryString) {
        String string = queryString.toLowerCase();
        String[] stringGroupByFields = null;
        List<String> list = new ArrayList<String>();
        if (string.contains("where") && (string.contains("group by")) && string.contains("order by")) {
            String whereString = string.split("where")[1].trim();
            String groupByString = whereString.split("group by")[1].trim();
            String stringbeforeOrderBy = groupByString.split("order by")[0].trim();
            if (stringbeforeOrderBy.contains(",")) {
                stringGroupByFields = stringbeforeOrderBy.split(",");
                for (int i = 0; i < stringGroupByFields.length; i++) {
                    list.add(stringGroupByFields[i]);
                }
            } else {
                list.add(stringbeforeOrderBy);
            }
            return list;
        } else if (string.contains("group by")) {
            String notWhereString = string.split("group by")[1].trim();
            if (notWhereString.contains(",")) {
                stringGroupByFields = notWhereString.split(",");
                for (int i = 0; i < stringGroupByFields.length; i++) {
                    list.add(stringGroupByFields[i]);
                }
            } else {
                list.add(notWhereString);
            }
            return list;
        } else {
            return null;
        }
    }

    /*
     * extract the selected fields from the query string. Please note that we will
     * need to extract the field(s) after "select" clause followed by a space from
     * the query string. For eg: select city,win_by_runs from data/ipl.csv from the
     * query mentioned above, we need to extract "city" and "win_by_runs". Please
     * note that we might have a field containing name "from_date" or "from_hrs".
     * Hence, consider this while parsing.
     */

    public List<String> getFields(String queryString) {
        String stringSelect = queryString.toLowerCase().split("select")[1].trim();
        String stringFrom = stringSelect.split("from")[0].trim();
        String[] selectFields = null;
        List<String> list = new ArrayList<String>();
        if (stringFrom.contains(",")) {
            selectFields = stringFrom.split(",");
            for (int i = 0; i < selectFields.length; i++) {
                list.add(selectFields[i].trim());
            }
            return list;
        } else {
            list.add(stringFrom);
            return list;
        }
    }

    /*
     * extract the conditions from the query string(if exists). for each condition,
     * we need to capture the following:
     * 1. Name of field
     * 2. condition
     * 3. value
     *
     * For eg: select city,winner,team1,team2,player_of_match from data/ipl.csv
     * where season >= 2008 or toss_decision != bat
     *
     * here, for the first condition, "season>=2008" we need to capture:
     * 1. Name of field: season
     * 2. condition: >=
     * 3. value: 2008
     *
     * the query might contain multiple conditions separated by OR/AND operators.
     * Please consider this while parsing the conditions.
     *
     */

    public List<Restriction> getConditions(String queryString) {

        /*
         *  Logic -- Pass the queryString to the getConditionPartQuery to get our condition as a String.
         * If the where keyword is not there then condition string will be null hence return null. else
         * split the string on (and or keyword). The main idea here is we are splitting on (space and space)
         * not just (and) because some field name may also contain ...and... as there substring.
         */
        String inLower = queryString.trim();
        String tokens[] = inLower.trim().split("where");

        if (tokens.length == 1) {
            return null;
        }

        String conditions[] = tokens[1].trim().split("order by|group by");
        String strings[] = conditions[0].trim().split(" and | or ");
        List<Restriction> restrictionList = new LinkedList<Restriction>();
        for (String string : strings) {
            String condition = "";
            if (string.contains(">=")) {
                condition = ">=";
            } else if (string.contains("<=")) {
                condition = "<=";
            } else if (string.contains("!=")) {
                condition = "!=";
            } else if (string.contains(">")) {
                condition = ">";
            } else if (string.contains("<")) {
                condition = "<";
            } else if (string.contains("=")) {
                condition = "=";
            }
            String name = string.split(condition)[0].trim();
            String value = string.split(condition)[1].trim().replaceAll("'", "");
            Restriction restrictionInstance = new Restriction(name, value, condition);
            restrictionList.add(restrictionInstance);
        }
        return restrictionList;
    }

    /*
     * extract the logical operators(AND/OR) from the query, if at all it is
     * present. For eg: select city,winner,team1,team2,player_of_match from
     * data/ipl.csv where season >= 2008 or toss_decision != bat and city =
     * bangalore
     *
     * the query mentioned above in the example should return a List of Strings
     * containing [or,and]
     */

    public List<String> getLogicalOperators(String queryString) {
        String string = queryString.toLowerCase();
        String[] stringAndOr = null;
        List<String> list = new ArrayList<String>();
        if (string.contains("where")) {
            String stringWhere = string.split("where")[1].trim();
            stringAndOr = stringWhere.split(" ");
            for (int i = 0; i < stringAndOr.length; i++) {
                if (stringAndOr[i].equals("and") || stringAndOr[i].equals("or")) {
                    list.add(stringAndOr[i]);
                }
            }
            return list;
        } else {
            return null;
        }
    }

    /*
     * extract the aggregate functions from the query. The presence of the aggregate
     * functions can determined if we have either "min" or "max" or "sum" or "count"
     * or "avg" followed by opening braces"(" after "select" clause in the query
     * string. in case it is present, then we will have to extract the same. For
     * each aggregate functions, we need to know the following:
     * 1. type of aggregate function(min/max/count/sum/avg)
     * 2. field on which the aggregate function is being applied
     *
     * Please note that more than one aggregate function can be present in a query
     *
     *
     */

    public List<AggregateFunction> getAggregateFunctions(String queryString) {
        String query = queryString.toLowerCase();
        String[] temp = null;
        String[] aggregateFunctions = new String[]{"min", "max", "count", "avg", "sum"};
        ArrayList<AggregateFunction> list = new ArrayList<>();
        temp = query.split("from")[0].trim().split("select")[1].trim().split(",");
        for (String string : aggregateFunctions) {
            for (String str : temp) {
                if (str.contains(string)) {
                    AggregateFunction aggregateFunction = new AggregateFunction(string.trim(), str.substring(str.indexOf("(") + 1, str.indexOf(")")).trim());
                    list.add(aggregateFunction);
                }
            }
        }
        return list;
    }
}
